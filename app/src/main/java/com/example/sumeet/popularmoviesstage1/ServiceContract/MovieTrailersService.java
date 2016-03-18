package com.example.sumeet.popularmoviesstage1.ServiceContract;

import com.example.sumeet.popularmoviesstage1.model.MovieTrailerEndpoint;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 18/3/16.
 */
public interface MovieTrailersService {


    //http://img.youtube.com/vi/RFinNxS5KN4/mqdefault.jpg

    @GET("/3/movie/{MovieID}/videos")
    Call<MovieTrailerEndpoint> getMovieTrailers(@Path("MovieID")int movieID, @Query("api_key")String apiKey);
}
