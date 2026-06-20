package DAO;

import Model.User;
import java.util.List;

public interface UserDAO {

    boolean addUser(User user);

    boolean updateUser(User user);

    boolean deleteUser(int userID);

    User searchUserByUsername(String username);

    boolean isUsernameExists(String username);

    User login(String username, String password);

    List<User> getAllUsers();
}

