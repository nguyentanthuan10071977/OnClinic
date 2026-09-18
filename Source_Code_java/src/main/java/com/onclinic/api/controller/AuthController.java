package com.onclinic.api.controller;

import com.onclinic.api.model.User;
import com.onclinic.api.service.AuthService;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {
    private final AuthService auth;

    public record RegisterRequest(@Email @NotBlank String email, @NotBlank @Size(min=6) String password,
                                  @NotBlank String fullName, @NotNull LocalDate birthday, User.Role role) {}
    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}

    @PostMapping("/register")
    public Map<String,String> register(@RequestBody RegisterRequest r) {
        return Map.of("token", auth.register(r.email(), r.password(), r.fullName(), r.birthday(), r.role()));
    }

    @PostMapping("/login")
    public Map<String,String> login(@RequestBody LoginRequest r) {
        return Map.of("token", auth.login(r.email(), r.password()));
    }

    @PostMapping("/forgot-password")
    public Map<String,String> forgot(@RequestParam @Email String email) {
        // Production implementation should send a one-time reset token by email.
        return Map.of("message", "If the email exists, a password-reset message will be sent.");
    }

    @PostMapping("/logout")
    public Map<String,String> logout() {
        // JWT is stateless; production clients should delete the token.
        return Map.of("message", "Logged out. Remove the JWT from the client.");
    }
}