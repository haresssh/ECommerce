CREATE DATABASE IF NOT EXISTS orders_db;
USE orders_db;

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    product_id BIGINT,
    quantity INT,
    status VARCHAR(255),
    created_at DATETIME
);

-- Optional: Insert some initial orders if needed for testing history
INSERT IGNORE INTO orders (id, user_id, product_id, quantity, status, created_at) VALUES
(1, 1, 1, 1, 'COMPLETED', NOW()),
(2, 2, 4, 2, 'PENDING', NOW());
