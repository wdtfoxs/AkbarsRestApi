package ru.akbarsdigital.restapi.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@AllArgsConstructor
@ToString
public class LoginDto {
    private String email;
    private String password;
}
