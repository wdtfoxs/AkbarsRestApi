package ru.akbarsdigital.restapi.configurations.root.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import ru.akbarsdigital.restapi.configurations.root.security.model.JwtAuthenticationToken;
import ru.akbarsdigital.restapi.configurations.root.security.model.UserDetailsImpl;
import ru.akbarsdigital.restapi.configurations.root.security.util.JwtTokenUtils;
import ru.akbarsdigital.restapi.service.UserService;

@Log4j2
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String email, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        UserDetailsImpl parsedUser = jwtTokenUtils.parseToken(jwtAuthenticationToken.getToken());

        if (parsedUser == null)
            throw new BadCredentialsException("JWT token is not valid");
        if (!jwtTokenUtils.isCreatedBeforeLastPasswordReset(parsedUser.getLastPasswordChange(), userService.lastPasswordChange(parsedUser.getId())))
            throw new BadCredentialsException("JWT token has expired");

        return parsedUser;
    }
}
