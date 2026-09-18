package com.onclinic.api.controller;

import com.onclinic.api.model.User;
import com.onclinic.api.repository.UserRepository;
import com.onclinic.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController @RequestMapping("/api/users") @RequiredArgsConstructor
public class UserController {
    private final AuthService auth;
    private final UserRepository users;
    private final PasswordEncoder encoder;

    @GetMapping("/me")
    public User me(java.security.Principal p) { return auth.current(p.getName()); }

    @PutMapping("/me")
    public User update(java.security.Principal p, @RequestBody User input) {
        User u = auth.current(p.getName());
        if (input.getEmail() != null && !input.getEmail().isBlank()) u.setEmail(input.getEmail());
        if (input.getFullName() != null && !input.getFullName().isBlank()) u.setFullName(input.getFullName());
        if (input.getPhone() != null) u.setPhone(input.getPhone());
        if (input.getAddress() != null) u.setAddress(input.getAddress());
        if (input.getBirthday() != null) {
            if (input.getBirthday().isAfter(LocalDate.now()) || input.getBirthday().isBefore(LocalDate.of(1850,1,1)))
                throw new IllegalArgumentException("Invalid birthday");
            u.setBirthday(input.getBirthday());
        }
        return users.save(u);
    }

    @PutMapping("/me/password")
    public Map<String,String> changePassword(java.security.Principal p, @RequestBody Map<String,String> body) {
        User u = auth.current(p.getName());
        String oldPassword = body.get("oldPassword"), newPassword = body.get("newPassword");
        if (newPassword == null || newPassword.length() < 6) throw new IllegalArgumentException("Minimum password length is 6");
        if (!encoder.matches(oldPassword, u.getPassword())) throw new IllegalArgumentException("Old password is invalid");
        u.setPassword(encoder.encode(newPassword)); users.save(u);
        return Map.of("message", "Password changed successfully");
    }

    @PostMapping("/become-doctor")
    public User becomeDoctor(java.security.Principal p) {
        User u = auth.current(p.getName());
        u.setRole(User.Role.DOCTOR);
        return users.save(u);
    }
}