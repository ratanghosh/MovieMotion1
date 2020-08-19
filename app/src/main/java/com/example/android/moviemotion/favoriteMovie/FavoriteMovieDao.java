package com.example.android.moviemotion.favoriteMovie;

import android.icu.text.Replaceable;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteMovieDao {

    // to call all the favorite movies from the room database
    @Query("SELECT * FROM favoriteMovieTable ")
    List<FavoriteMovieModel> loadAllFavoriteMovies();



    @Insert(onConflict = OnConflictStrategy.REPLACE)
   void insertFavoriteMovie(FavoriteMovieModel favoriteMovieModel);

    @Delete
    void deleteFavoriteMovie(FavoriteMovieModel favoriteMovieModel);



    @Query("DELETE FROM favoriteMovieTable WHERE movieId = :movieId")
     void deleteByMovieId(int movieId);


    @Query("SELECT movieId FROM favoriteMovieTable")
    List<Integer> getAllIds();







}
