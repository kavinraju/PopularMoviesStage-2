package srilasaka_developers.skr.kavinraju.movie_reel.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

    /*
        This ViewModel is used when MovieDetailsActivity is launced from PopularMovies & TopRatedMovies Fragment.
     */

public class MovieDetailsViewModel extends ViewModel {
    //Bitmaps
    private MutableLiveData<List<byte[]>> castBitmapBytes;
    private MutableLiveData<List<byte[]>> trailerBitmapBytes;

    MovieDetailsViewModel(MutableLiveData<List<byte[]>> castBitmapBytes, MutableLiveData<List<byte[]>> trailerBitmapBytes) {
        Log.d("MovieDetailsViewModel", "constructor");
        this.castBitmapBytes =  castBitmapBytes;
        this.trailerBitmapBytes = trailerBitmapBytes;
    }

    public MutableLiveData<List<byte[]>> getCastBitmapBytes() {
        Log.d("MovieDetailsViewModel", "getCastBitmapBytes");
        return castBitmapBytes;
    }

    public MutableLiveData<List<byte[]>> getTrailerBitmapBytes() {
        Log.d("MovieDetailsViewModel", "getTrailerBitmapBytes");
        return trailerBitmapBytes;
    }
}
