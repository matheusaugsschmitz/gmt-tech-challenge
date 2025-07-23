package com.gmt.gmttechchallenge.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${security.jwt-token.secret}")
    private String jwtSecret;

    public String generateToken(UserDetails userDetails) {
        try {
            return JWT.create()
                    .withIssuer("gmt-test-api")
                    .withSubject(userDetails.getUsername())
                    .withExpiresAt(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant())
                    .sign(Algorithm.HMAC256(jwtSecret));
        } catch (JWTCreationException exception) {
            throw new RuntimeException("something something something"); //TODO you know what to do
        }
    }

    public String validateToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(jwtSecret))
                    .withIssuer("gmt-test-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (TokenExpiredException exception) {
            throw exception;
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("something something something"); //TODO you know what to do
        }
    }
}
