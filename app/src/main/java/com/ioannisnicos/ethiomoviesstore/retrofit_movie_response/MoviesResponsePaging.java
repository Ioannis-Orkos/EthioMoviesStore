package com.ioannisnicos.ethiomoviesstore.retrofit_movie_response;

import com.google.gson.annotations.SerializedName;
import com.ioannisnicos.ethiomoviesstore.models.Movies;


import java.util.List;

public class MoviesResponsePaging {

    @SerializedName("records")
    private  List<Movies> results;
    private Paging paging;
    private String message;


    public MoviesResponsePaging(List<Movies> results, Paging paging, String message) {
        this.results = results;
        this.paging = paging;
        this.message = message;
    }

    public MoviesResponsePaging() {
    }

    public List<Movies> getResults() {
        return results;
    }

    public void setResults(List<Movies> results) {
        this.results = results;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
