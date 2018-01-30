package ru.akbarsdigital.restapi.util.validator;

import org.springframework.beans.factory.annotation.Autowired;
import ru.akbarsdigital.restapi.repository.UserRepository;
import ru.akbarsdigital.restapi.util.PhoneValidation;
import ru.akbarsdigital.restapi.util.validator.interfaces.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {
    @Autowired
    private UserRepository userRepository;
    private boolean unique;

    @Override
    public void initialize(Phone constraintAnnotation) {
        this.unique = constraintAnnotation.unique();
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        return phone != null && PhoneValidation.validatePhone(phone) && (!unique || !userRepository.existsByPhone(phone));
    }
}
