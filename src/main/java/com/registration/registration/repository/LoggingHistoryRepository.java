package com.registration.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.registration.registration.model.LoggingHistory;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @author cheikh diop, sosthene
 * Interface de gestion des données des historiques de connexion.
 *
 * Cette interface étend JpaRepository, permettant d'effectuer des opérations
 * CRUD (Create, Read, Update, Delete) sur les entités LoggingHistory dans
 * la base de données.
 *
 * Elle offre également des méthodes personnalisées pour récupérer des enregistrements
 * d'historique de connexion en fonction de critères spécifiques, facilitant ainsi
 * le suivi et l'analyse des tentatives de connexion des utilisateurs.
 */
public interface LoggingHistoryRepository extends JpaRepository<LoggingHistory, Long> {

    /**
     * Recherche un enregistrement d'historique de connexion par adresse IP et date de tentative.
     *
     * @param ip l'adresse IP de l'utilisateur lors de la tentative de connexion.
     * @param attemptDate la date de la tentative de connexion.
     * @return un Optional contenant l'enregistrement trouvé, ou un Optional vide si aucun
     *         enregistrement n'est associé à l'IP et à la date fournies.
     */
    Optional<LoggingHistory> findByIpAndAttemptDate(String ip, LocalDate attemptDate);
}
