package com.example.sep.controllers;

import com.example.sep.dtos.AuthRequest;
import com.example.sep.models.User;
import com.example.sep.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authenticate")
public class AuthController {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public AuthController(CustomUserDetailsService customUserDetailsService){
        this.customUserDetailsService=customUserDetailsService;
    }

    @PostMapping
    public ResponseEntity<User> authenticate(@RequestBody AuthRequest authRequest) {
        User user=customUserDetailsService.loadUser(authRequest);
        if(user==null)
            return new ResponseEntity<>(user, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(user,HttpStatus.OK);

    }
}

