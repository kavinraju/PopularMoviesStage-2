package com.popularmovies_stage2.kavinraju.popularmovies.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.popularmovies_stage2.kavinraju.popularmovies.Database.MovieDatabase;

import java.util.List;
    /*
    This ViewModel is used when MovieDetailsActivity is launched from FavoriteMovies Fragment.
    */
public class FavMovieDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private MovieDatabase movieDatabase;
    private int movieID;

    public FavMovieDetailsViewModelFactory(MovieDatabase movieDatabase, int movieID) {
        this.movieDatabase = movieDatabase;
        this.movieID = movieID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new FavMovieDetailsViewModel(movieDatabase, movieID);
    }
}
