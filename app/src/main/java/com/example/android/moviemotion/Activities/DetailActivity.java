package com.example.android.moviemotion.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.moviemotion.Adapters.ReviewAdapter;
import com.example.android.moviemotion.Constant;
import com.example.android.moviemotion.R;
import com.example.android.moviemotion.Review;
import com.example.android.moviemotion.databinding.ActivityDetailBinding;
import com.example.android.moviemotion.favoriteMovie.FavoriteMovieDatabase;
import com.example.android.moviemotion.favoriteMovie.FavoriteMovieExecutor;
import com.example.android.moviemotion.favoriteMovie.FavoriteMovieModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DetailActivity extends AppCompatActivity {


    // String for log message
    public static final String LOG_TAG = DetailActivity.class.getName();

    private static String movieId;
    private static String movieName;
    private static String posterPath;


    private RequestQueue mQueue;

    //RecyclerView recyclerView;
    List<Review> reviews;
    ActivityDetailBinding activityDetailBinding;
    //private  TextView reviewTitleTextView;
    private FavoriteMovieDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        reviews = new ArrayList<>();

        mDb = FavoriteMovieDatabase.getInstance(getApplicationContext());

        mQueue = Volley.newRequestQueue(this);


        Intent intent = getIntent();


        if (getIntent().hasExtra(Constant.MOVIE_ID)) {

            getSupportActionBar().setTitle(getIntent().getStringExtra(Constant.MOVIE_TITLE));

            movieId = getIntent().getStringExtra(Constant.MOVIE_ID);
            movieName = getIntent().getStringExtra(Constant.MOVIE_TITLE);
            posterPath = getIntent().getStringExtra(Constant.POSTER_PATH);


        }


        // Set poster imageView

        Picasso.get()
                .load(Constant.POSTER_BASE_URL + posterPath)
                .into(activityDetailBinding.posterIv);

        // Set Title
        activityDetailBinding.titleTv.setText(movieName);

        // extract data for movie details
        movieDetails(movieId);

        setTrailer();


        if (getIntent().hasExtra(Constant.MOVIE_ID)) {

            jsonParseReviews(getIntent().getStringExtra(Constant.MOVIE_ID));

        }


        if (intent == null) {

            Log.d(LOG_TAG, String.valueOf(R.string.data_pass_error_in_details_activity));
        }


        makeFavorite(movieId, movieName, posterPath);


    }


    // setting trailer

    public void setTrailer() {


        activityDetailBinding.trailerOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().hasExtra(Constant.MOVIE_ID)) {
                    String currentMovieID = getIntent().getStringExtra(Constant.MOVIE_ID);
                    jsonParseTrailer(currentMovieID, 0);


                }

            }

        });

        activityDetailBinding.trailerTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().hasExtra(Constant.MOVIE_ID)) {
                    String currentMovieID = getIntent().getStringExtra(Constant.MOVIE_ID);
                    jsonParseTrailer(currentMovieID, 1);

                }
            }

        });

    }




    // json parse for trailers

    public void jsonParseTrailer(String currentMovieID, final int index) {

        String trailerFinalUrl = Constant.BASE_URL + currentMovieID + Constant.TRAILER_URL_EXTENSION + Constant.API_KEY;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, trailerFinalUrl, null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                            //JSONArray jsonArray = response.getJSONArray("results");

                            JSONArray jsonArray = response.getJSONArray(getString(R.string.results));

                            JSONObject results = jsonArray.getJSONObject(index);
                            String trailer_key = results.getString(getString(R.string.key));
                            String trailer_url = Constant.YOUTUBE_BASE_URL + trailer_key;


                            try {
                                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer_url));
                                startActivity(myIntent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(getApplicationContext(), R.string.no_application_can_handle_this_request, Toast.LENGTH_LONG).show();
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




    public void jsonParseReviews(String movieID) {
        reviews.clear();

        // private void jsonParseReviews(String movieID){
        String reviewsFinalUrl = Constant.BASE_URL + movieID + Constant.REVIEWS_URL_EXTENSION + Constant.API_KEY;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, reviewsFinalUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray(getString(R.string.results));

                            for (int i = 0; i < jsonArray.length(); i++) {
                                // data obtain for current review
                                JSONObject currentReview = jsonArray.getJSONObject(i);

                                // declare a new review
                                Review review = new Review();

                                // set data for individual item in the movie object
                                review.setAuthor(currentReview.getString(getString(R.string.review_author)));
                                review.setContent(currentReview.getString(getString(R.string.review_content)));

                                reviews.add(review);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //recyclerView.setLayoutManager(new GridLayoutManager(DetailActivity.this, 1,
                        //RecyclerView.VERTICAL, false));

                        activityDetailBinding.reviewRecyclerView.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
                        activityDetailBinding.reviewRecyclerView.setHasFixedSize(true);

                        ReviewAdapter mReviewAdapter = new ReviewAdapter(DetailActivity.this, reviews);
                        activityDetailBinding.reviewRecyclerView.setAdapter(mReviewAdapter);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });

        mQueue.add(request);

    }


    public void makeFavorite(final String mMovieId, final String mMovieName, final String mPosterPath) {
        // Image for favorite


        activityDetailBinding.favoriteBorderIv.setVisibility(View.VISIBLE);
        activityDetailBinding.favoriteIv.setVisibility(View.GONE);

        // check if the movie ID is already within the favorite movie list


        FavoriteMovieExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                List<Integer> favoriteMoviesId = mDb.favoriteMovieDao().getAllIds();

                for(int i = 0; i < favoriteMoviesId.size(); i++){
                    int idCheck = favoriteMoviesId.get(i);
                    if( idCheck == Integer.parseInt(mMovieId)) {
                        activityDetailBinding.favoriteBorderIv.setVisibility(View.GONE);
                        activityDetailBinding.favoriteIv.setVisibility(View.VISIBLE);
                    }
                }


            }
        });


        activityDetailBinding.favoriteBorderIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityDetailBinding.favoriteBorderIv.setVisibility(View.GONE);
                activityDetailBinding.favoriteIv.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), movieName + getString(R.string.added_on_favorite_list), Toast.LENGTH_SHORT).show();

                // save the movieID, movieName and posterPath in the room database

                int movieId = Integer.parseInt(mMovieId);

                String movieName = mMovieName;
                String posterPath = mPosterPath;

                final FavoriteMovieModel favoriteMovieModel = new FavoriteMovieModel(movieId, movieName, posterPath);

                FavoriteMovieExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.favoriteMovieDao().insertFavoriteMovie(favoriteMovieModel);

                    }
                });


            }

        });


        activityDetailBinding.favoriteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityDetailBinding.favoriteBorderIv.setVisibility(View.VISIBLE);
                activityDetailBinding.favoriteIv.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), mMovieName + getString(R.string.removed_from_favorite_list), Toast.LENGTH_SHORT).show();


                FavoriteMovieExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int movieId = Integer.parseInt(mMovieId);

                        mDb.favoriteMovieDao().deleteByMovieId(movieId);


                    }
                });


            }

        });


    }

    public void movieDetails(String movieID) {

        // url for movie details
        String detailsFinalUrl = Constant.BASE_URL + movieID + Constant.API_KEY_STRING + Constant.API_KEY;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, detailsFinalUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            // Set poster imageView
                            String posterPath = (Constant.POSTER_BASE_URL + response.getString(Constant.POSTER_PATH));
                            Picasso.get()
                                    .load(posterPath)
                                    .into(activityDetailBinding.posterIv);

                            // Set Title
                            activityDetailBinding.titleTv.setText(movieName);

                            //set plot
                            activityDetailBinding.overviewTv.setText(response.getString(Constant.JSON_OVERVIEW));

                            //set user Ratings
                            activityDetailBinding.ratingsTv.setText(response.getString(Constant.JSON_VOTE_AVERAGE));

                            //set release date
                            activityDetailBinding.releaseDateTv.setText(response.getString(Constant.JSON_RELEASE_DATE));


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