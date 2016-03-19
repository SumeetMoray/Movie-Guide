package com.example.sumeet.popularmoviesstage1.fragments;

import android.content.ContentValues;
import android.content.Intent;
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
import android.support.v4.view.MenuItemCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sumeet.popularmoviesstage1.R;
import com.example.sumeet.popularmoviesstage1.ServiceContract.MovieReviewsService;
import com.example.sumeet.popularmoviesstage1.ServiceContract.MovieTrailersService;
import com.example.sumeet.popularmoviesstage1.VolleySingleton;
import com.example.sumeet.popularmoviesstage1.adapters.MovieDetailAdapter;
import com.example.sumeet.popularmoviesstage1.data.MoviesContract;
import com.example.sumeet.popularmoviesstage1.model.Movie;
import com.example.sumeet.popularmoviesstage1.model.MovieReview;
import com.example.sumeet.popularmoviesstage1.model.MovieReviewsEndpoint;
import com.example.sumeet.popularmoviesstage1.model.MovieTrailer;
import com.example.sumeet.popularmoviesstage1.model.MovieTrailerEndpoint;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailFragment extends Fragment implements Target{

    @Bind(R.id.movieReviewsList)
    RecyclerView movieReviewsList;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.actionBarImage)
    ImageView actionBarImage;


    MovieDetailAdapter movieDetailAdapter;


    final String BASE_URL_API = "http://api.themoviedb.org";

    final String BASE_URL= "http://api.themoviedb.org/3/movie/";
    final String API_KEY_PARAM = "api_key";

    final String API_KEY ="65d0d0521287ca89086b923344334318";



    Movie movieForDisplay = null;

    ArrayList<MovieReview> reviewsList = new ArrayList<>();

    ArrayList<MovieTrailer> movieTrailersList = new ArrayList<>();



    // default constructor

    public MovieDetailFragment() {
        super();
    }


    // getters and Setters

    public Movie getMovieForDisplay() {
        return movieForDisplay;
    }

    public void setMovieForDisplay(Movie movieForDisplay) {
        this.movieForDisplay = movieForDisplay;

        if(movieDetailAdapter!=null)
        {
            movieDetailAdapter.setMovieForDisplay(movieForDisplay);

        }
    }

    private ShareActionProvider mShareActionProvider;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View fragmentView = inflater.inflate(R.layout.activity_movie_detail,container,false);

        ButterKnife.bind(this,fragmentView);



        if(savedInstanceState!=null) {
            movieForDisplay = savedInstanceState.getParcelable("movie");
        }



        // initialize the movie Reviews list
        movieDetailAdapter = new MovieDetailAdapter(reviewsList,movieForDisplay,getContext(),movieTrailersList);


        movieReviewsList.setAdapter(movieDetailAdapter);

        movieReviewsList.setLayoutManager(new GridLayoutManager(getActivity(),1));


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });




        setHasOptionsMenu(true);
        toolbar.inflateMenu(R.menu.menu_movie_browser);


        shareAction();


        checkWetherFavourite();
        updateTitleAndBackdrop();
        loadTrailersAndReviews();



        return  fragmentView;
    }


    public void shareAction() {


        String firstTrailerURL = "";


        if (movieTrailersList.size() > 0) {
            firstTrailerURL = "https://www.youtube.com/watch?v=" + movieTrailersList.get(0).getKey();
        }


        Intent sendIntent = new Intent();

        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, firstTrailerURL);


        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "A movie recommendation !!");

        sendIntent.setType("text/plain");


        if (movieTrailersList.size() > 0) {

            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/watch?v=" + movieTrailersList.get(0).getKey()));

        }


        ShareActionProvider mShareActionProvider = new ShareActionProvider(getActivity());

        if (mShareActionProvider != null && sendIntent != null) {
            mShareActionProvider.setShareIntent(sendIntent);
        }

        try {


            MenuItem item = toolbar.getMenu().findItem(R.id.action_share);
            MenuItemCompat.setActionProvider(item, mShareActionProvider);

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }





    public void loadTrailersAndReviews()
    {

        //if(!checkWetherFavourite())
        //{

            if(movieForDisplay!=null) {

                makeRequestReviews();

                makeRequestTrailers();

                //makeRequestReviewsVolley();
                //makeRequestTrailersVolley();
            }
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



    public void updateTitleAndBackdrop()
    {

        if(movieForDisplay!=null) {

            toolbar.setTitle(movieForDisplay.getOriginalTitle());

            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w342/" + movieForDisplay.getBackdropImageURL()).placeholder(R.drawable.images).into(actionBarImage);
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w342/" + movieForDisplay.getBackdropImageURL()).placeholder(R.drawable.images).into(this);


        }


    }




    // request reviews
    public void makeRequestReviewsVolley()
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

                        movieReview.setAuthor(reviewJSON.getString("author"));
                        movieReview.setContent(reviewJSON.getString("content"));

                        reviewsList.add(movieReview);

                    }

                    movieDetailAdapter.notifyDataSetChanged();



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



    // Making request using volley. Commented Out !!



    public void makeRequestTrailersVolley()
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


                        MovieTrailer trailer = new MovieTrailer();

                        trailer.setKey(result.getString("key"));

                        trailer.setName(result.getString("name"));


//                                startActivity(new Intent(
  //                              Intent.ACTION_VIEW,
    //                            Uri.parse("https://www.youtube.com/watch?v=" + key)));


                        movieTrailersList.add(trailer);
                    }

                    shareAction();

                    movieDetailAdapter.notifyDataSetChanged();



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







    // obtaining colors dynamically from the backdrop image poster
    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            //actionBarImage.setImageBitmap(bitmap);

            Palette palette = Palette.from(bitmap).generate();

            int color = 323235;
            int vibrant = palette.getVibrantColor(color);
            int vibrantLight = palette.getLightVibrantColor(color);
            int vibrantDark = palette.getDarkVibrantColor(color);
            int muted = palette.getMutedColor(color);
            int mutedLight = palette.getLightMutedColor(color);
            int mutedDark = palette.getDarkMutedColor(color);

            Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();

            //if(vibrantSwatch!=null) {
              //  originalTitle.setTextColor(vibrantSwatch.getTitleTextColor());
            //}




            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getActivity().getWindow().setStatusBarColor(vibrantDark);

            }

            fab.setBackgroundTintList(ColorStateList.valueOf(vibrantDark));
            //fab.setBackgroundColor(vibrantDark);

            //originalTitle.setBackgroundColor(vibrantDark);

            collapsingToolbarLayout.setContentScrimColor(vibrant);


            //actionBarImage.setImageBitmap(bitmap);

            movieDetailAdapter.notifyColorChange(vibrantDark,muted);

    }



    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }



    // Making request for obtaining Reviews
    public void makeRequestReviews()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieReviewsService movieReviewsService = retrofit.create(MovieReviewsService.class);

        Call<MovieReviewsEndpoint> movieReviewsEndpointCall = movieReviewsService.getMovieReviews(movieForDisplay.getMovieId(),API_KEY);

        movieReviewsEndpointCall.enqueue(new Callback<MovieReviewsEndpoint>() {
            @Override
            public void onResponse(Call<MovieReviewsEndpoint> call, retrofit2.Response<MovieReviewsEndpoint> response) {


                MovieReviewsEndpoint movieReviewsEndpoint = response.body();

                if(movieReviewsEndpoint!=null) {

                    List<MovieReview> movieReviewList = movieReviewsEndpoint.getResults();
                    reviewsList.addAll(movieReviewList);

                    movieDetailAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<MovieReviewsEndpoint> call, Throwable t) {

            }
        });


    }

    public void makeRequestTrailers()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        MovieTrailersService movieTrailersService = retrofit.create(MovieTrailersService.class);


        Call<MovieTrailerEndpoint> movieTrailerEndpointCall = movieTrailersService.getMovieTrailers(movieForDisplay.getMovieId(),API_KEY);

        movieTrailerEndpointCall.enqueue(new Callback<MovieTrailerEndpoint>() {
            @Override
            public void onResponse(Call<MovieTrailerEndpoint> call, retrofit2.Response<MovieTrailerEndpoint> response) {


                MovieTrailerEndpoint movieTrailerEndpoint = response.body();

                if (movieTrailerEndpoint != null)
                {

                    List<MovieTrailer> trailerList = movieTrailerEndpoint.getResults();

                    movieTrailersList.addAll(trailerList);



                    movieDetailAdapter.notifyDataSetChanged();
                }

                shareAction();

            }

            @Override
            public void onFailure(Call<MovieTrailerEndpoint> call, Throwable t) {

            }
        });

    }



    // when the fab is clicked mark the movie as favourite and store it into the database
    @OnClick(R.id.fab)
    public void markMovieAsFavourite()
    {
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

            values.clear();



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


    @Override
    public void onDestroy() {
        super.onDestroy();

        // unbind the butterknife library
        ButterKnife.unbind(this);
    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(movieForDisplay!=null) {
            outState.putParcelable("movie", movieForDisplay);
        }

    }

}