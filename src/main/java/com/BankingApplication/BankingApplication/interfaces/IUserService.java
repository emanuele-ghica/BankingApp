package com.BankingApplication.BankingApplication.interfaces;

public interface IUserService {
    String registerUser(String name, String email, String password);
    String loginUser(String email, String password);
    void logout(String token);
}
