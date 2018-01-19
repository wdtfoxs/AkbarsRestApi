package ru.akbarsdigital.restapi.exception;

public class RestException extends RuntimeException {
    public RestException(String message) {
        super(message);
    }
}
