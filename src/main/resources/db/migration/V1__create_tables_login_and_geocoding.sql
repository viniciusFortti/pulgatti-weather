-- Users Table for Authentication
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL, -- Stores the encrypted password hash (BCrypt)
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Cities Coordinates Table (Geocoding Cache)
-- Prevents calling the Open-Meteo Geocoding API every time for the same city
CREATE TABLE cities_coordinates (
                                    id BIGSERIAL PRIMARY KEY,
                                    city_name VARCHAR(100) NOT NULL,
                                    state VARCHAR(2) NOT NULL DEFAULT 'RS',
                                    latitude NUMERIC(8, 4) NOT NULL,
                                    longitude NUMERIC(8, 4) NOT NULL,
                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    CONSTRAINT unique_city_state UNIQUE (city_name, state)
);

-- Database Index for ultra-fast queries by city name
CREATE INDEX idx_cities_name ON cities_coordinates(city_name);