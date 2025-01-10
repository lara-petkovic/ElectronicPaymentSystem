package com.example.sep.repositories;

import com.example.sep.models.Transaction;
import com.example.sep.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByUsernameAndPassword(String username, String password);
}
