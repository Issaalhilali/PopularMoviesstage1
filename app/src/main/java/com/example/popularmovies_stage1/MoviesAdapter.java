package com.example.popularmovies_stage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.popularmovies_stage1.Constants.Constants;
import com.example.popularmovies_stage1.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private ArrayList<Movie> movieList;
    private Context context;
    private MovieItemClickListener listener;

    public MoviesAdapter(Context context, ArrayList<Movie> movieList, MovieItemClickListener listener){
        this.movieList = movieList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_row_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Picasso.get().load(Constants.POSTER_BASE_URL+movieList.get(position).getPosterPath())
                .into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return movieList!=null?movieList.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_list_item)
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick(R.id.iv_list_item)
        public void onClick(){
            listener.onItemClick(movieList.get(getAdapterPosition()));


        }
        }
    }

