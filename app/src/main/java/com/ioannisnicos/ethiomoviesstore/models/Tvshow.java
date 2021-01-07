package com.ioannisnicos.ethiomoviesstore.models;

import java.util.List;

public class Tvshow {

    private String imdb_id;
    private String type;
    private String title;
    private String status;
    //@SerializedName("synopsis")
    private String synopsis;
    private String country;

    private String year;
    private String runtime;
    private String released;
    private String last_updated;

    private String trailer;

    private String poster;
    private String banner;

    private int num_seasons;
    private int last_episode;

    private int votes;
    private int percentage;
    private double rating;
    private String available;
    private List<String> genres;


    public Tvshow(String imdb_id, String type, String title, String status, String synopsis, String country, String year, String runtime, String released, String last_updated, String trailer, String poster, String banner, int num_seasons, int last_episode, int votes, int percentage, double rating, String available, List<String> genres) {
        this.imdb_id = imdb_id;
        this.type = type;
        this.title = title;
        this.status = status;
        this.synopsis = synopsis;
        this.country = country;
        this.year = year;
        this.runtime = runtime;
        this.released = released;
        this.last_updated = last_updated;
        this.trailer = trailer;
        this.poster = poster;
        this.banner = banner;
        this.num_seasons = num_seasons;
        this.last_episode = last_episode;
        this.votes = votes;
        this.percentage = percentage;
        this.rating = rating;
        this.available = available;
        this.genres = genres;
    }

    public Tvshow(String imdb_id, String type, String title, String poster, String last_updated) {
        this.imdb_id = imdb_id;
        this.type = type;
        this.title = title;
        this.last_updated = last_updated;
        this.poster = poster;

    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public int getNum_seasons() {
        return num_seasons;
    }

    public void setNum_seasons(int num_seasons) {
        this.num_seasons = num_seasons;
    }

    public int getLast_episode() {
        return last_episode;
    }

    public void setLast_episode(int last_episode) {
        this.last_episode = last_episode;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getAvailable() {
        return available;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAvailable(String available) {
        this.available = available;
    }
}
