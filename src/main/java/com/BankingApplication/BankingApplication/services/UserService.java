package com.BankingApplication.BankingApplication.services;

import com.BankingApplication.BankingApplication.models.User;
import com.BankingApplication.BankingApplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(String name, String email, String pin) {
        if (userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("Email is already in use");
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPin(pin);
        user.setBalance(0.0);
        return userRepository.save(user);
    };

    public User loginUser(String email, String pin) {
        User user = userRepository.findByEmail(email);
        if (user == null || !user.getPin().equals(pin)) {
            throw new IllegalArgumentException("Invalid email or pin!");
        }
        return user;
    }

    public void addMoney(User user, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Cannot add a negative amount of money.");
        }
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
        System.out.println("New balance: " + user.getBalance());
    }

    public void withdrawMoney(@org.jetbrains.annotations.NotNull User user, double amount) {
        if (amount > user.getBalance()) {
            throw new IllegalArgumentException("Insufficient funds.");
        }
        user.setBalance((user.getBalance() - amount));
        userRepository.save(user);
        System.out.println("New balance: " + user.getBalance());
    }

    public void transferMoney(User sender, String recipientEmail, double amount) {
        User recipient = userRepository.findByEmail(recipientEmail);
        if (recipient == null) {
            throw new IllegalArgumentException("There is no user with that email");
        }
        if (sender.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);
        userRepository.save(sender);
        userRepository.save(recipient);
    }

    public void checkBalance(User user) {
        System.out.println("Current balance: " + user.getBalance());
    }

}
