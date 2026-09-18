package com.onclinic.api.service;

import com.onclinic.api.model.User;
import com.onclinic.api.repository.UserRepository;
import com.onclinic.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service @RequiredArgsConstructor
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    @Transactional
    public String register(String email, String password, String fullName, LocalDate birthday, User.Role role) {
        if (users.findByEmail(email).isPresent()) throw new IllegalArgumentException("Email already registered");
        if (password == null || password.length() < 6) throw new IllegalArgumentException("Password must contain at least 6 characters");
        if (birthday == null || birthday.isAfter(LocalDate.now()) || birthday.isBefore(LocalDate.of(1850,1,1)))
            throw new IllegalArgumentException("Birthday must be before now and after 1850");
        User u = User.builder().email(email).password(encoder.encode(password)).fullName(fullName)
                .birthday(birthday).role(role == null ? User.Role.PATIENT : role).build();
        users.save(u);
        return jwt.generate(u.getId(), u.getEmail(), u.getRole().name());
    }

    public String login(String email, String password) {
        User u = users.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if (!encoder.matches(password, u.getPassword())) throw new IllegalArgumentException("Invalid email or password");
        return jwt.generate(u.getId(), u.getEmail(), u.getRole().name());
    }

    public User current(String email) {
        return users.findByEmail(email).orElseThrow();
    }
}