package com.registration.registration.model;

/**
 * @author cheikh diop, sosthene
 * Représente les rôles que peuvent avoir les utilisateurs dans le système.
 *
 * Cette énumération définit les différents niveaux d'accès et de permissions
 * qui peuvent être attribués aux utilisateurs. Chaque rôle détermine les
 * actions qu'un utilisateur peut effectuer au sein de l'application.
 *
 * <ul>
 *     <li><b>ADMIN</b> : Rôle avec tous les droits, y compris la gestion des utilisateurs,
 *     la configuration du système et l'accès complet aux données.</li>
 *     <li><b>USER</b> : Rôle standard pour les utilisateurs réguliers, ayant accès aux
 *     fonctionnalités de base de l'application.</li>
 *     <li><b>RECRUITER</b> : Rôle spécifique pour les recruteurs, leur permettant
 *     d'accéder aux fonctionnalités liées au recrutement et à la gestion des candidatures.</li>
 * </ul>
 */
public enum Role {
    /** Rôle d'administrateur avec des droits complets. */
    ADMIN,

    /** Rôle d'utilisateur avec des permissions standard. */
    USER,

    /** Rôle de recruteur, avec accès aux fonctionnalités de recrutement. */
    RECRUITER
}
