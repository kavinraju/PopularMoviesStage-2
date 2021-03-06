package srilasaka_developers.skr.kavinraju.movie_reel.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import srilasaka_developers.skr.kavinraju.movie_reel.Adapter.FavoriteMoviesAdapter;
import srilasaka_developers.skr.kavinraju.movie_reel.Database.MovieDatabase;
import srilasaka_developers.skr.kavinraju.movie_reel.Database.MovieEntry;
import srilasaka_developers.skr.kavinraju.movie_reel.HelperClass.AppExecutors;
import srilasaka_developers.skr.kavinraju.movie_reel.MovieDetailsActivity;
import srilasaka_developers.skr.kavinraju.movie_reel.R;
import srilasaka_developers.skr.kavinraju.movie_reel.ViewModel.FavoriteMoviesViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavoriteMoviesFragment extends Fragment implements FavoriteMoviesAdapter.MovieTileClickListener {

    public static String SHARED_ELEMENT_TRANSITION_EXTRA = "sharedElementTransition";

    //UI Components
    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;
    ProgressBar mProgressBar;

    // Constants
    private String currentSelectedbottomNavigation;

    private static int adapterPosition = 0;
    private static boolean isFirstTime = true;
    private static boolean isFromOnStop = false;
    // Database
    private MovieDatabase movieDatabase;
    private List<MovieEntry> movieEntries = new ArrayList<>();

    //Adapter
    private FavoriteMoviesAdapter favoriteMoviesAdapter;


    public FavoriteMoviesFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recycler_view , container , false);

        // Initializing UI Components
        mRecyclerView = rootView.findViewById(R.id.recycler_view_fragment);
        mProgressBar = rootView.findViewById(R.id.progressBar_fragment);

        // Making the network related UI components INVISIBLE
        rootView.findViewById(R.id.img_connention_failed).setVisibility(View.GONE);
        rootView.findViewById(R.id.btn_networkRetry).setVisibility(View.GONE);
        rootView.findViewById(R.id.tv_connection_failed).setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);

        movieDatabase = MovieDatabase.getInstance(getContext());

        //Load Movies from Database - from ViewModel
        FavoriteMoviesViewModel moviesViewModel = ViewModelProviders.of(this).get(FavoriteMoviesViewModel.class);
        moviesViewModel.getMovieEntries().observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntry> movie_Entries) {

                Log.d("rrr - Size - global", String.valueOf(movieEntries.size()));
                Log.d("rrr - Size - local", String.valueOf(movie_Entries.size()));

                if (isFirstTime || movie_Entries.size() > movieEntries.size()) {
                    setFavoriteMoviesAdapter(movie_Entries);
                    isFirstTime = false;
                    Log.d("rrr","isFirsttime");

                }else if (movie_Entries.size() < movieEntries.size() ) {
                    movieEntries = movie_Entries;
                    favoriteMoviesAdapter.updateFavMovieEntries(movie_Entries, adapterPosition);
                    Log.d("rrr","mid");

                }else if(movieEntries.size() == movie_Entries.size() && isFromOnStop){
                    setFavoriteMoviesAdapter(movie_Entries);
                    Log.d("rrr","eq");
                    isFromOnStop = false;
                }


            }
        });


        return rootView;
    }
    @Override
    public void onStop() {
        super.onStop();
        isFromOnStop = true;
    }


    private void setFavoriteMoviesAdapter(List<MovieEntry> movieEntries) {
        // Checking if the device is in PORTRAIT OR LANDSCAPE mode and then setting the respective span count
            this.movieEntries = movieEntries;
            mGridLayoutManager = getGridLayoutManager();
            mRecyclerView.setLayoutManager(mGridLayoutManager);
            mRecyclerView.setHasFixedSize(false);
            favoriteMoviesAdapter = new FavoriteMoviesAdapter(movieEntries, movieEntries.size(), this);
            mRecyclerView.setAdapter(favoriteMoviesAdapter);
    }


    public  void setCurrentSelectedbottomNavigation(String currentSelectedbottomNavigation) {
        this.currentSelectedbottomNavigation = currentSelectedbottomNavigation;
    }
    private GridLayoutManager getGridLayoutManager(){
        GridLayoutManager layoutManager;
        if (Objects.requireNonNull(getContext()).getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

            // if PORTRAIT mode set spanCount as
            layoutManager = new GridLayoutManager(getContext(),3);
        }
        else if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

            // if PORTRAIT mode set spanCount as
            layoutManager = new GridLayoutManager(getContext(),4);
        }else {
            layoutManager = new GridLayoutManager(getContext(),3);
        }

        return layoutManager;
    }

    @Override
    public void onMovieTitleClick(View view, final int clickedMoviePosition) {

        adapterPosition = clickedMoviePosition;

        switch (view.getId()) {
            case R.id.img_poster_recycerView_Card_HomeActivity:
                if (movieEntries.size() > 0) {
                /*
                     This condition is necessary if we click on item that hasn't even loaded, this is the situation when
                        there is no network and we try load data and do click.
                 */

                    Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                    intent.putExtra("isFromFavorite", true);
                    intent.putExtra("fav_movie", movieEntries.get(clickedMoviePosition).getMovieId());
                    intent.putExtra(SHARED_ELEMENT_TRANSITION_EXTRA, ViewCompat.getTransitionName(view));
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            Objects.requireNonNull(getActivity()),
                            view,
                            ViewCompat.getTransitionName(view));

                    startActivity(intent, optionsCompat.toBundle());

                }
                break;
            case R.id.img_favorite_recycerView_Card_HomeActivity:
                if (movieEntries.size() > 0){

                    // Delete the movie from favoriteList

                    view.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.heart_bounce));
                    view.setBackgroundResource(R.drawable.favorite_border_primary_24dp);
                    final int movieID = movieEntries.get(clickedMoviePosition).getMovieId();
                    AppExecutors.getInstance().executorForDatabase().execute(new Runnable() {
                        @Override
                        public void run() {
                            movieDatabase.movieDao().deleteMovie(movieEntries.get(clickedMoviePosition));
                            movieDatabase.movieDao().deleteCasts(movieID);
                            movieDatabase.movieDao().deleteTrailers(movieID);
                            movieDatabase.movieDao().deleteReviews(movieID);

                        }
                    });

                }
                break;
        }
    }
}
