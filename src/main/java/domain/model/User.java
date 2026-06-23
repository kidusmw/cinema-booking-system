package domain.model;

public class User {
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String role;
    private String phone;
    private String email;

    public User() {}

    public User(Long userId, String firstName, String lastName, String username,
                String password, String role, String phone, String email) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.email = email;
    }

    public boolean isAdmin() { return "admin".equals(role); }

    public boolean isCustomer() { return "customer".equals(role); }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
