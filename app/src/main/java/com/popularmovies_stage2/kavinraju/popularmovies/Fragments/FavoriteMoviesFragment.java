package com.popularmovies_stage2.kavinraju.popularmovies.Fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.popularmovies_stage2.kavinraju.popularmovies.Adapter.FavoriteMoviesAdapter;
import com.popularmovies_stage2.kavinraju.popularmovies.Data_Model.MovieDetail;
import com.popularmovies_stage2.kavinraju.popularmovies.Database.CastEntry;
import com.popularmovies_stage2.kavinraju.popularmovies.Database.MovieDatabase;
import com.popularmovies_stage2.kavinraju.popularmovies.Database.MovieEntry;
import com.popularmovies_stage2.kavinraju.popularmovies.HelperClass.AppExecutors;
import com.popularmovies_stage2.kavinraju.popularmovies.MovieDetailsActivity;
import com.popularmovies_stage2.kavinraju.popularmovies.R;
import com.popularmovies_stage2.kavinraju.popularmovies.ViewModel.FavoriteMoviesViewModel;

import java.util.List;
import java.util.Objects;

public class FavoriteMoviesFragment extends Fragment implements FavoriteMoviesAdapter.MovieTileClickListener {

    public static String SHARED_ELEMENT_TRANSITION_EXTRA = "sharedElementTransition";

    //UI Components
    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;
    ProgressBar mProgressBar;

    private String currentSelectedbottomNavigation;

    // Database
    private MovieDatabase movieDatabase;
    private List<MovieEntry> movieEntries;

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
            public void onChanged(@Nullable List<MovieEntry> movieEntries) {
                setFavoriteMoviesAdapter(movieEntries);
            }
        });


        return rootView;
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
        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

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
