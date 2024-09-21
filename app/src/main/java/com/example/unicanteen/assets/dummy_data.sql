-- Insert users
INSERT INTO user (userName, password, name, email, phoneNumber) VALUES
('user1', 'abc', 'John Doe', 'john@example.com', '1234567890'),
('user2', 'abc', 'Jane Doe', 'jane@example.com', '0987654321'),
('user3', 'abc', 'Alice Smith', 'alice@example.com', '5555555555'),
('user4', 'abc', 'Bob Johnson', 'bob@example.com', '4444444444');

-- Insert sellers
INSERT INTO seller (userId, shopType, shopName, createdDate, shopStatus) VALUES
(1, 'Restaurant', 'John\'s Diner', CURRENT_DATE, 'Open'),
(2, 'Cafe', 'Jane\'s Cafe', CURRENT_DATE, 'Open'),
(3, 'Grocery', 'Alice\'s Grocery', CURRENT_DATE, 'Open'),
(4, 'Bakery', 'Bob\'s Bakery', CURRENT_DATE, 'Closed');

-- Insert food items
INSERT INTO foodList (sellerId, foodName, type, price, createDate, modifyDate, status) VALUES
(1, 'Cheeseburger', 'Main Course', 8.99, CURRENT_DATE, CURRENT_DATE, 'Available'),
(1, 'Caesar Salad', 'Salad', 6.99, CURRENT_DATE, CURRENT_DATE, 'Available'),
(2, 'Cappuccino', 'Beverage', 3.50, CURRENT_DATE, CURRENT_DATE, 'Available'),
(3, 'Apples', 'Fruit', 0.99, CURRENT_DATE, CURRENT_DATE, 'Available'),
(4, 'Croissant', 'Pastry', 2.50, CURRENT_DATE, CURRENT_DATE, 'Unavailable');

-- Insert orders
INSERT INTO orders (userId, orderType, tableNo, totalPrice, status, createDate) VALUES
(1, 'Pick up', NULL, 15.98, 'Completed', CURRENT_DATE),
(2, 'Delivery', NULL, 9.99, 'Pending', CURRENT_DATE);

-- Insert order lists
INSERT INTO orderList (sellerId, foodId, userId, orderId, qty, totalPrice, status, createDate) VALUES
(1, 1, 1, 1, 1, 8.99, 'Complete', CURRENT_DATE),
(1, 2, 1, 1, 1, 6.99, 'Complete', CURRENT_DATE),
(2, 3, 2, 2, 1, 3.50, 'Waiting', CURRENT_DATE);

-- Insert payments
INSERT INTO payment (orderId, userId, totalAmt, createDate, status) VALUES
(1, 1, 15.98, CURRENT_DATE, 'Paid'),
(2, 2, 9.99, CURRENT_DATE, 'Pending');

-- Insert payment details
INSERT INTO paymentDetails (orderId, orderListId, sellerId, paymentId, amount, createDate, status) VALUES
(1, 1, 1, 1, 15.98, CURRENT_DATE, 'Completed'),
(2, 3, 2, 2, 3.50, CURRENT_DATE, 'Pending');
