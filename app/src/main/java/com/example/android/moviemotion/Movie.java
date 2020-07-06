package com.example.android.moviemotion;

import java.util.List;

public class Movie {
    private String originalTitle;
    private String overview;
    private String userRating;
    private String releaseDate;
    private String posterPath;
    private String movieID;


    /**
     * No args constructor for use in serialization
     */
    public Movie() {
    }

    // public constructor for the movie
    public Movie(String originalTitle, String overview, String userRating, String releaseDate, String posterPath, String movieID) {
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.movieID = movieID;

    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getMovieID() {
        return movieID;
    }


}