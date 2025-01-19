package com.deusto.strava_client.controller;

import com.deusto.strava_client.dto.ChallengeDTO;
import com.deusto.strava_client.service.ClientChallengeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
public class ChallengeClientController {

    @Autowired
    private ClientChallengeService stravaServerClientService;

    @GetMapping("/challenges")
    public String challenges(HttpSession session, Model model) {
        String token = (String) session.getAttribute("authToken");

        if (token == null) {
            return "redirect:/login";
        }

        // Obtener challenges activos
        List<ChallengeDTO> activeChallenges = stravaServerClientService.getActiveChallenges(token);
        model.addAttribute("activeChallenges", activeChallenges);

        return "challenges/challenges";
    }

    @GetMapping("/challenges/create")
    public String createChallengeForm(HttpSession session) {
        if (session.getAttribute("authToken") == null) {
            return "redirect:/login";
        }
        return "challenges/createChallenge";
    }

    @PostMapping("/challenges/create")
    public String createChallenge(
            HttpSession session,
            @RequestParam("name") String name,
            @RequestParam("sport") String sport,
            @RequestParam("targetDistance") float targetDistance,
            @RequestParam("targetTime") float targetTime,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            Model model) {

        String token = (String) session.getAttribute("authToken");

        if (token == null) {
            return "redirect:/login";
        }

        try {
            // Usa el constructor de ChallengeDTO tal como está
            ChallengeDTO challengeDTO = new ChallengeDTO(name, sport, targetDistance, targetTime, startDate, endDate);
            stravaServerClientService.createChallenge(token, challengeDTO);
            return "redirect:/challenges";
        } catch (Exception e) {
            model.addAttribute("error", "Error creating challenge: " + e.getMessage());
            return "challenges/createChallenge";
        }
    }
}
