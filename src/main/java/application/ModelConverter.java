package application;

import Model.Admin;
import Model.Customer;
import domain.model.User;

public class ModelConverter {

    public static Model.User toOldUser(User user) {
        if (user == null) return null;
        if ("admin".equals(user.getRole())) {
            return new Admin(
                user.getUserId().intValue(),
                user.getFirstName(), user.getLastName(),
                user.getUsername(), user.getPassword(),
                "active", null, user.getRole(),
                user.getPhone(), user.getEmail(), null
            );
        }
        return new Customer(
            user.getUserId().intValue(),
            user.getFirstName(), user.getLastName(),
            user.getUsername(), user.getPassword(),
            "active", null, user.getRole(),
            user.getPhone(), user.getEmail(), null
        );
    }
}
