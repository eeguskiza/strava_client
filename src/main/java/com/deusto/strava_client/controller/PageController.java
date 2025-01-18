package com.deusto.strava_client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    //lp
    @GetMapping("/strava")
    public String lp() {
        return "landingPage"; // Muestra templates/home.html
    }

    //home
    @GetMapping("/home")
    public String home() {
        return "home"; // Muestra templates/home.html
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

}
