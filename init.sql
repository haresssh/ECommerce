CREATE DATABASE IF NOT EXISTS productservice;
CREATE DATABASE IF NOT EXISTS userservice;
CREATE DATABASE IF NOT EXISTS orders_db;
CREATE DATABASE IF NOT EXISTS inventoryservice;

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
INSERT IGNORE INTO users (id, name, email, hashed_password, deleted) VALUES (1, 'Test User', 'test@example.com', '$2a$10$D7.k.1t.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1', 0);

USE productservice;
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    description VARCHAR(255),
    price DOUBLE,
    image_url VARCHAR(255),
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);
INSERT IGNORE INTO categories (id, name) VALUES (1, 'Electronics');
INSERT IGNORE INTO products (id, title, description, price, image_url, category_id) VALUES (1, 'iPhone 15', 'Latest iPhone', 999.99, 'http://example.com/iphone.jpg', 1);

USE inventoryservice;
CREATE TABLE IF NOT EXISTS inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT UNIQUE NOT NULL,
    quantity INT NOT NULL
);
INSERT IGNORE INTO inventory (id, product_id, quantity) VALUES (1, 1, 100);
