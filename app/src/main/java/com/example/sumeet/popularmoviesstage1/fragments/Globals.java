package com.example.sumeet.popularmoviesstage1.fragments;

import com.example.sumeet.popularmoviesstage1.model.Movie;

import java.util.ArrayList;

/**
 * Created by sumeet on 2/3/16.
 */
public class Globals {

    public static ArrayList<Movie> movieDataset;


    public static ArrayList<Movie> getMovieDataset()
    {
        if(movieDataset == null)
        {
            movieDataset = new ArrayList<Movie>();


        }

        return movieDataset;

    }

}
