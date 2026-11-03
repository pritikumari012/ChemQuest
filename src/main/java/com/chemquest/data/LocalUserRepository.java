package com.chemquest.data;

import com.chemquest.model.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Optional;
import java.util.Properties;

/**
 * LOCAL DEMO persistence. Accounts are saved in ~/.chemquest/users.properties.
 * This is NOT a SQL database. Only password hashes are written, never passwords.
 */
public class LocalUserRepository implements UserRepository {
    private final Path file;
    private final Properties users = new Properties();

    public LocalUserRepository() {
        this(Paths.get(System.getProperty("user.home"), ".chemquest", "users.properties"));
    }

    public LocalUserRepository(Path file) {
        this.file = file;
        load();
    }

    @Override
    public synchronized boolean existsByIdentifier(String identifier) {
        return users.containsKey(identifier);
    }

    @Override
    public synchronized Optional<User> findByIdentifier(String identifier) {
        String value = users.getProperty(identifier);
        if (value == null) {
            return Optional.empty();
        }
        int comma = value.lastIndexOf(',');
        if (comma < 0) {
            return Optional.empty();
        }
        String hash = value.substring(0, comma);
        Instant created;
        try {
            created = Instant.parse(value.substring(comma + 1));
        } catch (RuntimeException e) {
            created = Instant.EPOCH;
        }
        return Optional.of(new User(identifier, hash, created));
    }

    @Override
    public synchronized void save(User user) {
        users.setProperty(user.identifier(), user.passwordHash() + "," + user.createdAt());
        persist();
    }

    private void load() {
        if (!Files.exists(file)) {
            return;
        }
        try (BufferedReader in = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            users.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read " + file, e);
        }
    }

    private void persist() {
        try {
            Files.createDirectories(file.getParent());
            try (BufferedWriter out = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                users.store(out, "ChemQuest local demo accounts (password hashes only)");
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not write " + file, e);
        }
    }
}
