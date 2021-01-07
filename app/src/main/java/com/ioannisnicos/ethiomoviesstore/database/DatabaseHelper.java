package com.ioannisnicos.ethiomoviesstore.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    public static final String FAV_MOVIES_TABLE_NAME = "FavouriteMoviesTable";
    public static final String FAV_TV_SHOWS_TABLE_NAME = "FavouriteTVShowsTable";
    public static final String ID = "id";
    public static final String MOVIE_TYPE = "type";
    public static final String TV_SHOW_TYPE = "type";
    public static final String MOVIE_ID = "movie_id";
    public static final String TV_SHOW_ID = "tv_show_id";
    public static final String POSTER_PATH = "poster_path";
    public static final String NAME = "name";
    public static final String Release_Year = "release_year";
    public static final String LAST_UPDATED = "last_updated";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String queryCreateMovieTable = "CREATE TABLE " + FAV_MOVIES_TABLE_NAME + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MOVIE_ID + " TEXT, "
                + MOVIE_TYPE + " TEXT, "
                + POSTER_PATH + " TEXT, "
                + NAME + " TEXT, "
                + Release_Year + " TEXT )";
        String queryCreateTVShowTable = "CREATE TABLE " + FAV_TV_SHOWS_TABLE_NAME + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TV_SHOW_ID + " TEXT, "
                + TV_SHOW_TYPE + " TEXT, "
                + POSTER_PATH + " TEXT, "
                + NAME + " TEXT, "
                + LAST_UPDATED + " TEXT )";


        sqLiteDatabase.execSQL(queryCreateTVShowTable);
        sqLiteDatabase.execSQL(queryCreateMovieTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
