package com.BankingApplication.BankingApplication.services;
import com.BankingApplication.BankingApplication.interfaces.IUserService;
import com.BankingApplication.BankingApplication.models.User;
import com.BankingApplication.BankingApplication.repositories.UserRepository;
import com.BankingApplication.BankingApplication.token.JwtTokenManager;
import com.BankingApplication.BankingApplication.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenManager jwtTokenManager;
    @Autowired
    private UserValidator userValidator;

    public String registerUser(String name, String email, String password) {
        if (userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("Email is already in use");
        }

        userValidator.validateEmail(email);
        userValidator.validatePassword(password);

        String encryptedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(encryptedPassword);
        user.setBalance(0.0);
        userRepository.save(user);
        return jwtTokenManager.generateToken(email);
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or pin!");
        }
        return jwtTokenManager.generateToken(email);
    }

    public void logout(String token) {
        jwtTokenManager.invalidateToken(token);
    }

}


