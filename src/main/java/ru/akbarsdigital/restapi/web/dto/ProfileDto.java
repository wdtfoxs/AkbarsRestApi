package ru.akbarsdigital.restapi.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@AllArgsConstructor
@ToString
public class ProfileDto {
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private String phone;
    private String avatar;
    @JsonIgnore
    private String password;
}
