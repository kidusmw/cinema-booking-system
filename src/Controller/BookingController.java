package Controller;

import DAO.BookingDAO;
import DAO.BookingDAOimp;
import DAO.SeatDAO;
import DAO.SeatDAOimp;
import Model.Booking;
import Model.UserSession;
import View.BookingPage;

import javax.swing.*;
import java.util.Date;

public class BookingController {
    BookingPage view;
    BookingDAO bookingDAO;
    SeatDAO seatDAO;

    public BookingController(BookingPage view) {
        this.view = view;
        this.bookingDAO = new BookingDAOimp();
        this.seatDAO = new SeatDAOimp();
        view.txtAmount.setText("0.00");

        view.btnBook.addActionListener(e -> makeBooking());
    }

    public void makeBooking() {
        try {
            String seatID = view.txtSeatID.getText().trim();
            String showIDText = view.txtShowID.getText().trim();
            String amountText = view.txtAmount.getText().trim();

            if (seatID.isEmpty() || showIDText.isEmpty() || amountText.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Please fill in all fields!");
                return;
            }

            // Validates that the input text is a number before proceeding
            int parsedShowID = Integer.parseInt(showIDText);

            if (!seatDAO.isSeatAvailable(seatID)) {
                JOptionPane.showMessageDialog(view, "Seat is already booked!");
                return;
            }

            Booking b = new Booking();
            b.setBookingDate(new java.sql.Date(System.currentTimeMillis())); // Match java.sql.Date
            b.setBookingStatus("CONFIRMED");

            // ✅ FIXED: Passing the showIDText String directly to match the Booking model!
            b.setShowID(showIDText);

            b.setUserID(UserSession.getUserID());
            b.setMovieName("Selected Movie");

            if (bookingDAO.addBooking(b)) {
                seatDAO.updateSeatStatus(seatID, "BOOKED");
                JOptionPane.showMessageDialog(view, "Booking Successful!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(view, "Booking Failed");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Invalid format! Please enter numbers for Show ID and Amount.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "An error occurred: " + e.getMessage());
        }
    }

    public void clearFields() {
        view.txtSeatID.setText("");
        view.txtShowID.setText("");
        view.txtAmount.setText("0.00");
    }
}