package ru.akbarsdigital.restapi.util.validation;

import ru.akbarsdigital.restapi.util.interfaces.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<Phone, String> {
    private static final String PHONE_PATTERN = "^\\+7[0-9]{10}$";

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        return phone != null && Pattern.compile(PHONE_PATTERN).matcher(phone).matches();
    }
}
