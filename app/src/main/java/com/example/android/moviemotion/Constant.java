package com.example.android.moviemotion;

public class Constant {

    //Please place here your own API KEY
    public static final String API_KEY = "9bfd4d51f3b06a0fe9f09bba827e2242"; // API key
    public static final String API_KEY_STRING = "?api_key="; // API key

    public static final String URL_POPULAR_MOVIE = "https://api.themoviedb.org/3/movie/popular?api_key=";
    public static final String URL_TOP_RATED_MOVIE = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
    public static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185//";
    public static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";


    /* Constant values for the names of each respective parameter */
    public static final String MOVIE_TITLE = "movie_title";
    public static final String POSTER_PATH = "poster_path";
    public static final String MOVIE_ID = "movie_id";
    public static final String TRAILER_URL_EXTENSION = "/videos?api_key=";
    public static final String REVIEWS_URL_EXTENSION = "/reviews?api_key=";

    // Key received from jSON
    public static final String JSON_OVERVIEW = "overview";
    public static final String JSON_VOTE_AVERAGE = "vote_average";
    public static final String JSON_RELEASE_DATE = "release_date";
    public static final String JSON_RESULTS = "results";
    public static final String JSON_TITLE = "title";
    public static final String JSON_ID = "id";
    public static final String JSON_POSTER_PATH = "poster_path";
}
