-- Bank Management System Database Schema
-- Create database
CREATE DATABASE IF NOT EXISTS bank_management_system;
USE bank_management_system;

-- Create customers table
CREATE TABLE IF NOT EXISTS customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(200) NOT NULL,
    date_of_birth DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create accounts table
CREATE TABLE IF NOT EXISTS accounts (
    account_number VARCHAR(20) PRIMARY KEY,
    customer_id INT NOT NULL,
    account_type VARCHAR(20) NOT NULL CHECK (account_type IN ('SAVINGS', 'CURRENT', 'FIXED')),
    balance DECIMAL(15,2) DEFAULT 0.00,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'CLOSED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);

-- Create transactions table
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    account_number VARCHAR(20) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL CHECK (transaction_type IN ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER')),
    amount DECIMAL(15,2) NOT NULL,
    description VARCHAR(200),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    balance_after DECIMAL(15,2) NOT NULL,
    FOREIGN KEY (account_number) REFERENCES accounts(account_number) ON DELETE CASCADE
);

-- Create users table for login authentication
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'EMPLOYEE' CHECK (role IN ('ADMIN', 'EMPLOYEE')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default admin user (password: admin123)
INSERT INTO users (username, password, role) VALUES 
('admin', 'admin123', 'ADMIN')
ON DUPLICATE KEY UPDATE username = username;

-- Insert sample customer data
INSERT INTO customers (first_name, last_name, email, phone, address, date_of_birth) VALUES 
('John', 'Doe', 'john.doe@email.com', '1234567890', '123 Main St, City', '1990-01-15'),
('Jane', 'Smith', 'jane.smith@email.com', '9876543210', '456 Oak Ave, Town', '1985-05-22'),
('Robert', 'Johnson', 'robert.j@email.com', '5551234567', '789 Pine Rd, Village', '1992-08-10')
ON DUPLICATE KEY UPDATE email = email;

-- Insert sample account data
INSERT INTO accounts (account_number, customer_id, account_type, balance) VALUES 
('ACC001', 1, 'SAVINGS', 5000.00),
('ACC002', 1, 'CURRENT', 2000.00),
('ACC003', 2, 'SAVINGS', 10000.00),
('ACC004', 3, 'CURRENT', 7500.00)
ON DUPLICATE KEY UPDATE account_number = account_number;

-- Insert sample transaction data
INSERT INTO transactions (account_number, transaction_type, amount, description, balance_after) VALUES 
('ACC001', 'DEPOSIT', 5000.00, 'Initial deposit', 5000.00),
('ACC002', 'DEPOSIT', 2000.00, 'Initial deposit', 2000.00),
('ACC003', 'DEPOSIT', 10000.00, 'Initial deposit', 10000.00),
('ACC004', 'DEPOSIT', 7500.00, 'Initial deposit', 7500.00)
ON DUPLICATE KEY UPDATE transaction_id = transaction_id;
