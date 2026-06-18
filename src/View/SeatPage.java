package View;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SeatPage extends JFrame {
    public JTextField txtSeatID;
    public JTextField txtSeatNumber;
    public JTextField txtSeatType;
    public JTextField txtStatus;
    public JTextField txtShowID;

    public JButton btnAdd;
    public JButton btnUpdate;
    public JButton btnDelete;
    public JButton btnSearch;

    public JTable table;
    public DefaultTableModel model;

    public SeatPage() {

        super("Seat Page");

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(255, 228, 235));

        setLayout(new BorderLayout(10, 10));

        // ================= TOP =================
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(new Color(255, 228, 235));

        JLabel title = new JLabel("SEAT MANAGEMENT");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(new Color(199, 21, 133));

        ImageIcon icon = new ImageIcon("images/seat.png");
        Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));

        topPanel.add(title);
        topPanel.add(imageLabel);

        add(topPanel, BorderLayout.NORTH);

        // ================= FORM =================
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(new Color(255, 228, 235));

        formPanel.add(new JLabel("Seat ID"));
        txtSeatID = new JTextField();
        formPanel.add(txtSeatID);

        formPanel.add(new JLabel("Seat Number"));
        txtSeatNumber = new JTextField();
        formPanel.add(txtSeatNumber);

        formPanel.add(new JLabel("Seat Type"));
        txtSeatType = new JTextField();
        formPanel.add(txtSeatType);

        formPanel.add(new JLabel("Status"));
        txtStatus = new JTextField();
        formPanel.add(txtStatus);

        formPanel.add(new JLabel("Show ID"));
        txtShowID = new JTextField();
        formPanel.add(txtShowID);

        add(formPanel, BorderLayout.CENTER);

        // ================= BUTTONS =================
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(255, 228, 235));

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnSearch = new JButton("Search");

        styleButton(btnAdd);
        styleButton(btnUpdate);
        styleButton(btnDelete);
        styleButton(btnSearch);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnSearch);

        add(buttonPanel, BorderLayout.SOUTH);

        // ================= TABLE =================
        model = new DefaultTableModel();

        model.setColumnIdentifiers(new String[]{
                "SeatID",
                "SeatNumber",
                "SeatType",
                "Status",
                "ShowID"
        });

        table = new JTable(model);
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.EAST);

        setVisible(true);
    }

    // ================= STYLE BUTTON =================
    public void styleButton(JButton button) {
        button.setBackground(new Color(255,105,180));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }
}
