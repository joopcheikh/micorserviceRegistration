package com.registration.registration.DTO;

import lombok.Data;

/**
 * @author sosthene
 *
 * Classe représentant une requête de connexion d'utilisateur.
 *
 * Cette classe contient les informations nécessaires pour authentifier un utilisateur,
 * y compris son adresse email, son mot de passe et son adresse IP.
 *
 */
@Data
public class LoginRequest {
    private String email; // L'adresse email de l'utilisateur.
    private String password; // Le mot de passe de l'utilisateur.
    private String ip; // Champ IP uniquement pour la requête.

    // Les getters et setters sont générés automatiquement par Lombok grâce à l'annotation @Data.
}
