package com.example.popularmovies_stage1;


import android.content.Intent;
import android.content.res.Configuration;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.popularmovies_stage1.Constants.Constants;
import com.example.popularmovies_stage1.Constants.NetworkUtils;
import com.example.popularmovies_stage1.Network.Client;
import com.example.popularmovies_stage1.Network.Services;
import com.example.popularmovies_stage1.model.Movie;
import com.example.popularmovies_stage1.model.Movies;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.popularmovies_stage1.Constants.Constants.MOVIE_LIST;
import static com.example.popularmovies_stage1.Constants.Constants.SORT_BY;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Toolbar toolbar;
    ConstraintLayout constraintLayout;
    ProgressBar progressBar;

    private MoviesAdapter moviesAdapter;
    private ArrayList<Movie> movieList;
    private Call<Movies> moviesCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        toolbar = (Toolbar) findViewById(R.id.tooblar);
        constraintLayout = (ConstraintLayout) findViewById(R.id.no_network_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        setSupportActionBar(toolbar);

        recyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE?5:2));
        recyclerView.setHasFixedSize(true);

        if(savedInstanceState == null || !savedInstanceState.containsKey(MOVIE_LIST)){
            movieList = new ArrayList<>();
            showTopRatedMovies();
        }else if (savedInstanceState.containsKey(MOVIE_LIST)){
            if(!NetworkUtils.isOnline(this)){
                hideList();
            }else {
                movieList = savedInstanceState.getParcelableArrayList(MOVIE_LIST);
            }
            if(savedInstanceState.containsKey(SORT_BY)){
                getSupportActionBar().setTitle(savedInstanceState.getString(SORT_BY));
            }
        }
        if(movieList == null)
            movieList = new ArrayList<>();

        moviesAdapter = new MoviesAdapter(this, movieList, new MovieItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(Constants.MOVIE_DETAILS, movie);
                startActivity(intent);
            }
        });
        moviesAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(moviesAdapter);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_LIST, movieList);
        outState.putString(SORT_BY, getSupportActionBar().getTitle().toString());
        super.onSaveInstanceState(outState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.top_rated) {
            showTopRatedMovies();
            return true;
        }else if ( id == R.id.most_popular){
            showMostPopularMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.bn_retry)
    public void retry(){
        if (getSupportActionBar().getTitle().toString().equals(getString(R.string.top_rated)))
            showTopRatedMovies();
        else
            showMostPopularMovies();
    }

    private void showList(){
        constraintLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void hideList(){
        constraintLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showTopRatedMovies(){
        getSupportActionBar().setTitle(R.string.top_rated);
        if(!NetworkUtils.isOnline(this)){
            hideList();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        Services movieService = Client.getServices();
        moviesCall= movieService.getTopRatedMovies();
        moviesCall.enqueue(callback);
    }

    private void showMostPopularMovies(){
        getSupportActionBar().setTitle(R.string.most_popular);
        if(!NetworkUtils.isOnline(this)){
            hideList();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        Services movieService = Client.getServices();
        moviesCall = movieService.getMostPopularMovies();
        moviesCall.enqueue(callback);
    }

    private Callback callback = new Callback<Movies>() {
        @Override
        public void onResponse(retrofit2.Call<Movies> call, Response<Movies> response) {
            Movies movies = response.body();

            if(movies != null){
                movieList.clear();
                movieList.addAll(movies.getMovies());
                moviesAdapter.notifyDataSetChanged();
            }
            showList();
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(Call<Movies> call, Throwable t) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), R.string.list_loading_error, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (moviesCall !=null){
            if(moviesCall.isExecuted()){
                moviesCall.cancel();
            }
        }
    }
}