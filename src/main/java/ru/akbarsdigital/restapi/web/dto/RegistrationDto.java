package ru.akbarsdigital.restapi.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.akbarsdigital.restapi.util.EmailValidation;
import ru.akbarsdigital.restapi.util.validator.interfaces.EmailUnique;
import ru.akbarsdigital.restapi.util.validator.interfaces.Phone;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class RegistrationDto {
    @NotBlank(message = "Empty email")
    @Email(message = "Wrong email format", regexp = EmailValidation.EMAIL_PATTERN)
    @EmailUnique
    private String email;
    @NotBlank(message = "Empty password")
    private String password;
    @Phone(unique = true)
    private String phone;
}
