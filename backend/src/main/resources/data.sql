-- Seed Users (Passwords are BCrypt hashes)
-- admin / admin123 -> $2a$10$9GvF4zR16K65B9G0.g4TJeBqLd1p5.3T4jQ9eB5Z2y4o6h9K8L6e.
-- customer / customer123 -> $2a$10$8.UnVuG9HHgffUDAlk8GP.3nS8dK4tZc/5gX3x8qB7r6D5G4.j8F2
INSERT INTO users (username, password, email, contact_number, email_verified, mobile_verified, email_verification_code, mobile_otp, role) VALUES
('admin', '$2a$10$9GvF4zR16K65B9G0.g4TJeBqLd1p5.3T4jQ9eB5Z2y4o6h9K8L6e.', 'admin@shoestore.com', '+919999999999', TRUE, TRUE, NULL, NULL, 'ADMIN'),
('customer', '$2a$10$8.UnVuG9HHgffUDAlk8GP.3nS8dK4tZc/5gX3x8qB7r6D5G4.j8F2', 'customer@shoestore.com', '+919876543210', TRUE, TRUE, NULL, NULL, 'CUSTOMER');

-- Seed Products (Shoes: Campus, Nike, Skechers, Puma, Sparx)
INSERT INTO products (product_id, name, brand, category, color, size, price, description, rating, review_count, stock_count, trending_score) VALUES
-- Nike Products
('P101', 'Nike Air Max 90', 'Nike', 'Shoes', 'Black', 8, 8999.00, 'Classic Air Max cushioning with lightweight comfort.', 4.80, 185, 12, 95),
('P102', 'Nike Air Max 90', 'Nike', 'Shoes', 'Black', 9, 8999.00, 'Classic Air Max cushioning with lightweight comfort.', 4.80, 192, 8, 97),
('P103', 'Nike Air Max 90', 'Nike', 'Shoes', 'White', 8, 8999.00, 'Triple White classic design for lifestyle wear.', 4.70, 120, 15, 88),
('P104', 'Nike Revolution 6', 'Nike', 'Shoes', 'Grey', 8, 3699.00, 'Sustainable running shoe with plush cushioning.', 4.50, 240, 20, 75),
('P105', 'Nike Revolution 6', 'Nike', 'Shoes', 'Black', 10, 3699.00, 'Comfortable training and running shoe.', 4.60, 280, 5, 82),

-- Puma Products
('P201', 'Puma Smash v2', 'Puma', 'Shoes', 'Black', 8, 3499.00, 'Tennis-inspired casual sneaker with suede upper.', 4.40, 160, 14, 85),
('P202', 'Puma Smash v2', 'Puma', 'Shoes', 'Black', 9, 3499.00, 'Tennis-inspired casual sneaker with suede upper.', 4.40, 145, 18, 80),
('P203', 'Puma Smash v2', 'Puma', 'Shoes', 'White', 8, 3499.00, 'Classic clean court sneaker.', 4.30, 95, 0, 60), -- Out of stock
('P204', 'Puma Flyer Runner', 'Puma', 'Shoes', 'Blue', 8, 2999.00, 'Ultra-lightweight mesh upper running shoe.', 4.50, 320, 25, 92),
('P205', 'Puma Flyer Runner', 'Puma', 'Shoes', 'Black', 8, 2999.00, 'Comfortable lifestyle running shoe in Black.', 4.60, 410, 10, 96),

-- Campus Products
('P301', 'Campus Rodeo Pro', 'Campus', 'Shoes', 'Blue', 8, 1699.00, 'Stylish running shoes with responsive tech.', 4.20, 78, 30, 65),
('P302', 'Campus Rodeo Pro', 'Campus', 'Shoes', 'Black', 8, 1699.00, 'Responsive cushioning with breathable mesh.', 4.30, 89, 4, 70),
('P303', 'Campus First', 'Campus', 'Shoes', 'Grey', 9, 1499.00, 'Budget friendly running shoes for daily use.', 4.10, 50, 40, 50),

-- Skechers Products
('P401', 'Skechers Go Walk Max', 'Skechers', 'Shoes', 'Black', 8, 4999.00, 'Athletic mesh slip-on shoe with air-cooled goga mat cushion.', 4.90, 510, 11, 99),
('P402', 'Skechers Go Walk Max', 'Skechers', 'Shoes', 'Grey', 9, 4999.00, 'Comfort walking shoe with maximum support.', 4.85, 480, 7, 94),
('P403', 'Skechers D-Lites Fit', 'Skechers', 'Shoes', 'White', 8, 5999.00, 'Chunky retro style sneakers with memory foam.', 4.70, 310, 13, 89),

-- Sparx Products
('P501', 'Sparx Running SX04', 'Sparx', 'Shoes', 'Red', 8, 1199.00, 'Breathable running shoe with flexible sole.', 4.20, 190, 50, 72),
('P502', 'Sparx Running SX04', 'Sparx', 'Shoes', 'Black', 8, 1299.00, 'Durable training shoes for everyday activities.', 4.30, 210, 35, 78),
('P503', 'Sparx Casual SC09', 'Sparx', 'Shoes', 'Grey', 7, 999.00, 'Lightweight canvas sneakers for daily travel.', 4.00, 115, 15, 45),

-- Slippers (Campus and Puma)
('P601', 'Campus Comfort Slipper', 'Campus', 'Slippers', 'Blue', 8, 599.00, 'Ultra-soft memory foam slides for indoor and casual comfort.', 4.40, 85, 20, 65),
('P602', 'Puma Softcat Slides', 'Puma', 'Slippers', 'Black', 9, 1299.00, 'Water-resistant lightweight slides with textured grip.', 4.50, 110, 15, 78),

-- Sandals (Sparx and Nike)
('P701', 'Sparx Leather Sandals', 'Sparx', 'Sandals', 'Brown', 8, 1199.00, 'Casual leather strap sandals with supportive arch bed.', 4.30, 95, 25, 70),
('P702', 'Nike Canyon Sandals', 'Nike', 'Sandals', 'Black', 9, 4499.00, 'Rugged adventure sandals with adjustable triple-strapping.', 4.70, 60, 8, 88),

-- Party Wear Heels (Skechers and Puma)
('P801', 'Luxury Stiletto Heels', 'Skechers', 'Party Wear Heels', 'Pink', 7, 5999.00, 'Glow heels with cushioned footbed for party style and comfort.', 4.60, 45, 10, 82),
('P802', 'Puma Satin Pumps', 'Puma', 'Party Wear Heels', 'Black', 8, 6999.00, 'Chic pointed toe heels with premium satin finish.', 4.75, 30, 6, 85),

-- Long Boots (Nike)
('P901', 'Timberland Tall Leather Boots', 'Nike', 'Long Boots', 'Brown', 9, 9999.00, 'Tall waterproof leather boots with heavy-duty traction soles.', 4.80, 40, 12, 90),

-- Short Boots (Puma)
('P902', 'Chelsea Suede Boots', 'Puma', 'Short Boots', 'Grey', 8, 7499.00, 'Ankle-high slip-on suede boots with flexible elastic side gussets.', 4.60, 55, 10, 84);

-- Seed Orders
INSERT INTO orders (order_id, product_id, status, payment_status, delivery_date, username) VALUES
('ORD-1001', 'P101', 'Processing', 'PAID', '2026-07-02', 'customer'),
('ORD-1002', 'P201', 'Shipped', 'PAID', '2026-06-30', 'customer'),
('ORD-1003', 'P301', 'Delivered', 'PAID', '2026-06-25', 'customer'),
-- Let's also create an order for admin to test role security on orders (admin has an order)
('ORD-9001', 'P401', 'Processing', 'PAID', '2026-07-05', 'admin');
