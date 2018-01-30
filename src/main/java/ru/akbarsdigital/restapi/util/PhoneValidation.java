package ru.akbarsdigital.restapi.util;

import java.util.regex.Pattern;

public class PhoneValidation {
    private static final String PHONE_PATTERN = "^\\+7[0-9]{10}$";
    private static final Pattern PHONE = Pattern.compile(PHONE_PATTERN);

    public static boolean validatePhone(String phone){
        return PHONE.matcher(phone).matches();
    }
}
