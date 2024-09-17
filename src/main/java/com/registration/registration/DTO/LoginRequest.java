package com.registration.registration.DTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
    private String ip; // Champ IP uniquement pour la requête
    
    // getters et setters
}
