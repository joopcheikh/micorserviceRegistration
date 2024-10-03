package com.registration.registration.emailrest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

/**
 * @author sosthene
 * Interface de gestion des opérations liées à la réinitialisation des mots de passe des utilisateurs.
 *
 * Cette interface étend JpaRepository et permet d'effectuer des opérations CRUD (Create, Read, Update, Delete)
 * sur les entités UserReset dans la base de données.
 *
 * Elle fournit également des méthodes personnalisées pour vérifier l'existence d'un utilisateur par email
 * et mettre à jour le mot de passe associé.
 */
public interface UserResetRepository extends JpaRepository<UserReset, Long> {

    /**
     * Vérifie si un utilisateur existe dans la base de données en fonction de son adresse email.
     *
     * @param email l'adresse email de l'utilisateur à vérifier.
     * @return true si un utilisateur avec l'email spécifié existe, sinon false.
     */
    boolean existsByEmail(String email);

    /**
     * Met à jour le mot de passe d'un utilisateur en fonction de son adresse email.
     *
     * @param email l'adresse email de l'utilisateur dont le mot de passe doit être mis à jour.
     * @param password le nouveau mot de passe à appliquer.
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserReset u SET u.password = ?2 WHERE u.email = ?1")
    void updatePassword(String email, String password);
}
