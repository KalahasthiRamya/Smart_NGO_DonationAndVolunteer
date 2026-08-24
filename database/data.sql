-- ==================================================
-- Smart NGO Management Platform - Seed Data Script
-- Valid BCrypt hashes for demo credentials:
-- 'admin123'     -> $2a$10$gL6aiL4FvT8dNYpGiMjVoumW4AxHiyRPSyGAWSWhdidmG200PCerW
-- 'donor123'     -> $2a$10$eWARVG4tG.2bmH9yqsFd2OCaISY9xoRJ/v0LG4lfuaoBbilHEBpw2
-- 'volunteer123' -> $2a$10$okWHBjJ9M70ZO9UnwJ0Txu2xOSO.CAN1goRaBXjNsdK8UEe5MnFtG
-- ==================================================

-- Insert Users (Admin, Donors, Volunteers)
INSERT INTO users (id, name, email, password, phone, role, status, created_at) VALUES
(1, 'System Admin', 'admin@smartngo.com', '$2a$10$gL6aiL4FvT8dNYpGiMjVoumW4AxHiyRPSyGAWSWhdidmG200PCerW', '9876543210', 'ADMIN', 'ACTIVE', NOW()),
(2, 'Rahul Patil', 'rahul@gmail.com', '$2a$10$eWARVG4tG.2bmH9yqsFd2OCaISY9xoRJ/v0LG4lfuaoBbilHEBpw2', '9876543210', 'DONOR', 'ACTIVE', NOW()),
(3, 'Priya Sharma', 'priya@gmail.com', '$2a$10$eWARVG4tG.2bmH9yqsFd2OCaISY9xoRJ/v0LG4lfuaoBbilHEBpw2', '8765432109', 'DONOR', 'ACTIVE', NOW()),
(4, 'Amit Verma', 'amit@gmail.com', '$2a$10$eWARVG4tG.2bmH9yqsFd2OCaISY9xoRJ/v0LG4lfuaoBbilHEBpw2', '9123456780', 'DONOR', 'ACTIVE', NOW()),
(5, 'Neha Joshi', 'neha@gmail.com', '$2a$10$eWARVG4tG.2bmH9yqsFd2OCaISY9xoRJ/v0LG4lfuaoBbilHEBpw2', '9988776655', 'DONOR', 'INACTIVE', NOW()),
(6, 'Sneha Desai', 'sneha@gmail.com', '$2a$10$okWHBjJ9M70ZO9UnwJ0Txu2xOSO.CAN1goRaBXjNsdK8UEe5MnFtG', '9876501234', 'VOLUNTEER', 'ACTIVE', NOW()),
(7, 'Rohan Kulkarni', 'rohan@gmail.com', '$2a$10$okWHBjJ9M70ZO9UnwJ0Txu2xOSO.CAN1goRaBXjNsdK8UEe5MnFtG', '8765012345', 'VOLUNTEER', 'ACTIVE', NOW()),
(8, 'Pooja Mehta', 'pooja@gmail.com', '$2a$10$okWHBjJ9M70ZO9UnwJ0Txu2xOSO.CAN1goRaBXjNsdK8UEe5MnFtG', '7654012345', 'VOLUNTEER', 'ACTIVE', NOW()),
(9, 'Karan Singh', 'karan@gmail.com', '$2a$10$okWHBjJ9M70ZO9UnwJ0Txu2xOSO.CAN1goRaBXjNsdK8UEe5MnFtG', '6543012345', 'VOLUNTEER', 'INACTIVE', NOW());

-- Insert Donors
INSERT INTO donors (id, user_id, total_donations, status, created_at) VALUES
(1, 2, 15000.00, 'ACTIVE', '2024-01-10'),
(2, 3, 10500.00, 'ACTIVE', '2024-02-15'),
(3, 4, 7500.00, 'ACTIVE', '2024-03-01'),
(4, 5, 5000.00, 'INACTIVE', '2024-03-20');

-- Insert Volunteers
INSERT INTO volunteers (id, user_id, skills, status, joined_date) VALUES
(1, 6, 'Teaching', 'ACTIVE', '2024-01-10'),
(2, 7, 'Logistics', 'ACTIVE', '2024-01-12'),
(3, 8, 'Content Writing', 'ACTIVE', '2024-01-15'),
(4, 9, 'Event Mgmt', 'INACTIVE', '2024-01-18');

-- Insert Campaigns
INSERT INTO campaigns (id, name, description, category, target_amount, collected_amount, start_date, end_date, status) VALUES
(1, 'Education for All', 'Provide free school supplies and scholarships to underprivileged children', 'Education', 60000.00, 40000.00, '2024-01-01', '2024-12-31', 'ACTIVE'),
(2, 'Health & Wellness Drive', 'Free health checkup camps and essential medicines in rural areas', 'Health', 50000.00, 30000.00, '2024-02-01', '2024-11-30', 'ACTIVE'),
(3, 'Green Earth Initiative', 'Tree plantation and community recycling awareness programs', 'Environment', 40000.00, 20000.00, '2024-03-01', '2024-10-31', 'ACTIVE'),
(4, 'Clean Environment Campaign', 'Suburban sanitation and waste segregation awareness', 'Environment', 30000.00, 15000.00, '2024-04-01', '2024-09-30', 'ACTIVE');

-- Insert Donations
INSERT INTO donations (id, donor_id, campaign_id, amount, donation_date, payment_method, transaction_id, status) VALUES
(1, 1, 1, 15000.00, '2024-05-01 10:30:00', 'UPI', 'TXN_10001', 'SUCCESS'),
(2, 2, 2, 10500.00, '2024-04-28 14:15:00', 'CARD', 'TXN_10002', 'SUCCESS'),
(3, 3, 1, 7500.00, '2024-04-25 11:00:00', 'NET_BANKING', 'TXN_10003', 'SUCCESS'),
(4, 4, 3, 5000.00, '2024-04-20 16:45:00', 'UPI', 'TXN_10004', 'SUCCESS');

-- Insert Tasks
INSERT INTO tasks (id, title, description, campaign_id, assigned_volunteer_id, start_date, due_date, priority, status) VALUES
(1, 'Food Donation Drive', 'Organize and distribute meal boxes at community center', 2, 1, '2024-06-01', '2024-06-05', 'HIGH', 'COMPLETED'),
(2, 'Tree Plantation Drive', 'Plant 200 saplings in East Ridge Park', 3, 2, '2024-06-05', '2024-06-10', 'MEDIUM', 'IN_PROGRESS'),
(3, 'Teaching Workshop', 'Conduct math & science tutorial for grade 5 students', 1, 3, '2024-06-10', '2024-06-15', 'HIGH', 'ASSIGNED');

-- Insert Attendance
INSERT INTO attendance (id, volunteer_id, task_id, attendance_date, status) VALUES
(1, 1, 1, '2024-06-01', 'PRESENT'),
(2, 2, 2, '2024-06-05', 'PRESENT'),
(3, 3, 3, '2024-06-10', 'PRESENT');

-- Insert Notifications
INSERT INTO notifications (id, user_id, type, message, status, created_at) VALUES
(1, 2, 'DONATION', 'Thank you for your generous donation of ₹15,000 to Education for All.', 'UNREAD', NOW()),
(2, 6, 'TASK', 'You have been assigned to Food Donation Drive.', 'UNREAD', NOW());
