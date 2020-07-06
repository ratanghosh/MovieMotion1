package com.example.android.moviemotion;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {


    private String mUrl;

    public MovieLoader(Context context, String url) {
        super(context);
        mUrl = url;
        forceLoad();
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }



    @Override
    public List<Movie> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<Movie> movies = MovieUtils.fetchMovieData(mUrl);
        return movies;
    }
}
