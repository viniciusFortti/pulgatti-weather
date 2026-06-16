# pulgatti-weather

API de previsão do tempo com login simples (email/senha), cache de geocoding e de previsão, e histórico de pesquisas processado de forma assíncrona via Kafka.

## Stack

- Java 26 + Spring Boot 4.1 (Web MVC, Security, Data JPA, Kafka, Data Redis)
- PostgreSQL (dados), Redis (cache de previsão, 24h), Kafka (histórico assíncrono)
- Flyway (migrations), JWT (autenticação stateless)
- Geocoding e previsão via [Open-Meteo](https://open-meteo.com/)

## Subindo o ambiente

1. Suba a infraestrutura (Postgres, Redis, Kafka):

   ```bash
   docker compose -f docker-compose/docker-compose.yml up -d
   ```

2. Rode a aplicação:

   ```bash
   ./gradlew bootRun
   ```

   A API fica disponível em `http://localhost:8080`.

## Autenticação

Todos os endpoints, exceto `/api/auth/**`, exigem um JWT no header:

```
Authorization: Bearer <token>
```

O token é obtido no login e expira em 60 minutos (configurável em `app.jwt.expiration-minutes`).

---

## APIs

### 1. Auth — `/api/auth`

#### Registrar usuário

```
POST /api/auth/register
Content-Type: application/json
```

```json
{
  "name": "Vinicius",
  "email": "vinicius@example.com",
  "password": "senha123"
}
```

**Resposta `201 Created`:**

```json
{
  "id": 1,
  "name": "Vinicius",
  "email": "vinicius@example.com",
  "createdAt": "2026-06-16T16:29:54.395846"
}
```

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Vinicius","email":"vinicius@example.com","password":"senha123"}'
```

#### Login

```
POST /api/auth/login
Content-Type: application/json
```

```json
{
  "email": "vinicius@example.com",
  "password": "senha123"
}
```

**Resposta `200 OK`:**

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "user": {
    "id": 1,
    "name": "Vinicius",
    "email": "vinicius@example.com",
    "createdAt": "2026-06-16T16:29:54.395846"
  }
}
```

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"vinicius@example.com","password":"senha123"}'
```

---

### 2. Cidades — `/api/cities`

CRUD sobre a tabela `cities_coordinates` (cache de geocoding). Requer autenticação.

#### Listar todas

```bash
curl http://localhost:8080/api/cities \
  -H "Authorization: Bearer <token>"
```

#### Buscar por id

```bash
curl http://localhost:8080/api/cities/1 \
  -H "Authorization: Bearer <token>"
```

#### Criar manualmente

```
POST /api/cities
Content-Type: application/json
```

```json
{
  "cityName": "Porto Alegre",
  "state": "RS",
  "latitude": -30.0346,
  "longitude": -51.2177
}
```

```bash
curl -X POST http://localhost:8080/api/cities \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"cityName":"Porto Alegre","state":"RS","latitude":-30.0346,"longitude":-51.2177}'
```

#### Atualizar

```bash
curl -X PUT http://localhost:8080/api/cities/1 \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"cityName":"Porto Alegre","state":"RS","latitude":-30.03,"longitude":-51.21}'
```

#### Remover

```bash
curl -X DELETE http://localhost:8080/api/cities/1 \
  -H "Authorization: Bearer <token>"
```

> Essas cidades também são preenchidas automaticamente (cache sob demanda) quando você consulta `/api/weather/forecast` para uma cidade ainda não cadastrada.

---

### 3. Previsão do tempo — `/api/weather`

```
GET /api/weather/forecast?city={nome da cidade}
```

Fluxo:
1. Busca as coordenadas da cidade em `cities_coordinates`; se não existir, consulta a API de geocoding do Open-Meteo e salva.
2. Busca a previsão atual no cache Redis (TTL de 24h); em cache miss, consulta a API de forecast do Open-Meteo e armazena o resultado.
3. Publica um evento no Kafka para registrar a pesquisa no histórico do usuário (processado de forma assíncrona).

**Resposta `200 OK`:**

```json
{
  "cityName": "Viamão",
  "state": "BR",
  "latitude": -30.0811,
  "longitude": -51.0233,
  "temperature": 13.0,
  "windspeed": 8.7,
  "weatherCode": 0,
  "observedAt": "2026-06-16T20:00"
}
```

```bash
curl "http://localhost:8080/api/weather/forecast?city=Viamao" \
  -H "Authorization: Bearer <token>"
```

---

### 4. Histórico de pesquisas — `/api/search-history`

Lista o histórico do usuário autenticado, mais recente primeiro. É populado de forma assíncrona pelo consumer Kafka após cada chamada a `/api/weather/forecast`.

```bash
curl http://localhost:8080/api/search-history \
  -H "Authorization: Bearer <token>"
```

**Resposta `200 OK`:**

```json
[
  {
    "id": 4,
    "searchedCity": "Viamão",
    "searchedAt": "2026-06-16T17:05:35.458103",
    "snapshotTemperature": 13.0,
    "snapshotWeatherCode": 0
  }
]
```

---

## Erros

Respostas de erro seguem o formato `ProblemDetail` (RFC 9457):

```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "City coordinates not found: 99"
}
```

| Status | Quando ocorre |
|---|---|
| 400 | Validação de campos do request body |
| 401 | Credenciais inválidas no login, ou token JWT ausente/inválido/expirado |
| 404 | Recurso não encontrado (cidade, ou cidade não localizada na API de geocoding) |
| 409 | Email já cadastrado no registro |
| 502 | Falha ao chamar a API externa do Open-Meteo |
