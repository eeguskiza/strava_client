package com.deusto.strava_client.controller;

import com.deusto.strava_client.dto.UserDTO;
import com.deusto.strava_client.service.ClientAuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    //lp
    @GetMapping("/strava")
    public String lp() {
        return "landingPage"; // Muestra templates/home.html
    }

    @Autowired
    private ClientAuthService stravaServerClientService;

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        String token = (String) session.getAttribute("authToken");
        if (token == null) {
            System.out.println("Acceso denegado. No hay sesión activa.");
            return "redirect:/login";
        }

        try {
            // Recupera la información del usuario usando el token
            UserDTO user = stravaServerClientService.getUserInfo(token);
            model.addAttribute("userName", user.getName()); // Pasa el nombre al modelo
        } catch (Exception e) {
            System.out.println("Error al obtener la información del usuario: " + e.getMessage());
            return "redirect:/login";
        }

        return "main/home"; // Muestra templates/home.html
    }


    //login
    @GetMapping("/login")
    public String login() {
        return "auth/login"; // Muestra templates/auth/login.html
    }

    //login
    @GetMapping("/register")
    public String register() {
        return "auth/register"; // Muestra templates/auth/login.html
    }

    @GetMapping("/ai")
    public String ai(HttpSession session) {
        if (session.getAttribute("authToken") == null) {
            return "redirect:/login";
        }
        return "ai";
    }

    @GetMapping("/settings")
    public String settings(HttpSession session) {
        if (session.getAttribute("authToken") == null) {
            return "redirect:/login";
        }
        return "settings/settings";
    }


}
