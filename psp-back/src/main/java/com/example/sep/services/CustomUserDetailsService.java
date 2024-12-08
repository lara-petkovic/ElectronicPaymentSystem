package com.example.sep.services;

import com.example.sep.dtos.AuthRequest;
import com.example.sep.models.User;
import com.example.sep.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class CustomUserDetailsService  {

    @Autowired
    private UserRepository userRepository;

    public User loadUser(AuthRequest authRequest)  {
        return userRepository.getUserByUsernameAndPassword(authRequest.getUsername(), authRequest.getPassword());
    }
}
