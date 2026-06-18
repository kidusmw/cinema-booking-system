package Model;

import java.util.Date;

public class Customer extends User {

    private String address;

    public Customer() {
        super();
    }

    public Customer(int userID, String firstName, String lastName,
                    String username, String password, String loginStatus,
                    Date registrationDate, String role, String phone,
                    String email, String address) {

        super(userID, firstName, lastName, username, password,
                loginStatus, registrationDate, role, phone, email);

        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    @Override
    public String toString() {
        return "Customer{address='" + address + "', " + super.toString() + "}";
    }
}
