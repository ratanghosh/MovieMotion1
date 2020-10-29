package com.example.android.moviemotion.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.moviemotion.favoriteMovie.FavoriteMovieDatabase;
import com.example.android.moviemotion.favoriteMovie.FavoriteMovieModel;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<FavoriteMovieModel>> favoriteMovieModels;

    public MainViewModel(@NonNull Application application) {
        super(application);
        FavoriteMovieDatabase favoriteMovieDatabase = FavoriteMovieDatabase.getInstance(this.getApplication());
        favoriteMovieModels = favoriteMovieDatabase.favoriteMovieDao().loadAllFavoriteMovies();
    }

    public LiveData<List<FavoriteMovieModel>> getFavoriteMovieModels() {
        return favoriteMovieModels;
    }

}
