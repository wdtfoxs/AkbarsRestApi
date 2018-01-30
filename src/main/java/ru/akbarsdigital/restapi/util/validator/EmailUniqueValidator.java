package ru.akbarsdigital.restapi.util.validator;

import org.springframework.beans.factory.annotation.Autowired;
import ru.akbarsdigital.restapi.repository.UserRepository;
import ru.akbarsdigital.restapi.util.validator.interfaces.EmailUnique;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailUniqueValidator implements ConstraintValidator<EmailUnique, String> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return email != null && !userRepository.existsByEmail(email);
    }
}
