package com.registration.registration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.registration.registration.DTO.LoginRequest;
import com.registration.registration.DTO.RefreshTokenRequest;
import com.registration.registration.emailrest.EmailService;
import com.registration.registration.model.User;
import com.registration.registration.service.AuthenticationResponse;
import com.registration.registration.service.AuthenticationService;
import com.registration.registration.service.ErrorResponse;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            emailService.sendEmail(user.getEmail(), "CONFIRMATION CREATION DU COMPTE",
                    "Bonjour, " + user.getLastname()
                            + ", votre compte a bien a bien été crée. Vous pouvez maintenant passer votre candidature.");
            return ResponseEntity.ok(authenticationService.register(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("duplicate"));
        }

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            System.out.println("voici le refresh token: " + refreshTokenRequest.getRefreshToken());
            AuthenticationResponse response = authenticationService.refreshToken(refreshTokenRequest.getRefreshToken());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid or expired refresh token"));
        }
    }

}
