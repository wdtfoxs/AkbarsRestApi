package ru.akbarsdigital.restapi.util;

import java.util.regex.Pattern;

public class EmailValidation {
    public static final String EMAIL_PATTERN = "^[_A-z0-9-+]+(\\.[_A-z0-9-]+)*@[A-z0-9-]+(\\.[A-z0-9]+)*(\\.[A-z]{2,})$";
    private static final Pattern EMAIL = Pattern.compile(EMAIL_PATTERN);

    public static boolean validateEmail(String email){
        return EMAIL.matcher(email).matches();
    }
}
