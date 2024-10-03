package com.registration.registration.emailrest;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author sosthene
 * Service de gestion de l'envoi d'emails et de la réinitialisation des mots de passe.
 *
 * Cette classe gère l'envoi d'emails, la génération de codes de validation et de tokens,
 * ainsi que la validation de ces derniers. Elle utilise le repository UserResetRepository
 * pour vérifier l'existence d'adresses email dans le système.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserResetRepository userRepository; // Injection du repository

    private ConcurrentHashMap<String, CodeEntry> validationCodes = new ConcurrentHashMap<>();
    private final long expirationTime = TimeUnit.MINUTES.toMillis(15); // 15 minutes

    // Classe interne pour gérer les codes et leur timestamp
    private static class CodeEntry {
        String code;
        long timestamp;

        CodeEntry(String code, long timestamp) {
            this.code = code;
            this.timestamp = timestamp;
        }
    }

    private ConcurrentHashMap<String, String> tokens = new ConcurrentHashMap<>();

    /**
     * Génère un token unique pour un email donné.
     *
     * @param email l'adresse email pour laquelle le token est généré.
     * @return le token généré.
     */
    public String generateToken(String email) {
        String token = UUID.randomUUID().toString(); // Générer un token unique
        tokens.put(email, token); // Associer le token à l'email
        return token;
    }

    /**
     * Supprime le token associé à un email.
     *
     * @param email l'adresse email dont le token doit être supprimé.
     */
    public void removeToken(String email) {
        tokens.remove(email);
        validationCodes.remove(email);
    }

    /**
     * Valide le token associé à un email.
     *
     * @param email l'adresse email à vérifier.
     * @param token le token à valider.
     * @return true si le token est valide, sinon false.
     */
    public boolean validateToken(String email, String token) {
        String storedToken = tokens.get(email);
        return storedToken != null && storedToken.equals(token);
    }

    /**
     * Génère un code de validation aléatoire à six chiffres.
     *
     * @return le code de validation généré.
     */
    public String generateValidationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    /**
     * Envoie un email à un destinataire spécifié.
     *
     * @param to l'adresse email du destinataire.
     * @param subject le sujet de l'email.
     * @param body le corps du message.
     */
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("webmaster@gatsmapping.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    /**
     * Stocke un code de validation associé à un email avec un timestamp.
     *
     * @param email l'adresse email pour laquelle le code est stocké.
     * @param code le code de validation à stocker.
     */
    public void storeValidationCode(String email, String code) {
        // Stocker le code avec un timestamp
        long timestamp = System.currentTimeMillis();
        validationCodes.put(email, new CodeEntry(code, timestamp));
    }

    /**
     * Valide un code de validation pour un email donné.
     *
     * @param email l'adresse email à vérifier.
     * @param code le code de validation à valider.
     * @return true si le code est valide et non expiré, sinon false.
     */
    public boolean validateCode(String email, String code) {
        CodeEntry entry = validationCodes.get(email);

        // Vérifier si le code existe
        if (entry == null) {
            return false; // Code non trouvé
        }

        // Vérifier l'expiration
        if (System.currentTimeMillis() - entry.timestamp > expirationTime) {
            validationCodes.remove(email); // Supprimer le code expiré
            return false; // Code expiré
        }

        // Vérifier si le code correspond
        return entry.code.equals(code);
    }

    /**
     * Vérifie si un email existe dans la base de données.
     *
     * @param email l'adresse email à vérifier.
     * @return true si l'email existe, sinon false.
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email); // Utiliser le repository pour vérifier l'existence de l'email
    }
}
