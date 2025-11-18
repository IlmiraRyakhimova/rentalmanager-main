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


--changeset ilmira:001-3
CREATE TABLE apartment_addresses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    postal_code VARCHAR(20),
    country VARCHAR(255),
    city VARCHAR(255),
    district VARCHAR(255),
    street VARCHAR(255),
    house_number VARCHAR(50),
    floor_number INTEGER,
    apartment_number INTEGER,
    distance_from_city_center_km DOUBLE PRECISION,
    distance_from_airport_km DOUBLE PRECISION,
    distance_from_beach_km DOUBLE PRECISION
);


--changeset ilmira:001-4
CREATE TABLE apartments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255),
    accommodation_type VARCHAR(255),
    agent_id UUID NOT NULL REFERENCES users(id),
    owner_id UUID NOT NULL REFERENCES users(id),
    address_id UUID REFERENCES apartment_addresses(id),
    area_in_square_meters DOUBLE PRECISION,
    number_of_rooms INTEGER,
    number_of_bathrooms INTEGER,
    price_per_night DECIMAL(10, 2),
    description VARCHAR(2000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


--changeset ilmira:001-5
CREATE INDEX idx_apartments_agent_id ON apartments(agent_id);
CREATE INDEX idx_apartments_owner_id ON apartments(owner_id);
CREATE INDEX idx_apartments_address_id ON apartments(address_id);

