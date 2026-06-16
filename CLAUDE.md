# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project status

This is an early-stage Spring Boot project. Only the application bootstrap class
(`PulgattiWeatherApplication`) and the database schema (Flyway migrations) exist so far —
no controllers, services, repositories, or entities have been written yet. When implementing
features, you are largely establishing the architecture, not following an existing pattern.

## Commands

Build, test, and run via the Gradle wrapper:

```bash
./gradlew build              # compile + run tests
./gradlew test                # run all tests
./gradlew test --tests PulgattiWeatherApplicationTests   # run a single test class
./gradlew bootRun             # run the application locally
```

Infrastructure (Postgres, Redis, Kafka) is provided via Docker Compose and must be running
before the app starts, since `application.yaml` points at `localhost` for all three:

```bash
docker compose -f docker-compose/docker-compose.yml up -d
```

- Postgres: `localhost:5432`, db `pulgatti_weather`, user/pass `root`/`root`
- Redis: `localhost:6379`
- Kafka: `localhost:9092` (KRaft mode, single broker, consumer group `weather-group`)

## Architecture

- **Java 26**, Spring Boot 4.1.0, Gradle Kotlin DSL (`build.gradle.kts`).
- **Database migrations are managed by Flyway**, not Hibernate DDL — `spring.jpa.hibernate.ddl-auto`
  is set to `validate`, so JPA entities must always match the schema defined in
  `src/main/resources/db/migration/V*.sql`. Any schema change requires a new
  `V{n}__description.sql` migration file (sequential, never edit existing migrations).
- Current schema (see `V1__create_tables_login_and_geocoding.sql`, `V2__create_table_search_history.sql`):
  - `users` — auth (BCrypt-hashed passwords).
  - `cities_coordinates` — geocoding cache keyed on `(city_name, state)`, intended to avoid
    repeated calls to the Open-Meteo Geocoding API.
  - `search_history` — FK to `users`, intended to be populated **asynchronously via Kafka**
    rather than written synchronously in the request path. Stores a snapshot of temperature/weather
    code at search time for future analytics.
- Redis is wired in as `spring-boot-starter-data-redis-reactive` — intended for caching
  (e.g. weather/geocoding lookups), reactive client.
- Kafka is wired in for the async write path described above (history persistence), consumer
  group `weather-group`, `auto-offset-reset: earliest`.
- Lombok is available on both main and test source sets.
