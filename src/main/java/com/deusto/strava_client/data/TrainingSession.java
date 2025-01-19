package com.deusto.strava_client.data;

import java.util.Date;

public class TrainingSession {
    private Long id;
    private String sport;
    private float distance;
    private Date startDate;
    private float duration;

    // Constructor
	public TrainingSession() {
	}
    public TrainingSession(Long id, String sport, float distance, Date startDate, float duration) {
        this.id = id;
        this.sport = sport;
        this.distance = distance;
        this.startDate = startDate;
        this.duration = duration;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public String getSport() {
        return sport;
    }

    public float getDistance() {
        return distance;
    }

    public Date getStartDate() {
        return startDate;
    }

    public float getDuration() {
        return duration;
    }
}
