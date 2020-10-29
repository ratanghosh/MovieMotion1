package com.example.android.moviemotion.Activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.moviemotion.Adapters.MovieAdapter;
import com.example.android.moviemotion.Constant;
import com.example.android.moviemotion.Movie;
import com.example.android.moviemotion.R;
import com.example.android.moviemotion.ViewModel.MainViewModel;
import com.example.android.moviemotion.databinding.ActivityMainBinding;
import com.example.android.moviemotion.favoriteMovie.FavoriteMovieDatabase;
import com.example.android.moviemotion.favoriteMovie.FavoriteMovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    // String for log message
    public static final String LOG_TAG = MainActivity.class.getName();

    //  URL to fetch the data from server
    public static String MOVIES_URL;
    ActivityMainBinding activityMainBinding;
    //RecyclerView recyclerView;
    List<Movie> movies;

    MovieAdapter movieAdapter;

    private Menu mMainMenu;

    private FavoriteMovieDatabase mDb;

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 100;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        //recyclerView = findViewById(R.id.moviesList);
        movies = new ArrayList<>();

        setupSharedPreferences();


    }

    private void setupSharedPreferences() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadTypeFromPreferences(sharedPreferences);
        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void loadTypeFromPreferences(SharedPreferences sharedPreferences) {

        setGridUponType(sharedPreferences.getString(getString(R.string.movieType), getString(R.string.popular)));

    }

    public void setGridUponType(String movieType) {


        if (movieType.equals(getString(R.string.favorite))) {
            movies.clear();
            extractFavoriteMovieFromRoomDatabase();


        } else if (movieType.equals(getString(R.string.top_rated))) {

            movies.clear();
            MOVIES_URL = Constant.URL_TOP_RATED_MOVIE + Constant.API_KEY;
            extractMovieDataFromServer(MOVIES_URL);


        } else {

            // default popular type of movies will be selected

            movies.clear();
            MOVIES_URL = Constant.URL_POPULAR_MOVIE + Constant.API_KEY;
            extractMovieDataFromServer(MOVIES_URL);


        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister VisualizerActivity as an OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMainMenu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getString(R.string.movieType))) {
            loadTypeFromPreferences(sharedPreferences);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {

            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void extractMovieDataFromServer(String moviesUrl) {
        movies.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, moviesUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray movieArray = response.getJSONArray(Constant.JSON_RESULTS);


                            for (int i = 0; i < movieArray.length(); i++) {

                                // data obtain for current movie
                                JSONObject currentMovie = movieArray.getJSONObject(i);

                                // declare a new movie
                                Movie movie = new Movie();

                                // set data for individual item in the movie object
                                movie.setTitle(currentMovie.getString(Constant.JSON_TITLE));
                                movie.setMovieID(currentMovie.getString(Constant.JSON_ID));
                                movie.setPosterPath(currentMovie.getString(Constant.JSON_POSTER_PATH));

                                // add new movie data
                                movies.add(movie);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        setMovieToUi();
                    } // end of public void onResponse(JSONObject response)
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG_TAG, R.string.on_response_error + error.getMessage());

                if (error instanceof TimeoutError
                        || error instanceof AuthFailureError || error instanceof ServerError
                        || error instanceof NetworkError || error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                }


            }
        });

        queue.add(jsonObjectRequest);

    }

    public void setMovieToUi() {
        activityMainBinding.moviesListRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, calculateNoOfColumns(this),
                RecyclerView.VERTICAL, false));
        movieAdapter = new MovieAdapter(MainActivity.this, movies);
        activityMainBinding.moviesListRecyclerView.setAdapter(movieAdapter);
        movieAdapter.setOnItemClickListener(MainActivity.this);

    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(this, DetailActivity.class);
        Movie clickedItem = movies.get(position);

        intent.putExtra(Constant.MOVIE_TITLE, clickedItem.getTitle());
        intent.putExtra(Constant.MOVIE_ID, clickedItem.getMovieID());
        intent.putExtra(Constant.POSTER_PATH, clickedItem.getPosterPath());


        startActivity(intent);

    }


    public void extractFavoriteMovieFromRoomDatabase() {

        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getFavoriteMovieModels().observe(this, new Observer<List<FavoriteMovieModel>>() {
            @Override
            public void onChanged(List<FavoriteMovieModel> favoriteMovieModelList) {

                for (int i = 0; i < favoriteMovieModelList.size(); i++) { // get the data from favorite movie and set them on movies.

                    String movieIdString = "" + favoriteMovieModelList.get(i).getMovieId();

                    Movie movie = new Movie();

                    movie.setMovieID(movieIdString);
                    movie.setTitle(favoriteMovieModelList.get(i).getMovieName());
                    movie.setPosterPath(favoriteMovieModelList.get(i).getPosterPath());

                    movies.add(movie);

                    setMovieToUi();

                }

            }
        });

    }


}



