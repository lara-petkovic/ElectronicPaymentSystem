package com.example.sep.services;

import com.example.sep.EncryptionUtil;
import com.example.sep.dtos.RegisterRequest;
import com.example.sep.models.User;
import com.example.sep.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EncryptionUtil encryptionUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username);
        if(user!=null)
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
        return null;
    }

    public User getUserByUsername (String username){
        return userRepository.getUserByUsername(username);
    }

    public void saveUser(RegisterRequest request) {
        try {
            var user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setTwoFactorKey(encryptionUtil.encrypt(request.getTfa()));
            userRepository.save(user);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
