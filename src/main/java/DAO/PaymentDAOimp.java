package DAO;

import Database.DatabaseConnection;
import Model.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOimp implements PaymentDAO {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PaymentDAOimp.class);

    public boolean addPayment(Payment payment) {
        String sql =
            "INSERT INTO Payment (Status, Total_Amount, Verification_code, Booking_ID) VALUES (?, ?, ?, ?)";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, payment.getStatus());

            ps.setDouble(2, payment.getTotalAmount());
            ps.setString(3, payment.getOtp());
            ps.setInt(4, payment.getBookingID());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                log.debug("Payment inserted successfully");
                return true;
            } else {
                log.warn("Database rejected the insert. Check if BookingID exists.");
                return false;
            }
        } catch (SQLException e) {
            log.error("SQL EXCEPTION: {}", e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean updatePayment(Payment payment) {
        String sql =
            "UPDATE Payment SET Status=?, TotalAmount=?, OTP=?, BookingID=? WHERE PaymentID=?";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, payment.getStatus());
            ps.setDouble(2, payment.getTotalAmount());
            ps.setString(3, payment.getOtp());
            ps.setInt(4, payment.getBookingID());
            ps.setString(5, payment.getPaymentID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Database operation failed", e);
        }
        return false;
    }

    @Override
    public boolean deletePayment(String paymentID) {
        String sql = "DELETE FROM Payment WHERE PaymentID=?";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, paymentID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Database operation failed", e);
        }
        return false;
    }

    @Override
    public Payment searchPaymentById(String paymentID) {
        String sql = "SELECT * FROM Payment WHERE PaymentID=?";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, paymentID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPayment(rs);
                }
            }
        } catch (SQLException e) {
            log.error("Database operation failed", e);
        }
        return null;
    }

    @Override
    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM Payment";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            log.error("Database operation failed", e);
        }
        return list;
    }

    @Override
    public Payment getPaymentByBooking(String bookingID) {
        String sql = "SELECT * FROM Payment WHERE BookingID=?";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, bookingID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPayment(rs);
                }
            }
        } catch (SQLException e) {
            log.error("Database operation failed", e);
        }
        return null;
    }

    @Override
    public boolean verifyPayment(String paymentID, String otp) {
        String sql = "SELECT OTP FROM Payment WHERE PaymentID=?";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, paymentID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedOtp = rs.getString("OTP");
                    if (otp != null && otp.equals(storedOtp)) {
                        updatePaymentStatus(paymentID, "PAID");
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Database operation failed", e);
        }
        return false;
    }

    @Override
    public boolean updatePaymentStatus(String paymentID, String status) {
        String sql = "UPDATE Payment SET Status=? WHERE PaymentID=?";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, status);
            ps.setString(2, paymentID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Database operation failed", e);
        }
        return false;
    }

    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setPaymentID(rs.getString("PaymentID"));
        p.setStatus(rs.getString("Status"));
        p.setTotalAmount(rs.getDouble("TotalAmount"));
        p.setOtp(rs.getString("OTP"));
        p.setBookingID(Integer.parseInt(rs.getString("BookingID")));
        return p;
    }
}
