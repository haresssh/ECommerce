CREATE DATABASE IF NOT EXISTS productservice;
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

INSERT IGNORE INTO categories (id, name) VALUES 
(1, 'Electronics'),
(2, 'Books'),
(3, 'Clothing'),
(4, 'Home & Kitchen');

INSERT IGNORE INTO products (id, title, description, price, image_url, category_id) VALUES 
(1, 'iPhone 15', 'Latest Apple iPhone', 999.99, 'https://example.com/iphone15.jpg', 1),
(2, 'MacBook Pro 16', 'M3 Max Chip', 2499.99, 'https://example.com/macbook.jpg', 1),
(3, 'Sony WH-1000XM5', 'Noise Cancelling Headphones', 349.99, 'https://example.com/sony.jpg', 1),
(4, 'The Alchemist', 'A novel by Paulo Coelho', 14.99, 'https://example.com/alchemist.jpg', 2),
(5, 'Clean Code', 'A Handbook of Agile Software Craftsmanship', 35.00, 'https://example.com/cleancode.jpg', 2),
(6, 'Cotton T-Shirt', '100% Cotton Basic Tee', 19.99, 'https://example.com/tshirt.jpg', 3),
(7, 'Denim Jeans', 'Classic Fit Jeans', 49.99, 'https://example.com/jeans.jpg', 3),
(8, 'Coffee Maker', 'Programmable Coffee Maker', 79.99, 'https://example.com/coffeemaker.jpg', 4);
