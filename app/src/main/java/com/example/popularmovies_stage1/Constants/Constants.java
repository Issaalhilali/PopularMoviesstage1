package com.example.popularmovies_stage1.Constants;

import com.example.popularmovies_stage1.BuildConfig;

public class Constants {
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String API_KEY = "api_key";
    public static final String API_KEY_VALUE = BuildConfig.API_KEY;

    public static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185";
    public static final String BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w342";

    //intent object key
    public static final String MOVIE_DETAILS = "movie_details";
    public static final String MOVIE_LIST = "movie_list";
    public static final String SORT_BY = "sort_by";
}