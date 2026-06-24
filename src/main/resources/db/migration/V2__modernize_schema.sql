-- ============================================================
-- V2: Modernize Schema
-- Purpose: Widen PKs to BIGINT, add CHECK constraints,
--          add audit columns, fix missing indexes, and
--          widen verification_code for future hash storage.
-- ============================================================

-- 1. Widen all primary keys and foreign keys from INT to BIGINT
ALTER TABLE "user"   ALTER COLUMN user_id   TYPE BIGINT;
ALTER TABLE movie    ALTER COLUMN movie_id   TYPE BIGINT;
ALTER TABLE hall     ALTER COLUMN hall_id    TYPE BIGINT;
ALTER TABLE seat     ALTER COLUMN seat_id    TYPE BIGINT;
ALTER TABLE showtime ALTER COLUMN show_id    TYPE BIGINT;
ALTER TABLE booking  ALTER COLUMN booking_id TYPE BIGINT;
ALTER TABLE payment  ALTER COLUMN payment_id TYPE BIGINT;

ALTER TABLE seat     ALTER COLUMN hall_id    TYPE BIGINT;
ALTER TABLE showtime ALTER COLUMN movie_id   TYPE BIGINT;
ALTER TABLE showtime ALTER COLUMN hall_id    TYPE BIGINT;
ALTER TABLE booking  ALTER COLUMN user_id    TYPE BIGINT;
ALTER TABLE booking  ALTER COLUMN show_id    TYPE BIGINT;
ALTER TABLE booking_seat ALTER COLUMN booking_id TYPE BIGINT;
ALTER TABLE booking_seat ALTER COLUMN seat_id    TYPE BIGINT;
ALTER TABLE payment  ALTER COLUMN booking_id TYPE BIGINT;

-- 2. Add CHECK constraints for enumerated string columns
ALTER TABLE "user"   ADD CONSTRAINT chk_user_role
    CHECK (role IN ('admin', 'customer'));

ALTER TABLE booking  ADD CONSTRAINT chk_booking_status
    CHECK (status IN ('pending', 'confirmed', 'cancelled', 'no-show'));

ALTER TABLE payment  ADD CONSTRAINT chk_payment_status
    CHECK (status IN ('pending', 'paid', 'failed', 'refunded'));

ALTER TABLE seat     ADD CONSTRAINT chk_seat_type
    CHECK (seat_type IN ('regular', 'vip', 'disabled'));

ALTER TABLE seat     ADD CONSTRAINT chk_seat_status
    CHECK (status IN ('available', 'booked', 'reserved'));

ALTER TABLE hall     ADD CONSTRAINT chk_hall_type
    CHECK (hall_type IN ('regular', 'vip', 'imax'));

-- 3. Add NOT NULL where missing
ALTER TABLE "user" ALTER COLUMN email SET NOT NULL;

-- 4. Add audit columns (updated_at) to all tables
ALTER TABLE "user"   ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ DEFAULT NOW();
ALTER TABLE movie    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ DEFAULT NOW();
ALTER TABLE hall     ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ DEFAULT NOW();
ALTER TABLE seat     ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ DEFAULT NOW();
ALTER TABLE showtime ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ DEFAULT NOW();
ALTER TABLE booking  ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ DEFAULT NOW();
ALTER TABLE payment  ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ DEFAULT NOW();

-- 5. Widen verification_code for future hash compatibility
ALTER TABLE payment ALTER COLUMN verification_code TYPE VARCHAR(64);

-- 6. Add missing indexes for common query patterns
CREATE INDEX IF NOT EXISTS idx_user_username ON "user"(username);
CREATE INDEX IF NOT EXISTS idx_user_role ON "user"(role);
CREATE INDEX IF NOT EXISTS idx_payment_booking ON payment(booking_id);
