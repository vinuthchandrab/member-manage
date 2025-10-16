CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS member (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS role (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS "user" (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role_id UUID,
    CONSTRAINT fk_role FOREIGN KEY(role_id) REFERENCES role(id)
);

-- Seed roles
INSERT INTO role (id, name) VALUES
    ('11111111-1111-1111-1111-111111111111', 'admin'),
    ('22222222-2222-2222-2222-222222222222', 'user')
ON CONFLICT (id) DO NOTHING;

-- Seed users: password hashes are placeholders (use real hashes in production)
INSERT INTO "user" (id, username, password_hash, role_id) VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'admin', 'hash_admin_pw', '11111111-1111-1111-1111-111111111111'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'normaluser', 'hash_user_pw', '22222222-2222-2222-2222-222222222222')
ON CONFLICT (id) DO NOTHING;

