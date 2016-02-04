package com.example.sumeet.popularmoviesstage1;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MovieDetailActivity extends AppCompatActivity {


    final String BASE_URL= "http://api.themoviedb.org/3/movie/";
    final String API_KEY_PARAM = "api_key";

    final String API_KEY ="65d0d0521287ca89086b923344334318";


    Movie movieForDisplay;
    ArrayList<MovieReview> reviewsList = new ArrayList<>();


    ImageView moviePoster;
    TextView originalTitle,releaseDate,voteAverage,plotSynopsis,monthAndDay,reviews;

    String[] months = {"January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        movieForDisplay = new Movie();

        movieForDisplay.setPosterURL(getIntent().getStringExtra(Movie.POSTER_URL_KEY));
        movieForDisplay.setPlotSynopsis(getIntent().getStringExtra(Movie.PLOT_SYNOPSIS_KEY));
        movieForDisplay.setOriginalTitle(getIntent().getStringExtra(Movie.ORIGINAL_TITLE_KEY));
        movieForDisplay.setReleaseDate(getIntent().getStringExtra(Movie.RELEASE_DATE_KEY));
        movieForDisplay.setUserRating(getIntent().getDoubleExtra(Movie.USER_RATING_KEY,0));
        movieForDisplay.setMovieId(getIntent().getIntExtra(Movie.MOVIE_ID_KEY,0));





        moviePoster = (ImageView) findViewById(R.id.movie_poster);
        releaseDate = (TextView) findViewById(R.id.release_date_year);
        voteAverage = (TextView) findViewById(R.id.vote_average);
        plotSynopsis = (TextView) findViewById(R.id.plotSynopsis);
        monthAndDay = (TextView) findViewById(R.id.month_day);

        originalTitle = (TextView) findViewById(R.id.original_title);

        reviews = (TextView) findViewById(R.id.reviews);

        if(movieForDisplay!=null) {



            Picasso.with(this).load("http://image.tmdb.org/t/p/w185/" + movieForDisplay.getPosterURL()).into(moviePoster);
            originalTitle.setText(movieForDisplay.getOriginalTitle());
            //releaseDate.setText(movieForDisplay.getReleaseDate());
            voteAverage.setText(String.valueOf(movieForDisplay.getUserRating()) + " / 10");
            plotSynopsis.setText(movieForDisplay.getPlotSynopsis());


            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");


            try {

                java.util.Date dDate = sdf.parse(movieForDisplay.getReleaseDate());

                releaseDate.setText(String.valueOf(dDate.getYear()+1900));

                monthAndDay.setText(months[dDate.getMonth()-1] + " " + String.valueOf(dDate.getDay()));


            } catch (ParseException e) {
                e.printStackTrace();
            }




        }


        makeRequest();

    }



    public void makeRequest()
    {

        String url = BASE_URL + movieForDisplay.getMovieId() + "/reviews";

        Uri builtUri = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter(API_KEY_PARAM,API_KEY)
                .build();


        String builtURL = builtUri.toString();


        StringRequest request = new StringRequest(Request.Method.GET, builtURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for(int i = 0;i<jsonArray.length();i++){

                        JSONObject reviewJSON = jsonArray.getJSONObject(i);

                        MovieReview movieReview = new MovieReview();

                        movieReview.setAuthorName(reviewJSON.getString("author"));
                        movieReview.setReview(reviewJSON.getString("content"));

                        reviewsList.add(movieReview);

                    }




                    updateReviews();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        VolleySingleton.getInstance(this).addToRequestQueue(request);

    }



    public void updateReviews()
    {
        String reviewString = "";


        for(MovieReview review : reviewsList)
        {

            reviewString = reviewString + "\n\n" + review.getAuthorName() + "\n\n" +
                    review.getReview();


        }


        reviews.setText(reviewString);



    }



}
