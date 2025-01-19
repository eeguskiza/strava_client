package com.deusto.strava_client.proxy;

import com.deusto.strava_client.data.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class ServiceProxy implements IServiceProxy {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String AUTH_URL = "http://localhost:8080/api/auth";
    private static final String CHALLENGES_URL = "http://localhost:8080/api/challenges";
    private static final String SESSIONS_URL = "http://localhost:8080/api/sessions";

    /**
     * Authentication Methods
     */
    @Override
    public String login(Credentials credentials) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(AUTH_URL + "/login", credentials, String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error comunicando con el servidor de autenticación: " + e.getMessage());
        }
    }

    @Override
    public boolean register(User user) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(AUTH_URL + "/user", user, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            throw new RuntimeException("Error comunicando con el servidor de registro: " + e.getMessage());
        }
    }

    @Override
    public User getUserInfo(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<User> response = restTemplate.exchange(AUTH_URL + "/info", HttpMethod.GET, requestEntity, User.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la información del usuario: " + e.getMessage());
        }
    }

    /**
     * Challenge Methods
     */
    @Override
    public List<Challenge> getActiveChallenges(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("token", token); // Asegúrate de usar "token" como nombre del header
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Challenge[]> response = restTemplate.exchange(
                    CHALLENGES_URL + "/active",
                    HttpMethod.GET,
                    entity,
                    Challenge[].class
            );

            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving active challenges: " + e.getMessage());
        }
    }


    @Override
    public List<Challenge> getMyActiveChallenges(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("token", token); // Asegúrate de usar "token" como nombre del header
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Challenge[]> response = restTemplate.exchange(
                    CHALLENGES_URL + "/my-active",
                    HttpMethod.GET,
                    entity,
                    Challenge[].class
            );

            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving my active challenges: " + e.getMessage());
        }
    }



    @Override
    public void createChallenge(String token, Challenge challenge) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Challenge> entity = new HttpEntity<>(challenge, headers);
            restTemplate.postForEntity(CHALLENGES_URL, entity, Void.class);
        } catch (Exception e) {
            throw new RuntimeException("Error creating challenge: " + e.getMessage());
        }
    }

    @Override
    public void joinChallenge(String token, String challengeId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            String url = CHALLENGES_URL + "/join/" + challengeId; // Endpoint del servidor
            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
        } catch (Exception e) {
            throw new RuntimeException("Error joining challenge: " + e.getMessage());
        }
    }


    /**
     * Session Methods
     */
    @Override
    public List<TrainingSession> getSessions(String token, LocalDate startDate, LocalDate endDate) {
        try {
            String url = String.format("%s?startDate=%s&endDate=%s", SESSIONS_URL, startDate.toString(), endDate.toString());
            HttpHeaders headers = new HttpHeaders();
            headers.set("token", token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<TrainingSession[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, TrainingSession[].class);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las sesiones: " + e.getMessage(), e);
        }
    }

    @Override
    public void createTrainingSession(String token, TrainingSession session) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("token", token);
            HttpEntity<TrainingSession> entity = new HttpEntity<>(session, headers);
            restTemplate.postForEntity(SESSIONS_URL, entity, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Error creando la sesión: " + e.getMessage());
        }
    }
}
