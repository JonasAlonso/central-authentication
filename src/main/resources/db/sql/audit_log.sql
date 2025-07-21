CREATE TABLE audit_log (
    id SERIAL PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL,
    client_id VARCHAR(100),
    user_id VARCHAR(100),
    endpoint VARCHAR(200),
    http_method VARCHAR(10),
    status_code INT,
    scopes VARCHAR(200),
    ip_address VARCHAR(100),
    trace_id VARCHAR(100),
    message TEXT
);