package com.itanoji.time.cutter.Utils;

public class Validation {
    public static String validateLogin(String login) {
        if(!login.matches("^[a-zA-Z][a-zA-Z0-9]*$")) {
            return "Недопустимые символы!";
        }
        return null;
    }

    public static String validatePassword(String password) {
        if(password.length() < 6) {
            return "Пароль должен содержать не менее 6 символов!";
        }
        return null;
    }
}
