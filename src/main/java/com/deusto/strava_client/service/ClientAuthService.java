package com.deusto.strava_client.service;

import com.deusto.strava_client.dto.CredentialsDTO;
import com.deusto.strava_client.dto.UserDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
public class ClientAuthService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String AUTH_URL = "http://localhost:8080/api/auth";

    public String authenticate(String email, String password, String service) {
        try {
            CredentialsDTO credentials = new CredentialsDTO(email, password, service);
            ResponseEntity<String> response = restTemplate.postForEntity(AUTH_URL + "/login", credentials, String.class);
            System.out.println("Token recibido del servidor: " + response.getBody());
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error comunicando con el servidor de autenticación: " + e.getMessage());
        }
    }

    public boolean register(String email, String name, Date birthDate, double weight, double height, String password, String service) {
        try {
            UserDTO registerDTO = new UserDTO(email, name, birthDate, password, weight, height, service);
            ResponseEntity<String> response = restTemplate.postForEntity(AUTH_URL + "/user", registerDTO, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Usuario registrado exitosamente: " + email);
                return true;
            } else {
                System.err.println("Error al registrar el usuario: " + response.getBody());
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error al registrar el usuario: " + e.getMessage());
            throw new RuntimeException("Error comunicando con el servidor de registro: " + e.getMessage());
        }
    }

    public UserDTO getUserInfo(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<UserDTO> response = restTemplate.exchange(AUTH_URL + "/info", HttpMethod.GET, requestEntity, UserDTO.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la información del usuario: " + e.getMessage());
        }
    }
}
