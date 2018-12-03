package srilasaka_developers.skr.kavinraju.movie_reel.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import srilasaka_developers.skr.kavinraju.movie_reel.Database.MovieDatabase;
import srilasaka_developers.skr.kavinraju.movie_reel.Database.MovieEntry;

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
