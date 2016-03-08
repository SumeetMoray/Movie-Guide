package com.example.sumeet.popularmoviesstage1.model;

/**
 * Created by sumeet on 4/2/16.
 */
public class MovieReview {

    int reviewID; // primary key
    int movieID; // foreign key
    String authorName;
    String review;


    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
