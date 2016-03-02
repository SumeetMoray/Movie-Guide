package com.example.sumeet.popularmoviesstage1.fragments;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.example.sumeet.popularmoviesstage1.model.Movie;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class MovieBrowserFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    //ArrayList<Movie> dataset;
    ArrayList<Movie> dataset;

    int currentSortOption;
    final int SORT_BY_POPULARITY = 0;
    final int SORT_BY_VOTE_AVERAGE = 1;

    String url="";

    CoordinatorLayout coordinatorLayout;


    final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";




    final String PAGE_PARAM = "page";
    Integer currentPage=1;
    Integer totalPages=-1;

    final String VOTE_COUNT_PARAM = "vote_count.gte";

    final String API_KEY ="65d0d0521287ca89086b923344334318";

    final String API_KEY_PARAM = "api_key";
    final String SORT_PARAM = "sort_by";
    final String SORT_OPTION_VOTE_AVERAGE = "vote_average.desc";
    final String SORT_OPTION_POPULARITY = "popularity.desc";


    RecyclerView recyclerView;
    MoviesAdapter adapter;
    GridLayoutManager layoutManager;
    Spinner sortOptions;


    boolean isTwoPane = false;

    final String isTwoPane_KEY = "isTwoPane";


    Toolbar toolbar;
    AppBarLayout appBarLayout;


    public boolean isTwoPane() {
        return isTwoPane;
    }

    public void setTwoPane(boolean twoPane) {
        isTwoPane = twoPane;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        dataset = Globals.getMovieDataset();

        View fragmentView = inflater.inflate(R.layout.activity_movie_browser,null,false);

        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.recyclerViewMovies);

        adapter = new MoviesAdapter(dataset,getActivity(),this);
        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(), 2);

        toolbar = (Toolbar) fragmentView.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));





        // restore instance state
        // an instance state needs to be restored after a configuration change
        if(savedInstanceState != null) {
            isTwoPane = savedInstanceState.getBoolean(isTwoPane_KEY, false);
        }



        Log.d("TwoPaneCheck",String.valueOf(isTwoPane));

        if(isTwoPane) {

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                layoutManager.setSpanCount(1);

            }
        }


        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        if (metrics.widthPixels >= 600 && (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT))
        {
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



        recyclerView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);






                if (layoutManager.findLastVisibleItemPosition()>=(dataset.size()-1)) {

                    if (currentPage <= (totalPages - 1)) {

                        Log.d("backdropCheck",String.valueOf(dataset.size()) + " : " + String.valueOf(currentPage));

                        currentPage = currentPage + 1;

                        Snackbar.make(coordinatorLayout,"Loading page : " + String.valueOf(currentPage),Snackbar.LENGTH_SHORT).show();

                            //changeColor();

                        makeRequest();



                    }

                }

            }
        });





        Log.d("URL", url);


        sortOptions = (Spinner) fragmentView.findViewById(R.id.spinnerSortOptions);
        sortOptions.setOnItemSelectedListener(this);


        if(savedInstanceState == null) {
            // make a request only if its the first launch

        }

        coordinatorLayout = (CoordinatorLayout) fragmentView.findViewById(R.id.coordinatorLayout);


        appBarLayout = (AppBarLayout) fragmentView.findViewById(R.id.appBar);







        return fragmentView;
    }



    public void changeColor()
    {

        if(dataset.size()!=0) {


            int min = 1;
            int max = dataset.size()-1;

            Random r = new Random();
            int rand = r.nextInt(max - min + 1) + min;


            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + dataset.get(rand).getPosterURL()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {


//                actionBarImage.setImageBitmap(bitmap);

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
            });

        }
    }



    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

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

                        adapter.notifyDataSetChanged();


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


    /*

    Spinner Item selected method Interface methods

     */

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if(position == 0)
        {
            if(currentSortOption == SORT_BY_VOTE_AVERAGE)
            {
                dataset.clear();
                currentPage = 1;

                makeRequest();
                adapter.notifyDataSetChanged();

            }


            currentSortOption = SORT_BY_POPULARITY;


            //dataset.clear();
            //currentPage = 1;

            //makeRequest();

        }else if(position == 1)
        {

            if(currentSortOption == SORT_BY_POPULARITY)
            {
                dataset.clear();
                currentPage = 1;
                makeRequest();
                adapter.notifyDataSetChanged();

            }


            currentSortOption = SORT_BY_VOTE_AVERAGE;
            //dataset.clear();
            //currentPage = 1;
            //makeRequest();
        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    public void movieSelected(Movie movie) {


        if (getActivity() instanceof MainActivity)
        {
            MainActivity mainActivity = (MainActivity)getActivity();
            mainActivity.movieSelected(movie);
        }

    }


    public interface fragmentCallback{

        public void movieSelected(Movie movie);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(isTwoPane_KEY , isTwoPane);

    }

}