package com.example.identity.services;

import com.example.identity.entities.Users;
import com.example.identity.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public Users authenticateUser(String email, String password) {
        return usersRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPasswordHash()))
                .orElseThrow(() -> new RuntimeException("Invalid Credentials"));
    }

    public Users registerUser(String email, String fullName, String password, Integer graduationYear) {
        if (usersRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already in use");
        }

        Users user = new Users();
        user.setEmail(email);
        user.setName(fullName);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setGraduationYear(graduationYear);

        return usersRepository.save(user);
    }
}
