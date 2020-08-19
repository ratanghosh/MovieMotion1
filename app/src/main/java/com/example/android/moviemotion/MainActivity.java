package com.example.android.moviemotion;


import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
import com.example.android.moviemotion.favoriteMovie.Activity.FavoriteMovieActivity;
import com.example.android.moviemotion.favoriteMovie.FavoriteMovieAdapter;
import com.example.android.moviemotion.favoriteMovie.FavoriteMovieDatabase;
import com.example.android.moviemotion.favoriteMovie.FavoriteMovieExecutor;
import com.example.android.moviemotion.favoriteMovie.FavoriteMovieModel;

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
    private static final String MOVIE_ID = "movie_id";
    private static final String POSTER_PATH = "poster_path";

    // String for log message
    public static final String LOG_TAG = MainActivity.class.getName();


    // Key received from jSON
    private static final String JSON_RESULTS = "results";
    private static final String JSON_TITLE = "title";

    private static final String JSON_ID = "id";

    private static final String JSON_POSTER_PATH = "poster_path";

    // Final URL on create
    public static String MOVIES_URL = URL_POPULAR_MOVIE + API_KEY;


    // TextView that is displayed when the list is empty
    private TextView mEmptyStateTextView;


    RecyclerView recyclerView;
    List<Movie> movies;

    MovieAdapter movieAdapter;

    FavoriteMovieAdapter favoriteMovieAdapter;

    private Menu mMainMenu;

    private FavoriteMovieDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.moviesList);
        movies = new ArrayList<>();

        extractMovieDataFromServer(MOVIES_URL);




    }

    private void enableMenuItem(MenuItem item) {
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)),
                0, spanString.length(), 0);
        item.setTitle(spanString);
    }

    private void disableMenuItem(MenuItem item) {
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),
                0, spanString.length(), 0);
        item.setTitle(spanString);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMainMenu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.most_popular) {
            movies.clear();

            MOVIES_URL = URL_POPULAR_MOVIE + API_KEY;
            extractMovieDataFromServer(MOVIES_URL);

            enableMenuItem(mMainMenu.findItem(R.id.most_popular));
            disableMenuItem(mMainMenu.findItem(R.id.top_rated));
            disableMenuItem(mMainMenu.findItem(R.id.favorite));

            return true;
        }

        if (item.getItemId() == R.id.top_rated) {
            movies.clear();
            MOVIES_URL = URL_TOP_RATED_MOVIE + API_KEY;
            extractMovieDataFromServer(MOVIES_URL);

            disableMenuItem(mMainMenu.findItem(R.id.most_popular));
            enableMenuItem(mMainMenu.findItem(R.id.top_rated));
            disableMenuItem(mMainMenu.findItem(R.id.favorite));

            return true;
        }

        if (item.getItemId() == R.id.favorite) {
            movies.clear();

            //Intent fMovieIntent = new Intent(MainActivity.this, FavoriteMovieActivity.class);
            // startActivity(fMovieIntent);


            extractFavoriteMovieFromRoomDatabase();


            disableMenuItem(mMainMenu.findItem(R.id.most_popular));
            disableMenuItem(mMainMenu.findItem(R.id.top_rated));
            enableMenuItem(mMainMenu.findItem(R.id.favorite));


            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void extractMovieDataFromServer(String songsUrl) {
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
                                movie.setMovieID(currentMovie.getString(JSON_ID));
                                movie.setPosterPath(currentMovie.getString(JSON_POSTER_PATH));

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
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4,
                RecyclerView.VERTICAL, false));
        movieAdapter = new MovieAdapter(MainActivity.this, movies);
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.setOnItemClickListener(MainActivity.this);

    }


    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(this, DetailActivity.class);
        Movie clickedItem = movies.get(position);

        intent.putExtra(MOVIE_TITLE, clickedItem.getTitle());
        intent.putExtra(MOVIE_ID, clickedItem.getMovieID());
        intent.putExtra(POSTER_PATH, clickedItem.getPosterPath());


        startActivity(intent);

    }


    public void extractFavoriteMovieFromRoomDatabase() {


        mDb = FavoriteMovieDatabase.getInstance(getApplicationContext());

        FavoriteMovieExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<FavoriteMovieModel> favoriteMovies = mDb.favoriteMovieDao().loadAllFavoriteMovies();


                for (int i = 0; i < favoriteMovies.size(); i++) {

                    String movieIdString = "" + favoriteMovies.get(i).getMovieId();

                    Movie movie = new Movie();

                    movie.setMovieID(movieIdString);
                    movie.setTitle(favoriteMovies.get(i).getMovieName());
                    movie.setPosterPath(favoriteMovies.get(i).getPosterPath());


                    movies.add(movie);



                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4,
                                RecyclerView.VERTICAL, false));
                        movieAdapter = new MovieAdapter(MainActivity.this, movies);
                        recyclerView.setAdapter(movieAdapter);
                        movieAdapter.setOnItemClickListener(MainActivity.this);


                    }
                });



            }
        });


    }
}



