package com.registration.registration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.registration.registration.DTO.LoginRequest;
import com.registration.registration.emailrest.EmailService;
import com.registration.registration.model.Role;
import com.registration.registration.model.User;
import com.registration.registration.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthenticationService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private LoggingHistoryService loggingHistoryService; 
    @Autowired
    private EmailService emailService;

    @Autowired
    public AuthenticationService(
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            AuthenticationManager authenticationManager) {

        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;

    }

    public AuthenticationResponse register(User userRigistry) {
        User user = new User();
        user.setFirstname(userRigistry.getFirstname());
        user.setLastname(userRigistry.getLastname());
        user.setTypeCandidat(userRigistry.getTypeCandidat());
        user.setEmail(userRigistry.getEmail());
        user.setPassword(passwordEncoder.encode(userRigistry.getPassword()));
        user.setRole(Role.USER);

        String emailBody = String.format(
        "Bonjour %s,\n\n" +
        "Votre compte sur Armée SN a été créé avec succès. Vous pouvez maintenant passer votre candidature.\n\n" +
        "Merci !",
        user.getFirstname()
        );

      
        // Return JSON response
        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser);
        emailService.sendEmail(user.getEmail(), "Alerte creation de compte gatsmapping", emailBody);
     

        return new AuthenticationResponse(token, savedUser.getRole().name());
    }

  public AuthenticationResponse login(LoginRequest authUser) {
        try {
            // Tente l'authentification
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authUser.getEmail(),
                            authUser.getPassword()
                    )
            );

            User user = userRepository.findUserByEmail(authUser.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            String token = jwtService.generateToken(user);

            return new AuthenticationResponse(token, user.getRole().name());

        } catch (AuthenticationException e) {
            loggingHistoryService.logFailedAttempt(authUser.getIp(),authUser.getEmail());

            throw new RuntimeException("Authentication failed: Invalid email or password");
        }
    }


}
