package com.BankingApplication.BankingApplication.validators;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UserValidator {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.])[A-Za-z\\d@$!%*.?&]{8,12}$";

    public void validateEmail(String email) {
        if (!Pattern.compile(EMAIL_REGEX).matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    public void validatePassword(String password) {
        if (!Pattern.compile(PASSWORD_REGEX).matcher(password).matches()) {
            throw new IllegalArgumentException("Password must be between 8 and 12 characters, " +
                    "contain at least one uppercase letter, one number, and one special character.");
        }
    }
}
