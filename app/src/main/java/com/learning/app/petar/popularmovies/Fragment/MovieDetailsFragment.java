package com.learning.app.petar.popularmovies.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.learning.app.petar.popularmovies.API.TheMovieDataBaseAPI;
import com.learning.app.petar.popularmovies.MainActivity;
import com.learning.app.petar.popularmovies.Model.Movie;
import com.learning.app.petar.popularmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Petar on 16.9.2016..
 */
public class MovieDetailsFragment extends Fragment {

    private ImageView mBackDropImageView;
    private TextView mReleaseYearTextView;
    private TextView mAverageVoteTextView;
    private TextView mOverViewTextView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_detail_fragment,null);
        Movie movie = getArguments().getParcelable(MainActivity.KEY_MOVIE_PARCELABLE);
        mBackDropImageView = (ImageView) view.findViewById(R.id.backdropImageView);
        mReleaseYearTextView = (TextView) view.findViewById(R.id.releaseYearTextView);
        mOverViewTextView = (TextView) view.findViewById(R.id.overViewTextView);
        mAverageVoteTextView = (TextView) view.findViewById(R.id.averageVoteTextView);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(movie.getTitle());
         Picasso.with(getContext()).load(TheMovieDataBaseAPI.GET_IMAGE_URI + movie.getPoster()).placeholder(R.drawable.logo).into(mBackDropImageView);
        mReleaseYearTextView.setText(TheMovieDataBaseAPI.getYear(movie.getReleaseDate()));
        mAverageVoteTextView.setText(movie.getAverageVote()+"/10");
        mOverViewTextView.setText(movie.getOverview());

        return view;
    }
}
