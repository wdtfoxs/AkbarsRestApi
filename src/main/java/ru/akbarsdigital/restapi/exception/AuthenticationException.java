package ru.akbarsdigital.restapi.exception;

public class AuthenticationException extends RestException {
    public AuthenticationException(String message) {
        super(message);
    }
}
