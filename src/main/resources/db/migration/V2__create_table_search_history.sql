-- Search History Table (Asynchronously populated by Kafka)
CREATE TABLE search_history (
                                id BIGSERIAL PRIMARY KEY,
                                user_id BIGINT NOT NULL,
                                searched_city VARCHAR(100) NOT NULL,
                                searched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Optional metrics snapshot at the moment of search (great for future analytics/dashboards)
                                snapshot_temperature NUMERIC(4, 1),
                                snapshot_weather_code INT,

                                CONSTRAINT fk_history_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Index on user_id to instantly load the user's "Recent Searches" dashboard
CREATE INDEX idx_history_user ON search_history(user_id);
