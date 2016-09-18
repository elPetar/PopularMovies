package com.learning.app.petar.popularmovies.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.learning.app.petar.popularmovies.API.TheMovieDataBaseAPI;
import com.learning.app.petar.popularmovies.Model.Movie;
import com.learning.app.petar.popularmovies.R;
import com.squareup.picasso.Picasso;


/**
 * Created by Petar on 16.9.2016..
 */
public class PopMoviesAdapter extends RecyclerView.Adapter<PopMoviesAdapter.PopMoviesViewHolder> {
    public  interface IPopMoviesClick{
        void onMovieClicked(int position);
    }

    private static final String TAG = PopMoviesAdapter.class.getSimpleName();
    private Movie[]mMovies;
    private Activity mContext;
    private IPopMoviesClick mListener;

    public PopMoviesAdapter(Movie[] movies,Activity context, IPopMoviesClick listener){
        mMovies = movies;
        mContext = context;
        mListener = listener;
    }

    @Override
    public PopMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_movies_item_1,parent,false);
        PopMoviesViewHolder viewHolder = new PopMoviesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PopMoviesViewHolder holder, int position) {
        holder.bindWithData(position);
    }

    @Override
    public int getItemCount() {
        return mMovies.length;
    }

    public class  PopMoviesViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        ImageView mMovieImage;
        private int mPosition;

        public PopMoviesViewHolder(View itemView) {
            super(itemView);
            mMovieImage = (ImageView) itemView.findViewById(R.id.movieImageView);
            itemView.setOnClickListener(this);
        }
        public void bindWithData(int position){
            mPosition = position;
            String image = TheMovieDataBaseAPI.GET_IMAGE_URI + mMovies[position].getImage();
            Picasso.with(mContext).load(image).placeholder(R.drawable.logo).into(mMovieImage);                   ;

        }

        @Override
        public void onClick(View view) {
            mListener.onMovieClicked(mPosition);
        }
    }

}
