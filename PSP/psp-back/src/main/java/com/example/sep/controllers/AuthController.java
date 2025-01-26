/*package com.example.sep.controllers;

import com.example.sep.JwtTokenProvider;
import com.example.sep.dtos.AuthMessage;
import com.example.sep.dtos.LoginRequest;
import com.example.sep.dtos.RegisterRequest;
import com.example.sep.models.User;
import com.example.sep.services.LoginAttemptService;
import com.example.sep.services.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);



    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user=userDetailsService.saveUser(request);
        logger.info("New user with username "+request.getUsername()+ " added.");
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthMessage> login(@RequestBody LoginRequest request) {
        String username=request.getUsername();

        if (loginAttemptService.isLocked(username)) {
            logger.info("User "+request.getUsername()+ " tried to log in 5 times with wrong password.");
            return ResponseEntity.status(HttpStatus.LOCKED)
                    .body(new AuthMessage("Account is locked. Try again after 5 minutes."));
        }

        var user = userDetailsService.loadUserByUsername(request.getUsername());
        if(user!=null) {
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                String token = jwtTokenProvider.generateToken(user.getUsername());
                logger.info("User "+request.getUsername()+ " logged in.");

                return ResponseEntity.ok()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .body(new AuthMessage("LOGGED IN"));
            }
        }else{
            logger.warn("User "+request.getUsername()+ " not found.");
        }
        loginAttemptService.loginFailed(username);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthMessage("INVALID CREDENTIALS"));
    }


}*/
package com.example.sep.controllers;

import com.example.sep.EncryptionUtil;
import com.example.sep.JwtTokenProvider;
import com.example.sep.dtos.AuthMessage;
import com.example.sep.dtos.LoginRequest;
import com.example.sep.dtos.RegisterRequest;
import com.example.sep.models.User;
import com.example.sep.services.LoginAttemptService;
import com.example.sep.services.UserDetailsServiceImpl;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@RestController
@RequestMapping("api/authenticate")
public class AuthController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LoginAttemptService loginAttemptService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private EncryptionUtil encryptionUtil;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        request.setTfa(generateSecretKey());
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        userDetailsService.saveUser(request);
        logger.info("New user with username "+request.getUsername()+ " added.");
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
    }

    @GetMapping("/generate-qr/{username}")
    public ResponseEntity<String> generateQrCode(@PathVariable String username) throws Exception {
        User user =  userDetailsService.getUserByUsername(username);
        if (user == null || encryptionUtil.decrypt(user.getTwoFactorKey()) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found or 2FA not enabled.");
        }

        String otpAuthURL = String.format(
                "otpauth://totp/%s?secret=%s&issuer=YourAppName",
                username, encryptionUtil.decrypt(user.getTwoFactorKey())
        );

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 300, 300);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            String qrCodeBase64 = Base64.getEncoder().encodeToString(pngOutputStream.toByteArray());
            return ResponseEntity.ok(qrCodeBase64);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating QR code.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthMessage> login(@RequestBody LoginRequest request) throws Exception {
        String username = request.getUsername();

        if (loginAttemptService.isLocked(username)) {
            logger.warn("Account is locked "+request.getUsername());
            return ResponseEntity.status(HttpStatus.LOCKED)
                    .body(new AuthMessage("Account is locked. Try again after 5 minutes."));
        }

        User user =  userDetailsService.getUserByUsername(username);
        if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {

            if (!verifyTotpCode(encryptionUtil.decrypt(user.getTwoFactorKey()), request.getTfacode())) {
                loginAttemptService.loginFailed(username);
                logger.warn("Invalid 2fa code for "+request.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthMessage("Invalid 2FA code."));
            }

            loginAttemptService.resetAttempts(username);

            String token = jwtTokenProvider.generateToken(user.getUsername());
            logger.info("User "+request.getUsername()+ " logged in.");

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(new AuthMessage("LOGGED IN"));
        }

        loginAttemptService.loginFailed(username);
        logger.warn("Invalid credentials for "+request.getUsername());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthMessage("Invalid credentials."));
    }

    public String generateSecretKey() {
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        return key.getKey();
    }

    private boolean verifyTotpCode(String secretKey, String code) {
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        return googleAuthenticator.authorize(secretKey, Integer.parseInt(code));
    }
}
