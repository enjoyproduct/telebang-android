package com.neo2.telebang.helper;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by Billy on 11/3/16.
 */

public class Validation {
    // Regular Expression
    // you can change the expression based on your need
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    // Error Messages
    private static final String REQUIRED_MSG = "Please enter all required fields";
    private static final String EMAIL_INVALID_MSG = "Invalid email";
    private static final String EMAIL_ENTER_MSG = "You must enter an email address";
    private static final String USERNAME_LONG_MSG = "Username to 6 and 30 characters long";
    private static final String PASSWORD_LONG_MSG = "Passwords should be at least 6 characters";
    private static final String PASSWORD_NOT_MATCH_MSG = "Passwords do not match";

    public static String isValidUsername(String username) {
        String msgResponse = null;
        int size = 0;
        if (!TextUtils.isEmpty(username))
            size = username.length();

        if (size < 6 || size > 30) {
            msgResponse = USERNAME_LONG_MSG;
        }

        return msgResponse;
    }

    public static String isValidEmail(String email) {
        String msgResponse = null;

        if (TextUtils.isEmpty(email)) {
            msgResponse = EMAIL_ENTER_MSG;
        }

        if (!Pattern.matches(EMAIL_REGEX, email)) {
            msgResponse = EMAIL_INVALID_MSG;
        }

        return msgResponse;
    }

    public static String isValidPassword(String password, String verify_password) {
        String msgResponse = isValidPassword(password);

        if (!TextUtils.isEmpty(msgResponse))
            return msgResponse;

        if (TextUtils.isEmpty(verify_password) || !verify_password.equals(password)) {
            msgResponse = PASSWORD_NOT_MATCH_MSG;
        }

        return msgResponse;
    }

    public static String isValidPassword(String password) {
        String msgResponse = null;
        int size = 0;
        if (!TextUtils.isEmpty(password))
            size = password.length();

        if (size < 6) {
            msgResponse = PASSWORD_LONG_MSG;
        }

        return msgResponse;
    }
}
