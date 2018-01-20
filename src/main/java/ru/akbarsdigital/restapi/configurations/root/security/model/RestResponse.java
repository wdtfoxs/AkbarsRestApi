package ru.akbarsdigital.restapi.configurations.root.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RestResponse {
    private int code;
    private String message;
}