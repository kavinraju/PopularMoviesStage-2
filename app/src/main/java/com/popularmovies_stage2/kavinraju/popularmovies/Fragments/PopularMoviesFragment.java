package com.popularmovies_stage2.kavinraju.popularmovies.Fragments;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.popularmovies_stage2.kavinraju.popularmovies.Adapter.MovieListAdapter_HomeActivity;
import com.popularmovies_stage2.kavinraju.popularmovies.Data_Model.MovieDetail;
import com.popularmovies_stage2.kavinraju.popularmovies.Database.MovieDatabase;
import com.popularmovies_stage2.kavinraju.popularmovies.HelperClass.HelperMethods;
import com.popularmovies_stage2.kavinraju.popularmovies.MovieDetailsActivity;
import com.popularmovies_stage2.kavinraju.popularmovies.R;
import com.popularmovies_stage2.kavinraju.popularmovies.Utils.JsonUtils;
import com.popularmovies_stage2.kavinraju.popularmovies.Utils.NetworkUtils;
import com.popularmovies_stage2.kavinraju.popularmovies.ViewModel.FragmentViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PopularMoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<MovieDetail>> ,
                                                                View.OnClickListener,
                                                                MovieListAdapter_HomeActivity.MovieTileClickListener {


    // Constants used
    private static int MOVIES_QUERY_LOADER_ID = 99;
    private static String MOVIES_QUERY_EXTRA = "movies_query";
    private static String PAGE_NUMBER_EXTRA = "page_number";
    private String currentSelectedbottomNavigation;
    public static String SHARED_ELEMENT_TRANSITION_EXTRA = "sharedElementTransition";
    private static int defaultPageNo = 1;
    private static boolean defaultLoading = true;
    private boolean isFirstTime = true;

    //Keys used for savedInstancestate
    private static String MOVIES_KEY ="movie_list";
    private static String MAXIMUM_PAGES_KEY ="maximumPages";
    private static String LOADING_KEY ="loading";
    private static String TOTAL_ITEM_COUNT_KEY ="totalItemCount";
    private static String PREVIOUS_TOTAL_ITEM_COUNT_KEY ="previous_totalItemCount";
    private static String VISIBLE_ITEM_COUNT_KEY ="visibleItemCount";
    private static String PAST_VISIBLE_ITEM_COUNT_KEY ="past_visibileItemCount";
    private static String VISIBLE_THRESHOLD_KEY ="visibleThreshold";
    private static String CURRENT_PAGE_NUMBER_KEY ="pageNo";
    private static String CURRENT_SELECTED_BOTTOM_NAVI_KEY ="currentSelectedbottomNavigation";

    // Variables used to load additional pages
    private int currentPage = 1;
    private int maximumPages = 0;
    private boolean loading = true;
    private int totalItemCount = 0;
    private int previous_totalItemCount = 0;
    private int visibleItemCount = 0;
    private int past_visibileItemCount = 0;
    private int visibleThreshold = 15;

    //POJO class
    private ArrayList<MovieDetail> movieDetails;

    // RecyclerView Adapter
    private MovieListAdapter_HomeActivity movieListAdapter_homeActivity;

    //UI Components
    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;
    TextView mTextView;
    ImageView mImageView;
    Button mButton_retry;
    ProgressBar mProgressBar;
    Toast mToast;

    // Database
    private MovieDatabase movieDatabase;

    private List<Integer> favMovieIds;

    //Empty Constructor
    public PopularMoviesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing Database
        movieDatabase = MovieDatabase.getInstance(getContext());

        FragmentViewModel viewModel = ViewModelProviders.of(this).get(FragmentViewModel.class);
        viewModel.getMovieIDs().observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(@Nullable List<Integer> integers) {
                favMovieIds = integers;
                Log.d("favMovieIds",String.valueOf(favMovieIds.size()));
                // This sets the Adapter whenever there is a change in List of Fav. Movie IDs
                if (isFirstTime){
                    isFirstTime = false;
                }else {
                    setMovieListAdapter_HomeActivity();
                }
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recycler_view , container , false);

        // Initializing UI Components
        mRecyclerView = rootView.findViewById(R.id.recycler_view_fragment);
        mTextView = rootView.findViewById(R.id.tv_connection_failed);
        mImageView = rootView.findViewById(R.id.img_connention_failed);
        mProgressBar = rootView.findViewById(R.id.progressBar_fragment);
        mButton_retry = rootView.findViewById(R.id.btn_networkRetry);

        //Set ProgressBar visibility as GONE
        HelperMethods.showProgressBar(mProgressBar,false);

        mButton_retry.setOnClickListener(this);

        // Checking if the device is in PORTRAIT OR LANDSCAPE mode and then setting the respective span count
        mGridLayoutManager = getGridLayoutManager();
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(movieListAdapter_homeActivity);


        setUpPagination();


        // For Debugging purpose
        if(defaultLoading){
            Log.d("defaultLoading", "true");
        }else {
            Log.d("defaultLoading", "false");
        }

        //Checking if network is available and setting up UI accordingly
        if (isNetworkAvailable()){
            setNetworkFailedUI(false);
        }
        else {
            setNetworkFailedUI(true);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null){
            Log.d("savedInstanceState","!null");
            loading = savedInstanceState.getBoolean(LOADING_KEY);

            currentPage = savedInstanceState.getInt(CURRENT_PAGE_NUMBER_KEY);
            totalItemCount = savedInstanceState.getInt(TOTAL_ITEM_COUNT_KEY);
            previous_totalItemCount = savedInstanceState.getInt(PREVIOUS_TOTAL_ITEM_COUNT_KEY);
            visibleItemCount = savedInstanceState.getInt(VISIBLE_ITEM_COUNT_KEY);
            past_visibileItemCount = savedInstanceState.getInt(PAST_VISIBLE_ITEM_COUNT_KEY);
            visibleThreshold = savedInstanceState.getInt(VISIBLE_THRESHOLD_KEY);
            maximumPages = savedInstanceState.getInt(MAXIMUM_PAGES_KEY);

            currentSelectedbottomNavigation = savedInstanceState.getString(CURRENT_SELECTED_BOTTOM_NAVI_KEY);
            movieDetails = (ArrayList<MovieDetail>) savedInstanceState.getSerializable(MOVIES_KEY);
            if (movieDetails == null){
                Log.d("movieDetails", "null");
            }

            // Setting up UI with previous DataSet by setting error UI and Progress bar GONE
            setNetworkFailedUI(false);

            HelperMethods.showProgressBar(mProgressBar,false);

            // Setting up UI with previous DataSet only if there is some Data available
            if (movieDetails != null) {
                setMovieListAdapter_HomeActivity();
            }

        } else if(defaultLoading){

            //set Popular by default
            runLoaderManager(currentSelectedbottomNavigation, defaultPageNo);
            //Log.d("defaultLoading","true");
            defaultLoading = false;

        }

    }

    private void setMovieListAdapter_HomeActivity() {
        Log.d("setMovieListAdapter","1");
        mGridLayoutManager = getGridLayoutManager();
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        movieListAdapter_homeActivity = new MovieListAdapter_HomeActivity(movieDetails, movieDetails.size(),favMovieIds, this);
        mRecyclerView.setAdapter(movieListAdapter_homeActivity);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(LOADING_KEY,loading);

        outState.putInt(CURRENT_PAGE_NUMBER_KEY,currentPage);
        outState.putInt(TOTAL_ITEM_COUNT_KEY,totalItemCount);
        outState.putInt(PREVIOUS_TOTAL_ITEM_COUNT_KEY,previous_totalItemCount);
        outState.putInt(VISIBLE_ITEM_COUNT_KEY,visibleItemCount);
        outState.putInt(PAST_VISIBLE_ITEM_COUNT_KEY,past_visibileItemCount);
        outState.putInt(VISIBLE_THRESHOLD_KEY,visibleThreshold);
        outState.putInt(MAXIMUM_PAGES_KEY, maximumPages);

        outState.putString(CURRENT_SELECTED_BOTTOM_NAVI_KEY,currentSelectedbottomNavigation);

        outState.putSerializable(MOVIES_KEY,  movieDetails);

    }

    // Loader Methods
    @NonNull
    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader< ArrayList<MovieDetail> > onCreateLoader(int id, @Nullable final Bundle args) {

        return new AsyncTaskLoader< ArrayList<MovieDetail> >(Objects.requireNonNull(getContext())) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (args == null){
                    return;
                }

                Log.d("Popular Movies Page No", String.valueOf(currentPage));

                /*
                    If network is not available and Data Set (movieDetails) is null we set Network error UI
                    else If   network is not available and Data Set (movieDetails) is available we just show a Toast message
                    else show progress bar and load set RecyclerView Visible
                 */
                if (!isNetworkAvailable() && movieDetails == null){
                    setNetworkFailedUI(true);
                }else if (!isNetworkAvailable()){
                    showToastMessage();
                }else {
                    setNetworkFailedUI(false);
                    HelperMethods.showProgressBar(mProgressBar,true);
                }


            }

            @Override
            public ArrayList<MovieDetail> loadInBackground() {

                assert args != null;
                String movie_rating_type = args.getString(MOVIES_QUERY_EXTRA);
                int page_number = args.getInt(PAGE_NUMBER_EXTRA);

                if (movie_rating_type == null && page_number <= 0){
                    return null;
                }


                try {
                    // Using NetworkUtils Helper class to build URL and getting its responce as JSON string
                    URL movies_URL = NetworkUtils.buildURL_for_MoivesList(movie_rating_type, page_number);
                    String jsonString =  NetworkUtils.getResponceFromURl(movies_URL);

                    Log.d("pageNo ",String.valueOf(page_number));
                    return JsonUtils.getMovieArrayList(jsonString);

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }



        };
    }


    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<MovieDetail>> loader, ArrayList<MovieDetail> movieDetails) {

        if (movieDetails != null ){

            if (currentPage > defaultPageNo){
                // adding data from nextPage
                movieListAdapter_homeActivity.addMovies(movieDetails);
                HelperMethods.showProgressBar(mProgressBar,false);
            }else {
                // Initialization
                this.movieDetails = movieDetails;

                maximumPages = movieDetails.get(0).getTotalPages(); // get maximum number of pages
                Log.d("maximumPages",String.valueOf(maximumPages));

                // Checking if the device is in PORTRAIT OR LANDSCAPE mode and then setting the respective span count
                mGridLayoutManager = getGridLayoutManager();
                mRecyclerView.setLayoutManager(mGridLayoutManager);
                mRecyclerView.setHasFixedSize(false);
                movieListAdapter_homeActivity = new MovieListAdapter_HomeActivity(movieDetails,movieDetails.size(),favMovieIds,this);
                mRecyclerView.setAdapter(movieListAdapter_homeActivity);

                HelperMethods.showProgressBar(mProgressBar,false);
            }



        }
        else if(!isNetworkAvailable() && this.movieDetails == null) {
            //NetworkError Code goes here
            //Check if MovieDetails obeject has any data
            setNetworkFailedUI(true);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<MovieDetail>> loader) {

    }

    public  void setCurrentSelectedbottomNavigation(String currentSelectedbottomNavigation) {
        this.currentSelectedbottomNavigation = currentSelectedbottomNavigation;
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){
            case R.id.btn_networkRetry:

                if (isNetworkAvailable()){
                    setNetworkFailedUI(false);
                    runLoaderManager(currentSelectedbottomNavigation,currentPage);
                }else {
                    setNetworkFailedUI(true);
                }

                break;
        }

    }

    @Override
    public void onMovieTitleClick(View view, int clickedMoviePosition) {

        switch (view.getId()){
            case R.id.img_poster_recycerView_Card_HomeActivity:
                if(movieDetails.size() > 0) {
            /*
                 This condition is necessary if we click on item that hasn't even loaded, this is the situation when
                    there is no network and we try load data and do click.
             */
                    Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                    intent.putExtra("movie", movieDetails.get(clickedMoviePosition));
                    intent.putExtra("isFromFavorite", false);
                    intent.putExtra(SHARED_ELEMENT_TRANSITION_EXTRA, ViewCompat.getTransitionName(view));
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            Objects.requireNonNull(getActivity()),
                            view,
                            ViewCompat.getTransitionName(view));

                    startActivity(intent, optionsCompat.toBundle());

                }
                break;
            case R.id.img_favorite_recycerView_Card_HomeActivity:

                break;
        }
    }


    /*
        **********************HELPER METHODS**************************
     */

    // Helper Method to set up Pagination
    private void setUpPagination(){

        // Addding addOnScrollListener to listen to UI scroll.
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                totalItemCount = mGridLayoutManager.getItemCount();
                visibleItemCount = mGridLayoutManager.getChildCount();
                past_visibileItemCount = mGridLayoutManager.findFirstVisibleItemPosition();


                if (dy > 0) { // True only if the user scrolls down.

                    if (loading){
                        Log.d("RecyclerScroll","(1) loading");
                        if (totalItemCount > previous_totalItemCount){
                            /*If data is loading and  totalItemCount greater than previous_totalItemCount then set loading to false and
                                increase the currentPage number and set previous_totalItemCount to totalItemCount.*/
                            loading = false;
                            previous_totalItemCount = totalItemCount;
                            //currentPage++;
                            Log.d("RecyclerScroll","totalItemCount > previous_totalItemCount");
                        }
                    }

                    if (!loading && (totalItemCount - visibleItemCount) <= (past_visibileItemCount + visibleThreshold)){
                        /*
                            If loading is false and
                            if ( total num. of Items - num. of items visible on screen ) is less than or equal to ( num. of item scrolled out + visible threshold )
                            then if network is available currentPage is increase by one, loading is set True and Data for next page is loaded.
                         */
                        if ( currentPage <= maximumPages){
                            Log.d("RecyclerScroll","3");
                            if (isNetworkAvailable()) {
                                currentPage++;
                                loading = true;
                                runLoaderManager(currentSelectedbottomNavigation, currentPage);
                            }else {
                                showToastMessage();
                            }
                        }
                    }else {
                        Log.d("RecyclerScroll","3) " +String.valueOf(totalItemCount)+"-"+String.valueOf(visibleItemCount)+"<="+String.valueOf(past_visibileItemCount)+"+" + String.valueOf(visibleThreshold) );
                    }

                    /*
                        If totalItemCount is less than previous_totalItemCount then we reset the state.
                        currentPage is set to defaultPageNo = 1.
                     */
                    if (totalItemCount < previous_totalItemCount){
                        currentPage = defaultPageNo;
                        previous_totalItemCount = totalItemCount;
                        if (totalItemCount == 0){
                            loading = true;
                        }
                    }
                }
            }
        });

    }
    // Helper Method to start Loader
    private void runLoaderManager(String movie_rating_type, int pageNo){

        Bundle movieQueryBundle = new Bundle();
        movieQueryBundle.putString(MOVIES_QUERY_EXTRA , movie_rating_type);
        movieQueryBundle.putInt(PAGE_NUMBER_EXTRA, pageNo);

        LoaderManager loaderManager = getLoaderManager();
        Loader<String> movieloader = loaderManager.getLoader(MOVIES_QUERY_LOADER_ID);

        if (movieloader == null){
            loaderManager.initLoader(MOVIES_QUERY_LOADER_ID , movieQueryBundle ,this ).forceLoad();
        }else {
            loaderManager.restartLoader(MOVIES_QUERY_LOADER_ID,movieQueryBundle,this).forceLoad();
        }

    }


    // Helper Method to get GridLayoutManager basaed on Screen Orientation
        /*
            Rubric
         <Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails>
         */
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

    // Helper Method to show a Network error Toast message
    private void showToastMessage() {


        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) Objects.requireNonNull(getActivity()).findViewById(R.id.customToast));
        TextView textView = layout.findViewById(R.id.toast_txt);
        textView.setText(R.string.check_your_internet_connection);
        ImageView imageView = layout.findViewById(R.id.toast_img);
        imageView.setBackgroundResource(R.drawable.signal_wifi_off_accent_48dp);


        if (mToast != null){
            mToast.cancel();
            mToast = new Toast(getContext());
            mToast.setGravity(Gravity.BOTTOM, 0, 0);
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.setView(layout);
            mToast.show();
        }else {
            mToast = new Toast(getContext());
            mToast.setGravity(Gravity.BOTTOM, 0, 0);
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.setView(layout);
            mToast.show();
        }
    }

    // Helper Method to check if network is available
    public boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    // Helper Method to set UI on Network Error
    private void setNetworkFailedUI(boolean noConnection){
        if (noConnection){
            mImageView.setVisibility(View.VISIBLE);
            mTextView.setVisibility(View.VISIBLE);
            mButton_retry.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }else {
            mImageView.setVisibility(View.GONE);
            mTextView.setVisibility(View.GONE);
            mButton_retry.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

}
