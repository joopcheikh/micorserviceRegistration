package com.registration.registration.model;

/**
 * @author cheikh diop, sosthene
 * Représente les types de candidats dans le système.
 *
 * Cette énumération définit les différentes catégories de candidats
 * qui peuvent être enregistrés. Les candidats peuvent être classés
 * comme Militaire ou Civil, ce qui peut influencer le processus
 * d'inscription et les rôles qui leur sont assignés.
 */
public enum TypeCandidat {
    /** Représente un candidat militaire. */
    Militaire,
    /** Représente un candidat civil. */
    Civil
}
