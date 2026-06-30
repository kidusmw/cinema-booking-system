package domain.service;

import domain.model.User;
import infrastructure.persistence.JdbcUserRepository;
import infrastructure.security.BCryptPasswordHasher;
import java.util.Optional;

public class AuthService {
    private final JdbcUserRepository userRepo;
    private final BCryptPasswordHasher hasher;

    public AuthService(JdbcUserRepository userRepo, BCryptPasswordHasher hasher) {
        this.userRepo = userRepo;
        this.hasher = hasher;
    }

    public Optional<User> login(String username, String plainPassword) {
        Optional<User> userOpt = userRepo.findByUsername(username);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        User user = userOpt.get();
        if (hasher.verify(plainPassword, user.getPassword())) {
            return Optional.of(user);
        }
        if (plainPassword.equals(user.getPassword())) {
            String hashed = hasher.hash(plainPassword);
            user.setPassword(hashed);
            userRepo.save(user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public User register(
            String username,
            String plainPassword,
            String firstName,
            String lastName,
            String role,
            String phone,
            String email) {
        String hashed = hasher.hash(plainPassword);
        User user = new User(null, firstName, lastName, username, hashed, role, phone, email);
        return userRepo.save(user);
    }
}
