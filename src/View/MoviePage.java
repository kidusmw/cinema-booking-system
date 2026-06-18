package View;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
public class MoviePage extends JFrame {
    public JTextField txtMovieID;
    public JTextField txtTitle;
    public JTextField txtGenre;
    public JTextField txtDuration;
    public JTextField txtRating;

    public JButton btnAdd;
    public JButton btnUpdate;
    public JButton btnDelete;
    public JButton btnSearch;

    public JTable table;
    public DefaultTableModel model;

    public MoviePage() {

        super("Movie Page");

        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(255, 228, 235));

        setLayout(new BorderLayout(10, 10));

        // ================= TOP PANEL =================
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(255, 228, 235));
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JLabel title = new JLabel("MOVIE MANAGEMENT");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(new Color(199, 21, 133));

        topPanel.add(title);

// 1
        topPanel.add(createMovie("image/The other woman.png", "The Other Woman"));

// 2
        topPanel.add(createMovie("image/Pretty woman.png", "Pretty Woman"));

// 3
        topPanel.add(createMovie("image/Chapter by the dozen.png", "Cheaper by the Dozen"));

// 4
        topPanel.add(createMovie("image/P.S._I_Still_Love_You_Poster.png", "To All the Boys I've Loved Before"));

// 5
        topPanel.add(createMovie("image/thinking like man.png", "Think Like a Man"));
        topPanel.add(createMovie("image/Pride and Prejudice.png", "Pride and Prejudice"));
        add(topPanel, BorderLayout.NORTH);

        // ================= FORM PANEL =================
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(new Color(255, 228, 235));

        formPanel.add(new JLabel("Movie ID"));
        txtMovieID = new JTextField();
        formPanel.add(txtMovieID);

        formPanel.add(new JLabel("Title"));
        txtTitle = new JTextField();
        formPanel.add(txtTitle);

        formPanel.add(new JLabel("Genre"));
        txtGenre = new JTextField();
        formPanel.add(txtGenre);

        formPanel.add(new JLabel("Duration"));
        txtDuration = new JTextField();
        formPanel.add(txtDuration);

        formPanel.add(new JLabel("Rating"));
        txtRating = new JTextField();
        formPanel.add(txtRating);

        add(formPanel, BorderLayout.CENTER);

        // ================= BUTTON PANEL =================
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
        model = new DefaultTableModel(
                new String[]{"Movie ID", "Title", "Genre", "Duration", "Rating"}, 0);

        table = new JTable(model);
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.EAST);

        setVisible(true);
    }

    // ===== MOVIE PANEL METHOD =====
    private JPanel createMoviePanel(String path, String name) {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 228, 235));

        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(80, 100, Image.SCALE_SMOOTH);

        JLabel pic = new JLabel(new ImageIcon(img));
        JLabel label = new JLabel(name);

        label.setFont(new Font("Arial", Font.PLAIN, 12));

        panel.add(pic);
        panel.add(label);

        return panel;
    }

    public void styleButton(JButton button) {

        button.setBackground(new Color(255,105,180));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }
    private JPanel createMovie(String path, String name) {

        JPanel p = new JPanel();
        p.setBackground(new Color(255, 228, 235));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(80, 100, Image.SCALE_SMOOTH);

        JLabel pic = new JLabel(new ImageIcon(img));
        JLabel label = new JLabel(name);

        pic.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        label.setFont(new Font("Arial", Font.PLAIN, 11));

        p.add(pic);
        p.add(label);

        return p;
    }

    public static void main(String[] args) {
        new MoviePage();
    }
}