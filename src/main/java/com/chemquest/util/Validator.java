package com.chemquest.util;

import java.util.regex.Pattern;

/** Input validation helpers for the sign-up and login forms. */
public final class Validator {
    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE = Pattern.compile("^\\+?[0-9]{10,13}$");

    private Validator() { }

    /**
     * Returns the cleaned identifier (lower-case email, or phone without spaces/dashes),
     * or null when the text is neither a valid email nor a valid phone number.
     */
    public static String normalizeIdentifier(String raw) {
        if (raw == null) {
            return null;
        }
        String text = raw.trim();
        if (text.contains("@")) {
            String email = text.toLowerCase();
            return EMAIL.matcher(email).matches() ? email : null;
        }
        String phone = text.replaceAll("[\\s\\-()]", "");
        return PHONE.matcher(phone).matches() ? phone : null;
    }

    /** Returns a message describing the first password problem, or null when the password is fine. */
    public static String passwordProblem(String password) {
        if (password.length() < 8) {
            return "Password must be at least 8 characters long.";
        }
        boolean letter = password.chars().anyMatch(Character::isLetter);
        boolean digit = password.chars().anyMatch(Character::isDigit);
        if (!letter || !digit) {
            return "Password must contain at least one letter and one number.";
        }
        return null;
    }
}
