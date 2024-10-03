package com.registration.registration.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author cheikh diop, sosthene
 * Représente l'historique des connexions des utilisateurs dans le système.
 *
 * Cette classe enregistre les tentatives de connexion des utilisateurs,
 * incluant des informations sur l'adresse IP, la ville d'origine,
 * la date de la tentative, l'email de l'utilisateur,
 * et le nombre de tentatives échouées.
 * Ces données sont essentielles pour le suivi de la sécurité
 * et l'analyse des comportements de connexion.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "logging_history")
public class LoggingHistory {

    /** Identifiant unique de l'enregistrement de l'historique des connexions. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Adresse IP de l'utilisateur lors de la tentative de connexion. */
    @Column(nullable = false)
    private String ip;


    /** Ville d'origine de l'utilisateur, si disponible. */
    @Column(nullable = true)
    private String city;

    /** Date de la tentative de connexion. */
    @Column(nullable = false)
    private LocalDate attemptDate;

    /** Adresse email de l'utilisateur tentant de se connecter. */
    @Column(nullable = false)
    private String email;

    /** Nombre de tentatives de connexion échouées. */
    @Column(nullable = false)
    private int attempts;
}
