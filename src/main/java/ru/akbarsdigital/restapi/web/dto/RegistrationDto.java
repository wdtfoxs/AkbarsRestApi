package ru.akbarsdigital.restapi.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RegistrationDto {
    private String email;
    private String password;
    private String phone;
}
