package ru.akbarsdigital.restapi.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ConfirmDto {
    private String phone;
    private String code;
}
