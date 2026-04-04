-- Chèn các Roles
INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO roles (name) VALUES ('ROLE_USER') ON DUPLICATE KEY UPDATE name=name;

-- Chèn Account Admin (Mật khẩu: admin123 đã mã hóa BCrypt)
-- Mật khẩu bcrypt cho 'admin123' có thể dùng: $2a$10$wY1twTgPQzR4I5c9N/X4P.vD/dD8HhRYV4M9/H/vL/T.f3K2jC0XW
INSERT INTO accounts (username, password, enabled, full_name, email, role_id) 
SELECT 'admin', '$2a$10$wY1twTgPQzR4I5c9N/X4P.vD/dD8HhRYV4M9/H/vL/T.f3K2jC0XW', 1, 'Administrator', 'admin@store.com', r.id 
FROM roles r WHERE r.name = 'ROLE_ADMIN'
ON DUPLICATE KEY UPDATE full_name='Administrator';

-- Chèn Account User (Mật khẩu: user123)
-- Mật khẩu bcrypt cho 'user123'
INSERT INTO accounts (username, password, enabled, full_name, email, role_id) 
SELECT 'user', '$2a$10$X0.h2WlR1mUfVl1Y3o.v4.vD/dD8HhRYV4M9/H/vL/T.f3K2jC0XW', 1, 'Test User', 'user@store.com', r.id 
FROM roles r WHERE r.name = 'ROLE_USER'
ON DUPLICATE KEY UPDATE full_name='Test User';

-- Chèn Categories
INSERT INTO categories (name) VALUES ('Điện thoại') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO categories (name) VALUES ('Laptop') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO categories (name) VALUES ('Phụ kiện') ON DUPLICATE KEY UPDATE name=name;

-- Chèn Products (Sử dụng category_id tương đối)
INSERT INTO products (name, price, description, image, category_id) 
SELECT 'iPhone 15 Pro Max', 30000000, 'Apple iPhone 15 Pro Max 256GB', '', c.id FROM categories c WHERE c.name = 'Điện thoại'
ON DUPLICATE KEY UPDATE price=30000000;

INSERT INTO products (name, price, description, image, category_id) 
SELECT 'Samsung Galaxy S24 Ultra', 28000000, 'Samsung Galaxy S24 Ultra 256GB AI', '', c.id FROM categories c WHERE c.name = 'Điện thoại'
ON DUPLICATE KEY UPDATE price=28000000;

INSERT INTO products (name, price, description, image, category_id) 
SELECT 'MacBook Pro 14 M3', 40000000, 'Apple MacBook Pro 14 inch M3 2023', '', c.id FROM categories c WHERE c.name = 'Laptop'
ON DUPLICATE KEY UPDATE price=40000000;

INSERT INTO products (name, price, description, image, category_id) 
SELECT 'Dell XPS 15', 35000000, 'Dell XPS 15 9530 Core i7', '', c.id FROM categories c WHERE c.name = 'Laptop'
ON DUPLICATE KEY UPDATE price=35000000;

INSERT INTO products (name, price, description, image, category_id) 
SELECT 'AirPods Pro 2', 6000000, 'Tai nghe Apple AirPods Pro 2', '', c.id FROM categories c WHERE c.name = 'Phụ kiện'
ON DUPLICATE KEY UPDATE price=6000000;

INSERT INTO products (name, price, description, image, category_id) 
SELECT 'Sạc Anker 65W', 1000000, 'Củ sạc nhanh Anker 65W 3 cổng', '', c.id FROM categories c WHERE c.name = 'Phụ kiện'
ON DUPLICATE KEY UPDATE price=1000000;

INSERT INTO products (name, price, description, image, category_id) 
SELECT 'Ốp lưng iPhone 15', 500000, 'Ốp lưng chính hãng Apple', '', c.id FROM categories c WHERE c.name = 'Phụ kiện'
ON DUPLICATE KEY UPDATE price=500000;
