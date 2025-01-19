package com.deusto.strava_client.data;

import java.util.Date;

public class Challenge {
    private Long id;
    private String creatorName;
    private String name;
    private String sport;
    private float targetDistance;
    private float targetTime;
    private Date startDate;
    private Date endDate;

    // Constructor
    public Challenge(String name, String sport, float targetDistance, float targetTime, Date startDate, Date endDate) {
        this.name = name;
        this.sport = sport;
        this.targetDistance = targetDistance;
        this.targetTime = targetTime;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    // Getters y Setters
    public Long getId() {
        return id;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getName() {
        return name;
    }

    public String getSport() {
        return sport;
    }

    public float getTargetDistance() {
        return targetDistance;
    }

    public float getTargetTime() {
        return targetTime;
    }

    public Date getStartDate() {
        return (Date) startDate;
    }

    public Date getEndDate() {
        return (Date) endDate;
    }
}
