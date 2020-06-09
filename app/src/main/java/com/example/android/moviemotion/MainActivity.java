package com.example.android.moviemotion;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {



    GridView gridView;

    public int page;


    private static final int MOVIE_LOADER_ID = 1;


    // TextView that is displayed when the list is empty
    private TextView mEmptyStateTextView;

    // Adapter for the list of movies
    private MovieAdapter mAdapter;



          //  "https://api.themoviedb.org/3/movie/popular?page=2";
           // = "http://api.themoviedb.org/3/movie/popular?";
    //api_key=9bfd4d51f3b06a0fe9f09bba827e2242  savedInstanceState

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



        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               // Movie currentMovie = mAdapter.getItem(position);

                launchDetailActivity(position);



            }
        });



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
    }








    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences genre = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        String MOVIE_REQUEST_URL = genre.getString("movie_genre","https://api.themoviedb.org/3/movie/popular?");

        Uri baseUri = Uri.parse(MOVIE_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();




        uriBuilder.appendQueryParameter("api_key","9bfd4d51f3b06a0fe9f09bba827e2242");
        //uriBuilder.appendQueryParameter("page",pageNumber );
        //uriBuilder.appendQueryParameter("query", "popular");
        //uriBuilder.appendQueryParameter("show-tags", "contributor");



        return new MovieLoader(this, uriBuilder.toString());
    }



    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display " No earthquakes found"
        mEmptyStateTextView.setText(R.string.no_movie);
        // Clear the adapter of previous earthquake data
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        SharedPreferences genre = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        String MOVIE_REQUEST_URL = genre.getString("movie_genre","https://api.themoviedb.org/3/movie/popular?");

        MenuItem item_popular = menu.findItem(R.id.popular_movie);
        MenuItem item_top_rated = menu.findItem(R.id.top_rated_movie);

        if (MOVIE_REQUEST_URL == "https://api.themoviedb.org/3/movie/popular?")
        {
            item_popular.setChecked(true);
        }

        else if (MOVIE_REQUEST_URL == "https://api.themoviedb.org/3/movie/top_rated?") {
            item_top_rated.setChecked(true);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
       if (id == R.id.popular_movie) {

           SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
           SharedPreferences.Editor myEdit = sharedPreferences.edit();
           myEdit.putString(("movie_genre"),"https://api.themoviedb.org/3/movie/popular?");
           myEdit.commit();
           item.setChecked(true);

           //getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);



           finish();
           startActivity(getIntent());

           return true;
        }

        if (id == R.id.top_rated_movie) {

            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString(("movie_genre"),"https://api.themoviedb.org/3/movie/top_rated?");
            myEdit.commit();
            item.setChecked(true);

            //getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null,  this);


            finish();
            startActivity(getIntent());


            return true;
        }


        return super.onOptionsItemSelected(item);
    }



    private void launchDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        intent.putExtra("full_name", "Ratan");
        startActivity(intent);
    }



}

