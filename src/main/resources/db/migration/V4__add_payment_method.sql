-- ============================================================
-- V4: Add payment_method column
-- Purpose: Add the missing payment_method column to the payment
--          table, matching JdbcPaymentRepository expectations.
-- ============================================================

ALTER TABLE payment
  ADD COLUMN IF NOT EXISTS payment_method VARCHAR(50) DEFAULT 'unknown';
