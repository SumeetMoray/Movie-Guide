package com.example.sumeet.popularmoviesstage1.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.sumeet.popularmoviesstage1.R;
import com.example.sumeet.popularmoviesstage1.model.Movie;

public class MainActivity extends AppCompatActivity implements MovieBrowserFragment.fragmentCallback {


    @Override
    public void movieSelected(Movie movie) {

        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();

        movieDetailFragment.setMovieForDisplay(movie);


        // Two pane case
        if(findViewById(R.id.movie_detail_container)!= null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.movie_detail_container,movieDetailFragment)
                    .commit();

        }

        // one pane case
        if(findViewById(R.id.movie_detail_container)== null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_holder,movieDetailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState==null)
        {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.movie_detail_container,new MovieDetailFragment())
//                    .commit();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_holder,new MovieBrowserFragment())
                    .commit();

        }
    }

}
