package com.gmt.gmttechchallenge.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${security.jwt-token.secret}")
    private String jwtSecret;

    public String generateToken(String username) {
        try {
            return JWT.create()
                    .withIssuer("gmt-test-api")
                    .withSubject(username)
                    .withExpiresAt(LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.UTC))
                    .sign(Algorithm.HMAC256(jwtSecret));
        } catch (JWTCreationException exception) {
            throw new RuntimeException("something something something"); //TODO you know what to do
        }
    }
}
