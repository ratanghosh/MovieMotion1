package com.example.android.moviemotion.favoriteMovie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.moviemotion.Movie;
import com.example.android.moviemotion.MovieAdapter;
import com.example.android.moviemotion.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.MyViewHolder> {

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185//";

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;



    private Context mContext;
    private List<FavoriteMovieModel> mFavoriteMovieList;

    public FavoriteMovieAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;

    }


    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.movie_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {




        String movieTitleString = mFavoriteMovieList.get(position).getMovieName();
        holder.movieTitle.setText(movieTitleString);

        String posterUrl = (POSTER_BASE_URL + mFavoriteMovieList.get(position).getPosterPath());
        Picasso.get().load(posterUrl).into(holder.movieCoverImage);

    }

    @Override
    public int getItemCount() {
        if (mFavoriteMovieList == null) {
            return 0;
        }
        return mFavoriteMovieList.size();

    }

    public void setTasks(List<FavoriteMovieModel> fMoviesList) {
        mFavoriteMovieList = fMoviesList;
        notifyDataSetChanged();
    }

    public List<FavoriteMovieModel> getTasks() {

        return mFavoriteMovieList;
    }

    public interface ItemClickListener {
        void onItemClickListener(FavoriteMovieModel favoriteMovieModel);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView movieTitle;
        ImageView movieCoverImage;



        public MyViewHolder(View itemView) {
            super(itemView);


            movieTitle = itemView.findViewById(R.id.movie_title);
            movieCoverImage = itemView.findViewById(R.id.coverImage);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {


            FavoriteMovieModel favoriteMovieModel = new FavoriteMovieModel(
                    mFavoriteMovieList.get(getAdapterPosition()).getId(),
                    mFavoriteMovieList.get(getAdapterPosition()).getMovieId(),
                    mFavoriteMovieList.get(getAdapterPosition()).getMovieName(),
                    mFavoriteMovieList.get(getAdapterPosition()).getPosterPath());



            mItemClickListener.onItemClickListener(favoriteMovieModel);

        }
    }


}