DROP DATABASE IF EXISTS pims_db;
CREATE DATABASE pims_db;
USE pims_db;

SELECT user_id, username, password, role, full_name
FROM users;
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'Cashier') NOT NULL,
    full_name VARCHAR(100) NOT NULL
);

CREATE TABLE suppliers (
    supplier_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    address TEXT
);

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
    FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id) ON DELETE SET NULL
);

CREATE TABLE sales (
    sale_id INT PRIMARY KEY AUTO_INCREMENT,
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

CREATE TABLE sale_items (
    sale_item_id INT PRIMARY KEY AUTO_INCREMENT,
    sale_id INT NOT NULL,
    medicine_id INT NOT NULL,
    quantity_sold INT NOT NULL,
    price_at_sale DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (sale_id) REFERENCES sales(sale_id) ON DELETE CASCADE, PRIMARY KEY (supplier_id),
    FOREIGN KEY (medicine_id) REFERENCES medicines(medicine_id)
);

INSERT INTO users (username, password, role, full_name) VALUES
('admin', 'admin123', 'Admin', 'System Administrator'),
('cashier', 'cash123', 'Cashier', 'Front Counter Cashier');

INSERT INTO suppliers (name, contact_person, phone, email, address) VALUES
('MediSupply SA', ' Lesego Molotsi', '0119876543', 'lsgMolotsi@medisupply.co.za', '12 Voortrekker Rd, Pretoria'),
('PharmaCorp Distributors', 'Leleti Khumalo', '0123456789', 'leletiK@pharmacorp.co.za', '45 Church St, Johannesburg'),
('HealthLine Wholesalers', 'Candy Botha', '0219876543', 'candybotha@healthline.co.za', '8 Main Rd, Cape Town');

INSERT INTO medicines (name, company, medicine_type, price, quantity_in_stock, reorder_level, expiry_date, supplier_id) VALUES
('Panado 500mg', 'Adcock Ingram', 'Tablet', 35.50, 120, 20, '2027-03-15', 1),
('Amoxicillin 250mg', 'Aspen Pharmacare', 'Capsule', 89.99, 45, 15, '2026-11-20', 2),
('Corenza C', 'Aspen Pharmacare', 'Syrup', 65.00, 8, 10, '2026-09-30', 2),
('Insulin Actrapid', 'Novo Nordisk', 'Injection', 250.00, 30, 5, '2026-10-05', 3),
('Betamethasone Cream', 'GSK', 'Cream', 55.75, 25, 10, '2027-01-10', 1);