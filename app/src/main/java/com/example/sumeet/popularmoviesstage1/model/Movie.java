package com.example.sumeet.popularmoviesstage1.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by sumeet on 21/10/15.
 */
public class Movie implements Parcelable{



    String originalTitle;
    String posterURL;
    String backdropImageURL;
    String plotSynopsis;
    Double userRating;
    String releaseDate;
    int movieId;

    protected Movie(Parcel in) {
        originalTitle = in.readString();
        posterURL = in.readString();
        backdropImageURL = in.readString();
        plotSynopsis = in.readString();
        releaseDate = in.readString();
        movieId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalTitle);
        dest.writeString(posterURL);
        dest.writeString(backdropImageURL);
        dest.writeString(plotSynopsis);
        dest.writeString(releaseDate);
        dest.writeInt(movieId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };




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
