package ui.model;

import java.util.Date;

public class User  {

    private int userID;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String loginStatus;
    private Date registrationDate;
    private String role;
    private String phone;
    private String email;

    public User() {}

    public User(int userID, String firstName, String lastName,
                String username, String password, String loginStatus,
                Date registrationDate, String role, String phone, String email) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.loginStatus = loginStatus;
        this.registrationDate = registrationDate;
        this.role = role;
        this.phone = phone;
        this.email = email;
    }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getLoginStatus() { return loginStatus; }
    public void setLoginStatus(String loginStatus) { this.loginStatus = loginStatus; }

    public Date getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(Date registrationDate) { this.registrationDate = registrationDate; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return "User{userID=" + userID + ", username='" + username +
                "', role='" + role + "', email='" + email + "'}";
    }
}
