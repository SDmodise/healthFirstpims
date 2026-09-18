-- ==============================
-- HealthFirst Pharmacy Inventory Management System 
-- Database Creation Script
-- ==============================

CREATE DATABASE IF NOT EXISTS pims_db;
USE pims_db;

-- -------------------------------------------
-- Table: users
-- -------------------------------------------
CREATE TABLE users (
user_id INT PRIMARY KEY AUTO_INCREMENT,
username VARCHAR(50) UNIQUE NOT NULL,
password VARCHAR(255) NOT NULL,
role ENUM('Admin', 'Cashier') NOT NULL,
full_name VARCHAR(100) NOT NULL
);

-- -----------------------------
-- Table: Suppliers
-- -----------------------------

CREATE TABLE suppliers (
suppliers_id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(100) NOT NULL,
contact_person VARCHAR(100),
phone VARCHAR(20),
email VARCHAR(100),
address TEXT
);

-- -------------------------------
-- Table: medicines
-- -------------------------------
CREATE TABLE medicines (
medicine_id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(150) NOT NULL,
company VARCHAR(100),
medicine_type VARCHAR(50),
price DECIMAL(10,2) NOT NULL,
quantity_in_stock INT NOT NULL DEFAULT 0,
reorder_level INT NOT NULL DEFAULT 10,
expiry_date DATE,
supplier_id INT,
FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id)
	ON DELETE SET NULL
);

-- ---------------------------
-- Table: sales
-- ---------------------------

CREATE TABLE sales (
sales_id INT PRIMARY KEY AUTO_INCREMENT,
sales_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
user_id INT,
FOREIGN KEY (user_id) REFERENCES users(user_id)
	ON DELETE SET NULL
);

-- ------------------------
-- Table: sale_items
-- ------------------------

CREATE TABLE sales_items (
sale_item_id INT PRIMARY KEY AUTO_INCREMENT,
sale_id INT NOT NULL,
medicine_id INT NOT NULL,
quantity_sold INT NOT NULL,
price_at_sale DECIMAL(10,2) NOT NULL,
FOREIGN KEY (sale_id) REFERENCES sales(sale_id)
	ON DELETE CASCADE,
FOREIGN KEY (medicine_id) REFERENCES medicines(medicine_id)
);

-- =============================
-- SAMPLE DATA 
-- =============================
-- Default users (plain text here for demo simplicity -
-- see note below on password handling)

INSERT INTO users (username, password, role, full_name) VALUES 
('admin', 'admin123', 'Admin', 'System Administrator'),
('cashier', 'cash123', 'Cashier','Front Counter Cashier');

-- Suppliers 
INSERT INTO suppliers (name, contact_person, phone, email, address) VALUES
('MediSupply SA', 'Lesego Molotsi', '0123456789', 'lsgmol@medisupply.co.za', '12 Lillian Rd, Pretoria'),
('BigPharma', 'John Sibiya', '0136748267', 'jSibiya@bigpharma.co.za', '22 Peters St, Johannesburg'),
('PharmaCorp Distributors', 'Leleti Khumalo', '0535658899', 'lelethikhumalo@pharmaorpdist.co.za', '8 Avenue Dr, Cape Town');

-- Medicines
INSERT INTO medicines (name, company, medicines_type, price, quantity_in_stock, reorder_level, expiry_data, supplier_id) VALUES
('Panado 500mg', 'Adcock Ingram', 'Tablet', '35.50', '120', '20', '2027-09-30', 1),
('Amoxicillin 250mg', 'Aspen Phamarcare', 'Capsule', '89.99', '45', '15', '2026-11-20', 2),
('Corenza C', 'Aspen Pharmacare', 'Syrup', '65.00', '8', '10', '2026-09-30', 2),
('Insulin Actrapid', 'Novo Nordisk', 'Injection', 250.00, 30, 5, '2026-10-05', 3),
('Betamethasone Cream', 'GSK', 'Cream', 55.75, 25, 10, '2027-01-10', 1);


