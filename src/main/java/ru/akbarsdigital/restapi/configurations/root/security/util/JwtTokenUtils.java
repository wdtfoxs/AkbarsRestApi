package ru.akbarsdigital.restapi.configurations.root.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akbarsdigital.restapi.configurations.root.security.model.UserDetailsImpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtTokenUtils {

    @Value("${jwt.secret}")
    private String secret;

    public UserDetailsImpl parseToken(String token) {
        UserDetailsImpl u = null;

        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            u = new UserDetailsImpl(Long.parseLong((String) body.get("userId")), body.getSubject(), LocalDateTime.ofInstant(body.getIssuedAt().toInstant(), ZoneId.systemDefault()));
        } catch (JwtException e) {
            e.printStackTrace();
        }
        return u;
    }

    public String generateToken(Long id, String email, LocalDateTime lastPasswordChange) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("userId", id + "");

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(lastPasswordChange.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean isCreatedBeforeLastPasswordReset(LocalDateTime created, LocalDateTime lastPasswordReset) {
        return (lastPasswordReset != null && created.plusMinutes(1).isAfter(lastPasswordReset));
    }

}
