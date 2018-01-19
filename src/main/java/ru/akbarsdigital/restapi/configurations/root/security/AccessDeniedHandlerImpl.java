package ru.akbarsdigital.restapi.configurations.root.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import ru.akbarsdigital.restapi.configurations.root.security.model.ApiException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Autowired
    private MappingJackson2HttpMessageConverter converter;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        log.info(e);
        response.setContentType("application/json");
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.getWriter().write(converter.getObjectMapper().writeValueAsString(new ApiException(HttpStatus.FORBIDDEN.value(), "Access is denied")));
    }
}
