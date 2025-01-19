package com.deusto.strava_client.service;

import com.deusto.strava_client.dto.ChallengeDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ClientChallengeService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String CHALLENGES_URL = "http://localhost:8080/api/challenges";

    public List<ChallengeDTO> getActiveChallenges(String token) {
        try {
            String url = CHALLENGES_URL + "/active";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<ChallengeDTO[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, ChallengeDTO[].class);

            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving active challenges: " + e.getMessage());
        }
    }

    public void createChallenge(String token, ChallengeDTO challengeDTO) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);

            HttpEntity<ChallengeDTO> entity = new HttpEntity<>(challengeDTO, headers);
            restTemplate.postForEntity(CHALLENGES_URL, entity, Void.class);
        } catch (Exception e) {
            throw new RuntimeException("Error creating challenge: " + e.getMessage());
        }
    }
}
