package com.BankingApplication.BankingApplication.interfaces;

public interface IBalanceService {
     void addMoney(String token, double amount);
     void withdrawMoney(String token, double amount);
     void transferMoney(String senderToken, String recipientEmail, double amount);
     void checkBalance(String token);
}
