package com.example.identity.services;

import com.example.identity.entities.Users;
import com.example.identity.repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private UsersRepository usersRepository;
    private PasswordEncoder passwordEncoder;
    private AuthService authService;

    @BeforeEach
    void setup() {
        usersRepository = mock(UsersRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        authService = new AuthService(usersRepository, passwordEncoder);
    }

    @Test
    void authenticateUser_shouldReturnUser_whenCredentialsValid() {
        String email = "a@x.com";
        String rawPassword = "pass";
        Users user = new Users();
        user.setEmail(email);
        user.setPasswordHash("encoded");

        when(usersRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, "encoded")).thenReturn(true);

        Users res = authService.authenticateUser(email, rawPassword);

        assertNotNull(res);
        assertEquals(email, res.getEmail());
        verify(usersRepository).findByEmail(email);
        verify(passwordEncoder).matches(rawPassword, "encoded");
    }

    @Test
    void authenticateUser_shouldThrow_whenInvalidCredentials() {
        String email = "a@x.com";
        String rawPassword = "wrong";

        Users user = new Users();
        user.setEmail(email);
        user.setPasswordHash("encoded");

        when(usersRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, "encoded")).thenReturn(false);

        var ex = assertThrows(RuntimeException.class, () -> authService.authenticateUser(email, rawPassword));
        assertTrue(ex.getMessage().contains("Invalid Credentials"));
        verify(usersRepository).findByEmail(email);
        verify(passwordEncoder).matches(rawPassword, "encoded");
    }

    @Test
    void authenticateUser_shouldThrow_whenUserNotFound() {
        String email = "no@x.com";

        when(usersRepository.findByEmail(email)).thenReturn(Optional.empty());

        var ex = assertThrows(RuntimeException.class, () -> authService.authenticateUser(email, "p"));
        assertTrue(ex.getMessage().contains("Invalid Credentials"));
        verify(usersRepository).findByEmail(email);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void registerUser_shouldSaveAndReturnUser_whenEmailNotTaken() {
        String email = "new@x.com";
        String fullName = "New User";
        String rawPassword = "pw";
        Integer grad = 2027;

        when(usersRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn("encoded");
        // simulate save returns the user object
        when(usersRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Users saved = authService.registerUser(email, fullName, rawPassword, grad);

        assertNotNull(saved);
        assertEquals(email, saved.getEmail());
        assertEquals(fullName, saved.getName());
        assertEquals(grad, saved.getGraduationYear());
        assertEquals("encoded", saved.getPasswordHash());
        verify(usersRepository).existsByEmail(email);
        verify(passwordEncoder).encode(rawPassword);
        verify(usersRepository).save(any(Users.class));
    }

    @Test
    void registerUser_shouldThrow_whenEmailExists() {
        String email = "exists@x.com";

        when(usersRepository.existsByEmail(email)).thenReturn(true);

        var ex = assertThrows(RuntimeException.class, () -> authService.registerUser(email, "n", "p", 2025));
        assertTrue(ex.getMessage().contains("Email already in use"));
        verify(usersRepository).existsByEmail(email);
        verify(usersRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }
}
