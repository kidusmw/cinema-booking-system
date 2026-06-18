package View;
import javax.swing.*;
import java.awt.*;

public class Userspace extends JFrame {
    public JTextField txtUserID;
    public JPasswordField txtPassword;

    public JButton btnLogin;

    public Userspace() {

        super("Cinema Booking System");


        setSize(500, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(255, 228, 235));

        setLayout(new BorderLayout(10, 10));

        // ================= TOP =================
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(new Color(255, 228, 235));

        JLabel title = new JLabel("WELCOME TO CINEMA SYSTEM");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(199, 21, 133));

        ImageIcon icon = new ImageIcon("images/cinema.png");
        Image img = icon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));

        topPanel.add(title);
        topPanel.add(imageLabel);

        add(topPanel, BorderLayout.NORTH);

        // ================= FORM =================
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBackground(new Color(255, 228, 235));

        JLabel lblUser = new JLabel("User ID");
        lblUser.setFont(new Font("Arial", Font.BOLD, 14));

        txtUserID = new JTextField();

        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Arial", Font.BOLD, 14));

        txtPassword = new JPasswordField();

        formPanel.add(lblUser);
        formPanel.add(txtUserID);

        formPanel.add(lblPass);
        formPanel.add(txtPassword);

        add(formPanel, BorderLayout.CENTER);

        // ================= BUTTON =================
        btnLogin = new JButton("LOGIN");

        btnLogin.setBackground(new Color(255, 105, 180));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(255, 228, 235));
        btnPanel.add(btnLogin);

        add(btnPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}


