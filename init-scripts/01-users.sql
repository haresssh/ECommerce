CREATE DATABASE IF NOT EXISTS userservice;
USE userservice;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    hashed_password VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME,
    deleted BIT(1)
);

-- Password is 'password' for all users (BCrypt hash)
INSERT IGNORE INTO users (id, name, email, hashed_password, deleted, created_at, updated_at) VALUES 
(1, 'Alice Smith', 'alice@example.com', '$2a$10$D7.k.1t.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1', 0, NOW(), NOW()),
(2, 'Bob Jones', 'bob@example.com', '$2a$10$D7.k.1t.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1', 0, NOW(), NOW()),
(3, 'Charlie Brown', 'charlie@example.com', '$2a$10$D7.k.1t.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1', 0, NOW(), NOW());
