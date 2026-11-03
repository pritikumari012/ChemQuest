package com.chemquest.service;

import com.chemquest.data.UserRepository;
import com.chemquest.model.User;
import com.chemquest.util.PasswordHasher;
import com.chemquest.util.Validator;

import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.Optional;

/** Registration and login rules. LOCAL/DEMO authentication - not connected to SQL yet. */
public class AuthenticationService {
    private final UserRepository repository;

    public AuthenticationService(UserRepository repository) {
        this.repository = repository;
    }

    public User register(String rawIdentifier, String password, String confirm) throws AuthException {
        if (rawIdentifier == null || rawIdentifier.isBlank()) {
            throw new AuthException("Please enter your email address or phone number.");
        }
        String id = Validator.normalizeIdentifier(rawIdentifier);
        if (id == null) {
            throw new AuthException("Enter a valid email (name@example.com) or a phone number with 10-13 digits.");
        }
        if (password == null || password.isEmpty()) {
            throw new AuthException("Please choose a password.");
        }
        String problem = Validator.passwordProblem(password);
        if (problem != null) {
            throw new AuthException(problem);
        }
        if (confirm == null || confirm.isEmpty()) {
            throw new AuthException("Please confirm your password.");
        }
        if (!password.equals(confirm)) {
            throw new AuthException("Passwords do not match.");
        }
        try {
            if (repository.existsByIdentifier(id)) {
                throw new AuthException("An account with this email or phone number already exists. Please log in.");
            }
            User user = new User(id, PasswordHasher.hash(password), Instant.now());
            repository.save(user);
            return user;
        } catch (UncheckedIOException e) {
            throw new AuthException("Could not save your account on this computer: " + e.getCause().getMessage());
        }
    }

    public User login(String rawIdentifier, String password) throws AuthException {
        if (rawIdentifier == null || rawIdentifier.isBlank() || password == null || password.isEmpty()) {
            throw new AuthException("Please enter your email/phone number and password.");
        }
        String id = Validator.normalizeIdentifier(rawIdentifier);
        Optional<User> found = id == null ? Optional.empty() : repository.findByIdentifier(id);
        // Same message for "no such user" and "wrong password" so accounts cannot be guessed.
        if (found.isEmpty() || !PasswordHasher.verify(password, found.get().passwordHash())) {
            throw new AuthException("Incorrect email/phone number or password.");
        }
        return found.get();
    }
}
