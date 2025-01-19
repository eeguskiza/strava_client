package com.deusto.strava_client.data;

public class Credentials {
    private String email;
    private String password;
    private String service;

    public Credentials(String email, String password, String service) {
        this.email = email;
        this.password = password;
        this.service = service;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return "CredentialsDTO{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", service='" + service + '\'' +
                '}';
    }
}
