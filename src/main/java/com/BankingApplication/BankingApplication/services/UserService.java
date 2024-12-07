package com.BankingApplication.BankingApplication.services;
import com.BankingApplication.BankingApplication.models.User;
import com.BankingApplication.BankingApplication.repositories.UserRepository;
import com.BankingApplication.BankingApplication.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(String name, String email, String password) {
        if (userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("Email is already in use");
        }

        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Password must be between 8 and 12 characters, " +
                    "contain at least one uppercase letter, one number, and one special character.");
        }

        String encryptedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(encryptedPassword);
        user.setBalance(0.0);
        userRepository.save(user);
        return JwtUtil.generateToken(email);
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or pin!");
        }
        String token = JwtUtil.generateToken(email);
        System.out.println(token);
        return token;
    }

    public void logout(String token) {
        JwtUtil.addToBlacklist(token);
    }

    public void addMoney(String token, double amount) {
            User user = getUserFromToken(token);
            if (amount <= 0) {
                throw new IllegalArgumentException("Cannot add a negative amount of money.");
            }
            user.setBalance(user.getBalance() + amount);
            userRepository.save(user);
            System.out.println("New balance: " + user.getBalance());
    }

    public void withdrawMoney(String token, double amount) {
            User user = getUserFromToken(token);
            if (amount > user.getBalance()) {
                throw new IllegalArgumentException("Insufficient funds.");
            }
            user.setBalance((user.getBalance() - amount));
            userRepository.save(user);
            System.out.println("New balance: " + user.getBalance());
    }

    public void transferMoney(String senderToken, String recipientEmail, double amount) {
            User sender = getUserFromToken(senderToken);
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

    public void checkBalance(String token) {
        User user = getUserFromToken(token);
            System.out.println("Current balance: " + user.getBalance());
    }

    //helper method to get the user from the token
    private User getUserFromToken(String token) {
        try {
            if (JwtUtil.isBlacklisted(token)) {
                throw new IllegalArgumentException("Invalid or expired token. Please log in again.");
            }

            String email = JwtUtil.validateToken(token);
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new IllegalArgumentException("User not found.");
            }
            return user;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid or expired token. Please log in again.");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.])[A-Za-z\\d@$!%*.?&]{8,12}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).matches();
    }

}


