package com.ioannisnicos.ethiomoviesstore.models;

public class UsersAdditionalStatus {

    private int id;
    private String google_id;
    private int user_id;
    private String subscription_status;
    private String movie_status;
    private String movie_available;
    private String tvshow_status;
    private String tvshow_available;


    public UsersAdditionalStatus(int id, String google_id, int user_id, String subscription_status, String movie_status, String movie_available, String tvshow_status, String tvshow_available) {
        this.id = id;
        this.google_id = google_id;
        this.user_id = user_id;
        this.subscription_status = subscription_status;
        this.movie_status = movie_status;
        this.movie_available = movie_available;
        this.tvshow_status = tvshow_status;
        this.tvshow_available = tvshow_available;
    }

    public UsersAdditionalStatus(int id, String google_id, String subscription_status) {
        this.id = id;
        this.google_id = google_id;
        this.subscription_status = subscription_status;
    }

    public UsersAdditionalStatus() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoogle_id() {
        return google_id;
    }

    public void setGoogle_id(String google_id) {
        this.google_id = google_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSubscription_status() {
        return subscription_status;
    }

    public void setSubscription_status(String subscription_status) {
        this.subscription_status = subscription_status;
    }

    public String getMovie_status() {
        return movie_status;
    }

    public void setMovie_status(String movie_status) {
        this.movie_status = movie_status;
    }

    public String getMovie_available() {
        return movie_available;
    }

    public void setMovie_available(String movie_available) {
        this.movie_available = movie_available;
    }

    public String getTvshow_status() {
        return tvshow_status;
    }

    public void setTvshow_status(String tvshow_status) {
        this.tvshow_status = tvshow_status;
    }

    public String getTvshow_available() {
        return tvshow_available;
    }

    public void setTvshow_available(String tvshow_available) {
        this.tvshow_available = tvshow_available;
    }
}
