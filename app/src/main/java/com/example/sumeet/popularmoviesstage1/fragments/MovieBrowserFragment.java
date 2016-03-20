package com.example.sumeet.popularmoviesstage1.fragments;

import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.example.sumeet.popularmoviesstage1.R;
import com.example.sumeet.popularmoviesstage1.VolleySingleton;
import com.example.sumeet.popularmoviesstage1.adapters.MoviesAdapter;
import com.example.sumeet.popularmoviesstage1.data.MoviesContract;
import com.example.sumeet.popularmoviesstage1.model.Movie;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieBrowserFragment extends Fragment implements AdapterView.OnItemSelectedListener , Target{


    ArrayList<Movie> dataset = new ArrayList<>();

    int currentSortOption;
    final int SORT_BY_POPULARITY = 0;
    final int SORT_BY_VOTE_AVERAGE = 1;
    final int SORT_BY_FAVOURITES = 2;

    String url="";

    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.appBar)
    AppBarLayout appBarLayout;

    @Bind(R.id.recyclerViewMovies)
    RecyclerView recyclerView;

    final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";




    final String PAGE_PARAM = "page";
    Integer currentPage=1;
    Integer totalPages=-1;

    final String VOTE_COUNT_PARAM = "vote_count.gte";

    // Example : final String API_KEY = "1234567890";
    final String API_KEY ="INSERT-YOUR-API-KEY-HERE";


    final String API_KEY_PARAM = "api_key";
    final String SORT_PARAM = "sort_by";
    final String SORT_OPTION_VOTE_AVERAGE = "vote_average.desc";
    final String SORT_OPTION_POPULARITY = "popularity.desc";



    MoviesAdapter adapter;
    GridLayoutManager layoutManager;

    @Bind(R.id.spinnerSortOptions)
    Spinner sortOptions;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View fragmentView = inflater.inflate(R.layout.activity_movie_browser,null,false);

        ButterKnife.bind(this,fragmentView);


        adapter = new MoviesAdapter(dataset,getActivity(),this);
        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(), 2);




        toolbar.setTitle(getResources().getString(R.string.app_name));


        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        if (metrics.widthPixels >= 600 && (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT))
        {
            // in case of larger width of tables set the column count to 3
            layoutManager.setSpanCount(3);
        }




        recyclerView.setLayoutManager(layoutManager);

        // bind spinner



        Log.d("backdropCheck",String.valueOf(dataset.size()));


        currentPage = ((dataset.size())/20);

        if(currentPage == 0)
        {
            currentPage = 1;
            makeRequest();
        }


        Log.d("backdropCheck",String.valueOf(dataset.size()) + " : " + String.valueOf(currentPage));


        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);




            }
        });


        recyclerView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);



                if(currentSortOption != SORT_BY_FAVOURITES)
                {



                if (layoutManager.findLastVisibleItemPosition()==(dataset.size()-1)) {

                    if (currentPage <= (totalPages - 1)) {

                        Log.d("backdropCheck",String.valueOf(dataset.size()) + " : " + String.valueOf(currentPage));

                        currentPage = currentPage + 1;

                        Snackbar.make(coordinatorLayout,"Loading page : " + String.valueOf(currentPage),Snackbar.LENGTH_SHORT).show();

                            //changeColor();

                            makeRequest();
                        }
                    }

                }

            }
        });





        Log.d("URL", url);



        sortOptions.setOnItemSelectedListener(this);


        if(savedInstanceState == null) {
            // make a request only if its the first launch

        }



        if(sortOptions.getSelectedItemPosition() == 2)
        {
            displayFavourites();
        }



        //loadFirstMovie();

        return fragmentView;
    }




    void makeRequest()
    {

        if(currentSortOption == SORT_BY_POPULARITY)
        {
            Uri builtUri = Uri.parse(BASE_URL)
                    .buildUpon()
                    .appendQueryParameter(PAGE_PARAM,String.valueOf(currentPage))
                    .appendQueryParameter(API_KEY_PARAM,API_KEY)
                    .appendQueryParameter(SORT_PARAM,SORT_OPTION_POPULARITY)
                    .build();


            url = builtUri.toString();

        }else if(currentSortOption == SORT_BY_VOTE_AVERAGE)
        {

            Uri builtUri = Uri.parse(BASE_URL)
                    .buildUpon()
                    .appendQueryParameter(PAGE_PARAM,String.valueOf(currentPage))
                    .appendQueryParameter(API_KEY_PARAM,API_KEY)
                    .appendQueryParameter(SORT_PARAM,SORT_OPTION_VOTE_AVERAGE)
                    .appendQueryParameter(VOTE_COUNT_PARAM,String.valueOf(100))
                    .build();

            url = builtUri.toString();

        }else if(currentSortOption == SORT_BY_FAVOURITES)
        {
            return;

        }



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                //Toast.makeText(MovieBrowser.this,response.toString(),Toast.LENGTH_LONG).show();


                try {
                    JSONArray moviesArray = response.getJSONArray("results");

                    totalPages = response.getInt("total_pages");



                    for(int i = 0; i < moviesArray.length();i++)
                    {
                        JSONObject movieObject = moviesArray.getJSONObject(i);

                        Movie movie = new Movie();

                        movie.setOriginalTitle(movieObject.getString("title"));
                        movie.setPlotSynopsis(movieObject.getString("overview"));
                        movie.setPosterURL(movieObject.getString("poster_path"));
                        movie.setReleaseDate(movieObject.getString("release_date"));
                        movie.setUserRating(movieObject.getDouble("vote_average"));
                        movie.setMovieId(movieObject.getInt("id"));
                        movie.setBackdropImageURL(movieObject.getString("backdrop_path"));

                        dataset.add(movie);


                    }


                    adapter.notifyDataSetChanged();


                    //loadFirstMovie();



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


    /*

    Spinner Item selected method Interface methods

     */

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if(position == 0)
        {
            if(currentSortOption == SORT_BY_VOTE_AVERAGE || currentSortOption == SORT_BY_FAVOURITES)
            {
                dataset.clear();
                currentPage = 1;

                currentSortOption = SORT_BY_POPULARITY;
                makeRequest();
                adapter.notifyDataSetChanged();

            }
            //dataset.clear();
            //currentPage = 1;

            //makeRequest();

        }else if(position == 1)
        {

            if(currentSortOption == SORT_BY_POPULARITY || currentSortOption == SORT_BY_FAVOURITES)
            {
                dataset.clear();
                currentPage = 1;
                currentSortOption = SORT_BY_VOTE_AVERAGE;
                makeRequest();
                adapter.notifyDataSetChanged();

            }



            //dataset.clear();
            //currentPage = 1;
            //makeRequest();
        } else if (position == 2)
        {
            currentSortOption = SORT_BY_FAVOURITES;

            displayFavourites();


            //MainActivity mainActivity = (MainActivity)getActivity();
            //mainActivity.notifyDisplayFavourites();
        }

    }


    public void displayFavourites()
    {
        ArrayList<Movie> moviesList = new ArrayList<>();

        dataset.clear();

        Cursor cursor = getContext().getContentResolver().query(MoviesContract.Movie.CONTENT_URI,MoviesContract.Movie.PROJECTION_ALL,null,null,MoviesContract.Movie.SORT_ORDER_DEFAULT);

        while (cursor.moveToNext())
        {
            Movie movieForDisplay = new Movie();

            movieForDisplay.setMovieId(cursor.getInt(cursor.getColumnIndex(MoviesContract.Movie.MOVIE_ID)));
            movieForDisplay.setBackdropImageURL(cursor.getString(cursor.getColumnIndex(MoviesContract.Movie.BACKDROP_IMAGE_URL)));
            movieForDisplay.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MoviesContract.Movie.ORIGINAL_TITLE)));
            movieForDisplay.setPlotSynopsis(cursor.getString(cursor.getColumnIndex(MoviesContract.Movie.PLOT_SYNOPSIS)));
            movieForDisplay.setPosterURL(cursor.getString(cursor.getColumnIndex(MoviesContract.Movie.POSTER_URL)));
            movieForDisplay.setReleaseDate(cursor.getString(cursor.getColumnIndex(MoviesContract.Movie.RELEASE_DATE)));
            movieForDisplay.setUserRating((double)cursor.getInt(cursor.getColumnIndex(MoviesContract.Movie.USER_RATING)));

            dataset.add(movieForDisplay);
        }

        if(cursor.isClosed())
        {
            cursor.close();
        }


        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    // A callback mechanism for communicating the event and transferring the movie to the movie detail fragment
    public void movieSelected(Movie movie) {


        if (getActivity() instanceof MainActivity)
        {
            MainActivity mainActivity = (MainActivity)getActivity();
            mainActivity.movieSelected(movie);
        }

    }



    boolean isfirstLoad = false;
    public void loadFirstMovie() {
        if (!isfirstLoad) {
            try {

                Log.d("applog","Outside Is first Load");

                if (dataset != null && dataset.size()>0 &&(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {

                    movieSelected(dataset.get(0));

                    isfirstLoad = true;

                    Log.d("applog","Inside Is first Load");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }
    }




    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        Palette palette = Palette.from(bitmap).generate();


        int color = 000000;
        int vibrant = palette.getVibrantColor(color);
        //int vibrantLight = palette.getLightVibrantColor(default);
        int vibrantDark = palette.getDarkVibrantColor(color);
        //int muted = palette.getMutedColor(default);
        //int mutedLight = palette.getLightMutedColor(default);
        //int mutedDark = palette.getDarkMutedColor(default);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(vibrantDark);

        }

        //collapsingToolbarLayout.setContentScrimColor(vibrant);
        appBarLayout.setBackgroundColor(vibrant);
        toolbar.setBackgroundColor(vibrant);


    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }


    public interface fragmentCallback{

        public void movieSelected(Movie movie);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}