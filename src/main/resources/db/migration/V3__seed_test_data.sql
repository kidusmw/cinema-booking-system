-- ============================================================
-- V3: Seed Test Data
-- Purpose: Populate all tables with realistic demo data.
-- Idempotent: uses ON CONFLICT DO NOTHING so re-runs are safe.
-- ============================================================

-- 1. Users (admin + customer) ---------------------------------
INSERT INTO "user" (user_id, first_name, last_name, email, phone, username, password, role)
VALUES
    (1, 'Admin', 'User', 'admin@cinema.com', '+251911111111', 'admin',
     '$2a$10$N9qo8uLOickgx2ZMRZoMy.MrqZ9B4h3QzKjN5qXJZwLqDx7nM6fK', 'admin'),
    (2, 'John', 'Doe', 'customer@cinema.com', '+251922222222', 'customer',
     '$2a$10$N9qo8uLOickgx2ZMRZoMy.MrqZ9B4h3QzKjN5qXJZwLqDx7nM6fK', 'customer')
ON CONFLICT (user_id) DO NOTHING;

-- 2. Movies (5 popular films) ----------------------------------
INSERT INTO movie (movie_id, title, genre, duration_min, rating, description, language, poster_path)
VALUES
    (1, 'Inception', 'Sci-Fi', 148, 8.8,
     'A thief who steals corporate secrets through dream-sharing technology is given the task of planting an idea into the mind of a C.E.O.',
     'English', NULL),
    (2, 'The Matrix', 'Sci-Fi', 136, 8.7,
     'When a beautiful stranger leads computer hacker Neo to a forbidding underworld, he discovers the shocking truth—the life he knows is the elaborate deception of an evil cyber-intelligence.',
     'English', NULL),
    (3, 'The Dark Knight', 'Action', 152, 9.0,
     'When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.',
     'English', NULL),
    (4, 'Interstellar', 'Sci-Fi', 169, 8.7,
     'When Earth becomes uninhabitable in the future, a farmer and ex-NASA pilot is tasked with piloting a spacecraft along with a team of researchers to find a new planet for humans.',
     'English', NULL),
    (5, 'Parasite', 'Thriller', 132, 8.5,
     'Greed and class discrimination threaten the newly formed symbiotic relationship between the wealthy Park family and the destitute Kim clan.',
     'Korean', NULL)
ON CONFLICT (movie_id) DO NOTHING;

-- 3. Halls (2 halls) -------------------------------------------
INSERT INTO hall (hall_id, name, capacity, hall_type)
VALUES
    (1, 'Main Hall', 50, 'regular'),
    (2, 'VIP Room',  30, 'vip')
ON CONFLICT (hall_id) DO NOTHING;

-- 4. Seats (generated for each hall) ----------------------------
-- Hall 1: rows A–E, seats 1–10 per row (50 regular seats)
INSERT INTO seat (hall_id, seat_number, seat_type, status)
SELECT 1, chr(64 + (s - 1) / 10 + 1) || ((s - 1) % 10 + 1), 'regular', 'available'
FROM generate_series(1, 50) AS s
ON CONFLICT (hall_id, seat_number) DO NOTHING;

-- Hall 2: rows A–C, seats 1–10 per row (30 VIP seats)
INSERT INTO seat (hall_id, seat_number, seat_type, status)
SELECT 2, chr(64 + (s - 1) / 10 + 1) || ((s - 1) % 10 + 1), 'vip', 'available'
FROM generate_series(1, 30) AS s
ON CONFLICT (hall_id, seat_number) DO NOTHING;

-- 5. Showtimes (5 showtimes over next 3 days) -------------------
INSERT INTO showtime (show_id, movie_id, hall_id, show_date, show_time, ticket_price)
VALUES
    (1, 1, 1, CURRENT_DATE + 1, '14:00:00', 10.00),
    (2, 2, 1, CURRENT_DATE + 1, '18:00:00', 12.00),
    (3, 3, 2, CURRENT_DATE + 2, '15:00:00', 15.00),
    (4, 4, 1, CURRENT_DATE + 2, '20:00:00', 14.00),
    (5, 5, 2, CURRENT_DATE + 3, '16:00:00', 8.00)
ON CONFLICT (show_id) DO NOTHING;

-- 6. Bookings (2 confirmed bookings for customer) ---------------
INSERT INTO booking (booking_id, user_id, show_id, movie_name, status, total_amount, booking_date)
VALUES
    (1, 2, 1, 'Inception', 'confirmed', 20.00, NOW()),
    (2, 2, 3, 'The Dark Knight', 'confirmed', 30.00, NOW())
ON CONFLICT (booking_id) DO NOTHING;

-- 7. Booking_Seats (2 seats per booking) ------------------------
-- Booking 1 → seats A1, A2 in hall 1 (seat_ids 1, 2)
INSERT INTO booking_seat (booking_id, seat_id, price)
VALUES
    (1, (SELECT seat_id FROM seat WHERE hall_id = 1 AND seat_number = 'A1'), 10.00),
    (1, (SELECT seat_id FROM seat WHERE hall_id = 1 AND seat_number = 'A2'), 10.00),
    (2, (SELECT seat_id FROM seat WHERE hall_id = 2 AND seat_number = 'A1'), 15.00),
    (2, (SELECT seat_id FROM seat WHERE hall_id = 2 AND seat_number = 'A2'), 15.00)
ON CONFLICT (booking_id, seat_id) DO NOTHING;

-- 8. Payments (2 paid payments linked to bookings) --------------
INSERT INTO payment (payment_id, booking_id, status, amount, verification_code, paid_at)
VALUES
    (1, 1, 'paid', 20.00, 'PAY-001', NOW()),
    (2, 2, 'paid', 30.00, 'PAY-002', NOW())
ON CONFLICT (payment_id) DO NOTHING;

-- 9. Update seat statuses to 'booked' for used seats ------------
UPDATE seat SET status = 'booked'
WHERE seat_id IN (
    SELECT seat_id FROM booking_seat WHERE booking_id IN (1, 2)
);

-- 10. Sync sequences after explicit ID inserts ------------------
SELECT setval('"user_user_id_seq"',     COALESCE((SELECT MAX(user_id)     FROM "user"), 1));
SELECT setval('movie_movie_id_seq',      COALESCE((SELECT MAX(movie_id)    FROM movie), 1));
SELECT setval('hall_hall_id_seq',        COALESCE((SELECT MAX(hall_id)    FROM hall), 1));
SELECT setval('showtime_show_id_seq',    COALESCE((SELECT MAX(show_id)    FROM showtime), 1));
SELECT setval('booking_booking_id_seq',  COALESCE((SELECT MAX(booking_id) FROM booking), 1));
SELECT setval('payment_payment_id_seq',  COALESCE((SELECT MAX(payment_id) FROM payment), 1));
