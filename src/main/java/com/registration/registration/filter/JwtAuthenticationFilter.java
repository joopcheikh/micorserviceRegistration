package com.registration.registration.filter;

import com.registration.registration.service.JwtService;
import com.registration.registration.service.UserDetailsServiceImp;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author cheikh diop
 * Filtre d'authentification JWT.
 *
 * Ce filtre est chargé de vérifier la validité des tokens JWT présents dans les requêtes HTTP.
 * Il s'assure que l'utilisateur est authentifié et que ses informations d'identification
 * sont valides avant de poursuivre le traitement de la requête.
 *
 * <p>Le filtre extrait le token JWT de l'en-tête "Authorization", puis il utilise
 * le service JwtService pour extraire le nom d'utilisateur associé au token. Si l'utilisateur
 * est valide et que le contexte de sécurité ne contient pas déjà d'authentification,
 * il crée un objet UsernamePasswordAuthenticationToken et l'assigne au contexte de sécurité
 * pour le reste de la requête.</p>
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImp userDetailsServiceImp;

    @Autowired
    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsServiceImp userDetailsServiceImp) {
        this.jwtService = jwtService;
        this.userDetailsServiceImp = userDetailsServiceImp;
    }

    /**
     * Méthode de filtrage interne qui est appelée pour chaque requête HTTP.
     *
     * @param request la requête HTTP.
     * @param response la réponse HTTP.
     * @param filterChain la chaîne de filtres.
     * @throws ServletException si une erreur se produit lors du traitement de la requête.
     * @throws IOException si une erreur d'entrée/sortie se produit.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String autHeader = request.getHeader("Authorization");

        if (autHeader == null || !autHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = autHeader.substring(7);
        String username = jwtService.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsServiceImp.loadUserByUsername(username);

            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
