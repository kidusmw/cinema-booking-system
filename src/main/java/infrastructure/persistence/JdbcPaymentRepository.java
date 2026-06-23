package infrastructure.persistence;

import domain.model.Payment;
import domain.port.PaymentRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcPaymentRepository implements PaymentRepository {
    private final ConnectionProvider connectionProvider;

    public JdbcPaymentRepository(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public Payment save(Payment payment) {
        if (payment.getPaymentId() == null) {
            return insert(payment);
        } else {
            return update(payment);
        }
    }

    private Payment insert(Payment payment) {
        String sql =
                "INSERT INTO payment (booking_id, total_amount, status, verification_code, payment_method, paid_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps =
                        conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, payment.getBookingId());
            ps.setDouble(2, payment.getTotalAmount());
            ps.setString(3, payment.getStatus());
            ps.setString(4, payment.getOtp());
            ps.setString(5, payment.getPaymentMethod());
            ps.setTimestamp(
                    6,
                    payment.getPaymentDate() != null
                            ? Timestamp.valueOf(payment.getPaymentDate())
                            : null);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) payment.setPaymentId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert payment", e);
        }
        return payment;
    }

    private Payment update(Payment payment) {
        String sql =
                "UPDATE payment SET booking_id=?, total_amount=?, status=?, verification_code=?, payment_method=? WHERE payment_id=?";
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, payment.getBookingId());
            ps.setDouble(2, payment.getTotalAmount());
            ps.setString(3, payment.getStatus());
            ps.setString(4, payment.getOtp());
            ps.setString(5, payment.getPaymentMethod());
            ps.setLong(6, payment.getPaymentId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update payment", e);
        }
        return payment;
    }

    @Override
    public Optional<Payment> findById(Long id) {
        String sql = "SELECT * FROM payment WHERE payment_id = ?";
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find payment by id", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Payment> findAll() {
        String sql = "SELECT * FROM payment ORDER BY payment_id DESC";
        List<Payment> list = new ArrayList<>();
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all payments", e);
        }
        return list;
    }

    @Override
    public Optional<Payment> findByBookingId(Long bookingId) {
        String sql = "SELECT * FROM payment WHERE booking_id = ?";
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find payment by booking id", e);
        }
        return Optional.empty();
    }

    private Payment mapRow(ResultSet rs) throws SQLException {
        Payment payment =
                new Payment(
                        rs.getLong("payment_id"),
                        rs.getLong("booking_id"),
                        rs.getDouble("total_amount"),
                        rs.getString("status"),
                        rs.getString("verification_code"),
                        rs.getString("payment_method"));
        Timestamp ts = rs.getTimestamp("paid_at");
        if (ts != null) {
            payment.setPaymentDate(ts.toLocalDateTime());
        }
        return payment;
    }
}
