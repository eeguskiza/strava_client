package com.deusto.strava_client.controller;

import com.deusto.strava_client.data.*;
import com.deusto.strava_client.proxy.IServiceProxy;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
public class WebClientController {

    @Autowired
    private IServiceProxy serviceProxy;

    /**
     * Landing page
     */
    @GetMapping("/strava")
    public String landingPage() {
        return "landingPage";
    }

    /**
     * Login and register pages
     */
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/auth/login")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("service") String service,
            HttpSession session,
            Model model) {

        try {
            Credentials credentials = new Credentials(email, password, service);
            String token = serviceProxy.login(credentials);
            if (token != null && !token.isEmpty()) {
                session.setAttribute("authToken", token);
                return "redirect:/home";
            } else {
                model.addAttribute("error", "Invalid credentials or service.");
                return "auth/login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "auth/login";
        }
    }

    @PostMapping("/auth/register")
    public String register(
            @RequestParam("email") String email,
            @RequestParam("name") String name,
            @RequestParam("birthDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthDate,
            @RequestParam("weight") double weight,
            @RequestParam("height") double height,
            @RequestParam("password") String password,
            @RequestParam("service") String service,
            Model model) {

        try {
            User user = new User(email, name, birthDate, password, weight, height, service);
            boolean isRegistered = serviceProxy.register(user);
            if (isRegistered) {
                return "redirect:/login";
            } else {
                model.addAttribute("error", "Registration failed.");
                return "auth/register";
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "auth/register";
        }
    }

    /**
     * Home page
     */
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        String token = (String) session.getAttribute("authToken");
        if (token == null) {
            return "redirect:/login";
        }
        try {
            User user = serviceProxy.getUserInfo(token);
            model.addAttribute("userName", user.getName());
        } catch (Exception e) {
            return "redirect:/login";
        }
        return "main/home";
    }

    @GetMapping("/settings")
    public String userSettings(HttpSession session, Model model) {
        String token = (String) session.getAttribute("authToken");
        if (token == null) {
            return "redirect:/login";
        }
        try {
            User user = serviceProxy.getUserInfo(token);
            model.addAttribute("userName", user.getName());
            model.addAttribute("userEmail", user.getEmail());
            model.addAttribute("userWeight", user.getWeight());
            model.addAttribute("userHeight", user.getHeight());
        } catch (Exception e) {
            return "redirect:/login";
        }
        return "settings/settings";
    }

    @PostMapping("/settings/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }


    /**
     * Challenges
     */
    @GetMapping("/challenges")
    public String challenges(HttpSession session, Model model) {
        String token = (String) session.getAttribute("authToken");
        if (token == null) {
            return "redirect:/login";
        }
        try {
            // Obtener desafíos globales
            List<Challenge> globalChallenges = serviceProxy.getActiveChallenges(token);

            // Obtener desafíos del usuario
            List<Challenge> myChallenges = serviceProxy.getMyActiveChallenges(token);

            // Agregar al modelo
            model.addAttribute("globalChallenges", globalChallenges);
            model.addAttribute("myChallenges", myChallenges);

            return "challenges/challenges";
        } catch (Exception e) {
            model.addAttribute("error", "Error retrieving challenges: " + e.getMessage());
            return "challenges/challenges";
        }
    }

    @PostMapping("/challenges/join")
    public String joinChallenge(HttpSession session, @RequestParam("challengeId") String challengeId) {
        String token = (String) session.getAttribute("authToken");
        if (token == null) {
            return "redirect:/login";
        }
        try {
            // Llamar al proxy para unirse al desafío
            serviceProxy.joinChallenge(token, challengeId);
            return "redirect:/challenges";
        } catch (Exception e) {
            return "redirect:/challenges?error=join_failed";
        }
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
            Challenge challenge = new Challenge(name, sport, targetDistance, targetTime, startDate, endDate);
            serviceProxy.createChallenge(token, challenge);
            return "redirect:/challenges";
        } catch (Exception e) {
            model.addAttribute("error", "Error creating challenge: " + e.getMessage());
            return "challenges/createChallenge";
        }
    }

    /**
     * Sessions
     */
    @GetMapping("/sessions")
    public String sessions(HttpSession session, Model model) {
        String token = (String) session.getAttribute("authToken");
        if (token == null) {
            return "redirect:/login";
        }
        try {
            LocalDate now = LocalDate.now();
            LocalDate lastWeek = now.minusDays(7);
            LocalDate futureDate = now.plusMonths(1); // Hasta un mes en el futuro
            List<TrainingSession> activeSessions = serviceProxy.getSessions(token, lastWeek, futureDate); // Rango ajustado
            List<TrainingSession> historySessions = serviceProxy.getSessions(token, LocalDate.of(2000, 1, 1), now); // Histórico hasta hoy
            model.addAttribute("activeSessions", activeSessions);
            model.addAttribute("historySessions", historySessions);
            return "sessions/sessions";
        } catch (Exception e) {
            model.addAttribute("error", "Error retrieving sessions: " + e.getMessage());
            return "sessions/sessions";
        }
    }


    @GetMapping("/sessions/create")
    public String createSessionForm(HttpSession session, Model model) {
        if (session.getAttribute("authToken") == null) {
            return "redirect:/login";
        }
        model.addAttribute("trainingSession", new TrainingSession());
        return "sessions/createSession";
    }

    @PostMapping("/sessions/create")
    public String createSession(
            HttpSession session,
            @RequestParam("sport") String sport,
            @RequestParam("distance") float distance,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("duration") float duration,
            Model model) {

        String token = (String) session.getAttribute("authToken");
        if (token == null) {
            return "redirect:/login";
        }
        try {
            TrainingSession sessionDTO = new TrainingSession(null, sport, distance, startDate, duration);
            serviceProxy.createTrainingSession(token, sessionDTO);
            return "redirect:/sessions";
        } catch (Exception e) {
            model.addAttribute("error", "Error creating session: " + e.getMessage());
            return "sessions/createSession";
        }
    }

    @GetMapping("/ai")
    public String selectAssistant() {
        // Renderiza la vista de selección de asistente
        return "AI/chatbot"; // Nombre del archivo HTML (assistantSelection.html)
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("assistant") String assistant, Model model) {
        // Validar el asistente (edith o friday)
        if (!assistant.equals("edith") && !assistant.equals("friday")) {
            return "redirect:/assistant"; // Redirigir a la pantalla de selección si el asistente no es válido
        }

        // Añadir el nombre del asistente al modelo para personalizar la vista
        model.addAttribute("assistantName", assistant.equals("edith") ? "E.D.I.T.H" : "F.R.I.D.A.Y");
        return "chat"; // Renderizar la vista 'chat.html' desde /templates
    }


}
