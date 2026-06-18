package View;
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
public class BookingPage extends JFrame {
    public JTextField txtSeatID;
    public JTextField txtShowID;
    public JTextField txtAmount;

    public JButton btnBook;

    public JTable table;
    public DefaultTableModel model;

    public BookingPage() {

        super("Booking Page");

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(245, 245, 245));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setOpaque(false);
        add(mainPanel);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        topPanel.setOpaque(false);

        JLabel title = new JLabel("BOOK MOVIE TICKET");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(new Color(199, 21, 133));

        topPanel.add(loadScaledIcon("image/Tickate - Copy.png", 120, 120));
        topPanel.add(title);
        topPanel.add(loadScaledIcon("image/booking.png", 150, 100));

        mainPanel.add(topPanel, BorderLayout.NORTH);
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Enter Booking Details"));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Seat ID:"), gbc);

        txtSeatID = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtSeatID, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Show ID:"), gbc);

        txtShowID = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtShowID, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Amount:"), gbc);

        txtAmount = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtAmount, gbc);

        btnBook = new JButton("Book Seat");
        styleButton(btnBook);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(btnBook, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        model = new DefaultTableModel(
                new String[]{"BookingID", "SeatID", "ShowID", "Amount", "Date", "Status"}, 0);

        table = new JTable(model);
        table.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Existing Bookings"));

        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JLabel loadScaledIcon(String path, int width, int height) {

        ImageIcon icon = new ImageIcon(path);

        Image img = icon.getImage().getScaledInstance(
                width, height, Image.SCALE_SMOOTH);

        return new JLabel(new ImageIcon(img));
    }

    public void styleButton(JButton btn) {
        btn.setBackground(new Color(255, 105, 180));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setPreferredSize(new Dimension(150, 40));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BookingPage::new);
    }
}