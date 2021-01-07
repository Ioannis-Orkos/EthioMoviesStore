package com.ioannisnicos.ethiomoviesstore.retrofit_tvshow_response;

import com.google.gson.annotations.SerializedName;
import com.ioannisnicos.ethiomoviesstore.models.Tvshow;

import java.util.List;

public class TvshowsResponsePaging {

    @SerializedName("records")
    private List<Tvshow> results;
    private Paging paging;
    private String message;


    public TvshowsResponsePaging(List<Tvshow> results, Paging paging, String message) {
        this.results = results;
        this.paging = paging;
        this.message = message;
    }

    public TvshowsResponsePaging() {
    }

    public List<Tvshow> getResults() {
        return results;
    }

    public void setResults(List<Tvshow> results) {
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
