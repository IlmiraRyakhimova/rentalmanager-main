--liquibase formatted sql

--changeset ilmira:001-1

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50),
    email VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


--changeset ilmira:001-2
CREATE UNIQUE INDEX idx_users_id ON users(id);
CREATE UNIQUE INDEX idx_users_email ON users(email);
CREATE UNIQUE INDEX idx_users_phone_number ON users(phone_number);

