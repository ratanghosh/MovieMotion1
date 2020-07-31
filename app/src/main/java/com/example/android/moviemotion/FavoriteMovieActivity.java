package com.example.android.moviemotion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FavoriteMovieActivity extends AppCompatActivity {

    private static final String MOVIE_TITLE = "movie_title";
    private static final String MOVIE_ID= "movie_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movie);

        Button button = findViewById(R.id.favorite_movie_details_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoriteMovieActivity.this, DetailActivity.class);
                intent.putExtra(MOVIE_TITLE, "Ad Astra");
                intent.putExtra(MOVIE_ID, "419704");
                startActivity(intent);
            }
        });
    }
}
