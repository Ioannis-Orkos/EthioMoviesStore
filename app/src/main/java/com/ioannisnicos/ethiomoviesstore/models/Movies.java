package com.ioannisnicos.ethiomoviesstore.models;

import java.util.List;

public class Movies {

    private String imdb_id;
    private String type;
    private String title;
    //@SerializedName("synopsis")
    private String synopsis;

    private String year;
    private String runtime;
    private String released;

    private String trailer;

    private String poster;
    private String banner;

    private int votes;
    private int percentage;
    private double rating;
    private String available;
    private List<String> genres;


    public Movies(String imdb_id, String type, String title, String synopsis, String year, String runtime, String released, String trailer, String poster, String banner, int votes, int percentage, double rating, String available, List<String> genres) {
        this.imdb_id = imdb_id;
        this.type = type;
        this.title = title;
        this.synopsis = synopsis;
        this.year = year;
        this.runtime = runtime;
        this.released = released;
        this.trailer = trailer;
        this.poster = poster;
        this.banner = banner;
        this.votes = votes;
        this.percentage = percentage;
        this.rating = rating;
        this.available = available;
        this.genres = genres;
    }

    public Movies(String imdb_id, String type, String title, String poster, String released) {
        this.imdb_id = imdb_id;
        this.type = type;
        this.title = title;
        this.released = released;
        this.poster = poster;
    }

    public Movies() {
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

    public void setAvailable(String available) {
        this.available = available;
    }
}
