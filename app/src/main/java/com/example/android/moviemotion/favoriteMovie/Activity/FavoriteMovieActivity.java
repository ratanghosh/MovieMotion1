package com.example.android.moviemotion.favoriteMovie.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.android.moviemotion.DetailActivity;
import com.example.android.moviemotion.MainActivity;
import com.example.android.moviemotion.Movie;
import com.example.android.moviemotion.R;
import com.example.android.moviemotion.favoriteMovie.FavoriteMovieAdapter;
import com.example.android.moviemotion.favoriteMovie.FavoriteMovieDatabase;
import com.example.android.moviemotion.favoriteMovie.FavoriteMovieExecutor;
import com.example.android.moviemotion.favoriteMovie.FavoriteMovieModel;

import java.util.List;

public class FavoriteMovieActivity extends AppCompatActivity implements FavoriteMovieAdapter.ItemClickListener {

    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();
    // Member variables for the adapter and RecyclerView
    private RecyclerView mRecyclerView;
    private FavoriteMovieAdapter mAdapter;

    private FavoriteMovieDatabase mDb;

    private static final String MOVIE_TITLE = "movie_title";
    private static final String MOVIE_ID= "movie_id";
    private static final String POSTER_PATH= "poster_path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movie);

        // Set the RecyclerView to its corresponding view
        mRecyclerView = findViewById(R.id.favoriteMoviesList);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4,RecyclerView.VERTICAL,false));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new FavoriteMovieAdapter(this,this);
        mRecyclerView.setAdapter(mAdapter);

        mDb = FavoriteMovieDatabase.getInstance(getApplicationContext());



        retrieveTask();




    }


    @Override
    protected void onResume() {
        super.onResume();
        retrieveTask();
    }

    private void retrieveTask() {
        FavoriteMovieExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<FavoriteMovieModel> fMovies= mDb.favoriteMovieDao().loadAllFavoriteMovies();
                // We will be able to simplify this once we learn more
                // about Android Architecture Components
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setTasks(fMovies);
                    }
                });
            }
        });
    }

    @Override
    public void onItemClickListener( final FavoriteMovieModel favoriteMovieModel) {

        Intent intent = new Intent(this, DetailActivity.class);

        String movieIdString = "" + favoriteMovieModel.getMovieId();


        intent.putExtra(MOVIE_TITLE, favoriteMovieModel.getMovieName());
        intent.putExtra(MOVIE_ID, movieIdString);
        intent.putExtra(POSTER_PATH, favoriteMovieModel.getPosterPath());


        startActivity(intent);

    }
}
