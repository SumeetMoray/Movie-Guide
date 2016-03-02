package com.example.sumeet.popularmoviesstage1.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.sumeet.popularmoviesstage1.R;
import com.example.sumeet.popularmoviesstage1.VolleySingleton;
import com.example.sumeet.popularmoviesstage1.model.Movie;
import com.example.sumeet.popularmoviesstage1.model.MovieReview;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MovieDetailFragment extends Fragment{

    ImageView actionBarImage;


    final String BASE_URL= "http://api.themoviedb.org/3/movie/";
    final String API_KEY_PARAM = "api_key";

    final String API_KEY ="65d0d0521287ca89086b923344334318";


    Movie movieForDisplay;
    ArrayList<MovieReview> reviewsList = new ArrayList<>();


    public Movie getMovieForDisplay() {
        return movieForDisplay;
    }

    public void setMovieForDisplay(Movie movieForDisplay) {
        this.movieForDisplay = movieForDisplay;
    }

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


    public MovieDetailFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);


        View fragmentView = inflater.inflate(R.layout.activity_movie_detail,container,false);




        //movieForDisplay.setPosterURL(getIntent().getStringExtra(Movie.POSTER_URL_KEY));
        //movieForDisplay.setPlotSynopsis(getIntent().getStringExtra(Movie.PLOT_SYNOPSIS_KEY));
        //movieForDisplay.setOriginalTitle(getIntent().getStringExtra(Movie.ORIGINAL_TITLE_KEY));
        //movieForDisplay.setReleaseDate(getIntent().getStringExtra(Movie.RELEASE_DATE_KEY));
        //movieForDisplay.setUserRating(getIntent().getDoubleExtra(Movie.USER_RATING_KEY,0));
        //movieForDisplay.setMovieId(getIntent().getIntExtra(Movie.MOVIE_ID_KEY,0));



        Toolbar toolbar = (Toolbar) fragmentView.findViewById(R.id.toolbar);




        moviePoster = (ImageView) fragmentView.findViewById(R.id.movie_poster);
        releaseDate = (TextView) fragmentView.findViewById(R.id.release_date_year);
        voteAverage = (TextView) fragmentView.findViewById(R.id.vote_average);
        plotSynopsis = (TextView) fragmentView.findViewById(R.id.plotSynopsis);
        monthAndDay = (TextView) fragmentView.findViewById(R.id.month_day);

        originalTitle = (TextView) fragmentView.findViewById(R.id.original_title);

        reviews = (TextView) fragmentView.findViewById(R.id.reviews);
        actionBarImage = (ImageView) fragmentView.findViewById(R.id.actionBarImage);



        if(movieForDisplay!=null) {



            toolbar.setTitle(movieForDisplay.getOriginalTitle());
           // Log.d("backdropCheck",movieForDisplay.getBackdropImageURL());

            //Glide.with(getActivity()).load("http://image.tmdb.org/t/p/w342/" + movieForDisplay.getBackdropImageURL()).placeholder(R.drawable.images).into(actionBarImage);



            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w342/" + movieForDisplay.getBackdropImageURL()).placeholder(R.drawable.images).into(actionBarImage);
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + movieForDisplay.getPosterURL()).into(moviePoster);
            originalTitle.setText(movieForDisplay.getOriginalTitle());
            //releaseDate.setText(movieForDisplay.getReleaseDate());
            voteAverage.setText(String.valueOf(movieForDisplay.getUserRating()) + " / 10");
            plotSynopsis.setText(movieForDisplay.getPlotSynopsis());




            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");


            try {

                java.util.Date dDate = sdf.parse(movieForDisplay.getReleaseDate());

                releaseDate.setText(String.valueOf(dDate.getYear()+1900));

                //monthAndDay.setText(months[dDate.getMonth()-1] + " " + String.valueOf(dDate.getDay()));


            } catch (ParseException e) {
                e.printStackTrace();
            }


            final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) fragmentView.findViewById(R.id.collapsingToolbar);
            final FloatingActionButton floatingActionButton = (FloatingActionButton) fragmentView.findViewById(R.id.fab);



            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w342/" + movieForDisplay.getBackdropImageURL()).placeholder(R.drawable.images).into(new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                    //actionBarImage.setImageBitmap(bitmap);

                    Palette palette = Palette.from(bitmap).generate();


                    int color = 000000;
                    int vibrant = palette.getVibrantColor(color);
                    int vibrantLight = palette.getLightVibrantColor(color);
                    int vibrantDark = palette.getDarkVibrantColor(color);
                    int muted = palette.getMutedColor(color);
                    int mutedLight = palette.getLightMutedColor(color);
                    int mutedDark = palette.getDarkMutedColor(color);


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        getActivity().getWindow().setStatusBarColor(vibrantDark);
                        floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(vibrantDark));
                        originalTitle.setBackgroundColor(vibrantDark);
                        //originalTitle.setTextColor(000000);
                    }

                    collapsingToolbarLayout.setContentScrimColor(vibrant);

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });


            /*

            actionBarImage.getti
            mBackdrop.setResponseObserver(new PaletteNetworkImageView.ResponseObserver() {
                @Override
                public void onSuccess() {


                    int colorDark = mBackdrop.getDarkVibrantColor();
                    int colorLight = mBackdrop.getVibrantColor();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        getActivity().getWindow().setStatusBarColor(colorDark);
                        fab.setBackgroundTintList(ColorStateList.valueOf(colorDark));
                    }
                    mCollapsingToolbarLayout.setContentScrimColor(colorLight);
                }

                @Override
                public void onError() {

                }
            });

            */


            makeRequest();
            makeRequestTrailers();

        }




        return  fragmentView;
    }



    // request reviews
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


        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);

    }

    public void makeRequestTrailers()
    {
        //http://api.themoviedb.org/3/movie/281957/videos?api_key=65d0d0521287ca89086b923344334318


        String url = BASE_URL + movieForDisplay.getMovieId() + "/videos";


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

                    JSONArray resultsArray = jsonObject.getJSONArray("results");


                    for (int i = 0; i < resultsArray.length(); i++)
                    {

                        JSONObject result = resultsArray.getJSONObject(i);

                        String key = result.getString("key");

//                                startActivity(new Intent(
  //                              Intent.ACTION_VIEW,
    //                            Uri.parse("https://www.youtube.com/watch?v=" + key)));

                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }



    public void updateReviews()
    {
        String reviewString = "";

        for(MovieReview review : reviewsList)
        {

            reviewString = reviewString
                    + "\n\n\n"
                    + "++++++++++++++++++++++++++++++++++++++++++++++++++++++"
                    + "\n\n"
                    + review.getAuthorName()
                    + "\n\n"
                    + review.getReview();


        }

        reviewString = reviewString
                + "\n\n"
                 + "++++++++++++++++++++++++++++++++++++++++++++++++++++++"
        +"\n\n";

        reviews.setText(reviewString);


    }

}