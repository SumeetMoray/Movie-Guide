package com.example.sumeet.popularmoviesstage1.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.sumeet.popularmoviesstage1.R;
import com.example.sumeet.popularmoviesstage1.model.Movie;

public class MainActivity extends AppCompatActivity implements MovieBrowserFragment.fragmentCallback {

    MovieDetailFragment movieDetailFragment;

    @Override
    public void movieSelected(Movie movie) {

        movieDetailFragment = new MovieDetailFragment();

        movieDetailFragment.setMovieForDisplay(movie);


        // Two pane case
        // && (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)

        if((findViewById(R.id.movie_detail_container)!= null) )
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.movie_detail_container,movieDetailFragment)
                    .commit();

        } else
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_holder,movieDetailFragment)
                    .addToBackStack(null)
                    .commit();
        }


        // one pane case
        //if(findViewById(R.id.movie_detail_container)== null)
        //{

        //}
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        //setSupportActionBar(toolbar);






        if(savedInstanceState==null)
        {

            MovieBrowserFragment fragment = new MovieBrowserFragment();

            if(findViewById(R.id.movie_detail_container)!= null)
            {
                fragment.setTwoPane(true);
            } else
            {
                fragment.setTwoPane(false);

            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_holder,fragment)
                    .commit();

        }


        // the if block to ensure when the configuration is changed from portrait to landscape. The Fragment doesnt get
        // Stuck in the detail fragment.
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

            getSupportFragmentManager().popBackStackImmediate();
        }




    }

    @Override
    protected void onResume() {
        super.onResume();

        //getSupportActionBar().hide();

    }



    /*
    public void notifyDisplayFavourites()
    {

        movieDetailFragment.displayFavourites();

    }
    */



}
