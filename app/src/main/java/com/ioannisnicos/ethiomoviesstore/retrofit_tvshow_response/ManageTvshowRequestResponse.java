package com.ioannisnicos.ethiomoviesstore.retrofit_tvshow_response;

import com.google.gson.annotations.SerializedName;
import com.ioannisnicos.ethiomoviesstore.models.Movies;
import com.ioannisnicos.ethiomoviesstore.models.Stores;
import com.ioannisnicos.ethiomoviesstore.models.Tvshow;
import com.ioannisnicos.ethiomoviesstore.models.Users;
import com.ioannisnicos.ethiomoviesstore.retrofit_movie_response.MoviesRequestStatus;


public class ManageTvshowRequestResponse {

    @SerializedName("users_result")
    private Users users;

    @SerializedName("movies_result")
    private Tvshow movies;

    @SerializedName("stores_result")
    private Stores stores;

    private TvshowsRequestStatus status;



    public ManageTvshowRequestResponse(Users users, Tvshow movies, Stores stores, TvshowsRequestStatus status) {
        this.users = users;
        this.movies = movies;
        this.stores = stores;
        this.status = status;
    }


    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Tvshow getMovies() {
        return movies;
    }

    public void setMovies(Tvshow movies) {
        this.movies = movies;
    }

    public Stores getStores() {
        return stores;
    }

    public void setStores(Stores stores) {
        this.stores = stores;
    }

    public TvshowsRequestStatus getStatus() {
        return status;
    }

    public void setStatus(TvshowsRequestStatus status) {
        this.status = status;
    }
}
