package com.example.sumeet.popularmoviesstage1.data;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;

import java.net.URI;

/**
 * Created by sumeet on 6/3/16.
 */
public class MoviesContract {


    public static final String AUTHORITY =
            "org.rhythmsofnature.popularmovies";

    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);







    public static final class Movie
    {


        // column names for Movie Table
        public static final String MOVIE_ID = "movieID";
        public static final String RELEASE_DATE = "releaseDate";
        public static final String USER_RATING = "userRating";
        public static final String PLOT_SYNOPSIS= "plotSynopsis";
        public static final String BACKDROP_IMAGE_URL = "backdropImageURL";
        public static final String POSTER_URL = "posterURL";
        public static final String ORIGINAL_TITLE = "originalTitle";
        public static final String TABLE_NAME = "movieDetails";







        // constants and contract for content provider

        public static final String CONTENT_TYPE= ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/vnd.org.rhythmsofnature.popularmovies.movieDetails";

        public static final String CONTENT_ITEM_TYPE= ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/vnd.org.rhythmsofnature.popularmovies.movieDetails";


        public static final String[] PROJECTION_ALL =
                {Movie.MOVIE_ID,Movie.RELEASE_DATE,Movie.USER_RATING,Movie.PLOT_SYNOPSIS,Movie.BACKDROP_IMAGE_URL,Movie.POSTER_URL,Movie.ORIGINAL_TITLE};

        public static final String SORT_ORDER_DEFAULT =
                Movie.MOVIE_ID + " ASC";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(MoviesContract.CONTENT_URI,TABLE_NAME);


    }










}
