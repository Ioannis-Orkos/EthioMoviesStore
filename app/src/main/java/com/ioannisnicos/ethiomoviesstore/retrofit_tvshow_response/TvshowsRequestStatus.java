package com.ioannisnicos.ethiomoviesstore.retrofit_tvshow_response;

public class TvshowsRequestStatus {

    String movie_type;
    String tvshow_id;
    String user_id;
    String store_id;
    String req_date;
    String req_status;
    String tvshow_status;
    String req_user_msg;
    String req_store_msg;


    public String getReq_user_msg() {
        return req_user_msg;
    }

    public void setReq_user_msg(String req_user_msg) {
        this.req_user_msg = req_user_msg;
    }

    public String getReq_store_msg() {
        return req_store_msg;
    }

    public void setReq_store_msg(String req_store_msg) {
        this.req_store_msg = req_store_msg;
    }

    public String getTvshow_status() {
        return tvshow_status;
    }

    public void setTvshow_status(String tvshow_status) {
        this.tvshow_status = tvshow_status;
    }


    public TvshowsRequestStatus(String movie_type, String tvshow_id, String user_id, String store_id, String req_date, String req_status, String tvshow_status, String req_user_msg, String req_store_msg) {
        this.movie_type = movie_type;
        this.tvshow_id = tvshow_id;
        this.user_id = user_id;
        this.store_id = store_id;
        this.req_date = req_date;
        this.req_status = req_status;
        this.tvshow_status = tvshow_status;
        this.req_user_msg = req_user_msg;
        this.req_store_msg = req_store_msg;
    }

    public String getMovie_type() {
        return movie_type;
    }

    public void setMovie_type(String movie_type) {
        this.movie_type = movie_type;
    }

    public String getTvshow_id() {
        return tvshow_id;
    }

    public void setTvshow_id(String tvshow_id) {
        this.tvshow_id = tvshow_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getReq_date() {
        return req_date;
    }

    public void setReq_date(String req_date) {
        this.req_date = req_date;
    }

    public String getReq_status() {
        return req_status;
    }

    public void setReq_status(String req_status) {
        this.req_status = req_status;
    }
}