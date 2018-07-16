package com.popularmovies_stage2.kavinraju.popularmovies.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.popularmovies_stage2.kavinraju.popularmovies.Database.MovieDatabase;
import com.popularmovies_stage2.kavinraju.popularmovies.Database.MovieEntry;

import java.util.List;

public class FavoriteMoviesViewModel extends AndroidViewModel{

    private LiveData<List<MovieEntry>> movieEntries;

    public FavoriteMoviesViewModel(@NonNull Application application) {
        super(application);
        Log.d("Querying from Database","FavoriteMoviesViewModel");
        MovieDatabase movieDatabase = MovieDatabase.getInstance(this.getApplication());
        movieEntries = movieDatabase.movieDao().loadAllMovies();
    }


    public LiveData<List<MovieEntry>> getMovieEntries() {
        return movieEntries;
    }
}
