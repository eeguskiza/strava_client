package com.deusto.strava_client.service;

import com.deusto.strava_client.dto.TrainingSessionDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class ClientSessionService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String SESSIONS_URL = "http://localhost:8080/api/sessions";

    public List<TrainingSessionDTO> getSessions(String token, LocalDate startDate, LocalDate endDate) {
        try {
            String url = String.format("%s?startDate=%s&endDate=%s", SESSIONS_URL, startDate.toString(), endDate.toString());
            HttpHeaders headers = new HttpHeaders();
            headers.set("token", token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<TrainingSessionDTO[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, TrainingSessionDTO[].class);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las sesiones: " + e.getMessage(), e);
        }
    }

    public void createTrainingSession(String token, TrainingSessionDTO sessionDTO) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("token", token);
            HttpEntity<TrainingSessionDTO> entity = new HttpEntity<>(sessionDTO, headers);
            restTemplate.postForEntity(SESSIONS_URL, entity, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Error creando la sesión: " + e.getMessage());
        }
    }
}
