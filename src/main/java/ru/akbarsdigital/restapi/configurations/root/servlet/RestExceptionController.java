package ru.akbarsdigital.restapi.configurations.root.servlet;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.akbarsdigital.restapi.configurations.root.security.model.RestResponse;
import ru.akbarsdigital.restapi.exception.*;

@Log4j2
@ControllerAdvice
public class RestExceptionController extends ResponseEntityExceptionHandler {
    @Value("${log.auth}")
    private boolean logAuth;
    @Value("${log.registration}")
    private boolean logReg;
    @Value("${log.confirm}")
    private boolean logConfirm;
    @Value("${log.edit}")
    private boolean logEdit;

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new RestResponse(
                status.value(), ex.getMessage()), new HttpHeaders(), status);
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<RestResponse> handleRestException(RestException e) {
        log(e);
        return new ResponseEntity<>(new RestResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception e) {
        log.error(e);
        return new ResponseEntity<>(new RestResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> accessDeniedException(Exception e, WebRequest request) {
        log.info(e);
        return new ResponseEntity<>(new RestResponse(HttpStatus.FORBIDDEN.value(), e.getMessage()), new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    private void log(Exception e){
        if (e instanceof AuthenticationException && logAuth)
            log.warn(e);
        if (e instanceof RegistrationException && logReg)
            log.warn(e);
        if (e instanceof ConfirmationException && logConfirm)
            log.warn(e);
        if (e instanceof EditException && logEdit)
            log.warn(e);
        if (e instanceof RestException)
            log.warn(e);
    }
}
