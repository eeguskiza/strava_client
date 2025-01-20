package com.deusto.strava_client.data;

public class SessionData {
    private String name; // Nombre del deporte
    private double intensity; // Intensidad calculada
    private String color; // Color para el gráfico

    public SessionData(String name, double intensity, String color) {
        this.name = name;
        this.intensity = Math.min(100, Math.max(0, intensity)); // Asegura que esté entre 0 y 100
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public double getIntensity() {
        return intensity;
    }

    public String getColor() {
        return color;
    }
}
