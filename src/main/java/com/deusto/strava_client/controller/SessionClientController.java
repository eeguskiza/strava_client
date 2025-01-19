package com.deusto.strava_client.controller;

import com.deusto.strava_client.dto.TrainingSessionDTO;
import com.deusto.strava_client.service.ClientAuthService;
import com.deusto.strava_client.service.ClientSessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
public class SessionClientController {

    @Autowired
    private ClientSessionService stravaServerClientService;

    /**
     * Vista para listar las sesiones activas e históricas
     */
    @GetMapping("/sessions")
    public String sessions(HttpSession session, Model model) {
        String token = (String) session.getAttribute("authToken");

        if (token == null) {
            return "redirect:/login"; // Si no hay token, redirige al login
        }

        // Fechas para "Active" (última semana)
        LocalDate now = LocalDate.now();
        LocalDate lastWeek = now.minusDays(7);

        // Fechas para "History" (desde el año 2000)
        LocalDate historyStartDate = LocalDate.of(2000, 1, 1);

        // Llamada al servicio para obtener sesiones activas
        List<TrainingSessionDTO> activeSessions = stravaServerClientService.getSessions(token, lastWeek, now);

        // Llamada al servicio para obtener historial
        List<TrainingSessionDTO> historySessions = stravaServerClientService.getSessions(token, historyStartDate, now);

        // Añadimos las sesiones al modelo
        model.addAttribute("activeSessions", activeSessions);
        model.addAttribute("historySessions", historySessions);

        return "sessions/sessions"; // Carga el template de Thymeleaf
    }

    /**
     * Vista para cargar el formulario de creación de sesiones
     */
    @GetMapping("/sessions/create")
    public String createSessionForm(HttpSession session, Model model) {
        if (session.getAttribute("authToken") == null) {
            return "redirect:/login"; // Redirigir al login si no hay sesión activa
        }
        model.addAttribute("trainingSession", new TrainingSessionDTO());
        return "sessions/createSession"; // Renderizar la plantilla para crear una sesión
    }

    /**
     * Lógica para enviar los datos de una nueva sesión al servidor
     */
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
            return "redirect:/login"; // Redirigir al login si no hay sesión activa
        }

        try {
            TrainingSessionDTO sessionDTO = new TrainingSessionDTO(null, sport, distance, startDate, duration);
            stravaServerClientService.createTrainingSession(token, sessionDTO);
            return "redirect:/sessions"; // Redirigir a la lista de sesiones tras crear una nueva
        } catch (Exception e) {
            model.addAttribute("error", "Error creating session: " + e.getMessage());
            return "sessions/createSession"; // Volver al formulario con un mensaje de error
        }
    }
}
