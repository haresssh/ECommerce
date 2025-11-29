CREATE DATABASE IF NOT EXISTS inventoryservice;
USE inventoryservice;

CREATE TABLE IF NOT EXISTS inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT UNIQUE NOT NULL,
    quantity INT NOT NULL
);

INSERT IGNORE INTO inventory (id, product_id, quantity) VALUES 
(1, 1, 100), -- iPhone 15
(2, 2, 50),  -- MacBook Pro
(3, 3, 200), -- Sony Headphones
(4, 4, 500), -- The Alchemist
(5, 5, 300), -- Clean Code
(6, 6, 1000),-- T-Shirt
(7, 7, 800), -- Jeans
(8, 8, 150); -- Coffee Maker
