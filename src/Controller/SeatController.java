package Controller;
import DAO.SeatDAO;
import DAO.SeatDAOimp;
import Model.Seat;
import View.SeatPage;

import javax.swing.*;
import java.util.List;

public class SeatController {

    SeatPage view;
    SeatDAO dao;

    public SeatController(SeatPage view) {
        this.view = view;
        this.dao = new SeatDAOimp();

        loadSeats();

        view.btnAdd.addActionListener(e -> addSeat());
        view.btnUpdate.addActionListener(e -> updateSeat());
        view.btnDelete.addActionListener(e -> deleteSeat());
        view.btnSearch.addActionListener(e -> searchSeat());
    }

    // ADD SEAT
    public void addSeat() {

        try {
            Seat s = new Seat();

            s.setSeatID(view.txtSeatID.getText());
            s.setSeatNumber(view.txtSeatNumber.getText());
            s.setSeatType(view.txtSeatType.getText());
            s.setStatus(view.txtStatus.getText());
            s.setShowID(view.txtShowID.getText());

            if (dao.addSeat(s)) {
                JOptionPane.showMessageDialog(view, "Seat Added");
                loadSeats();
                clearFields();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Invalid Input");
        }
    }

    // UPDATE SEAT
    public void updateSeat() {

        try {
            Seat s = new Seat();

            s.setSeatID(view.txtSeatID.getText());
            s.setSeatNumber(view.txtSeatNumber.getText());
            s.setSeatType(view.txtSeatType.getText());
            s.setStatus(view.txtStatus.getText());
            s.setShowID(view.txtShowID.getText());

            if (dao.updateSeat(s)) {
                JOptionPane.showMessageDialog(view, "Seat Updated");
                loadSeats();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Invalid Input");
        }
    }

    // DELETE SEAT
    public void deleteSeat() {

        String id = view.txtSeatID.getText();

        if (dao.deleteSeat(id)) {
            JOptionPane.showMessageDialog(view, "Seat Deleted");
            loadSeats();
        }
    }

    // SEARCH SEAT
    public void searchSeat() {

        String id = view.txtSeatID.getText();

        Seat s = dao.searchSeatById(id);

        if (s != null) {
            view.txtSeatNumber.setText(s.getSeatNumber());
            view.txtSeatType.setText(s.getSeatType());
            view.txtStatus.setText(s.getStatus());
            view.txtShowID.setText(s.getShowID());
        } else {
            JOptionPane.showMessageDialog(view, "Seat Not Found");
        }
    }

    // LOAD TABLE
    public void loadSeats() {

        view.model.setRowCount(0);

        List<Seat> list = dao.getAllSeats();

        for (Seat s : list) {
            view.model.addRow(new Object[]{
                    s.getSeatID(),
                    s.getSeatNumber(),
                    s.getSeatType(),
                    s.getStatus(),
                    s.getShowID()
            });
        }
    }

    public void clearFields() {
        view.txtSeatID.setText("");
        view.txtSeatNumber.setText("");
        view.txtSeatType.setText("");
        view.txtStatus.setText("");
        view.txtShowID.setText("");
    }
}

