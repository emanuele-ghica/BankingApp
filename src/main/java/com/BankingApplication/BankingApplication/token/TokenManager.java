package com.BankingApplication.BankingApplication.token;

public interface TokenManager {
    String generateToken(String email);
    boolean isTokenInvalid(String token);

    boolean isTokenValid(String token);

    void invalidateToken(String token);
    String getEmailFromToken(String token);
}
