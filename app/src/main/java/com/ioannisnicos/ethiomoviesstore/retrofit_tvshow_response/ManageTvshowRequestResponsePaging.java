package com.ioannisnicos.ethiomoviesstore.retrofit_tvshow_response;

import com.google.gson.annotations.SerializedName;
import com.ioannisnicos.ethiomoviesstore.retrofit_movie_response.ManageMovieRequestResponse;
import com.ioannisnicos.ethiomoviesstore.retrofit_movie_response.Paging;
import com.ioannisnicos.ethiomoviesstore.retrofit_movie_response.QuerryResponse;

import java.util.List;

public class ManageTvshowRequestResponsePaging {

    @SerializedName("results")
    private List<ManageTvshowRequestResponse> result_data;

    @SerializedName("pageInfo")
    private Paging paging;

    private QuerryResponse querryMessage;


    public ManageTvshowRequestResponsePaging(List<ManageTvshowRequestResponse> result_data, Paging paging, QuerryResponse querryMessage) {
        this.result_data = result_data;
        this.paging = paging;
        this.querryMessage = querryMessage;
    }

    public List<ManageTvshowRequestResponse> getResult_data() {
        return result_data;
    }

    public void setResult_data(List<ManageTvshowRequestResponse> result_data) {
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
