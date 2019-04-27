package com.example.popularmovies_stage1;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies_stage1.Constants.Constants;
import com.example.popularmovies_stage1.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {


    @BindView(R.id.iv_backdrop) ImageView imageView;
    @BindView(R.id.tv_synopsis) TextView tvSynopsis;
    @BindView(R.id.tv_release_date) TextView tvReleaseDate;
    @BindView(R.id.tv_rating) TextView tvRating;
    @BindView(R.id.collapsingToolbarLayout) CollapsingToolbarLayout collapsingToolbarLayout;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent.hasExtra(Constants.MOVIE_DETAILS)){
            movie = intent.getParcelableExtra(Constants.MOVIE_DETAILS);
            addMovieDetails();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addMovieDetails() {
        collapsingToolbarLayout.setTitle(movie.getTitle());
        Picasso.get()
                .load(Constants.BACKDROP_BASE_URL + movie.getBackdropPath())
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);

        tvSynopsis.setText(movie.getOverview());
        tvReleaseDate.setText(movie.getReleaseDate());
        tvRating.setText(movie.getVoteAverage().toString() +"/10");
    }

}
