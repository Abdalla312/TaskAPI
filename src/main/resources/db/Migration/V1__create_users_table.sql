-- V1__Create_users_table.sql

CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY ,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--Create index on email
CREATE INDEX idx_users_email ON users(email);

--Create index on username
CREATE INDEX idx_users_username ON users(username);

COMMENT ON TABLE users IS 'Stores user account info';