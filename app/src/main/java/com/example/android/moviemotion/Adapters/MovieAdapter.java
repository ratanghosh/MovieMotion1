package com.example.android.moviemotion.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.moviemotion.Constant;
import com.example.android.moviemotion.Movie;
import com.example.android.moviemotion.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {




    private LayoutInflater inflater;
    private List<Movie> movies;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public MovieAdapter(Context context, List<Movie> movies) {
        this.inflater = LayoutInflater.from(context);
        this.movies = movies;

    }

    public void setTasks(List<Movie> moviesList) {
        movies = moviesList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // bind the data to create thumbnails in the main page
        String posterUrl = (Constant.POSTER_BASE_URL + movies.get(position).getPosterPath());
        Picasso.get().load(posterUrl).into(holder.movieCoverImage);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView movieCoverImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            movieCoverImage = itemView.findViewById(R.id.coverImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }

                }
            });


        }


    }


}















