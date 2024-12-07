package com.BankingApplication.BankingApplication.services;

import com.BankingApplication.BankingApplication.interfaces.IBalanceService;
import com.BankingApplication.BankingApplication.models.User;
import com.BankingApplication.BankingApplication.repositories.UserRepository;
import com.BankingApplication.BankingApplication.token.JwtTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BalanceService implements IBalanceService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenManager jwtTokenManager;


    public void addMoney(String token, double amount) {
        if(validateToken(token)) {
            String email = jwtTokenManager.getEmailFromToken(token);
            User user = userRepository.findByEmail(email);
            if (amount <= 0) {
                throw new IllegalArgumentException("Cannot add a negative amount of money.");
            }
            user.setBalance(user.getBalance() + amount);
            userRepository.save(user);
            System.out.println("New balance: " + user.getBalance());
        }
    }

    public void withdrawMoney(String token, double amount) {
        if(validateToken(token)) {
            String email = jwtTokenManager.getEmailFromToken(token);
            User user = userRepository.findByEmail(email);
            if (amount > user.getBalance()) {
                throw new IllegalArgumentException("Insufficient funds.");
            }
            user.setBalance((user.getBalance() - amount));
            userRepository.save(user);
            System.out.println("New balance: " + user.getBalance());
        }
    }

    public void transferMoney(String senderToken, String recipientEmail, double amount) {
            if(validateToken(senderToken)) {
                String senderEmail = jwtTokenManager.getEmailFromToken(senderToken);
                User sender = userRepository.findByEmail(senderEmail);
                User recipient = userRepository.findByEmail(recipientEmail);
                if (recipient == null) {
                    throw new IllegalArgumentException("There is no user with that email");
                }
                if (amount <= 0) {
                    throw new IllegalArgumentException("Transaction cannot be completed.");
                }

                if (sender.getBalance() < amount) {
                    throw new IllegalArgumentException("Insufficient funds");
                }
                sender.setBalance(sender.getBalance() - amount);
                recipient.setBalance(recipient.getBalance() + amount);
                userRepository.save(sender);
                userRepository.save(recipient);
            }
    }

    public void checkBalance(String token) {
        validateToken(token);
        String email = jwtTokenManager.getEmailFromToken(token);
        User user = userRepository.findByEmail(email);
        System.out.println("Current balance: " + user.getBalance());
    }

    private boolean validateToken(String token) {
        try {
            return jwtTokenManager.isTokenValid(token);
        } catch (SecurityException e) {
            throw new SecurityException("Invalid or expired token. Access denied.", e);
        }
    }

}
