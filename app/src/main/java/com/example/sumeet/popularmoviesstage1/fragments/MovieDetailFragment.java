package com.example.sumeet.popularmoviesstage1.fragments;

import android.content.ContentValues;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sumeet.popularmoviesstage1.R;
import com.example.sumeet.popularmoviesstage1.VolleySingleton;
import com.example.sumeet.popularmoviesstage1.data.MoviesContract;
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

public class MovieDetailFragment extends Fragment implements Target{

    Toolbar toolbar;

    FloatingActionButton fab;

    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton floatingActionButton;


    ImageView actionBarImage;


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





    public MovieDetailFragment() {
        super();
    }

    public Movie getMovieForDisplay() {
        return movieForDisplay;
    }

    public void setMovieForDisplay(Movie movieForDisplay) {
        this.movieForDisplay = movieForDisplay;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);



        setHasOptionsMenu(true);


        View fragmentView = inflater.inflate(R.layout.activity_movie_detail,container,false);




        //movieForDisplay.setPosterURL(getIntent().getStringExtra(Movie.POSTER_URL_KEY));
        //movieForDisplay.setPlotSynopsis(getIntent().getStringExtra(Movie.PLOT_SYNOPSIS_KEY));
        //movieForDisplay.setOriginalTitle(getIntent().getStringExtra(Movie.ORIGINAL_TITLE_KEY));
        //movieForDisplay.setReleaseDate(getIntent().getStringExtra(Movie.RELEASE_DATE_KEY));
        //movieForDisplay.setUserRating(getIntent().getDoubleExtra(Movie.USER_RATING_KEY,0));
        //movieForDisplay.setMovieId(getIntent().getIntExtra(Movie.MOVIE_ID_KEY,0));



        toolbar = (Toolbar) fragmentView.findViewById(R.id.toolbar);




        moviePoster = (ImageView) fragmentView.findViewById(R.id.movie_poster);
        releaseDate = (TextView) fragmentView.findViewById(R.id.release_date_year);
        voteAverage = (TextView) fragmentView.findViewById(R.id.vote_average);
        plotSynopsis = (TextView) fragmentView.findViewById(R.id.plotSynopsis);
        monthAndDay = (TextView) fragmentView.findViewById(R.id.month_day);

        originalTitle = (TextView) fragmentView.findViewById(R.id.original_title);

        reviews = (TextView) fragmentView.findViewById(R.id.reviews);
        actionBarImage = (ImageView) fragmentView.findViewById(R.id.actionBarImage);



        fab = (FloatingActionButton) fragmentView.findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!checkWetherFavourite()) {

                    ContentValues values = new ContentValues();

                    values.put(MoviesContract.Movie.MOVIE_ID, movieForDisplay.getMovieId());
                    values.put(MoviesContract.Movie.BACKDROP_IMAGE_URL, movieForDisplay.getBackdropImageURL());
                    values.put(MoviesContract.Movie.ORIGINAL_TITLE, movieForDisplay.getOriginalTitle());
                    values.put(MoviesContract.Movie.PLOT_SYNOPSIS, movieForDisplay.getPlotSynopsis());
                    values.put(MoviesContract.Movie.POSTER_URL, movieForDisplay.getPosterURL());
                    values.put(MoviesContract.Movie.RELEASE_DATE, movieForDisplay.getReleaseDate());
                    values.put(MoviesContract.Movie.USER_RATING, movieForDisplay.getUserRating());

                    getActivity().getContentResolver().insert(MoviesContract.Movie.CONTENT_URI, values);

                    fab.setImageResource(R.drawable.ic_favorite_white_24dp);

                }else
                {
                    int rowCount = getActivity().getContentResolver().delete(MoviesContract.Movie.CONTENT_URI,
                            MoviesContract.Movie.MOVIE_ID + " = ?",
                            new String[]{String.valueOf(movieForDisplay.getMovieId())});

                    if(rowCount == 1)
                    {
                        fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                    }

                }

            }
        });


        collapsingToolbarLayout = (CollapsingToolbarLayout) fragmentView.findViewById(R.id.collapsingToolbar);
        floatingActionButton = (FloatingActionButton) fragmentView.findViewById(R.id.fab);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        toolbar.inflateMenu(R.menu.menu_movie_browser);

        checkWetherFavourite();
        displayMovie();

        return  fragmentView;
    }


    public boolean checkWetherFavourite()
    {
        if(movieForDisplay!=null) {

            Cursor cursor = getActivity().getContentResolver().query(
                    MoviesContract.Movie.CONTENT_URI,
                    MoviesContract.Movie.PROJECTION_ALL,
                    MoviesContract.Movie.MOVIE_ID + " = ?",
                    new String[]{String.valueOf(movieForDisplay.getMovieId())},
                    null);

            Log.d("cursorCount", String.valueOf(cursor.getCount()));

            if (cursor.getCount() == 1) {
                fab.setImageResource(R.drawable.ic_favorite_white_24dp);
                return true;
            }
        }

        return false;
    }



    public void displayMovie()
    {

        if(movieForDisplay!=null) {



            toolbar.setTitle(movieForDisplay.getOriginalTitle());

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


            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w342/" + movieForDisplay.getBackdropImageURL()).placeholder(R.drawable.images).into(this);

            makeRequest();
            makeRequestTrailers();

        }


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
//                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getActivity().getWindow().setStatusBarColor(vibrantDark);
                //originalTitle.setTextColor(000000);
            }

            floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(vibrantDark));
            originalTitle.setBackgroundColor(vibrantDark);
            collapsingToolbarLayout.setContentScrimColor(vibrant);


            //actionBarImage.setImageBitmap(bitmap);

    }



    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_movie_browser, menu);

        //MenuItem item = menu.add("Fav");

        //item.setIcon(R.drawable.ic_favorite_white_24dp);


    }
}