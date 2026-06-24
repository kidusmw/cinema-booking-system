package domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import domain.model.User;
import domain.port.UserRepository;
import infrastructure.security.PasswordHasher;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock UserRepository userRepo;

    @Mock PasswordHasher hasher;

    @InjectMocks AuthService authService;

    @Test
    void loginSucceedsWithHashedPassword() {
        User user =
                new User(1L, "A", "B", "testuser", "$2a$10$hashed", "customer", "123", "a@b.com");
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(hasher.verify("plainpass", "$2a$10$hashed")).thenReturn(true);

        Optional<User> result = authService.login("testuser", "plainpass");

        assertTrue(result.isPresent());
        verify(userRepo, never()).save(any());
    }

    @Test
    void loginFailsWithWrongPassword() {
        User user =
                new User(1L, "A", "B", "testuser", "$2a$10$hashed", "customer", "123", "a@b.com");
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(hasher.verify("wrong", "$2a$10$hashed")).thenReturn(false);

        Optional<User> result = authService.login("testuser", "wrong");

        assertFalse(result.isPresent());
    }

    @Test
    void loginMigratesPlaintextToHash() {
        User user = new User(1L, "A", "B", "testuser", "plaintext", "customer", "123", "a@b.com");
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(hasher.verify("plaintext", "plaintext")).thenReturn(false);
        when(hasher.hash("plaintext")).thenReturn("$2a$10$newhash");
        when(userRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<User> result = authService.login("testuser", "plaintext");

        assertTrue(result.isPresent());
        assertEquals("$2a$10$newhash", result.get().getPassword());
        verify(userRepo).save(any());
    }

    @Test
    void loginReturnsEmptyForUnknownUser() {
        when(userRepo.findByUsername("nobody")).thenReturn(Optional.empty());
        Optional<User> result = authService.login("nobody", "pass");
        assertFalse(result.isPresent());
    }

    @Test
    void registerHashesPasswordAndSaves() {
        when(hasher.hash("mypassword")).thenReturn("$2a$10$salted");
        when(userRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result =
                authService.register(
                        "newuser", "mypassword", "New", "User", "customer", "555", "n@b.com");

        assertEquals("newuser", result.getUsername());
        assertEquals("$2a$10$salted", result.getPassword());
        assertEquals("customer", result.getRole());
        verify(hasher).hash("mypassword");
        verify(userRepo).save(any());
    }
}
