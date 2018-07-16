package com.popularmovies_stage2.kavinraju.popularmovies.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.popularmovies_stage2.kavinraju.popularmovies.Database.MovieDatabase;

public class MovieDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MovieDatabase movieDatabase;
    private final int movieID;

    public MovieDetailsViewModelFactory(MovieDatabase movieDatabase, int movieID) {
        this.movieDatabase = movieDatabase;
        this.movieID = movieID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieDetailsViewModel(movieDatabase, movieID);
    }
}
