package com.ioannisnicos.ethiomoviesstore.retrofit_movie_response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ManageMovieRequestResponsePaging {

    @SerializedName("results")
    private List<ManageMovieRequestResponse> result_data;

    @SerializedName("pageInfo")
    private Paging paging;

    private QuerryResponse querryMessage;


    public ManageMovieRequestResponsePaging(List<ManageMovieRequestResponse> result_data, Paging paging, QuerryResponse querryMessage) {
        this.result_data = result_data;
        this.paging = paging;
        this.querryMessage = querryMessage;
    }

    public List<ManageMovieRequestResponse> getResult_data() {
        return result_data;
    }

    public void setResult_data(List<ManageMovieRequestResponse> result_data) {
        this.result_data = result_data;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public QuerryResponse getQuerryMessage() {
        return querryMessage;
    }

    public void setQuerryMessage(QuerryResponse querryMessage) {
        this.querryMessage = querryMessage;
    }
}
