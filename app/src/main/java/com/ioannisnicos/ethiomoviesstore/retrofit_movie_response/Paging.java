package com.ioannisnicos.ethiomoviesstore.retrofit_movie_response;

import com.google.gson.annotations.SerializedName;

public class Paging {

    @SerializedName("current_page")
    private Integer page;
    @SerializedName("total_rows")
    private Integer totalResults;
    //dates missing
    @SerializedName("total_page")
    private Integer totalPages;


    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Paging(Integer page, Integer totalResults, Integer totalPages) {
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }


}
