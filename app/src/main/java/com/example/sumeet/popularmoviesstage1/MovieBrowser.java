package com.example.sumeet.popularmoviesstage1;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieBrowser extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayList<Movie> dataset = new ArrayList<Movie>();

    int currentSortOption;
    final int SORT_BY_POPULARITY = 0;
    final int SORT_BY_VOTE_AVERAGE = 1;

    String url="";

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_browser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMovies);

        adapter = new MoviesAdapter(dataset,this);
        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        // bind spinner



        recyclerView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (layoutManager.findLastVisibleItemPosition()==(dataset.size()-1)) {

                    if (currentPage <= (totalPages - 1)) {

                        currentPage = currentPage + 1;
                        makeRequest();
                    }

                }

            }
        });





        Log.d("URL", url);


        sortOptions = (Spinner) findViewById(R.id.spinnerSortOptions);
        sortOptions.setOnItemSelectedListener(this);

        makeRequest();



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_browser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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


        VolleySingleton.getInstance(this).addToRequestQueue(request);


    }


    /*

    Spinner Item selected method Interface methods

     */

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if(position == 0)
        {
            currentSortOption = SORT_BY_POPULARITY;
            dataset.clear();
            currentPage = 1;

            makeRequest();

        }else if(position == 1)
        {
            currentSortOption = SORT_BY_VOTE_AVERAGE;
            dataset.clear();
            currentPage = 1;
            makeRequest();
        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
