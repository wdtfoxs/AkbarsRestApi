package ru.akbarsdigital.restapi.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.akbarsdigital.restapi.util.interfaces.Phone;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class ConfirmDto {
    @Phone
    private String phone;
    @NotBlank(message = "Empty code")
    private String code;
}
