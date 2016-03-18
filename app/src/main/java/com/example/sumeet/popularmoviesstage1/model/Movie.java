package com.example.sumeet.popularmoviesstage1.model;

import android.os.Parcelable;

import java.util.Date;

/**
 * Created by sumeet on 21/10/15.
 */
public class Movie{


    public static String END_POINT_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=65d0d0521287ca89086b923344334318";


    // Keys for passing data from one activity to another using intents
    //public static final String ORIGINAL_TITLE_KEY = "OriginalTitle";
    //public static final String POSTER_URL_KEY = "PosterURL";
    //public static final String PLOT_SYNOPSIS_KEY = "PlotSynopsis";
    //public static final String USER_RATING_KEY = "UserRating";
    //public static final String RELEASE_DATE_KEY = "ReleaseDate";
    //public static final String MOVIE_ID_KEY = "MovieId";


    String originalTitle;
    String posterURL;
    String backdropImageURL;
    String plotSynopsis;
    Double userRating;
    String releaseDate;
    int movieId;

    public String getBackdropImageURL() {
        return backdropImageURL;
    }

    public void setBackdropImageURL(String backdropImageURL) {
        this.backdropImageURL = backdropImageURL;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public Double getUserRating() {
        return userRating;
    }

    public void setUserRating(Double userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
