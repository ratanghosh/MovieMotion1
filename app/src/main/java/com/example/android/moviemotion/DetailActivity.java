package com.example.android.moviemotion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.moviemotion.MovieUtils.overviewList;
import static com.example.android.moviemotion.MovieUtils.posterLinkList;
import static com.example.android.moviemotion.MovieUtils.ratingsList;
import static com.example.android.moviemotion.MovieUtils.releaseDateList;
import static com.example.android.moviemotion.MovieUtils.releaseDateList1;
import static com.example.android.moviemotion.MovieUtils.releaseDateList2;
import static com.example.android.moviemotion.MovieUtils.titleList;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;








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
        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        getSupportActionBar().setTitle(titleList.get(position));


        if (intent == null) {
            //closeOnError();
        }



        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            // closeOnError();
            return;
        }


            titleTv.setText(titleList.get(position));
            overviewTv.setText(overviewList.get(position));
            ratingsTv.setText(ratingsList.get(position));
            releaseDateTv.setText(releaseDateList.get(position));

            String posterPath = ("http://image.tmdb.org/t/p/w185//" +posterLinkList.get(position));



        populateUI();

        Picasso.with(this)
                .load(posterPath )
                .into(posterIv);









    }



    private void populateUI() {

    }


    }