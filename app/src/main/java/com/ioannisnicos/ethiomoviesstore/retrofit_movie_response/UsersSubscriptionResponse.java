package com.ioannisnicos.ethiomoviesstore.retrofit_movie_response;


import com.google.gson.annotations.SerializedName;
import com.ioannisnicos.ethiomoviesstore.models.Stores;
import com.ioannisnicos.ethiomoviesstore.models.Users;
import com.ioannisnicos.ethiomoviesstore.models.UsersAdditionalStatus;

import java.util.List;

public class UsersSubscriptionResponse {

    @SerializedName("users_list")
    private List<Users> stores_list;

    @SerializedName("users_list_status")
    private List<UsersAdditionalStatus> stores_list_status;
    private String message;

    public UsersSubscriptionResponse(List<Users> stores_list, List<UsersAdditionalStatus> stores_list_status, String message) {
        this.stores_list = stores_list;
        this.stores_list_status = stores_list_status;
        this.message = message;
    }

    public List<Users> getStores_list() {
        return stores_list;
    }

    public void setStores_list(List<Users> stores_list) {
        this.stores_list = stores_list;
    }

    public List<UsersAdditionalStatus> getStores_list_status() {
        return stores_list_status;
    }

    public void setStores_list_status(List<UsersAdditionalStatus> stores_list_status) {
        this.stores_list_status = stores_list_status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
