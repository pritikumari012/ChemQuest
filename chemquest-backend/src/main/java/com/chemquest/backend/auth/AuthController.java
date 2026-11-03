
package com.chemquest.backend.auth;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JdbcTemplate jdbc;
    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    public AuthController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // REGISTER
    @PostMapping("/register")
    public Map<String, String> register(
            @RequestBody Map<String, String> request) {

        String identifier = normalize(request.get("identifier"));
        String password = request.get("password");

        validateIdentifier(identifier);

        if (password == null || password.length() < 8) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password must contain at least 8 characters."
            );
        }

        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM users WHERE identifier = ?",
                Integer.class,
                identifier
        );

        if (count != null && count > 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Account already exists."
            );
        }

        String passwordHash = encoder.encode(password);

        try {
            jdbc.update(
                    "INSERT INTO users (identifier, password_hash) VALUES (?, ?)",
                    identifier, passwordHash
            );
        } catch (DuplicateKeyException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Account already exists."
            );
        }

        return Map.of(
                "message", "Account created successfully!",
                "identifier", identifier
        );
    }

    // LOGIN
    @PostMapping("/login")
    public Map<String, String> login(
            @RequestBody Map<String, String> request) {

        String identifier = normalize(request.get("identifier"));
        String password = request.get("password");

        if (identifier == null || identifier.isBlank()
                || password == null || password.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email/phone and password are required."
            );
        }

        try {
            Map<String, Object> user = jdbc.queryForMap(
                    "SELECT password_hash FROM users WHERE identifier = ?",
                    identifier
            );

            String storedHash = (String) user.get("password_hash");

            if (!encoder.matches(password, storedHash)) {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Incorrect email/phone or password."
                );
            }

            return Map.of(
                    "message", "Login successful!",
                    "identifier", identifier
            );

        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Incorrect email/phone or password."
            );
        }
    }

    private String normalize(String identifier) {
        return identifier == null ? null
                : identifier.trim().toLowerCase();
    }

    private void validateIdentifier(String identifier) {

        boolean validEmail = identifier != null && Pattern.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                identifier
        );

        boolean validPhone = identifier != null && Pattern.matches(
                "^\\+?[0-9]{10,13}$",
                identifier
        );

        if (!validEmail && !validPhone) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Enter a valid email or phone number."
            );
        }
    }
}
