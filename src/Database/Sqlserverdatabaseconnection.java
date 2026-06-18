package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Sqlserverdatabaseconnection {
    private static final String URL =
            "jdbc:sqlserver://localhost:1433;" +
                    "databaseName=Cinema_Booking_System;" +
                    "encrypt=false;" +
                    "trustServerCertificate=true;";
    private static final String USER = "java_user";
    private static final String PASSWORD = "java123";

    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("CONNECTED SUCCESSFULLY!");
            return con;

        } catch (Exception e) {
            System.out.println("CONNECTION FAILED!");
            e.printStackTrace();
        }
        return null;
    }
    public static void printDatabaseInfo() {
        String sql = """
        SELECT 
            DB_NAME() AS current_database,
            SUSER_SNAME() AS login_name,
            @@SERVERNAME AS server_name
        """;

        try (Connection conn = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                System.out.println("Connected database: " + rs.getString("current_database"));
                System.out.println("Login name: " + rs.getString("login_name"));
                System.out.println("Server name: " + rs.getString("server_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}