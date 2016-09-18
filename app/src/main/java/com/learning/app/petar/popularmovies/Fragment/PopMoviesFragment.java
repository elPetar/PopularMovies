package com.learning.app.petar.popularmovies.Fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learning.app.petar.popularmovies.Adapter.PopMoviesAdapter;
import com.learning.app.petar.popularmovies.MainActivity;
import com.learning.app.petar.popularmovies.Model.Movie;
import com.learning.app.petar.popularmovies.R;

import java.util.Arrays;

/**
 * Created by Petar on 15.9.2016..
 */
public class PopMoviesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static SwipeRefreshLayout swipeDown;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pop_movies_fragment_layout,null);
         swipeDown = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeDown.setOnRefreshListener(this);
        Parcelable[] parcelables = getArguments().getParcelableArray(MainActivity.KEY_MOVIES_PARCELABLE);
        Movie[] movies = Arrays.copyOf(parcelables,parcelables.length,Movie[].class);
        PopMoviesAdapter.IPopMoviesClick listener = (PopMoviesAdapter.IPopMoviesClick) getActivity();
        PopMoviesAdapter adapter = new PopMoviesAdapter(movies,getActivity(),listener);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.popularMoviesRecycleView);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onRefresh() {
        MainActivity main = (MainActivity) getActivity();
            if(main.mBound)
             main.mAPIRequestService.run(main.currentUri);
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity main = (MainActivity) getActivity();
        main.setActionBarTitle(main.currentUri);
        main.getSupportActionBar().setHomeButtonEnabled(true);
    }
}
