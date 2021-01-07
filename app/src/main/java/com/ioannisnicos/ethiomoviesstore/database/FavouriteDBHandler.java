package com.ioannisnicos.ethiomoviesstore.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.ioannisnicos.ethiomoviesstore.models.Movies;
import com.ioannisnicos.ethiomoviesstore.models.Tvshow;

import java.util.ArrayList;
import java.util.List;

public class FavouriteDBHandler {
    //MOVIES

    public static void addMovieToFav(Context context,String movieId,String movieType, String name, String posterPath, String releaseYear) {
        if (movieId == null) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        if (!isMovieFav(context, movieId)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.MOVIE_ID, movieId);
            contentValues.put(DatabaseHelper.MOVIE_TYPE, movieType);
            contentValues.put(DatabaseHelper.NAME, name);
            contentValues.put(DatabaseHelper.POSTER_PATH, posterPath);
             contentValues.put(DatabaseHelper.Release_Year, releaseYear);
            database.insert(DatabaseHelper.FAV_MOVIES_TABLE_NAME, null, contentValues);
        }
        database.close();
    }

    public static void removeMovieFromFav(Context context, String movieId) {
        if (movieId == null) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        if (isMovieFav(context, movieId)) {
            database.delete(DatabaseHelper.FAV_MOVIES_TABLE_NAME, DatabaseHelper.MOVIE_ID  + " = '" + movieId +"'", null);
        }
        database.close();
    }

    public static boolean isMovieFav(Context context, String movieId) {
        if (movieId == null) return false;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        boolean isMovieFav;
        Cursor cursor = database.query(DatabaseHelper.FAV_MOVIES_TABLE_NAME, null, DatabaseHelper.MOVIE_ID + " = '" + movieId +"'", null, null, null, null);
        if (cursor.getCount() == 1)
            isMovieFav = true;
        else
            isMovieFav = false;

        cursor.close();
        database.close();
        return isMovieFav;
    }

    public static List<Movies> getFavMovieBriefs(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        List<Movies> favMovies = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.FAV_MOVIES_TABLE_NAME, null, null, null, null, null, DatabaseHelper.ID + " DESC");
        while (cursor.moveToNext()) {
            String movieId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MOVIE_ID));
            String movieType = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MOVIE_TYPE));
            String posterPath = cursor.getString(cursor.getColumnIndex(DatabaseHelper.POSTER_PATH));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME));
            String releaseYear = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Release_Year));

            favMovies.add(new Movies(movieId,movieType,name,posterPath,releaseYear));
        }

        cursor.close();
        database.close();
        return favMovies;
    }





    //TV SHOWS

     public static void addTVShowToFav(Context context, String tvShowId,String tvshowType, String name, String posterPath, String last_updated) {
        if (tvShowId == null) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

         if (!isTVShowFav(context, tvShowId)) {
             ContentValues contentValues = new ContentValues();
             contentValues.put(DatabaseHelper.TV_SHOW_ID, tvShowId);
             contentValues.put(DatabaseHelper.TV_SHOW_TYPE, tvshowType);
             contentValues.put(DatabaseHelper.NAME, name);
             contentValues.put(DatabaseHelper.POSTER_PATH, posterPath);
             contentValues.put(DatabaseHelper.LAST_UPDATED,last_updated);
             database.insert(DatabaseHelper.FAV_TV_SHOWS_TABLE_NAME, null, contentValues);
         }
         database.close();
    }



    public static void removeTVShowFromFav(Context context, String tvShowId) {
        if (tvShowId == null) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        if (isTVShowFav(context, tvShowId)) {
            database.delete(DatabaseHelper.FAV_TV_SHOWS_TABLE_NAME, DatabaseHelper.TV_SHOW_ID + " = '" + tvShowId+"'", null);
        }
        database.close();
    }

    public static boolean isTVShowFav(Context context, String tvShowId) {
        if (tvShowId == null) return false;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        boolean isTVShowFav;
        Cursor cursor = database.query(DatabaseHelper.FAV_TV_SHOWS_TABLE_NAME, null, DatabaseHelper.TV_SHOW_ID + " = '" + tvShowId +"'", null, null, null, null);
        if (cursor.getCount() == 1)
            isTVShowFav = true;
        else
            isTVShowFav = false;

        cursor.close();
        database.close();
        return isTVShowFav;
    }


    public static List<Tvshow> getFavTVShowBriefs(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        List<Tvshow> favTVShows = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.FAV_TV_SHOWS_TABLE_NAME, null, null, null, null, null, DatabaseHelper.ID + " DESC");
        while (cursor.moveToNext()) {
            String tvshowId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TV_SHOW_ID));
            String tvshowType = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TV_SHOW_TYPE));
            String posterPath = cursor.getString(cursor.getColumnIndex(DatabaseHelper.POSTER_PATH));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME));
            String last_updated = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LAST_UPDATED));

            favTVShows.add(new Tvshow(tvshowId,tvshowType,name,posterPath,last_updated));
        }
        cursor.close();
        database.close();
        return favTVShows;
    }

}
