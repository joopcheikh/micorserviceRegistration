package com.registration.registration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import com.registration.registration.filter.JwtAuthenticationFilter;
import com.registration.registration.service.UserDetailsServiceImp;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author cheikh diop
 *
 * Configuration de la sécurité de l'application.
 *
 * Cette classe configure les paramètres de sécurité de Spring Security,
 * y compris les filtres d'authentification, les stratégies de session et
 * les autorisations d'accès aux endpoints.
 */
@Configuration
@EnableWebSecurity
@SecurityRequirement(name = "bearerAuth")
public class SecurityConfig {

    private UserDetailsServiceImp userDetailsServiceImp;
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(
            UserDetailsServiceImp userDetailsServiceImp,
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Crée un bean RestTemplate pour effectuer des appels HTTP.
     *
     * @return une instance de RestTemplate.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Configure le filtre de sécurité de l'application.
     *
     * @param httpSecurity la configuration de sécurité HTTP.
     * @return un objet SecurityFilterChain configuré.
     * @throws Exception si une erreur se produit lors de la configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // Désactive CSRF pour simplifier la configuration.
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers(
                                        "/login/**",
                                        "/register/**",
                                        "/swagger-ui/**",
                                        "/v3/api-docs",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui.html",
                                        "/webjars/**",
                                        "/email/send",
                                        "/email/validate",
                                        "/email/change-password")
                                .permitAll() // Permet l'accès sans authentification aux endpoints spécifiés.
                                .anyRequest()
                                .authenticated()) // Nécessite une authentification pour toutes les autres requêtes.
                .userDetailsService(userDetailsServiceImp)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configuration pour une gestion de session sans état.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Ajoute le filtre JWT avant le filtre d'authentification par nom d'utilisateur et mot de passe.
                .build();
    }

    /**
     * Crée un bean PasswordEncoder pour encoder les mots de passe.
     *
     * @return une instance de BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Crée un bean AuthenticationManager pour gérer l'authentification des utilisateurs.
     *
     * @param configuration la configuration d'authentification.
     * @return un AuthenticationManager.
     * @throws Exception si une erreur se produit lors de la création du gestionnaire d'authentification.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
