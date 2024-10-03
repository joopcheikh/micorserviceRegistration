package com.registration.registration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.registration.registration.DTO.LoginRequest;
import com.registration.registration.model.User;
import com.registration.registration.service.AuthenticationResponse;
import com.registration.registration.service.AuthenticationService;
import com.registration.registration.service.ErrorResponse;

/**
 * @author cheikh diop
 *
 * Contrôleur REST pour la gestion de l'authentification des utilisateurs.
 *
 * Ce contrôleur expose des endpoints pour l'enregistrement et la connexion des utilisateurs.
 * Il utilise le service AuthenticationService pour effectuer les opérations d'authentification.
 */
@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Enregistre un nouvel utilisateur.
     *
     * @param user l'objet User contenant les informations de l'utilisateur à enregistrer.
     * @return une réponse indiquant le succès ou l'échec de l'opération.
     * @throws Exception si l'utilisateur existe déjà.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            return ResponseEntity.ok(authenticationService.register(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("duplicate"));
        }
    }

    /**
     * Authentifie un utilisateur et génère une réponse d'authentification.
     *
     * @param loginRequest l'objet LoginRequest contenant les informations de connexion de l'utilisateur.
     * @return une réponse contenant les détails de l'authentification.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }
}
