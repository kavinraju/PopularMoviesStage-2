package srilasaka_developers.skr.kavinraju.movie_reel.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import srilasaka_developers.skr.kavinraju.movie_reel.Database.MovieDatabase;

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
