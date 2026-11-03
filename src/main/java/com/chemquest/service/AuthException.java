package com.chemquest.service;

/** Thrown with a user-friendly message when registration or login fails. */
public class AuthException extends Exception {
    public AuthException(String message) {
        super(message);
    }
}
