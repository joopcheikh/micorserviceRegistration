package com.registration.registration.config;

/**
 * @authors cheikh diop, sosthene
 *
 * Configuration de l'envoi d'emails dans l'application.
 *
 * Cette classe définit le bean `JavaMailSender`, qui est utilisé pour
 * envoyer des emails via SMTP. Les paramètres de configuration incluent
 * l'hôte, le port, les informations d'authentification et les propriétés
 * nécessaires pour la connexion au serveur SMTP.
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("mail.gatsmapping.com"); // Remplacez par l'hôte SMTP réel
        mailSender.setPort(465);

        mailSender.setUsername("webmaster@gatsmapping.com"); // Votre adresse email
        mailSender.setPassword("vPORL@h+VV_0"); // Mot de passe de l'email

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true"); // Activer SSL
        props.put("mail.debug", "true"); // Active le mode debug pour les logs

        return mailSender;
    }
}
