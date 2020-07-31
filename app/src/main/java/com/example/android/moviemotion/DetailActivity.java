package com.example.android.moviemotion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class DetailActivity extends AppCompatActivity {

    //Please place here your own API KEY
    public final static String API_KEY = "9bfd4d51f3b06a0fe9f09bba827e2242"; // API key

    // String for log message
    public static final String LOG_TAG = DetailActivity.class.getName();

    /* Constant values for the names of each respective parameter */
    private static final String MOVIE_TITLE = "movie_title";
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185//";
    private static final String POSTER_PATH = "poster_path";
    private static final String MOVIE_ID = "movie_id";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String TRAILER_URL_EXTENSION = "/videos?api_key=";
    private static final String REVIEWS_URL_EXTENSION = "/reviews?api_key=";
    private static final String JSON_TITLE= "title";
    private static final String JSON_OVERVIEW= "overview";
    private static final String JSON_VOTE_AVERAGE= "vote_average";
    private static final String JSON_RELEASE_DATE= "release_date";


    private static String movieId;
    private static String movieName;


    private RequestQueue mQueue;

    ListView reviewListView;
    final ArrayList<Review> reviews = new ArrayList<Review>();

    private static TextView reviewAuthorTextView;
    private static TextView reviewContentTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        mQueue = Volley.newRequestQueue(this);


        Intent intent = getIntent();


        if (getIntent().hasExtra(MOVIE_ID)) {

            getSupportActionBar().setTitle(getIntent().getStringExtra(MOVIE_TITLE));

            movieId = getIntent().getStringExtra(MOVIE_ID);
            movieName = getIntent().getStringExtra(MOVIE_TITLE);


        }

        // extract data for movie details
        movieDetails(movieId);


        // buttons for reviews
        Button trailerOneButton = findViewById(R.id.trailer_one_button);
        Button trailerTwoButton = findViewById(R.id.trailer_two_button);

        reviewAuthorTextView = findViewById(R.id.review_author_tv);
        reviewContentTextView = findViewById(R.id.review_content_tv);


        trailerOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().hasExtra(MOVIE_ID)) {
                    String currentMovieID = getIntent().getStringExtra(MOVIE_ID);
                    jsonParseTrailer(currentMovieID, 0);

                }

            }

        });

        trailerTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().hasExtra(MOVIE_ID)) {
                    String currentMovieID = getIntent().getStringExtra(MOVIE_ID);
                    jsonParseTrailer(currentMovieID, 1);

                }
            }

        });

        if (getIntent().hasExtra(MOVIE_ID)) {

            jsonParseReviews(getIntent().getStringExtra(MOVIE_ID));


        }


        if (intent == null) {

            Log.d(LOG_TAG, String.valueOf(R.string.data_pass_error_in_details_activity));
        }


        // dummy for show and hide favorite
        makeFavorite();


    }

    // json parse for trailers

    public void jsonParseTrailer(String currentMovieID, final int index) {

        String trailerFinalUrl = BASE_URL + currentMovieID + TRAILER_URL_EXTENSION + API_KEY;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, trailerFinalUrl, null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                            JSONArray jsonArray = response.getJSONArray("results");

                            JSONObject results = jsonArray.getJSONObject(index);
                            String trailer_one_key = results.getString("key");
                            String trailer_one_url = "https://www.youtube.com/watch?v=" + trailer_one_key;

                            try {
                                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer_one_url));
                                startActivity(myIntent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(getApplicationContext(), "No application can handle this request.", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });
        mQueue.add(request);

    }


    // JSON parse for reviews


    public void jsonParseReviews(String movieID) {

        // private void jsonParseReviews(String movieID){
        String reviewsFinalUrl = BASE_URL + movieID + REVIEWS_URL_EXTENSION + API_KEY;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, reviewsFinalUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                // data obtain for current review
                                JSONObject currentReview = jsonArray.getJSONObject(i);

                                // declare a new review
                                //Review review = new Review();

                                // set data for individual item in the movie object
                                // review.setAuthor(currentReview.getString("author"));
                                // review.setContent(currentReview.getString("content"));

                                reviewAuthorTextView.append("Review by " + currentReview.getString("author") + "\n\n" +
                                        "Review:  " + currentReview.getString("content") + "\n\n\n");
                                //reviewContentTextView.append(currentReview.getString("content")+"\n\n");


                                //reviews.add(review);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //ReviewAdapter mReviewAdapter = new ReviewAdapter(DetailActivity.this, reviews);
                        //reviewListView = (ListView) findViewById(R.id.reviews_list);
                        // reviewListView.setAdapter(mReviewAdapter);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });

        mQueue.add(request);

    }

    public void makeFavorite() {
        // Image for favorite
        final ImageView favoriteBorderIV = findViewById(R.id.favorite_border_iv);
        final ImageView favoriteIV = findViewById(R.id.favorite_iv);

        favoriteBorderIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteBorderIV.setVisibility(View.GONE);
                favoriteIV.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), movieName + movieId + " now on your favorite list", Toast.LENGTH_SHORT).show();

            }

        });

        favoriteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteBorderIV.setVisibility(View.VISIBLE);
                favoriteIV.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), movieName + "Removed from favorite list", Toast.LENGTH_SHORT).show();

            }

        });


    }

    public void movieDetails(String movieID) {

        // url for movie details
        String detailsFinalUrl =  BASE_URL + movieID + "?api_key=" + API_KEY;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, detailsFinalUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            // Set poster imageView
                            ImageView posterIv = findViewById(R.id.poster_iv);

                            String posterPath = (POSTER_BASE_URL + response.getString(POSTER_PATH));
                            Picasso.get()
                                    .load(posterPath)
                                    .into(posterIv);

                            // Set Title
                            TextView titleTv = findViewById(R.id.title_tv);
                            titleTv.setText(response.getString(JSON_TITLE));

                            //set plot
                            TextView overviewTv = findViewById(R.id.overview_tv);
                            overviewTv.setText(response.getString(JSON_OVERVIEW));

                            //set user Ratings
                            TextView ratingsTv = findViewById(R.id.ratings_tv);
                            ratingsTv.setText(response.getString(JSON_VOTE_AVERAGE));

                            //set release date
                            TextView releaseDateTv = findViewById(R.id.release_date_tv);
                            releaseDateTv.setText(response.getString(JSON_RELEASE_DATE));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });

        mQueue.add(request);

    }


}