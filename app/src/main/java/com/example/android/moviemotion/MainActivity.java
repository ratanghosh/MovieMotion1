package com.example.android.moviemotion;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {


    private static final int MOVIE_LOADER_ID = 1;


    //Please place here your own API KEY
    public final static String API_KEY = ""; // API key
    public final static String URL_POPULAR_MOVIE = "https://api.themoviedb.org/3/movie/popular?";
    public final static String URL_TOP_RATED_MOVIE = "https://api.themoviedb.org/3/movie/top_rated?";


    public static String MOVIES_URL = URL_POPULAR_MOVIE;

    public String movieSortType = "Popular";


    // TextView that is displayed when the list is empty
    private TextView mEmptyStateTextView;

    // Adapter for the list of movies
    private MovieAdapter mAdapter;
    private String movieSortUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create a new adapter that takes an empty list of movies as input
        mAdapter = new MovieAdapter(this, new ArrayList<Movie>());

        // Find a reference list in the layout
        GridView movieGridView = (GridView) findViewById(R.id.gridview);
        // so the list can be populated in the user interface
        movieGridView.setAdapter(mAdapter);

        // initialization for empty view
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        movieGridView.setEmptyView(mEmptyStateTextView);

        //display sort by type in the main page
        displayMessage();


        // by clicking start details activity

        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Movie currentMovie = mAdapter.getItem(position);

                launchDetailActivity(position);

            }
        });


        movieLoading();

    }


    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {


        Uri baseUri = Uri.parse(MOVIES_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("api_key", API_KEY);


        return new MovieLoader(this, uriBuilder.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {


        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display " No movie found"
        mEmptyStateTextView.setText(R.string.no_movie);
        // Clear the adapter of previous movie data
        mAdapter.clear();

        // If there is a valid list  then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (movies != null && !movies.isEmpty()) {
            mAdapter.addAll(movies);
        }


    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mAdapter.clear();

    }


    private void launchDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.top_rated:
                movieSortType = "Top Rated";
                displayMessage();
                MOVIES_URL = URL_TOP_RATED_MOVIE;
                getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
                movieLoading();
                return true;

            case R.id.most_popular:
                movieSortType = "Popular";
                displayMessage();
                MOVIES_URL = URL_POPULAR_MOVIE;
                getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void displayMessage() {
        TextView urlDisplay = (TextView) findViewById(R.id.sort_by_display_textView);
        urlDisplay.setText(movieSortType);

    }

    public void movieLoading() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);

        } else {

            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);


        }

        getLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);

    }


}


