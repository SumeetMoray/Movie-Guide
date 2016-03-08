package com.example.sumeet.popularmoviesstage1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sumeet on 7/3/16.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "movieDb";
    public static final int DB_VERSION = 1;


    public MovieDbHelper(Context context) {
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createStatement = "CREATE TABLE " + MoviesContract.Movie.TABLE_NAME + "("
                + MoviesContract.Movie.MOVIE_ID + " INTEGER PRIMARY KEY,"
                + MoviesContract.Movie.USER_RATING + " TEXT,"
                + MoviesContract.Movie.RELEASE_DATE + " TEXT,"
                + MoviesContract.Movie.POSTER_URL + " TEXT,"
                + MoviesContract.Movie.PLOT_SYNOPSIS + " TEXT,"
                + MoviesContract.Movie.ORIGINAL_TITLE + " TEXT,"
                + MoviesContract.Movie.BACKDROP_IMAGE_URL + " TEXT"
                + ")";


        db.execSQL(createStatement);





    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.Movie.TABLE_NAME);

        onCreate(db);

    }

}
