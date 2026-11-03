package com.chemquest.data;

import com.chemquest.model.User;

import java.util.Optional;

/**
 * Storage contract for accounts. Today: LocalUserRepository (a file on this computer).
 * Later: a JDBC/MySQL implementation can replace it without changing any UI code.
 */
public interface UserRepository {
    boolean existsByIdentifier(String identifier);

    Optional<User> findByIdentifier(String identifier);

    void save(User user);
}
