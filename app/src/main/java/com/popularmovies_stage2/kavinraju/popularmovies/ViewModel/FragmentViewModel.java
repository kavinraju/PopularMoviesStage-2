package com.popularmovies_stage2.kavinraju.popularmovies.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.popularmovies_stage2.kavinraju.popularmovies.Database.MovieDatabase;

import java.util.List;

public class FragmentViewModel extends AndroidViewModel {

    private LiveData<List<Integer>> movieIDs;

    public FragmentViewModel(@NonNull Application application) {
        super(application);
        Log.d("Querying from Database","FragmentViewModel");
        MovieDatabase movieDatabase = MovieDatabase.getInstance(this.getApplication());
        movieIDs = movieDatabase.movieDao().movieIDs();
    }

    public LiveData<List<Integer>> getMovieIDs() {
        return movieIDs;
    }
}
