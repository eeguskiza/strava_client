package com.deusto.strava_client.controller;

import com.deusto.strava_client.service.StravaServerClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthClientController {

    @Autowired
    private StravaServerClientService stravaServerClientService;

    @PostMapping("/api/auth/login")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("service") String service,
            Model model) {

        System.out.println("Solicitud recibida con email: " + email + ", servicio: " + service);

        try {
            // Llama al servicio y recibe el token
            String token = stravaServerClientService.authenticate(email, password, service);

            if (token != null && !token.isEmpty()) {
                System.out.println("Login exitoso, token recibido: " + token);
                return "redirect:/home";
            } else {
                model.addAttribute("error", "Invalid credentials or service.");
                return "auth/login"; // Devuelve a la página de login con un mensaje de error
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred during login: " + e.getMessage());
            return "auth/login"; // Devuelve a la página de login con un mensaje de error
        }
    }

}
