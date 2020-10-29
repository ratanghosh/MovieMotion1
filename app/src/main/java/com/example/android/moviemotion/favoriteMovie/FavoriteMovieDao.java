package com.example.android.moviemotion.favoriteMovie;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteMovieDao {


    // to call all the favorite movies from the room database
    @Query("SELECT * FROM favoriteMovieTable ")
    LiveData<List<FavoriteMovieModel>> loadAllFavoriteMovies();


    // delete favourite movie from the database by movieId
    @Query("DELETE FROM favoriteMovieTable WHERE movieId = :movieId")
    void deleteByMovieId(int movieId);

    // call all the movieId from the favorite movie table
    @Query("SELECT movieId FROM favoriteMovieTable")
    List<Integer> getAllIds();


    // Insert new movie in the favorite movie table
    @Insert()
    void insertFavoriteMovie(FavoriteMovieModel favoriteMovieModel);


}
