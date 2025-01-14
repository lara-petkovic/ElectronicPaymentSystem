package com.example.sep.controllers;

import com.example.sep.JwtTokenProvider;
import com.example.sep.dtos.AuthMessage;
import com.example.sep.dtos.LoginRequest;
import com.example.sep.dtos.RegisterRequest;
import com.example.sep.models.User;
import com.example.sep.services.LoginAttemptService;
import com.example.sep.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/authenticate")
public class AuthController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private LoginAttemptService loginAttemptService;


    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user=userDetailsService.saveUser(request);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthMessage> login(@RequestBody LoginRequest request) {
        String username=request.getUsername();

        if (loginAttemptService.isLocked(username)) {
            return ResponseEntity.status(HttpStatus.LOCKED)
                    .body(new AuthMessage("Account is locked. Try again after 5 minutes."));
        }

        var user = userDetailsService.loadUserByUsername(request.getUsername());
        if(user!=null) {
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                String token = jwtTokenProvider.generateToken(user.getUsername());

                return ResponseEntity.ok()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .body(new AuthMessage("LOGGED IN"));
            }
        }
        loginAttemptService.loginFailed(username);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthMessage("INVALID CREDENTIALS"));
    }


}