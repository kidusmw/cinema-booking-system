-- ============================================================
-- V1: Initial Schema
-- Source: script.sql (converted from UTF-16LE to UTF-8)
-- This is the exact DDL from the original schema definition.
-- Seed data and operational statements (CREATE ROLE, GRANT)
-- are intentionally excluded — they belong in V2 or app code.
-- ============================================================

CREATE TABLE IF NOT EXISTS "user" (
    user_id       SERIAL PRIMARY KEY,
    first_name    VARCHAR(50),
    last_name     VARCHAR(50),
    email         VARCHAR(100) UNIQUE,
    phone         VARCHAR(20),
    username      VARCHAR(100) UNIQUE NOT NULL,
    password      VARCHAR(255) NOT NULL,
    role          VARCHAR(20) NOT NULL DEFAULT 'customer',
    created_at    TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS movie (
    movie_id      SERIAL PRIMARY KEY,
    title         VARCHAR(200) NOT NULL,
    genre         VARCHAR(100),
    duration_min  INT CHECK (duration_min > 0),
    rating        DECIMAL(3, 1) CHECK (rating >= 0 AND rating <= 10),
    poster_path   VARCHAR(255),
    language      VARCHAR(50) DEFAULT 'English',
    release_date  DATE,
    description   TEXT,
    is_active     BOOLEAN DEFAULT TRUE,
    created_at    TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS hall (
    hall_id       SERIAL PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    capacity      INT NOT NULL CHECK (capacity > 0),
    hall_type     VARCHAR(50) DEFAULT 'regular',
    created_at    TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS seat (
    seat_id       SERIAL PRIMARY KEY,
    hall_id       INT NOT NULL REFERENCES hall(hall_id) ON DELETE CASCADE,
    seat_number   VARCHAR(10) NOT NULL,
    seat_type     VARCHAR(30) DEFAULT 'regular',
    status        VARCHAR(20) DEFAULT 'available',
    UNIQUE(hall_id, seat_number)
);

CREATE TABLE IF NOT EXISTS showtime (
    show_id       SERIAL PRIMARY KEY,
    movie_id      INT NOT NULL REFERENCES movie(movie_id) ON DELETE CASCADE,
    hall_id       INT NOT NULL REFERENCES hall(hall_id) ON DELETE CASCADE,
    show_date     DATE NOT NULL,
    show_time     TIME NOT NULL,
    ticket_price  DECIMAL(10, 2) DEFAULT 0.00,
    UNIQUE(hall_id, show_date, show_time)
);

CREATE TABLE IF NOT EXISTS booking (
    booking_id    SERIAL PRIMARY KEY,
    user_id       INT NOT NULL REFERENCES "user"(user_id) ON DELETE CASCADE,
    show_id       INT NOT NULL REFERENCES showtime(show_id) ON DELETE CASCADE,
    movie_name    VARCHAR(200),
    status        VARCHAR(50) DEFAULT 'pending',
    total_amount  DECIMAL(10, 2) DEFAULT 0.00,
    booking_date  TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS booking_seat (
    booking_id    INT NOT NULL REFERENCES booking(booking_id) ON DELETE CASCADE,
    seat_id       INT NOT NULL REFERENCES seat(seat_id) ON DELETE CASCADE,
    price         DECIMAL(10, 2) DEFAULT 0.00,
    PRIMARY KEY (booking_id, seat_id)
);

CREATE TABLE IF NOT EXISTS payment (
    payment_id    SERIAL PRIMARY KEY,
    booking_id    INT NOT NULL UNIQUE REFERENCES booking(booking_id) ON DELETE CASCADE,
    status        VARCHAR(20) DEFAULT 'pending',
    amount        DECIMAL(10, 2) NOT NULL,
    verification_code VARCHAR(20),
    paid_at       TIMESTAMPTZ,
    created_at    TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================================
-- Indexes
-- ============================================================

CREATE INDEX IF NOT EXISTS idx_seat_hall ON seat(hall_id);
CREATE INDEX IF NOT EXISTS idx_showtime_movie ON showtime(movie_id);
CREATE INDEX IF NOT EXISTS idx_showtime_hall ON showtime(hall_id);
CREATE INDEX IF NOT EXISTS idx_showtime_date ON showtime(show_date);
CREATE INDEX IF NOT EXISTS idx_booking_user ON booking(user_id);
CREATE INDEX IF NOT EXISTS idx_booking_show ON booking(show_id);
CREATE INDEX IF NOT EXISTS idx_booking_status ON booking(status);
