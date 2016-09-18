package com.learning.app.petar.popularmovies.API;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Petar on 16.9.2016..
 */
public class TheMovieDataBaseAPI {
    public static final String GET_IMAGE_URI  = "http://image.tmdb.org/t/p/w185//";
    public static final String API_KEY ="*************************";
    public static final String POPULAR_TV_SHOWS_URI ="http://api.themoviedb.org/3/tv/popular?api_key=";
    public static final String POPULAR_MOVIE_URI ="http://api.themoviedb.org/3/movie/popular?api_key=";
    public static final String TOP_RATED_MOVIES_URI ="http://api.themoviedb.org/3/movie/top_rated?api_key=";

    public static String JSONResponse;

    public static String getYear(String fullDate){
        SimpleDateFormat tf = new SimpleDateFormat("yyyy-MM-dd");

        Date parseTime;
        try {
            parseTime = tf.parse(fullDate);

        } catch (ParseException e) {
           return fullDate;
        }
        tf = new SimpleDateFormat("yyyy");
        return tf.format(parseTime);
    }
}
