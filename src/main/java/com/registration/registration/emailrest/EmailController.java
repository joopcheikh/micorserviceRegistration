package com.registration.registration.emailrest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sosthene
 * Contrôleur REST pour gérer les opérations liées aux emails et à la réinitialisation des mots de passe.
 *
 * Ce contrôleur expose des endpoints pour envoyer des emails avec des codes de validation,
 * valider ces codes, et changer le mot de passe d'un utilisateur.
 * Il utilise le service EmailService pour la logique métier associée.
 */
@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserResetRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Envoie un email contenant un code de validation à l'adresse spécifiée.
     *
     * @param emailRequest Contient l'adresse email, le sujet et le corps de l'email.
     * @return Réponse indiquant si l'email a été envoyé avec succès ou si l'email existe déjà.
     */
    @PostMapping("/send")
    public ResponseEntity<Object> sendEmail(@RequestBody EmailRequest emailRequest) {
        if (emailService.emailExists(emailRequest.getTo())) {
            String validationCode = emailService.generateValidationCode();
            emailService.storeValidationCode(emailRequest.getTo(), validationCode);

            String bodyWithCode = emailRequest.getBody() + "\nYour validation code is: " + validationCode;
            emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), bodyWithCode);

            // Retourner une réponse JSON
            return ResponseEntity.ok().body(new ApiResponse("success", "Email sent successfully!"));
        }
        // Retourner une réponse JSON pour un email existant
        return ResponseEntity.badRequest().body(new ApiResponse("error", "Email already exists!"));
    }

    // Classe interne pour les réponses API
    public static class ApiResponse {
        private String status;
        private String message;

        public ApiResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public class TokenResponse {
        private String token;

        public TokenResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    /**
     * Valide le code de validation fourni et génère un token si le code est valide.
     *
     * @param validationRequest Contient l'adresse email et le code de validation.
     * @return Un token si le code est valide, sinon un statut BAD_REQUEST.
     */
    @PostMapping("/validate")
    public ResponseEntity<TokenResponse> validateCode(@RequestBody ValidationRequest validationRequest) {
        boolean isValid = emailService.validateCode(validationRequest.getEmail(), validationRequest.getCode());

        if (isValid) {
            String token = emailService.generateToken(validationRequest.getEmail());
            TokenResponse response = new TokenResponse(token);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Change le mot de passe de l'utilisateur après validation du token.
     *
     * @param request Contient l'adresse email, le token et le nouveau mot de passe.
     * @return Une réponse indiquant si le mot de passe a été changé avec succès ou non.
     */
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody ChangePasswordRequest request) {
        boolean isValidToken = emailService.validateToken(request.getEmail(), request.getToken());
        if (isValidToken) {
            String encryptedPassword = passwordEncoder.encode(request.getPassword());
            userRepository.updatePassword(request.getEmail(), encryptedPassword);
            emailService.removeToken(request.getEmail());

            ApiResponse response = new ApiResponse("success", "Password changed successfully.");
            return ResponseEntity.ok(response);
        } else {
            ApiResponse response = new ApiResponse("error", "Invalid token or email.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public static class ChangePasswordRequest {
        private String email;
        private String token;
        private String password;

        // Getters et Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class EmailRequest {
        private String to;
        private String subject;
        private String body;

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }

    public static class ValidationRequest {
        private String email;
        private String code;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
