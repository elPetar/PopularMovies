package com.learning.app.petar.popularmovies;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.learning.app.petar.popularmovies.API.APIRequestService;
import com.learning.app.petar.popularmovies.API.TheMovieDataBaseAPI;
import com.learning.app.petar.popularmovies.Adapter.PopMoviesAdapter;
import com.learning.app.petar.popularmovies.Fragment.MovieDetailsFragment;
import com.learning.app.petar.popularmovies.Fragment.PopMoviesFragment;
import com.learning.app.petar.popularmovies.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements PopMoviesAdapter.IPopMoviesClick{
    public static final String KEY_MOVIES_PARCELABLE ="movies_parcelable" ;
    private static final String TAG_POP_MOVIES_FRAGMENT = "pop_movies_fragment_tag";
    private static final String TAG_DETAIL_MOVIE_FRAGMENT = "detail_movie_fragment_tag";

    public static final String KEY_MOVIE_PARCELABLE = "movie_parcelable";
    private static final String KEY_CURRENT_URI = "current_uri";
    private static final String TAG = MainActivity.class.getSimpleName();
    private Movie[] mMovies;
    public String currentUri;
    private String[] mMovieSort;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle drawerToggle;

    public  boolean mBound = false;
    public APIRequestService mAPIRequestService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(MainActivity.class.getSimpleName(),"onServiceConnected");
            mBound = true;
            APIRequestService.LocalBinder localBinder = (APIRequestService.LocalBinder) iBinder;
            mAPIRequestService = localBinder.getService();
            mAPIRequestService.main = MainActivity.this;
            mAPIRequestService.run(currentUri);
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };
    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setUpNavDrawer();
        currentUri = TheMovieDataBaseAPI.POPULAR_MOVIE_URI;
        if(savedInstanceState!=null) {
            if (savedInstanceState.containsKey(KEY_MOVIES_PARCELABLE)) {
                Parcelable[] parcelables = savedInstanceState.getParcelableArray(KEY_MOVIES_PARCELABLE);
                mMovies = Arrays.copyOf(parcelables, parcelables.length, Movie[].class);
            }
                currentUri = savedInstanceState.getString(KEY_CURRENT_URI);
        }
        bindToApiJSONDownloadService();
    }

    private void setUpNavDrawer() {
        mMovieSort = getResources().getStringArray(R.array.movie_sort);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.drawerList);
        // Set the list's click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_activated_1, mMovieSort));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.open_drawer, R.string.close_drawer) {
            //Called when a drawer has settled in a completely closed state
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                //Code to run when the drawer is closed
            }

            //Called when a drawer has settled in a completely open state.
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

        };
        mDrawerLayout.setDrawerListener(drawerToggle);
    }
    public void setActionBarTitle(String baseUrl) {
        switch (baseUrl) {
            case TheMovieDataBaseAPI.POPULAR_MOVIE_URI: getSupportActionBar().setTitle("Popular movies");break;
            case TheMovieDataBaseAPI.TOP_RATED_MOVIES_URI : getSupportActionBar().setTitle("Top rated movies");break;
            case TheMovieDataBaseAPI.POPULAR_TV_SHOWS_URI : getSupportActionBar().setTitle("Popular TV Show\'s");break;
        }
    }
    public void parseJSON(String baseUrl) throws JSONException {
        switch (baseUrl){
            case TheMovieDataBaseAPI.POPULAR_MOVIE_URI:
            case TheMovieDataBaseAPI.TOP_RATED_MOVIES_URI: mMovies = parseMovies();break;
            case TheMovieDataBaseAPI.POPULAR_TV_SHOWS_URI: mMovies = parseTvShows();break;
        }
    }
    private Movie[] parseTvShows() throws JSONException {
        JSONObject json = new JSONObject(TheMovieDataBaseAPI.JSONResponse);
        JSONArray results = json.getJSONArray("results");
        Movie[] movies = new Movie[results.length()];
        for(int i = 0;i<movies.length;i++){
            movies[i] = new Movie();
            movies[i].setImage(results.getJSONObject(i).getString("poster_path"));
            movies[i].setOverview(results.getJSONObject(i).getString("overview"));
            movies[i].setReleaseDate(results.getJSONObject(i).getString("first_air_date"));
            movies[i].setTitle(results.getJSONObject(i).getString("name"));
            movies[i].setBackdrop(results.getJSONObject(i).getString("backdrop_path"));
            movies[i].setAverageVote(results.getJSONObject(i).getDouble("vote_average"));
            movies[i].setId(results.getJSONObject(i).getLong("id"));

        }
        return movies;
    }
    private Movie[] parseMovies() throws JSONException{
        JSONObject json = new JSONObject(TheMovieDataBaseAPI.JSONResponse);
        JSONArray results = json.getJSONArray("results");
       Movie[] movies = new Movie[results.length()];
        for(int i = 0;i<movies.length;i++){
            movies[i] = new Movie();
            movies[i].setImage(results.getJSONObject(i).getString("poster_path"));
            movies[i].setOverview(results.getJSONObject(i).getString("overview"));
            movies[i].setReleaseDate(results.getJSONObject(i).getString("release_date"));
            movies[i].setTitle(results.getJSONObject(i).getString("title"));
            movies[i].setBackdrop(results.getJSONObject(i).getString("backdrop_path"));
            movies[i].setAverageVote(results.getJSONObject(i).getDouble("vote_average"));
            movies[i].setId(results.getJSONObject(i).getLong("id"));

        }
        return movies;
    }

    public void getFragmentToScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        PopMoviesFragment popMoviesFragment = (PopMoviesFragment) fragmentManager.findFragmentByTag(TAG_POP_MOVIES_FRAGMENT);
        if(popMoviesFragment!=null)
            fragmentManager.beginTransaction().remove(popMoviesFragment);

            popMoviesFragment = new PopMoviesFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArray(KEY_MOVIES_PARCELABLE,mMovies);
            popMoviesFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().
                    add(R.id.placeHolder, popMoviesFragment, TAG_POP_MOVIES_FRAGMENT);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();

    }


    //Movie selected listener
    @Override
    public void onMovieClicked(int position) {
        if(mMovies[position]==null){
            Toast.makeText(MainActivity.this, "Došlo je do greške!", Toast.LENGTH_SHORT).show();
            return;
        }
        MovieDetailsFragment movieDetailFragment = new MovieDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_MOVIE_PARCELABLE,mMovies[position]);
        movieDetailFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.placeHolder,movieDetailFragment,TAG_DETAIL_MOVIE_FRAGMENT);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mMovies!=null)
        outState.putParcelableArray(KEY_MOVIES_PARCELABLE,mMovies);
        outState.putString(KEY_CURRENT_URI,currentUri);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return (drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                switch (position) {
                    case 0:
                        currentUri = TheMovieDataBaseAPI.POPULAR_MOVIE_URI;
                        break;
                    case 1:
                        currentUri = TheMovieDataBaseAPI.TOP_RATED_MOVIES_URI;
                        break;
                    case 2:
                         currentUri = TheMovieDataBaseAPI.POPULAR_TV_SHOWS_URI;

                }
            if(mBound)
            mAPIRequestService.run(currentUri);
            mDrawerLayout.closeDrawers();
        }
    };



   private void bindToApiJSONDownloadService(){
       Intent intent = new Intent(this,APIRequestService.class);
       bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
       Log.d(TAG,"CretingServise OnStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"stopingServise");
        if(mBound)
            unbindService(mServiceConnection);
    }
}