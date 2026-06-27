-- Drop tables if they exist to allow clean seeding in dev mode
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;

-- Users Table (Role-based access)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    email_verified BOOLEAN DEFAULT FALSE,
    mobile_verified BOOLEAN DEFAULT FALSE,
    email_verification_code VARCHAR(50),
    mobile_otp VARCHAR(20),
    role VARCHAR(20) NOT NULL
);

-- Products Table (Shoe Catalog)
CREATE TABLE products (
    product_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    brand VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    color VARCHAR(30) NOT NULL,
    size INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    description TEXT,
    rating DECIMAL(3,2) DEFAULT 0.00,
    review_count INT DEFAULT 0,
    stock_count INT DEFAULT 0,
    trending_score INT DEFAULT 0
);

-- Orders Table (Customer Tracking)
CREATE TABLE orders (
    order_id VARCHAR(50) PRIMARY KEY,
    product_id VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    payment_status VARCHAR(50) DEFAULT 'PAID',
    delivery_date DATE NOT NULL,
    username VARCHAR(50) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);
