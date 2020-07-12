package com.example.android.moviemotion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DetailActivity extends AppCompatActivity {

    // String for log message
    public static final String LOG_TAG = DetailActivity.class.getName();

    /* Constant values for the names of each respective parameter */
    private static final String MOVIE_TITLE = "movie_title";
    private static final String MOVIE_PLOT = "movie_plot";
    private static final String MOVIE_RATINGS= "movie_ratings";
    private static final String MOVIE_RELEASE_DATE = "movie_release_date";
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185//";
    private static final String POSTER_PATH= "poster_path";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView posterIv = findViewById(R.id.poster_iv);
        TextView titleTv = findViewById(R.id.title_tv);
        TextView overviewTv = findViewById(R.id.overview_tv);
        TextView ratingsTv = findViewById(R.id.ratings_tv);
        TextView releaseDateTv = findViewById(R.id.release_date_tv);


        Intent intent = getIntent();


        if (getIntent().hasExtra(MOVIE_TITLE)) {

            getSupportActionBar().setTitle(getIntent().getStringExtra(MOVIE_TITLE));

            titleTv.setText(getIntent().getStringExtra(MOVIE_TITLE));
            overviewTv.setText(getIntent().getStringExtra(MOVIE_PLOT));
            ratingsTv.setText(getIntent().getStringExtra(MOVIE_RATINGS));
            releaseDateTv.setText(getIntent().getStringExtra(MOVIE_RELEASE_DATE));


            String posterPath = (POSTER_BASE_URL + getIntent().getStringExtra(POSTER_PATH));

            Picasso.get()
                    .load(posterPath)
                    .into(posterIv);


        }


        if (intent == null) {

            Log.d(LOG_TAG, String.valueOf(R.string.data_pass_error_in_details_activity));
        }


    }


}