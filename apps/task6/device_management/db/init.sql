--CREATE DATABASE device_management;

-- Таблица будет создана автоматически Hibernate, но вот ее структура:
/*
CREATE TABLE devices (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    device_key VARCHAR(255) NOT NULL UNIQUE,
    type_code VARCHAR(255) NOT NULL,
    model VARCHAR(255),
    location VARCHAR(255),
    status VARCHAR(255),
    metadata JSONB,
    home_id UUID,
    owner_account UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
*/