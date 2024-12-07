package com.BankingApplication.BankingApplication.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class JwtUtil {

    private static final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    private static final String SECRET_KEY = "mysecretkey";
    private static final long EXPIRATION_TIME = 30 * 60 * 1000;

    public static String generateToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public static String validateToken(String token) {
        if (isBlacklisted(token)) {
            throw new IllegalArgumentException("Token has been invalidated. Please log in again.");
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException e) {
            throw new IllegalArgumentException("Invalid or expired token.");
        }
    }

    public static void addToBlacklist(String token) {
        blacklistedTokens.add(token);
    }

    public static boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
