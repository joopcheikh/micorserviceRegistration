package com.registration.registration.emailrest;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author sosthene
 * Représente un utilisateur dans le processus de réinitialisation de mot de passe.
 *
 * Cette classe est utilisée pour stocker les informations nécessaires à la réinitialisation
 * du mot de passe d'un utilisateur. Elle contient les détails de l'utilisateur, notamment
 * son adresse email et son mot de passe, qui peut être temporaire ou nouveau.
 */
@Entity
@Table(name = "users")
public class UserReset {

    /** Identifiant unique de l'utilisateur. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Adresse email de l'utilisateur. */
    private String email;

    /** Mot de passe de l'utilisateur, utilisé lors de la réinitialisation. */
    private String password;

    // Getters and Setters

    /**
     * Retourne l'identifiant de l'utilisateur.
     *
     * @return l'identifiant unique de l'utilisateur.
     */
    public Long getId() {
        return id;
    }

    /**
     * Définit l'identifiant de l'utilisateur.
     *
     * @param id l'identifiant à définir.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retourne l'adresse email de l'utilisateur.
     *
     * @return l'adresse email de l'utilisateur.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Définit l'adresse email de l'utilisateur.
     *
     * @param email l'adresse email à définir.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retourne le mot de passe de l'utilisateur.
     *
     * @return le mot de passe de l'utilisateur.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Définit le mot de passe de l'utilisateur.
     *
     * @param password le mot de passe à définir.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
