package com.example.sumeet.popularmoviesstage1.ServiceContract;

import com.example.sumeet.popularmoviesstage1.model.MovieReviewsEndpoint;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 17/3/16.
 */
public interface MovieReviewsService {

    @GET("/3/movie/{MovieID}/reviews")
    Call<MovieReviewsEndpoint> getMovieReviews(@Path("MovieID") int movieID, @Query("api_key")String apiKey);

}
