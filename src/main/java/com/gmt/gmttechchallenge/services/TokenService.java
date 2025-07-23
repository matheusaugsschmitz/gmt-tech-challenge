package com.gmt.gmttechchallenge.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.gmt.gmttechchallenge.exception.UnexpectedBehaviourException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
public class TokenService {

    @Value("${security.jwt-token.issuer}")
    private String jwtIssuer;

    @Value("${security.jwt-token.secret}")
    private String jwtSecret;

    public String generateToken(UserDetails userDetails) {
        try {
            return JWT.create()
                    .withIssuer(jwtIssuer)
                    .withSubject(userDetails.getUsername())
                    .withExpiresAt(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant())
                    .sign(Algorithm.HMAC256(jwtSecret));
        } catch (JWTCreationException exception) {
            log.error("Unhandled exception in JWT creation", exception);
            throw new UnexpectedBehaviourException("An unexpected error occurred while creation the access token");
        }
    }

    public String validateToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(jwtSecret))
                    .withIssuer(jwtIssuer)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (TokenExpiredException exception) {
            throw exception;
        } catch (JWTVerificationException exception) {
            log.error("Unhandled exception in JWT verification", exception);
            throw new UnexpectedBehaviourException("An unexpected error occurred while validating the access token");
        }
    }
}
