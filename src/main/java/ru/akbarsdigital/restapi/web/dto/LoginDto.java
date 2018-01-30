package ru.akbarsdigital.restapi.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import ru.akbarsdigital.restapi.util.EmailValidation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Getter
@AllArgsConstructor
@ToString
public class LoginDto {
    @NotBlank(message = "Empty email")
    @Email(message = "Wrong email format", regexp = EmailValidation.EMAIL_PATTERN)
    private String email;
    @NotBlank(message = "Empty password")
    private String password;
}
