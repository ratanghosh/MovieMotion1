package com.example.android.moviemotion;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import android.view.MenuItem;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener {


    //Please place here your own API KEY
    public final static String API_KEY = "9bfd4d51f3b06a0fe9f09bba827e2242"; // API key

    public final static String URL_POPULAR_MOVIE = "https://api.themoviedb.org/3/movie/popular?api_key=";
    public final static String URL_TOP_RATED_MOVIE = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
    private static final String MOVIE_TITLE = "movie_title";
    private static final String MOVIE_PLOT = "movie_plot";
    private static final String MOVIE_RATINGS= "movie_ratings";
    private static final String MOVIE_RELEASE_DATE = "movie_release_date";
    private static final String MOVIE_ID= "movie_id";
    private static final String POSTER_PATH= "poster_path";

    // String for log message
    public static final String LOG_TAG = MainActivity.class.getName();


// Key received from jSON
    private static final String JSON_RESULTS= "results";
    private static final String JSON_TITLE= "title";
    private static final String JSON_OVERVIEW= "overview";
    private static final String JSON_ID= "id";
    private static final String JSON_RELEASE_DATE= "release_date";
    private static final String JSON_VOTE_AVERAGE= "vote_average";
    private static final String JSON_POSTER_PATH= "poster_path";

// Final URL on create
    public static String MOVIES_URL = URL_POPULAR_MOVIE + API_KEY;


    // TextView that is displayed when the list is empty
    private TextView mEmptyStateTextView;


    RecyclerView recyclerView;
    List<Movie> movies;
    //private static String JSON_URL = "http://starlord.hackerearth.com/studio";
    MovieAdapter movieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.moviesList);
        movies = new ArrayList<>();

        extractMovieData(MOVIES_URL);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.most_popular) {
            movies.clear();

            MOVIES_URL = URL_POPULAR_MOVIE + API_KEY;
            extractMovieData(MOVIES_URL);
            return true;
        }

        if (item.getItemId() == R.id.top_rated) {
            movies.clear();
            MOVIES_URL = URL_TOP_RATED_MOVIE + API_KEY;
            extractMovieData(MOVIES_URL);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void extractMovieData(String songsUrl) {
        movies.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, songsUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray movieArray = response.getJSONArray(JSON_RESULTS);


                            for (int i = 0; i < movieArray.length(); i++) {

                                // data obtain for current movie
                                JSONObject currentMovie = movieArray.getJSONObject(i);

                                // declare a new movie
                                Movie movie = new Movie();

                                // set data for individual item in the movie object
                                movie.setTitle(currentMovie.getString(JSON_TITLE));
                                movie.setOverview(currentMovie.getString(JSON_OVERVIEW));
                                movie.setMovieID(currentMovie.getString(JSON_ID));
                                movie.setReleaseDate(currentMovie.getString(JSON_RELEASE_DATE));
                                movie.setUserRating(currentMovie.getString(JSON_VOTE_AVERAGE));
                                movie.setPosterPath(currentMovie.getString(JSON_POSTER_PATH));

                                // add new movie data
                                movies.add(movie);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4, RecyclerView.VERTICAL, false));
                        movieAdapter = new MovieAdapter(MainActivity.this, movies);
                        recyclerView.setAdapter(movieAdapter);
                        movieAdapter.setOnItemClickListener(MainActivity.this);
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


    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(this, DetailActivity.class);
        Movie clickedItem = movies.get(position);

        intent.putExtra(MOVIE_TITLE, clickedItem.getTitle());
        intent.putExtra(MOVIE_PLOT, movies.get(position).getOverview());
        intent.putExtra(MOVIE_RATINGS, movies.get(position).getUserRating());
        intent.putExtra(MOVIE_RELEASE_DATE, movies.get(position).getReleaseDate());
        intent.putExtra(MOVIE_ID, movies.get(position).getMovieID());
        intent.putExtra(POSTER_PATH, movies.get(position).getPosterPath());

        startActivity(intent);

    }
}

