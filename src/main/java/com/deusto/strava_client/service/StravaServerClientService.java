package com.deusto.strava_client.service;

import com.deusto.strava_client.dto.CredentialsDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StravaServerClientService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String AUTH_URL = "http://localhost:8080/api/auth/login"; // URL del servidor

    public String authenticate(String email, String password, String service) {
        try {
            CredentialsDTO credentials = new CredentialsDTO(email, password, service);
            // Realiza la llamada al servidor y espera un String (el token)
            var response = restTemplate.postForEntity(AUTH_URL, credentials, String.class);

            // Imprime el token recibido
            System.out.println("Token recibido del servidor: " + response.getBody());

            // Retorna el token si es no nulo
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error comunicando con el servidor de autenticación: " + e.getMessage());
        }
    }

}

