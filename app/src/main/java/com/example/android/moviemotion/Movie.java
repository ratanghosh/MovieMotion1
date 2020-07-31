package com.example.android.moviemotion;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_table")
public class Movie {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String posterPath;
    private String movieID;


    /**
     * No args constructor for use in serialization
     */
    public Movie() {
    }

    // public constructor for the movie
    public Movie(String title, String posterPath, String movieID) {
        this.title = title;
        this.posterPath = posterPath;
        this.movieID = movieID;

    }

    // Getter Method

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getMovieID() {
        return movieID;
    }

    // Setter Methods
    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }


}