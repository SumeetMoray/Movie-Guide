package com.example.sumeet.popularmoviesstage1.discardedClasses;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.sumeet.popularmoviesstage1.R;
import com.example.sumeet.popularmoviesstage1.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sumeet on 21/10/15.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {


    ArrayList<Movie> dataset;
    Context context;

    public MoviesAdapter(ArrayList<Movie> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_adapter_item,null);


        MoviesViewHolder viewHolder = new MoviesViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MoviesViewHolder holder, final int position) {

        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + dataset.get(position).getPosterURL()).into(holder.moviePoster);




        holder.moviePoster.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Movie movie = dataset.get(position);

                Intent intent = new Intent(context, MovieDetailActivity.class);

                /*
                intent.putExtra(Movie.POSTER_URL_KEY,movie.getPosterURL());
                intent.putExtra(Movie.PLOT_SYNOPSIS_KEY,movie.getPlotSynopsis());
                intent.putExtra(Movie.ORIGINAL_TITLE_KEY,movie.getOriginalTitle());
                intent.putExtra(Movie.RELEASE_DATE_KEY,movie.getReleaseDate());
                intent.putExtra(Movie.USER_RATING_KEY,movie.getUserRating());
                intent.putExtra(Movie.MOVIE_ID_KEY,movie.getMovieId());

*/
                context.startActivity(intent);

            }
        });

    }


    @Override
    public int getItemCount() {


        return dataset.size();

    }



    class MoviesViewHolder extends RecyclerView.ViewHolder
    {

        ImageView moviePoster;

        public MoviesViewHolder(View itemView) {

            super(itemView);
            moviePoster = (ImageView) itemView.findViewById(R.id.movieImage);
        }
    }

}
