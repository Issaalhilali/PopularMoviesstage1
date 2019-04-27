package com.example.popularmovies_stage1.Network;

import com.example.popularmovies_stage1.model.Movies;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Services {
    @GET("movie/popular")
    Call<Movies>getMostPopularMovies();

    @GET("movie/top_rated")
    Call<Movies> getTopRatedMovies();
}
