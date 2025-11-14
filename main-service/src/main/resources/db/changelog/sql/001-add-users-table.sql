--liquibase formatted sql

--changeset ilmira:001-1

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    auth_service_user_id VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50),
    email VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


--changeset ilmira:001-2
CREATE UNIQUE INDEX idx_users_email ON users(email);
CREATE UNIQUE INDEX idx_users_auth_service_id ON users(auth_service_user_id);
CREATE INDEX idx_users_role ON users(role);