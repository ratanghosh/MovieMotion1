package com.example.android.moviemotion;


public class Movie {
    private String title;
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
    public Movie(String title, String overview, String userRating, String releaseDate, String posterPath, String movieID) {
        this.title = title;
        this.overview = overview;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.movieID = movieID;

    }

    // Getter Method

    public String getTitle() {
        return title;
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

    // Setter Methods
    public void setTitle(String title) {
        this.title = title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }


}