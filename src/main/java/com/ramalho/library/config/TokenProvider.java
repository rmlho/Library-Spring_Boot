package com.ramalho.library.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
public class TokenProvider {

    @Value("${JWT.KEY}")
    private String secret;

    @Value("${JWT.EXPIRATION}")
    private long expiration;

    public String generationToken(String email) {
        log.info("Begin token fabrication.");

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            log.info("Token fabrication successfully.");
            return JWT.create()
                    .withIssuer("library-api")
                    .withSubject(email)
                    .withExpiresAt(generationValidate())
                    .sign(algorithm);

        } catch (JWTCreationException creationException) {
            log.error("Error creating access token.");
            throw new RuntimeException("ERRO token fabrication", creationException);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            log.info("Token validate successfully.");
            return JWT.require(algorithm)
                    .withIssuer("library-api")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException verificationException) {
            log.error("error validating access token");
            return "";
        }
    }

    public Instant generationValidate() {
        return Instant.now().plusMillis(expiration);
    }
}
