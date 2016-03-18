package com.example.sumeet.popularmoviesstage1.model;

import java.util.List;

/**
 * Created by sumeet on 18/3/16.
 */
public class MovieTrailerEndpoint {


    int id;

    List<MovieTrailer> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MovieTrailer> getResults() {
        return results;
    }

    public void setResults(List<MovieTrailer> results) {
        this.results = results;
    }
}
