package ru.akbarsdigital.restapi.configurations.root.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.akbarsdigital.restapi.configurations.root.security.model.UserDetailsImpl;

@Component
public class JwtTokenUtils {

    @Value("${jwt.secret}")
    private String secret;

    public UserDetails parseToken(String token) {
        UserDetailsImpl u = null;

        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            u = new UserDetailsImpl(Long.parseLong((String) body.get("userId")), body.getSubject());
        } catch (JwtException e) {
            e.printStackTrace();
        }
        return u;
    }

    public String generateToken(Long id, String email) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("userId", id + "");

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
