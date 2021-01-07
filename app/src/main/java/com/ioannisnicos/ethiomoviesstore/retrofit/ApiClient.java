package com.ioannisnicos.ethiomoviesstore.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

        public static final String BASE_URL = "http://84d894c8764e.ngrok.io/e_movie_api/";
        //public static final String BASE_URL = "http://35.230.153.27/e_movie_api/";
        //public static final String BASE_URL = "http://34.74.106.238/e_movie_api/";
        //public static final String BASE_URL = "https://accretive-circles.000webhostapp.com/e_movie_api/";
        //public static final String BASE_URL = "http://192.168.1.238/ethio_movie_api/";
        //public static final String BASE_URL = "http://192.168.43.249/ethio_movie_api/";

        private static Retrofit retrofit = null;
        private static APIInterface retrofitApi = null;

        public synchronized static Retrofit getClient() {
            if (retrofit == null)
            {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)
                        .build();

                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build();
            }
            return retrofit;
        }

        public static APIInterface getRetrofitApi() {

            if (retrofitApi == null) retrofitApi = getClient().create(APIInterface.class);

            return retrofitApi;
        }
}