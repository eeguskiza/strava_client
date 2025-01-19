package com.deusto.strava_client.controller;

import com.deusto.strava_client.service.ClientAuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class AuthClientController {

    @Autowired
    private ClientAuthService stravaServerClientService;

    @PostMapping("/api/auth/login")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("service") String service,
            HttpSession session,
            Model model) {

        try {
            // Llama al servicio y recibe el token
            String token = stravaServerClientService.authenticate(email, password, service);

            if (token != null && !token.isEmpty()) {
                // Guardar el token en la sesión
                session.setAttribute("authToken", token);
                System.out.println("Login exitoso, token guardado en la sesión.");
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


    @PostMapping("/api/auth/register")
    public String register(
            @RequestParam("email") String email,
            @RequestParam("name") String name,
            @RequestParam("birthDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthDate, // Especificar el formato
            @RequestParam("weight") double weight,
            @RequestParam("height") double height,
            @RequestParam("password") String password,
            @RequestParam("service") String service,
            Model model) {

        try {
            boolean isRegistered = stravaServerClientService.register(
                    email, name, birthDate, weight, height, password, service);

            if (isRegistered) {
                System.out.println("Registro exitoso para: " + email);
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


}
