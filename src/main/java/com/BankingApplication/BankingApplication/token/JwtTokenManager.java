package com.BankingApplication.BankingApplication.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtTokenManager implements TokenManager {

    private static final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    private static final String SECRET_KEY = "my-secret-key";
    private static final long EXPIRATION_TIME = 30 * 60 * 1000;

    @Override
    public String generateToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    @Override
    public boolean isTokenValid(String token) {
        if (isTokenInvalid(token)) {
           return false;
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    @Override
    public void invalidateToken(String token) {
        blacklistedTokens.add(token);
    }

    @Override
    public String getEmailFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException e) {
            throw new IllegalArgumentException("Invalid or expired token.");
        }
    }

    @Override
    public boolean isTokenInvalid(String token) {
        return blacklistedTokens.contains(token);
    }
}
