package com.example.sumeet.popularmoviesstage1;

import java.util.Date;

/**
 * Created by sumeet on 21/10/15.
 */
public class Movie {



    // Keys for passing data from one activity to another using intents
    static final String ORIGINAL_TITLE_KEY = "OriginalTitle";
    static final String POSTER_URL_KEY = "PosterURL";
    static final String PLOT_SYNOPSIS_KEY = "PlotSynopsis";
    static final String USER_RATING_KEY = "UserRating";
    static final String RELEASE_DATE_KEY = "ReleaseDate";
    static final String MOVIE_ID_KEY = "MovieId";


    String originalTitle;
    String posterURL;
    String plotSynopsis;
    Double userRating;
    String releaseDate;
    int movieId;


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
