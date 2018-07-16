package com.popularmovies_stage2.kavinraju.popularmovies.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.List;

    /*
        This ViewModel is used when MovieDetailsActivity is launced from PopularMovies & TopRatedMovies Fragment.
     */


public class MovieDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    //Bitmaps
    private MutableLiveData<List<byte[]>> castBitmapBytes;
    private MutableLiveData<List<byte[]>> trailerBitmapBytes;

    public MovieDetailsViewModelFactory() {
        castBitmapBytes = new MutableLiveData<>();
        trailerBitmapBytes = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieDetailsViewModel(castBitmapBytes, trailerBitmapBytes);
    }

    public void loadIntoCast(List<byte[]> bytes){
        castBitmapBytes.postValue(bytes);
    }
    public void loadIntoTrailers(List<byte[]> bytes){
        trailerBitmapBytes.postValue(bytes);
    }
}
