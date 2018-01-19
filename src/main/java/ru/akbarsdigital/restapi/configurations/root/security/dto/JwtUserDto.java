package ru.akbarsdigital.restapi.configurations.root.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class JwtUserDto {
    private Long id;
    private String email;
}