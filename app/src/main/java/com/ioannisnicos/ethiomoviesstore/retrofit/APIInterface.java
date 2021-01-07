package com.ioannisnicos.ethiomoviesstore.retrofit;

import com.ioannisnicos.ethiomoviesstore.models.Movies;
import com.ioannisnicos.ethiomoviesstore.models.Stores;
import com.ioannisnicos.ethiomoviesstore.models.Tvshow;
import com.ioannisnicos.ethiomoviesstore.retrofit_movie_response.ManageMovieRequestResponsePaging;
import com.ioannisnicos.ethiomoviesstore.retrofit_movie_response.MoviesResponsePaging;
import com.ioannisnicos.ethiomoviesstore.retrofit_movie_response.QuerryResponse;
import com.ioannisnicos.ethiomoviesstore.retrofit_movie_response.UsersSubscriptionResponse;
import com.ioannisnicos.ethiomoviesstore.retrofit_tvshow_response.ManageTvshowRequestResponsePaging;
import com.ioannisnicos.ethiomoviesstore.retrofit_tvshow_response.TvshowsResponsePaging;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIInterface {

    //Stores------------------------------------------------------------------------
    @GET("stores/store_read_single_byID.php")
    Call<Stores> getUserInfo(@Query("id") String id);

    @FormUrlEncoded
    @POST("stores/store_register_onLogin.php")
    Call<QuerryResponse> RegisterUserOnLoginPost(@Field("google_id") String gi,
                                                 @Field("first_name") String fn,
                                                 @Field("last_name") String ln,
                                                 @Field("display_name") String dn,
                                                 @Field("email") String e
    );

    @FormUrlEncoded
    @POST("stores/store_update_bywhat.php")
    Call<QuerryResponse> UpdateToken(
            @Field("google_id") String gId,
            @Field("what_to_update") String what,
            @Field("notif_token") String token
    );

    @Multipart
    @POST("stores/store_update_profile_picture.php")
    Call<QuerryResponse> sendImage(@Part("google_id") RequestBody gid,
                                   @Part("what_to_update") RequestBody w,
                                   @Part MultipartBody.Part image
    );

    @Multipart
    @POST("stores/store_update_banner_picture.php")
    Call<QuerryResponse> sendImageBanner(@Part("google_id") RequestBody gid,
                                         @Part("what_to_update") RequestBody w,
                                         @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("stores/store_update_bywhat.php")
    Call<QuerryResponse> UpdateByWhatSetting(
            @Field("google_id") String gid,
            @Field("what_to_update") String w,
            @Field("display_name") String dn,
            @Field("business_name") String bn,
            @Field("description") String d,
            @Field("language") String l
    );

    @FormUrlEncoded
    @POST("stores/store_update_bywhat.php")
    Call<QuerryResponse> UpdateLocation(
            @Field("google_id") String gid,
            @Field("what_to_update") String w,
            @Field("address") String address,
            @Field("postcode") String postcode,
            @Field("geolat") String lat,
            @Field("geolng") String lng
    );


    //Users-----------------------------------------------------------------------

    @FormUrlEncoded
    @POST("users_forStore/store_UserSubs.php")
    Call<UsersSubscriptionResponse> getUserSubss(@Field("sgid")  String id,
                                                 @Field("req_s") String req_s
    );

    @FormUrlEncoded
    @POST("users_forStore/store_manage_subsRequest.php")
    Call<QuerryResponse> store_manage_request(@Field("uid") int uid,
                                              @Field("sgid") String sgid,
                                              @Field("req_s") String req_s
    );

    //Movies-------------------------------------------------------------------------

    @FormUrlEncoded
    @POST("movies_forStores/store_add_movies.php")
    Call<QuerryResponse> AddMyMoviesPost(@Field("sgi")  String sgi,
                                         @Field("mids") StringBuilder mids
    );

    @FormUrlEncoded
    @POST("movies_forStores/store_saveChange_movies.php")
    Call<QuerryResponse> saveChangeMyMoviesPost(@Field("sgi")  String sgi,
                                                @Field("mids_add") StringBuilder mids_add,
                                                @Field("mids_del") StringBuilder mids_del
    );


    @GET("movies_forStores/movie_readSingleByID.php")
    Call<Movies> getMovieDetail(@Query("id") String id);


    @GET("movies_forStores/readPaging_movies_forStores.php")
    Call<MoviesResponsePaging> getMoviesList( @Query("sgid") String sgid,
                                              @Query("page") Integer page,
                                              @Query("type") String type,
                                              @Query("genre") String genre,
                                              @Query("year") String year,
                                              @Query("order") String order,
                                              @Query("myMovie") String myMovie
    );


    @GET("movies_forStores/store_readManageRequestedPaging.php")
    Call<ManageMovieRequestResponsePaging> getRequestMovies(@Query("page") Integer page,
                                                            @Query("sgid") String ugid,
                                                            @Query("status") String status
    );

    @GET("movies_forStores/movie_search_readByPaging.php")
    Call<MoviesResponsePaging> getSearchMovies(
                                              @Query("sgid") String sgid,
                                              @Query("page") Integer page,
                                              @Query("search") String storeId,
                                              @Query("type") String type,
                                              @Query("genre") String genre,
                                              @Query("year") String year,
                                              @Query("order") String order
    );


    @FormUrlEncoded
    @POST("movies_forStores/store_mangRequestMovieRent.php")
    Call<QuerryResponse> mangRequestMovieRent( @Field("mid")  String mid,
                                               @Field("ugid") String ugid,
                                               @Field("sid")  int    sid,
                                               @Field("req_s") String req_s,
                                               @Field("store_msg")  String store_msg

    );

    @FormUrlEncoded
    @POST("movies_forUser/store_cancelRequestMovieRent.php")
    Call<QuerryResponse> cancelMovieRequestRent( @Field("mid")  String mid,
                                                 @Field("ugid") String ugid,
                                                 @Field("sid")  int    sid
    );





    //Tvshow-------------------------------------------------------------------------------------
    @GET("tvshows_forUser/readOne_tvshow_forUsers.php")
    Call<Tvshow> getTvshowDetail(@Query("id") String id);


    @FormUrlEncoded
    @POST("tvshows_forStores/store_add_tvshow.php")
    Call<QuerryResponse> AddMyTvshowsPost(@Field("sgi")   String sgi,
                                          @Field("tvids") StringBuilder mids
    );

    @FormUrlEncoded
    @POST("tvshows_forStores/store_saveChange_tvshows.php")
    Call<QuerryResponse> saveChangeMyTvshowsPost(@Field("sgi")       String sgi,
                                                 @Field("tvids_add") StringBuilder mids_add,
                                                 @Field("tvids_del") StringBuilder mids_del
    );

    @GET("tvshows_forStores/tvshow_search_readByPaging.php")
    Call<TvshowsResponsePaging> getSearchTvshows(
            @Query("sgid") String sgid,
            @Query("page") Integer page,
            @Query("search") String storeId,
            @Query("type") String type,
            @Query("genre") String genre,
            @Query("year") String year,
            @Query("order") String order
    );


    @GET("tvshows_forStores/readPaging_tvshows_forStores.php")
    Call<TvshowsResponsePaging> getTvshowsList(
            @Query("sgid") String sgid,
            @Query("page") Integer page,
            @Query("type") String type,
            @Query("genre") String genre,
            @Query("year") String year,
            @Query("order") String order,
            @Query("myMovie") String myMovie
    );


    @GET("tvshows_forStores/store_readManageRequestedPaging.php")
    Call<ManageTvshowRequestResponsePaging> getRequestTvshows(@Query("page") Integer page,
                                                              @Query("sgid") String ugid,
                                                              @Query("status") String status
    );

    @FormUrlEncoded
    @POST("tvshows_forStores/store_mangRequestTvshowRent.php")
    Call<QuerryResponse> mangRequestTvshowRent( @Field("tid")  String tid,
                                                @Field("ugid") String ugid,
                                                @Field("sid")  int    sid,
                                                @Field("req_s") String req_s,
                                                @Field("store_msg")  String store_msg

    );

    @FormUrlEncoded
    @POST("tvshows_forUser/store_cancelRequestTvshowRent.php")
    Call<QuerryResponse> cancelTvshowRequestRent( @Field("tid")  String mid,
                                                 @Field("ugid") String ugid,
                                                 @Field("sid")  int    sid
    );

}

