package Model;

import java.util.Date;

public class Admin extends User {
    private String employeeID;

    public Admin() {
        super();
    }

    public Admin(int userID, String firstName, String lastName,
                 String username, String password, String loginStatus,
                 Date registrationDate, String role, String phone,
                 String email, String employeeID) {
        super(userID, firstName, lastName, username, password,
                loginStatus, registrationDate, role, phone, email);
        this.employeeID = employeeID;
    }

    public String getEmployeeID() { return employeeID; }
    public void setEmployeeID(String employeeID) { this.employeeID = employeeID; }

    @Override
    public String toString() {
        return "Admin{employeeID='" + employeeID + "', " + super.toString() + "}";
    }
}
