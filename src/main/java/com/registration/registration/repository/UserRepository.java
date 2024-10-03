package com.registration.registration.repository;

import com.registration.registration.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author cheikh diop, sosthene
 * Interface de gestion des données des utilisateurs.
 *
 * Cette interface étend JpaRepository, ce qui permet d'effectuer des opérations
 * CRUD (Create, Read, Update, Delete) sur les entités User dans la base de données.
 *
 * Elle fournit également des méthodes personnalisées pour rechercher des utilisateurs
 * en fonction de critères spécifiques, facilitant ainsi l'accès et la gestion des
 * informations des utilisateurs.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Recherche un utilisateur par son adresse email.
     *
     * @param email l'adresse email de l'utilisateur à rechercher.
     * @return un Optional contenant l'utilisateur trouvé, ou un Optional vide si aucun
     *         utilisateur n'est associé à l'email fourni.
     */
    Optional<User> findUserByEmail(String email);
}
