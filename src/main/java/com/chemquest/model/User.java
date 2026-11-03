package com.chemquest.model;

import java.time.Instant;

/**
 * A registered account. identifier is the normalised email or phone number.
 * passwordHash is a salted PBKDF2 hash - the plain password is never stored.
 */
public record User(String identifier, String passwordHash, Instant createdAt) {

    /** A friendly short name for greetings, e.g. "asha" from asha@mail.com. */
    public String displayName() {
        int at = identifier.indexOf('@');
        return at > 0 ? identifier.substring(0, at) : identifier;
    }
}
