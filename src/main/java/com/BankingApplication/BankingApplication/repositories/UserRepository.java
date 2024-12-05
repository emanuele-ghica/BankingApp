package com.BankingApplication.BankingApplication.repositories;

import com.BankingApplication.BankingApplication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
