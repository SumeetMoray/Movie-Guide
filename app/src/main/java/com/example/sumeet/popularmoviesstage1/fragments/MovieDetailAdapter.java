package com.example.sumeet.popularmoviesstage1.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sumeet.popularmoviesstage1.R;
import com.example.sumeet.popularmoviesstage1.model.Movie;
import com.example.sumeet.popularmoviesstage1.model.MovieReview;
import com.example.sumeet.popularmoviesstage1.model.MovieTrailer;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sumeet on 17/3/16.
 */
public class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<MovieReview> movieReviewsDataset = null;
    List<MovieTrailer> movieTrailerList = null;
    Movie movieForDisplay = null;
    Context context = null;

    int titleColor = -1;
    int colorVibrant = -1;


    public MovieDetailAdapter(List<MovieReview> dataset,Movie movieForDisplay,Context context,List<MovieTrailer> trailersList) {
        this.movieReviewsDataset = dataset;
        this.movieForDisplay = movieForDisplay;
        this.context = context;
        this.movieTrailerList = trailersList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        if(viewType == 0)
        {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_movie_detail,null,false);

            RecyclerView.ViewHolder viewHolder = new ViewHolderMovieDetails(itemView);

            return viewHolder;
        }


        if(viewType == 1)
        {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie_review,null,false);

            RecyclerView.ViewHolder viewHolder = new ViewHolderMovieReviews(itemView);

            return viewHolder;

        }

        if(viewType == 2)
        {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie_trailer,null,false);

            RecyclerView.ViewHolder viewHolder = new ViewHolderMovieTrailers(itemView);

            return viewHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position)==0)
        {
            ViewHolderMovieDetails viewHolder = (ViewHolderMovieDetails) holder;

            displayMovieContent(viewHolder);

        }

        if(getItemViewType(position)==1)
        {
            ViewHolderMovieReviews viewHolder = (ViewHolderMovieReviews) holder;

            viewHolder.authorName.setText("Review By : " + movieReviewsDataset.get(position-1).getAuthor());
            viewHolder.review.setText(movieReviewsDataset.get(position-1).getContent());

        }

        if(getItemViewType(position) ==2)
        {
            ViewHolderMovieTrailers viewHolder = (ViewHolderMovieTrailers) holder;

            final int itemPosition =  position-1-movieReviewsDataset.size();

            viewHolder.trailerName.setText(movieTrailerList.get(itemPosition).getName());
            Picasso.with(context)
                    .load("http://img.youtube.com/vi/" + movieTrailerList.get(itemPosition).getKey() + "/mqdefault.jpg")
                    .into(viewHolder.trailerThumbnail);


            viewHolder.trailerThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/watch?v=" + movieTrailerList.get(itemPosition).getKey())));

                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return (1 + movieReviewsDataset.size() + movieTrailerList.size());
    }


    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);

        if(position == 0)
        {
            return 0;
        }
        if((position > 0)&& (position <= (1 + movieReviewsDataset.size()-1)))
        {
            return 1;

        }

        if ((position > (1+movieReviewsDataset.size() -1)) && (position <= (1 + movieReviewsDataset.size() + movieTrailerList.size()-1)))
        {
            return 2;
        }

        return 0;
    }

    public class ViewHolderMovieDetails extends RecyclerView.ViewHolder{



        @Bind(R.id.movie_poster)
        ImageView moviePoster;

        @Bind(R.id.original_title)
        TextView originalTitle;

        @Bind(R.id.release_date_year)
        TextView releaseDate;

        @Bind(R.id.vote_average)
        TextView voteAverage;

        @Bind(R.id.plotSynopsis)
        TextView plotSynopsis;

        @Bind(R.id.month_day)
        TextView monthAndDay;

        @Bind(R.id.trailersTitle)
        TextView trailersTitle;

        @Bind(R.id.reviewsTitle)
        TextView reviewsTitle;





        public ViewHolderMovieDetails(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }





    public class ViewHolderMovieReviews extends RecyclerView.ViewHolder{


        @Bind(R.id.authorName)
        TextView authorName;

        @Bind(R.id.movieReview)
        TextView review;


        public ViewHolderMovieReviews(View itemView) {
            super(itemView);


            ButterKnife.bind(this,itemView);


        }
    }



    public class ViewHolderMovieTrailers extends RecyclerView.ViewHolder{

        @Bind(R.id.trailerName)
        TextView trailerName;

        @Bind(R.id.trailerThumbnail)
        ImageView trailerThumbnail;

        public ViewHolderMovieTrailers(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }


    public void displayMovieContent(ViewHolderMovieDetails holder)
    {

        if(movieForDisplay!=null) {
            Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + movieForDisplay.getPosterURL()).into(holder.moviePoster);
            holder.originalTitle.setText(movieForDisplay.getOriginalTitle());
            //releaseDate.setText(movieForDisplay.getReleaseDate());
            holder.voteAverage.setText(String.valueOf(movieForDisplay.getUserRating()) + " / 10");
            holder.plotSynopsis.setText(movieForDisplay.getPlotSynopsis());


        if(titleColor != -1)
        {
            holder.originalTitle.setBackgroundColor(titleColor);
            holder.trailersTitle.setBackgroundColor(colorVibrant);
            holder.reviewsTitle.setBackgroundColor(colorVibrant);
        }


        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");

        try {

            java.util.Date dDate = sdf.parse(movieForDisplay.getReleaseDate());

            holder.releaseDate.setText(String.valueOf(dDate.getYear()+1900));

            //monthAndDay.setText(months[dDate.getMonth()-1] + " " + String.valueOf(dDate.getDay()));


        } catch (ParseException e) {
            e.printStackTrace();
        }

        }

    }




    public void notifyColorChange(int vibrantDark,int vibrant)
    {
        titleColor = vibrantDark;
        colorVibrant = vibrant;
        notifyItemChanged(0);
    }

}
