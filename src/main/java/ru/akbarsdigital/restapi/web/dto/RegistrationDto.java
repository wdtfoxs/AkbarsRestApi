package ru.akbarsdigital.restapi.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.akbarsdigital.restapi.util.interfaces.Phone;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class RegistrationDto {
    @NotBlank(message = "Empty email")
    @Email(message = "Wrong email format", regexp = "^[_A-z0-9-+]+(\\.[_A-z0-9-]+)*@[A-z0-9-]+(\\.[A-z0-9]+)*(\\.[A-z]{2,})$")
    private String email;
    @NotBlank(message = "Empty password")
    private String password;
    @Phone
    private String phone;
}
