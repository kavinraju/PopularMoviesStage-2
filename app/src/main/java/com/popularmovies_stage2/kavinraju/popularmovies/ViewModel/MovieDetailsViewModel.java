package com.popularmovies_stage2.kavinraju.popularmovies.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.popularmovies_stage2.kavinraju.popularmovies.Database.CastEntry;
import com.popularmovies_stage2.kavinraju.popularmovies.Database.MovieDatabase;
import com.popularmovies_stage2.kavinraju.popularmovies.Database.MovieEntry;
import com.popularmovies_stage2.kavinraju.popularmovies.Database.ReviewsEntry;
import com.popularmovies_stage2.kavinraju.popularmovies.Database.TrailerEntry;
import com.popularmovies_stage2.kavinraju.popularmovies.HelperClass.AppExecutors;

import java.util.List;

public class MovieDetailsViewModel extends ViewModel {


    private LiveData<MovieEntry> movieEntry;
    private LiveData<List<CastEntry>> castEntries;
    private LiveData<List<ReviewsEntry>> reviewsEntries;
    private LiveData<List<TrailerEntry>> trailerEntries;

    public MovieDetailsViewModel(MovieDatabase movieDatabase, int movieID) {
        movieEntry = movieDatabase.movieDao().findMovieEntry(movieID);
        castEntries = movieDatabase.movieDao().loadAllCasts(movieID);
        reviewsEntries = movieDatabase.movieDao().loadAllReviews(movieID);
        trailerEntries = movieDatabase.movieDao().loadAllTrailers(movieID);
        Log.d("Querying from Database","MovieDetailsViewModel");
    }

    public LiveData<MovieEntry> getMovieEntry() {
        return movieEntry;
    }

    public LiveData<List<CastEntry>> getCastEntries() {
        return castEntries;
    }

    public LiveData<List<ReviewsEntry>> getReviewsEntries() {
        return reviewsEntries;
    }

    public LiveData<List<TrailerEntry>> getTrailerEntries() {
        return trailerEntries;
    }


}
