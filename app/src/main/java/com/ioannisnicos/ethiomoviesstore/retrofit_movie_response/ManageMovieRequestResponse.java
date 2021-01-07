package com.ioannisnicos.ethiomoviesstore.retrofit_movie_response;

import com.google.gson.annotations.SerializedName;
import com.ioannisnicos.ethiomoviesstore.models.Movies;
import com.ioannisnicos.ethiomoviesstore.models.Stores;
import com.ioannisnicos.ethiomoviesstore.models.Users;

import java.util.Date;


public class ManageMovieRequestResponse {

    @SerializedName("users_result")
    private Users users;

    @SerializedName("movies_result")
    private Movies movies;

    @SerializedName("stores_result")
    private Stores stores;

    private MoviesRequestStatus status;



    public ManageMovieRequestResponse(Users users, Movies movies, Stores stores, MoviesRequestStatus status) {
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

    public Movies getMovies() {
        return movies;
    }

    public void setMovies(Movies movies) {
        this.movies = movies;
    }

    public Stores getStores() {
        return stores;
    }

    public void setStores(Stores stores) {
        this.stores = stores;
    }

    public MoviesRequestStatus getStatus() {
        return status;
    }

    public void setStatus(MoviesRequestStatus status) {
        this.status = status;
    }
}
