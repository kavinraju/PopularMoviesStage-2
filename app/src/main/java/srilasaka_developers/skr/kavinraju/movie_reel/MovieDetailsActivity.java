package srilasaka_developers.skr.kavinraju.movie_reel;


import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import srilasaka_developers.skr.kavinraju.movie_reel.Adapter.CastListAdapter;
import srilasaka_developers.skr.kavinraju.movie_reel.Adapter.ReviewsAdapter;
import srilasaka_developers.skr.kavinraju.movie_reel.Adapter.TrailersAdapter;
import srilasaka_developers.skr.kavinraju.movie_reel.Data_Model.CastDetails;
import srilasaka_developers.skr.kavinraju.movie_reel.Data_Model.MovieDetail;
import srilasaka_developers.skr.kavinraju.movie_reel.Data_Model.MovieDetail2;
import srilasaka_developers.skr.kavinraju.movie_reel.Data_Model.MovieReviews;
import srilasaka_developers.skr.kavinraju.movie_reel.Data_Model.MovieTrailers;
import srilasaka_developers.skr.kavinraju.movie_reel.Database.CastEntry;
import srilasaka_developers.skr.kavinraju.movie_reel.Database.MovieDatabase;
import srilasaka_developers.skr.kavinraju.movie_reel.Database.MovieEntry;
import srilasaka_developers.skr.kavinraju.movie_reel.Database.ReviewsEntry;
import srilasaka_developers.skr.kavinraju.movie_reel.Database.TrailerEntry;
import srilasaka_developers.skr.kavinraju.movie_reel.HelperClass.AppExecutors;
import srilasaka_developers.skr.kavinraju.movie_reel.HelperClass.HelperMethods;

import srilasaka_developers.skr.kavinraju.movie_reel.R;

import srilasaka_developers.skr.kavinraju.movie_reel.Utils.JsonUtils;
import srilasaka_developers.skr.kavinraju.movie_reel.Utils.NetworkUtils;
import srilasaka_developers.skr.kavinraju.movie_reel.ViewModel.FavMovieDetailsViewModel;
import srilasaka_developers.skr.kavinraju.movie_reel.ViewModel.FavMovieDetailsViewModelFactory;
import srilasaka_developers.skr.kavinraju.movie_reel.ViewModel.MovieDetailsViewModel;
import srilasaka_developers.skr.kavinraju.movie_reel.ViewModel.MovieDetailsViewModelFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

    /*
     NOTE: This class consists of data for both normal loading of Data for MovieDetails as well as for
     Favourite MovieDetails Screen.
     Code inside if ( fromFavorite ) is for  Favourite MovieDetails Screen and else part is for Normal Movie Details Screen.
    */

    /*
    Rubric
    <UI contains a screen for displaying the details for a selected movie, Trailers and Reviews.>
    */

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks,
        TrailersAdapter.OnTrailerTileClickListener, View.OnClickListener {

    //Constants for Loaders
    private static int MOVIE_DETAIL_QUERY_LOADER_ID = 100;
    private static String MOVIE_DETAIL_QUERY_EXTRA = "movie_detail_query";
    private static int MOVIE_LANGUAGE_QUERY_LOADER_ID = 101;
    private static String MOVIE_LANGUAGE_QUERY_EXTRA = "movie_language_query";
    private static int CAST_DETAILS_QUERY_LOADER_ID = 102;
    private static String CAST_DETAILS_QUERY_EXTRA = "cast_details_query";
    private static int TRAILERS_QUERY_LOADER_ID = 103;
    private static String TRAILERS_QUERY_EXTRA = "trailers_query";
    private static int REVIEWS_QUERY_LOADER_ID = 104;
    private static String REVIEWS_QUERY_EXTRA = "reviews_query";
    private static int CAST_BITMAPS_QUERY_LOADER_ID = 105;
    private static String CAST_BITMAPS_QUERY_EXTRA = "cast_bitmaps_query";
    private static int TRAILERS_BITMAPS_LOADER_ID = 106;
    private static String TRAILERS_BITMAPS_QUERY_EXTRA = "trailer_bitmaps_query";
    private static final String IS_FOR_CAST_BITMAPS_QUERY_EXTRA = "is_for_cast_bitmap_loading" ;
    private static final int CAST_ENTRIES_LOADER_ID = 107;

    //Keys used for savedInstancestate
    private static String MOVIE_DETAIL_KEY ="movie_detail";
    private static String MOVIE_DETAIL_2_KEY ="movie_detail_2";
    private static String LANGUAGE__KEY ="language";
    private static String CAST_LIST_KEY ="cast_list";
    private static String TRAILERS__KEY ="trailers";
    private static String REVIEWS__KEY ="reviews";
    private static String FAVORITE_KEY = "favorite";

    //Key used for SharedElementTransition
    public static String SHARED_ELEMENT_TRANSITION_EXTRA = "sharedElementTransition";

    // POJO Class
    private MovieDetail movieDetail;
    private MovieDetail2 movieDetail2;
    private ArrayList<CastDetails> castDetails;
    private ArrayList<MovieTrailers> movieTrailers;
    private ArrayList<MovieReviews> movieReviews;

    // Database
    private MovieDatabase movieDatabase;

    // Tables
    private MovieEntry movieEntry;
    private List<CastEntry> castEntries;
    private List<ReviewsEntry> reviewsEntries;
    private List<TrailerEntry> trailerEntries;

    //Bitmaps
    private List<byte[]> castBitmapBytes = new ArrayList<>();
    private List<byte[]> trailerBitmapBytes = new ArrayList<>();

    //Adapters
    private CastListAdapter castListAdapter;
    private TrailersAdapter trailersAdapter;
    private ReviewsAdapter reviewsAdapter;

    private int movieID;
    private String movieLanguageCode;
    private String language = "";
    private boolean showAllReviews = true;
    private boolean favorite;
    private boolean fromFavorite;

    // UI Components
    private Toast mToast;
    @BindView(R.id.tv_movie_title)
    TextView textView_movieTitle;
    @BindView(R.id.tv_movieLanguage)
    TextView textView_movie_language;
    @BindView(R.id.tv_genres)
    TextView textView_generes;
    @BindView(R.id.tv_releaseDate)
    TextView textView_releaseDate;
    @BindView(R.id.tv_tagline)
    TextView textView_tagline;
    @BindView(R.id.tv_description)
    TextView textView_description;
    @BindView(R.id.tv_rating)
    TextView textView_rating;
    @BindView(R.id.img_backDrop)
    ImageView imageView_backdrop;
    @BindView(R.id.img_poster)
    ImageView imageView_poster;
    @BindView(R.id.img_btn_back)
    ImageButton imageButton_back;
    @BindView(R.id.progressBar_movieDetails)
    ProgressBar progressBar;
    @BindView(R.id.img_btn_favorite)
    ImageButton imageButton_favorite;
    @BindView(R.id.recyclerView_casts)
    RecyclerView recyclerView_casts;
    @BindView(R.id.recyclerView_trailers)
    RecyclerView recyclerView_trailers;
    @BindView(R.id.recyclerView_reviews)
    RecyclerView recyclerView_reviews;
    @BindView(R.id.tv_total_reviews)
    TextView textView_total_reviews;
    @BindView(R.id.img_btn_more_reviews)
    ImageButton imageButton_more_reviews;
    @BindView(R.id.cv_reviews)
    CardView cardView_reviews;
    @BindView(R.id.progressBar_casts)
    ProgressBar progressBarCasts;
    /*@BindView(R.id.shimmer_details_layout)
    ShimmerFrameLayout shimmerFrameLayout;*/


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        // Getting the MovieDetail object from intent
        Bundle bundle = getIntent().getExtras();
        fromFavorite = getIntent().getBooleanExtra("isFromFavorite",false);

        //It works version greater that LOLLIPOP
        assert bundle != null;
        String imageTransitionName = bundle.getString(SHARED_ELEMENT_TRANSITION_EXTRA);
        imageView_poster.setTransitionName(imageTransitionName);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"font/BalooBhaina-Regular.ttf");
        textView_movieTitle.setTypeface(typeface);


        // Database
        movieDatabase = MovieDatabase.getInstance(getApplicationContext());


        // Setting up OnClickListener
        imageButton_more_reviews.setOnClickListener(this);
        imageButton_favorite.setOnClickListener(this);
        cardView_reviews.setOnClickListener(this);

        if (fromFavorite){
            //Favourite MovieDetails Screen
            movieID = getIntent().getIntExtra("fav_movie" , -1);
            setUIComponents();
            setUpFavMovieViewModel();

        }else {
            movieDetail = getIntent().getParcelableExtra("movie");
            setUpMovieDetailsViewModel();
        }


        if (savedInstanceState != null && !fromFavorite) {

            HelperMethods.showProgressBar(progressBar, false);
            loadImage(true);
            loadImage(false);

            movieDetail2 = savedInstanceState.getParcelable(MOVIE_DETAIL_2_KEY);
            castDetails = (ArrayList<CastDetails>) savedInstanceState.getSerializable(CAST_LIST_KEY);
            movieTrailers = (ArrayList<MovieTrailers>) savedInstanceState.getSerializable(TRAILERS__KEY);
            movieReviews = (ArrayList<MovieReviews>) savedInstanceState.getSerializable(REVIEWS__KEY);
            language = savedInstanceState.getString(LANGUAGE__KEY);
            favorite = savedInstanceState.getBoolean(FAVORITE_KEY);

            textView_movieTitle.setText(movieDetail.getOriginalTitle()); // Title is set here
            String date = HelperMethods.getdetiledDate(movieDetail.getReleaseDate());
            textView_releaseDate.setText(date);
            textView_rating.setText(String.valueOf(movieDetail.getVoteAverage()));
            textView_description.setText(movieDetail.getOverView());

            textView_movie_language.setText(language);


            if (favorite) {
                imageButton_favorite.setBackgroundResource(R.drawable.favorite_primary_24dp);
            } else {
                imageButton_favorite.setBackgroundResource(R.drawable.favorite_border_primary_24dp);
            }
            if( castBitmapBytes != null) {
                progressBarCasts.setVisibility(View.GONE);
            }


            //Check if the MovieDetails2 and ArrayList<CastDetails> is null or not
            if (movieDetail2 != null && castDetails != null && movieTrailers != null && movieReviews != null) {
                setUIComponentsUsingMovieDetails2(movieDetail2);
                setCastRecyclerView();
                setMovieTrailersRecyclerView();
                setMovieReviewsRecyclerView();
            } else {
                    setUIComponents();
            }

        }else if(!fromFavorite) {

            setUIComponents();
        }

    }// onCreate()


    @Override
    protected void onResume() {
        super.onResume();
        //shimmerFrameLayout.startShimmerAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //shimmerFrameLayout.stopShimmerAnimation();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mToast != null) {
            mToast.cancel();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (!fromFavorite){
            outState.putParcelable(MOVIE_DETAIL_KEY, movieDetail);
            outState.putParcelable(MOVIE_DETAIL_2_KEY, movieDetail2);
            outState.putSerializable(CAST_LIST_KEY, castDetails);
            outState.putSerializable(TRAILERS__KEY, movieTrailers);
            outState.putSerializable(REVIEWS__KEY, movieReviews);
            outState.putString(LANGUAGE__KEY, language);
            outState.putBoolean(FAVORITE_KEY, favorite);
        }
    }

    @OnClick(R.id.img_btn_back)
    public void onClick(){
        supportFinishAfterTransition();
    }

    @OnClick(R.id.img_btn_share)
    public void OnClickShare(View view) {
        StringBuilder builder = new StringBuilder();
        builder.append(movieDetail.getOriginalTitle())
                .append("\n")
                .append(textView_movie_language.getText().toString())
                .append(" Movie\n")
                .append("Release Date - ")
                .append(textView_releaseDate.getText().toString())
                .append("\n\n")
                .append("Overview-\n")
                .append(movieDetail.getOverView());
        if (movieTrailers.size() > 0) {
                Uri url = NetworkUtils.buildURL_for_youtube_link(movieTrailers.get(0).getKey());
                builder.append("\n\n")
                        .append("Trailer - ")
                        .append(url.toString());
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, builder.toString());
        Intent chooserIntent = Intent.createChooser(intent,"Share using");
        startActivity(chooserIntent);
    }

    @Override
    public void onTrailerClickListener(String key)
    {

        Uri url = NetworkUtils.buildURL_for_youtube_link(key);
        Intent intent = new Intent(Intent.ACTION_VIEW, url);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.img_btn_more_reviews:
            case R.id.cv_reviews:
                if (showAllReviews){
                    recyclerView_reviews.setVisibility(View.VISIBLE);

                    imageButton_more_reviews.animate()
                            .rotationBy(180f)
                            .setInterpolator(new LinearInterpolator())
                            .start();
                    showAllReviews = false;
                }
                else {
                    recyclerView_reviews.setVisibility(View.GONE);

                    imageButton_more_reviews.animate()
                            .rotationBy(-180f)
                            .setInterpolator(new LinearInterpolator())
                            .start();
                    showAllReviews = true;
                }
                break;
            case R.id.img_btn_favorite:

                if (fromFavorite) {

                    // Favourite MovieDetails Screen
                    // Delete From Database
                    imageButton_favorite.startAnimation(AnimationUtils.loadAnimation(this,R.anim.heart_bounce));
                    AppExecutors.getInstance().executorForDatabase().execute(new Runnable() {
                        @Override
                        public void run() {
                            movieDatabase.movieDao().deleteMovie(movieEntry);
                            movieDatabase.movieDao().deleteCasts(movieID);
                            movieDatabase.movieDao().deleteTrailers(movieID);
                            movieDatabase.movieDao().deleteReviews(movieID);
                        }
                    });

                    finish();
                }else {

                    // Normal MovieDetails Screen
                    if (favorite) {
                        favorite = false;
                        movieDetail.setFavorite(favorite);

                        if (movieEntry !=null) {

                            AppExecutors.getInstance().executorForDatabase().execute(new Runnable() {
                                @Override
                                public void run() {

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            imageButton_favorite.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.heart_bounce));
                                            imageButton_favorite.setBackgroundResource(R.drawable.favorite_border_primary_24dp);
                                            showToastMessage(R.drawable.favorite_border_accent_24dp,R.string.removed_from_favourite_list );
                                        }
                                    });
                                    // Delete from Database
                                    //movieEntry = movieDatabase.movieDao().findMovieEntry(movieID);
                                    movieDatabase.movieDao().deleteMovie(movieEntry);
                                    movieDatabase.movieDao().deleteCasts(movieID);
                                    movieDatabase.movieDao().deleteTrailers(movieID);
                                    movieDatabase.movieDao().deleteReviews(movieID);

                                }
                            });
                        }
                    } else {

                        // Database
                        if (movieDetail!=null && movieDetail2 !=null ) {

                            // Insert Movie Details into Database
                            loadCastDetailsIntoDatabase();

                            favorite = true;
                            movieDetail.setFavorite(favorite);

                        }else {
                            showToastMessage(R.drawable.progressbar_bk, R.string.please_wait_for_data_to_load);
                        }

                    }
                }
                break;
        }
    }


    /*
     **********************LoaderCallback METHODS**************************
     */

    // Loader CallBack methods
    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable final Bundle args) {

        if (id == MOVIE_DETAIL_QUERY_LOADER_ID){

            return new AsyncTaskLoader<MovieDetail2>(this) {
                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                }

                @Nullable
                @Override
                public MovieDetail2 loadInBackground() {
                    return null;

                }
            };

        }
        else if (id == MOVIE_LANGUAGE_QUERY_LOADER_ID){

            return new AsyncTaskLoader<String>(this) {
                @Nullable
                @Override
                public String loadInBackground() {
                    return null;
                }
            };

        }else if ( id == CAST_DETAILS_QUERY_LOADER_ID){
            return new AsyncTaskLoader<ArrayList<CastDetails>>(this) {
                @Nullable
                @Override
                public ArrayList<CastDetails> loadInBackground() {
                    return null;
                }
            };
        }else if ( id == TRAILERS_QUERY_LOADER_ID){
            return new AsyncTaskLoader<ArrayList< MovieTrailers>>(this) {
                @Nullable
                @Override
                public ArrayList<MovieTrailers> loadInBackground() {
                    return null;
                }
            };
        }else if( id == CAST_BITMAPS_QUERY_LOADER_ID){
            return new AsyncTaskLoader<List<Bitmap>>(this) {
                @Nullable
                @Override
                public List<Bitmap> loadInBackground() {
                    return null;
                }
            };
        } else if( id == CAST_ENTRIES_LOADER_ID){
            return new AsyncTaskLoader<Boolean>(this) {
                @Nullable
                @Override
                public Boolean loadInBackground() {
                    return null;
                }
            };
        }
        return new AsyncTaskLoader(this) {
            @Nullable
            @Override
            public Object loadInBackground() {
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    // MovieDetail2 LoaderCallback method
    private LoaderManager.LoaderCallbacks<MovieDetail2> movieDetailLoaderCallback() {

        return new LoaderManager.LoaderCallbacks<MovieDetail2>() {

            @SuppressLint("StaticFieldLeak")
            @NonNull
            @Override
            public Loader<MovieDetail2> onCreateLoader(int id, @Nullable final Bundle args) {

                return new AsyncTaskLoader<MovieDetail2>(getApplicationContext()) {

                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();
                        //Show ProgoressBar
                        //HelperMethods.showProgressBar(progressBar,true);
                        Log.d("Progress" , "1");
                    }

                    @Nullable
                    @Override
                    public MovieDetail2 loadInBackground() {

                        assert args != null;
                        int movieID = args.getInt(MOVIE_DETAIL_QUERY_EXTRA, -1);

                        if (movieID == -1) {
                            return null;
                        }

                        try {
                            URL url = NetworkUtils.buildURL_for_MovieDetail(movieID);
                            String jsonString = NetworkUtils.getResponceFromURl(url);
                            Log.d("URL: ", url.toString());
                            return JsonUtils.getMovieDetail2(jsonString);

                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }

                    }
                };
            }

            @Override
            public void onLoadFinished(@NonNull Loader<MovieDetail2> loader, MovieDetail2 data) {

                /*
                    Rubric
                    <Movie details layout contains title, release date, movie poster, vote average, and plot synopsis.>
                 */
                textView_movieTitle.setText(movieDetail.getOriginalTitle()); // Title is set here
                String date = HelperMethods.getdetiledDate(movieDetail.getReleaseDate());
                textView_releaseDate.setText(date);
                textView_rating.setText(String.valueOf(movieDetail.getVoteAverage()));
                textView_description.setText(movieDetail.getOverView());

                if (data != null) {
                    movieDetail2 = data;
                    textView_generes.setText("");

                    setUIComponentsUsingMovieDetails2(data);
                    HelperMethods.showProgressBar(progressBar,false);
                    //shimmerFrameLayout.setVisibility(View.GONE);
                    //shimmerFrameLayout.stopShimmerAnimation();
                }else {
                    //Network error code
                    showToastMessage(R.drawable.signal_wifi_off_accent_48dp, R.string.check_your_internet_connection);
                    HelperMethods.showProgressBar(progressBar,false);

                }

            }

            @Override
            public void onLoaderReset(@NonNull Loader<MovieDetail2> loader) {

            }

        };

    }

    // Movie Language LoaderCallback method
    private LoaderManager.LoaderCallbacks<String> languageLoaderCallback() {
        return new LoaderManager.LoaderCallbacks<String>() {
            @SuppressLint("StaticFieldLeak")
            @NonNull
            @Override
            public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {

                return new AsyncTaskLoader<String>(getApplicationContext()) {
                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();
                        //Show ProgoressBar
                        //HelperMethods.showProgressBar(progressBar,true);
                        Log.d("Progress" , "2");
                    }

                    @Nullable
                    @Override
                    public String loadInBackground() {

                        assert args != null;
                        String lang_code = args.getString(MOVIE_LANGUAGE_QUERY_EXTRA);

                        if (lang_code == null) {
                            return null;
                        }

                        try {
                            URL url = NetworkUtils.buildURL_for_movieLanguages();
                            String jsonString = NetworkUtils.getResponceFromURl(url);
                            Log.d("URL: ", url.toString());

                            // Get the list of languages (ISO 639-1 tags) used throughout TMDb, as a HashMap<String,String>
                            HashMap<String,String> languages = JsonUtils.getLanguages(jsonString);

                            assert languages != null;
                            String language = languages.get(lang_code);  // get the desired language
                            Log.d("MAP(lang): ",languages.get(lang_code));

                            return language;

                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }


                };
            }

            @Override
            public void onLoadFinished(@NonNull Loader<String> loader, String data) {

                if (data != null) {
                    // Display the movie language
                    textView_movie_language.setText(data);
                    language = data;
                    HelperMethods.showProgressBar(progressBar,false);
                }else {
                    // Netowork error code comes here
                    showToastMessage(R.drawable.signal_wifi_off_accent_48dp, R.string.check_your_internet_connection);
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<String> loader) {

            }
        };
    }

    //CastDetails LoaderCallback method
    private LoaderManager.LoaderCallbacks<ArrayList<CastDetails>> castDetailsLoaderCallbacks(){

        return new LoaderManager.LoaderCallbacks<ArrayList<CastDetails>>() {
            @SuppressLint("StaticFieldLeak")
            @NonNull
            @Override
            public Loader<ArrayList<CastDetails>> onCreateLoader(int id, @Nullable final Bundle args) {
                return new AsyncTaskLoader<ArrayList<CastDetails>>(getApplicationContext()) {

                    @Nullable
                    @Override
                    public ArrayList<CastDetails> loadInBackground() {

                        assert args != null;
                        int movieId = args.getInt(CAST_DETAILS_QUERY_EXTRA);

                        if (movieId < 0) {
                            return null;
                        }

                        try {
                            URL url = NetworkUtils.buildURL_for_castDetails(movieId);
                            String jsonString = NetworkUtils.getResponceFromURl(url);
                            Log.d("URL: ", url.toString());


                            return JsonUtils.getCastDetails(jsonString);

                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(@NonNull Loader<ArrayList<CastDetails>> loader, ArrayList<CastDetails> data) {

                if (data != null){

                    castDetails = data;
                    setCastRecyclerView();
                    // Load all Cast images as Bitmap
                    runCastBitmap_LoaderManager(castDetails);
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<ArrayList<CastDetails>> loader) {

            }
        };
    }

    // Movie Tailers LoaderCallback method
    private LoaderManager.LoaderCallbacks<ArrayList<MovieTrailers>> trailersLoaderCallbacks(){

        return new LoaderManager.LoaderCallbacks<ArrayList<MovieTrailers>>() {
            @SuppressLint("StaticFieldLeak")
            @NonNull
            @Override
            public Loader<ArrayList<MovieTrailers>> onCreateLoader(int id, @Nullable final Bundle args) {
                return new AsyncTaskLoader<ArrayList<MovieTrailers>>(getApplicationContext()) {
                    @Nullable
                    @Override
                    public ArrayList<MovieTrailers> loadInBackground() {
                        assert args != null;
                        int movieId = args.getInt(TRAILERS_QUERY_EXTRA);

                        if (movieId < 0) {
                            return null;
                        }

                        try {
                            URL url = NetworkUtils.buildURL_for_movieTrailers(movieId);
                            String jsonString = NetworkUtils.getResponceFromURl(url);
                            Log.d("URL: ", url.toString());

                            return JsonUtils.getMovieTrailers(jsonString);

                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(@NonNull Loader<ArrayList<MovieTrailers>> loader, ArrayList<MovieTrailers> data) {

                if (data != null){

                    movieTrailers = data;
                    setMovieTrailersRecyclerView();
                    runTrailersBitmap_LoaderManager(movieTrailers);
                }

            }

            @Override
            public void onLoaderReset(@NonNull Loader<ArrayList<MovieTrailers>> loader) {

            }
        };
    }

    // Movie Reviews LoaderCallback method
    private LoaderManager.LoaderCallbacks<ArrayList<MovieReviews>> reviewsLoaderCallbacks(){

        return new LoaderManager.LoaderCallbacks<ArrayList<MovieReviews>>() {
            @SuppressLint("StaticFieldLeak")
            @NonNull
            @Override
            public Loader<ArrayList<MovieReviews>> onCreateLoader(int id, @Nullable final Bundle args) {
                return new AsyncTaskLoader<ArrayList<MovieReviews>>(getApplicationContext()) {
                    @Nullable
                    @Override
                    public ArrayList<MovieReviews> loadInBackground() {
                        assert args != null;
                        int movieId = args.getInt(REVIEWS_QUERY_EXTRA);

                        if (movieId < 0) {
                            return null;
                        }

                        try {
                            URL url = NetworkUtils.buildURL_for_movieReviews(movieId);
                            String jsonString = NetworkUtils.getResponceFromURl(url);
                            Log.d("URL: ", url.toString());

                            return JsonUtils.getMovieReviews(jsonString);

                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(@NonNull Loader<ArrayList<MovieReviews>> loader, ArrayList<MovieReviews> data) {

                if (data != null){

                    movieReviews = data;
                    setMovieReviewsRecyclerView();
                }

            }

            @Override
            public void onLoaderReset(@NonNull Loader<ArrayList<MovieReviews>> loader) {

            }
        };
    }

    //Cast and Trailers Bitmaps LoaderCallback method
    private LoaderManager.LoaderCallbacks<List<Bitmap>> castBitmapBytesLoaderCallbacks(){

        return new LoaderManager.LoaderCallbacks<List<Bitmap>>() {
            @SuppressLint("StaticFieldLeak")
            @NonNull
            @Override
            public Loader<List<Bitmap>> onCreateLoader(int id, @Nullable final Bundle args) {
                return new AsyncTaskLoader<List<Bitmap>>(getApplicationContext()) {

                    @Nullable
                    @Override
                    public List<Bitmap> loadInBackground() {

                        assert args != null;
                        boolean isForCast = args.getBoolean(IS_FOR_CAST_BITMAPS_QUERY_EXTRA);

                        List<Bitmap> bitmaps;

                        if (isForCast){
                            if (castDetails == null && args.getSerializable(CAST_BITMAPS_QUERY_EXTRA) == null) {
                                return null;
                            }

                            bitmaps =  NetworkUtils.getCastsBitmap(castDetails);

                        }else {

                            if (castDetails == null && args.getSerializable(TRAILERS_BITMAPS_QUERY_EXTRA) == null) {
                                return null;
                            }

                            bitmaps = NetworkUtils.getTrailersBitmap(movieTrailers);
                        }


                        return bitmaps;

                    }
                };
            }

            @Override
            public void onLoadFinished(@NonNull Loader<List<Bitmap>> loader, List<Bitmap> data) {

                if (data != null){
                    int loaderID = loader.getId();

                    if (loaderID == CAST_BITMAPS_QUERY_LOADER_ID) {

                        castBitmapBytes = new ArrayList<>();
                        for (int i=0; i< data.size();i++){
                            castBitmapBytes.add(getBitmapAsByteArray(data.get(i)));
                        }
                        //castBitmapBytes = data;

                        progressBarCasts.setVisibility(View.GONE);
                        Log.d("loader","finished1");

                    }else if (loaderID == TRAILERS_BITMAPS_LOADER_ID){

                        trailerBitmapBytes = new ArrayList<>();

                        for (int i=0; i< data.size();i++){
                            trailerBitmapBytes.add(getBitmapAsByteArray(data.get(i)));
                        }
                        //trailerBitmapBytes = data;
                        Log.d("loader","finished2");
                    }

                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<List<Bitmap>> loader) {

            }
        };
    }

    //LoaderCallback method for loading Cast Details into Database
    private LoaderManager.LoaderCallbacks<Boolean> loadCastDetailsIntoDatabaseLoaderCallbacks(){

        return new LoaderManager.LoaderCallbacks<Boolean>() {
            @SuppressLint("StaticFieldLeak")
            @NonNull
            @Override
            public Loader<Boolean> onCreateLoader(int id, @Nullable final Bundle args) {
                return new AsyncTaskLoader<Boolean>(getApplicationContext()) {

                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();

                        imageButton_favorite.setBackgroundResource(R.drawable.favorite_primary_24dp);
                        imageButton_favorite.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.heart_bounce));
                    }

                    @Override
                    public Boolean loadInBackground() {

                        // && castBitmapBytes.size() > 0 && trailerBitmapBytes.size() > 0
                        if (castBitmapBytes != null && trailerBitmapBytes!=null){
                            // Loading Data for MovieDetails
                            loadMovieDetailsIntoDatabase();

                            loadCastsIntoDatabase();

                            //Loading Data for Trailers
                            loadTrailersIntoDatabase();

                            // Loading Data for Reviews
                            loadReviewsIntoDatabase();

                        }else if (castBitmapBytes == null && trailerBitmapBytes == null){

                            // Loading Data for MovieDetails
                            loadMovieDetailsIntoDatabase();

                            // Loading Data for Reviews
                            loadReviewsIntoDatabase();

                        }else if (castBitmapBytes != null && trailerBitmapBytes == null){

                            // Loading Data for MovieDetails
                            loadMovieDetailsIntoDatabase();

                            // Loading Data for Reviews
                            loadReviewsIntoDatabase();

                        }else if (trailerBitmapBytes != null && castBitmapBytes == null ){

                            // Loading Data for MovieDetails
                            loadMovieDetailsIntoDatabase();

                            // Loading Data for Reviews
                            loadReviewsIntoDatabase();

                        }


                        return true;

                    }
                };
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Boolean> loader, Boolean data) {

                if (data){
                    //imageButton_favorite.setBackgroundResource(R.drawable.favorite_primary_24dp);
                    //imageButton_favorite.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.heart_bounce));
                    showToastMessage(R.drawable.favorite_accent_24dp, R.string.added_to_fav_list);
                    Log.d("loader","finished");
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Boolean> loader) {

            }
        };
    }


    /*
     **********************HELPER METHODS**************************
     */

    //Helper Method to set UI Components - Favorite Movies Details & Normal Movie Details
    private void setUIComponents() {

        if (fromFavorite){

            // Favourite MovieDetails Screen

            imageButton_favorite.setBackgroundResource(R.drawable.favorite_primary_24dp);
            progressBarCasts.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);

        }else {

            HelperMethods.showProgressBar(progressBar, true);

            loadImage(true);
            loadImage(false);

            movieID = movieDetail.getId();
            movieLanguageCode = movieDetail.getOriginal_language();

            // Cache is enabled so as to obtain Bitmap later while saving in Database
            imageView_backdrop.setDrawingCacheEnabled(true);
            imageView_poster.setDrawingCacheEnabled(true);

            // Database
            FavMovieDetailsViewModelFactory viewModelFactory1 = new FavMovieDetailsViewModelFactory(movieDatabase, movieID);
            FavMovieDetailsViewModel movieDetailsViewModel = ViewModelProviders
                    .of(this, viewModelFactory1)
                    .get(FavMovieDetailsViewModel.class);
            movieDetailsViewModel.getMovieEntry().observe(this, new Observer<MovieEntry>() {
                @Override
                public void onChanged(@Nullable MovieEntry data) {
                    movieEntry = data;
                    favorite = movieEntry != null;
                        if (favorite) {
                            imageButton_favorite.setBackgroundResource(R.drawable.favorite_primary_24dp);
                        } else {
                            imageButton_favorite.setBackgroundResource(R.drawable.favorite_border_primary_24dp);
                        }
                }
            });


            // Loading Loaders to get movieDetail and Movie Language and Cast List
            runCastDetails_LoaderManager(movieID);
            runMovieDetail2_LoaderManager(movieID);
            runLanguageQuery_LoaderManager(movieLanguageCode);
            runMovieTrailers_LoaderManager(movieID);
            runMovieReviews_LoaderManager(movieID);
        }

    }

    /*
       HELPER METHOD TO SETUP VIEWMODEL
   */

    // Helper Method to set up ViewModel on MovieDetails screen - Normal Movie Details
    private  void setUpMovieDetailsViewModel(){
        MovieDetailsViewModelFactory viewModelFactory = new MovieDetailsViewModelFactory();
        viewModelFactory.loadIntoCast(castBitmapBytes);
        viewModelFactory.loadIntoTrailers(trailerBitmapBytes);
        MovieDetailsViewModel movieDetailsViewModel = ViewModelProviders.of(this,viewModelFactory).get(MovieDetailsViewModel.class);
        movieDetailsViewModel.getCastBitmapBytes().observe(this, new Observer<List<byte[]>>() {
            @Override
            public void onChanged(@Nullable List<byte[]> bytes) {
                castBitmapBytes = bytes;
            }
        });
        movieDetailsViewModel.getTrailerBitmapBytes().observe(this, new Observer<List<byte[]>>() {
            @Override
            public void onChanged(@Nullable List<byte[]> bytes) {
                trailerBitmapBytes = bytes;
            }
        });
    }

    // Helper Method to set up ViewModel on  Favourite MovieDetails screen - Favorite Movies Details
    private void setUpFavMovieViewModel() {

        FavMovieDetailsViewModelFactory viewModelFactory = new FavMovieDetailsViewModelFactory(movieDatabase, movieID);
        FavMovieDetailsViewModel movieDetailsViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(FavMovieDetailsViewModel.class);
        movieDetailsViewModel.getMovieEntry().observe(this, new Observer<MovieEntry>() {
            @Override
            public void onChanged(@Nullable MovieEntry data) {
                movieEntry = data;
                setFavUIMovieEntry();
                Log.d("MovieDetailsViewModel","movieEntry");
            }
        });
        movieDetailsViewModel.getCastEntries().observe(this, new Observer<List<CastEntry>>() {
            @Override
            public void onChanged(@Nullable List<CastEntry> data) {
                castEntries = data;
                Log.d("MovieDetailsViewModel","casts");
                setFavUICastEntries();
            }
        });
        movieDetailsViewModel.getReviewsEntries().observe(this, new Observer<List<ReviewsEntry>>() {
            @Override
            public void onChanged(@Nullable List<ReviewsEntry> data) {
                reviewsEntries = data;
                setFavUIReviewEntries();
                Log.d("MovieDetailsViewModel","reviews");
            }
        });
        movieDetailsViewModel.getTrailerEntries().observe(this, new Observer<List<TrailerEntry>>() {
            @Override
            public void onChanged(@Nullable List<TrailerEntry> data) {
                trailerEntries = data;
                setFavUITrailerEntries();
                Log.d("MovieDetailsViewModel","trailers");
            }
        });
    }

    /*
       HELPER METHOD TO SETUP FAV. MOVIES ENTRIES - Favorite Movies Details
   */

    // Favourite MovieDetails Screen
    private void setFavUIReviewEntries() {
        ArrayList<MovieReviews> movieReviews = new ArrayList<>();
        for (int i = 0; i < reviewsEntries.size(); i++) {
            MovieReviews reviews = new MovieReviews();
            reviews.setMovie_id(reviewsEntries.get(i).getMovieID());
            reviews.setContent(reviewsEntries.get(i).getContent());
            reviews.setAuthor(reviewsEntries.get(i).getAuthor());
            reviews.setReview_url(reviewsEntries.get(i).getReviewURL());
            movieReviews.add(reviews);
        }

        this.movieReviews = movieReviews;
        setMovieReviewsRecyclerView();
    }

    // Favourite MovieDetails Screen
    private void setFavUITrailerEntries() {
        ArrayList<MovieTrailers> movieTrailers = new ArrayList<>();
        for (int i = 0; i < trailerEntries.size(); i++) {
            MovieTrailers trailer= new MovieTrailers();
            trailer.setMovie_id(trailerEntries.get(i).getMovieId());
            trailer.setVideo_id(trailerEntries.get(i).getVideoId());
            trailer.setKey(trailerEntries.get(i).getKey());
            trailer.setName(trailerEntries.get(i).getName());
            trailer.setSite(trailerEntries.get(i).getSite());
            trailer.setType(trailerEntries.get(i).getType());
            trailer.setVideoThumbnail(trailerEntries.get(i).getVideoThumbnail());

            movieTrailers.add(trailer);
        }

        this.movieTrailers = movieTrailers;
        setMovieTrailersRecyclerView();
    }

    // Favourite MovieDetails Screen
    private void setFavUICastEntries() {
        ArrayList<CastDetails> castDetails = new ArrayList<>();
        for (int i = 0; i < castEntries.size(); i++) {
            CastDetails castDetail = new CastDetails();
            castDetail.setCharacter(castEntries.get(i).getCharacter());
            castDetail.setName(castEntries.get(i).getName());
            castDetail.setOrder(castEntries.get(i).getOrder());
            castDetail.setGender(castEntries.get(i).getGender());
            castDetail.setProfilePic(castEntries.get(i).getProfilePic());

            castDetails.add(castDetail);
        }

        this.castDetails = castDetails;
        setCastRecyclerView();
    }

    // Favourite MovieDetails Screen
    private void setFavUIMovieEntry() {
        if (movieEntry != null){
            imageView_poster.setImageBitmap(BitmapFactory.decodeByteArray(movieEntry.getPoster(), 0, movieEntry.getPoster().length));
            imageView_backdrop.setImageBitmap(BitmapFactory.decodeByteArray(movieEntry.getBackDrop(), 0, movieEntry.getBackDrop().length));
            textView_movieTitle.setText(movieEntry.getMovieTitle());
            textView_movie_language.setText(movieEntry.getMovie_language());
            textView_releaseDate.setText(movieEntry.getReleaseDate());
            textView_rating.setText(String.valueOf(movieEntry.getVoteAverage()));
            textView_tagline.setText(movieEntry.getTagline());
            textView_description.setText(movieEntry.getOverView());
            for (int i = 0; i < movieEntry.getGenres().size(); i++) {
                textView_generes.append(movieEntry.getGenres().get(i) + " ");
            }
        }

    }

    /*
       HELPER METHOD TO LOAD IMAGES - Normal Movie Details
   */

    //Helper Method to load Images
    @SuppressLint("StaticFieldLeak")
    private void loadImage(boolean backDrop) {

        if (backDrop) {

            String path = movieDetail.getBackDrop_path();
            String url = NetworkUtils.buildURL_for_Image(path, true).toString();

            int width = getDisplaySize();
            int height = (int) getResources().getDimension(R.dimen.movie_backDrop_height);

            loadGlide(imageView_backdrop,url);


        } else {

            String path = movieDetail.getPoster_path();
            String url = NetworkUtils.buildURL_for_Image(path, false).toString();

            int width = (int) getResources().getDimension(R.dimen.movie_poster_width);
            int height = (int) getResources().getDimension(R.dimen.movie_poster_height);

            loadGlide(imageView_poster, url);

           /* Picasso.with(this)
                    .load(url)
                    .resize(width, height)
                    .error(getResources().getDrawable(R.drawable.error_bk))
                    .into(imageView_poster, new Callback() {
                        @Override
                        public void onSuccess() {
                            supportStartPostponedEnterTransition();
                        }

                        @Override
                        public void onError() {
                            supportStartPostponedEnterTransition();
                        }
                    });*/
        }
    }

    private void loadGlide(ImageView imageView, String url){

        RequestOptions options = new RequestOptions()
                .error(R.drawable.error_bk)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(getApplicationContext())
                .load(url)
                .apply(options)
                .into(imageView);
    }

    /*
       HELPER METHOD TO RUN LOADER MANAGERS - Normal Movie Details
   */

    // Helper Method to start Loader for MovieDetails2
    private void runMovieDetail2_LoaderManager(int movieID) {

        // Parsing data into bundle
        Bundle movieDetailBundle = new Bundle();
        movieDetailBundle.putInt(MOVIE_DETAIL_QUERY_EXTRA, movieID);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Integer> movieLoader = loaderManager.getLoader(MOVIE_DETAIL_QUERY_LOADER_ID);

        // Check if loader is available already
        if (movieLoader == null) {
            /*loaderManager.initLoader(MOVIE_DETAIL_QUERY_LOADER_ID, movieDetailBundle, this).forceLoad();*/
            getSupportLoaderManager().initLoader(MOVIE_DETAIL_QUERY_LOADER_ID,movieDetailBundle,movieDetailLoaderCallback()).forceLoad();
        } else {
            /*loaderManager.restartLoader(MOVIE_DETAIL_QUERY_LOADER_ID, movieDetailBundle, this).forceLoad();*/
            getSupportLoaderManager().restartLoader(MOVIE_DETAIL_QUERY_LOADER_ID,movieDetailBundle,movieDetailLoaderCallback()).forceLoad();
        }

    }

    // Helper Method to start Loader for Movie Language
    private void runLanguageQuery_LoaderManager(String movieLanguageCode) {

        // Parsing data into bundle
        Bundle movieLangBundle = new Bundle();
        movieLangBundle.putString(MOVIE_LANGUAGE_QUERY_EXTRA , movieLanguageCode);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> movieLanguageLoader = loaderManager.getLoader(MOVIE_LANGUAGE_QUERY_LOADER_ID);

        // Check if loader is available already
        if (movieLanguageLoader == null) {
            getSupportLoaderManager().initLoader(MOVIE_LANGUAGE_QUERY_LOADER_ID,
                                        movieLangBundle,
                                        languageLoaderCallback()).forceLoad();
        } else {
            getSupportLoaderManager().restartLoader(MOVIE_LANGUAGE_QUERY_LOADER_ID,
                                        movieLangBundle,
                                        languageLoaderCallback()).forceLoad();
        }

    }

    // Helper Method to start Loader for Cast List
    private void runCastDetails_LoaderManager(int movieID){

        // Parsing data into bundle
        Bundle castDetailsBundle = new Bundle();
        castDetailsBundle.putInt(CAST_DETAILS_QUERY_EXTRA, movieID);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Integer> castDetailsLoader = loaderManager.getLoader(CAST_DETAILS_QUERY_LOADER_ID);

        // Check if loader is available already
        if (castDetailsLoader == null) {
            getSupportLoaderManager().initLoader(CAST_DETAILS_QUERY_LOADER_ID,
                    castDetailsBundle,
                    castDetailsLoaderCallbacks()).forceLoad();
        } else {
            getSupportLoaderManager().restartLoader(CAST_DETAILS_QUERY_LOADER_ID,
                    castDetailsBundle,
                    castDetailsLoaderCallbacks()).forceLoad();
        }
    }

    // Helper Method to start Loader for Movie Trailers
    private void runMovieTrailers_LoaderManager(int movieID){

        // Parsing data into bundle
        Bundle movieTrailersBundle = new Bundle();
        movieTrailersBundle.putInt(TRAILERS_QUERY_EXTRA, movieID);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Integer> movieTrailersLoader = loaderManager.getLoader(TRAILERS_QUERY_LOADER_ID);

        // Check if loader is available already
        if (movieTrailersLoader == null) {
            getSupportLoaderManager().initLoader(TRAILERS_QUERY_LOADER_ID,
                    movieTrailersBundle,
                    trailersLoaderCallbacks()).forceLoad();
        } else {
            getSupportLoaderManager().restartLoader(TRAILERS_QUERY_LOADER_ID,
                    movieTrailersBundle,
                    trailersLoaderCallbacks()).forceLoad();
        }
    }

    // Helper Method to start Loader for Movie Trailers
    private void runMovieReviews_LoaderManager(int movieID){
        // Parsing data into bundle
        Bundle movieReviewsBundle = new Bundle();
        movieReviewsBundle.putInt(REVIEWS_QUERY_EXTRA, movieID);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Integer> movieReviewsLoader = loaderManager.getLoader(REVIEWS_QUERY_LOADER_ID);

        if (movieReviewsLoader == null){
            getSupportLoaderManager().initLoader(REVIEWS_QUERY_LOADER_ID,
                        movieReviewsBundle,
                        reviewsLoaderCallbacks()).forceLoad();
        }else {
            getSupportLoaderManager().initLoader(REVIEWS_QUERY_LOADER_ID,
                        movieReviewsBundle,
                        reviewsLoaderCallbacks()).forceLoad();
        }
    }

    // Helper Method to start Loader for Cast Bitmap loading
    private void runCastBitmap_LoaderManager(ArrayList<CastDetails> castDetailsList){

        // Parsing data into bundle
        Bundle castBitmapBytesBundle = new Bundle();
        castBitmapBytesBundle.putSerializable(CAST_BITMAPS_QUERY_EXTRA, castDetailsList);
        castBitmapBytesBundle.putBoolean(IS_FOR_CAST_BITMAPS_QUERY_EXTRA, true);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<CastDetails> castDetailsLoader = loaderManager.getLoader(CAST_BITMAPS_QUERY_LOADER_ID);

        // Check if loader is available already
        if (castDetailsLoader == null) {
            getSupportLoaderManager().initLoader(CAST_BITMAPS_QUERY_LOADER_ID,
                    castBitmapBytesBundle,
                    castBitmapBytesLoaderCallbacks()).forceLoad();
        } else {
            getSupportLoaderManager().restartLoader(CAST_BITMAPS_QUERY_LOADER_ID,
                    castBitmapBytesBundle,
                    castBitmapBytesLoaderCallbacks()).forceLoad();
        }

    }

    // Helper Method to start Loader for Trailers Bitmap loading
    private void runTrailersBitmap_LoaderManager(ArrayList<MovieTrailers> movieTrailers){

        // Parsing data into bundle
        Bundle trailerBitmapBytesBundle = new Bundle();
        trailerBitmapBytesBundle.putSerializable(TRAILERS_BITMAPS_QUERY_EXTRA, movieTrailers);
        trailerBitmapBytesBundle.putBoolean(IS_FOR_CAST_BITMAPS_QUERY_EXTRA,false);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<MovieTrailers> movieTrailersLoader = loaderManager.getLoader(TRAILERS_BITMAPS_LOADER_ID);

        // Check if loader is available already
        if (movieTrailersLoader == null) {
            getSupportLoaderManager().initLoader(TRAILERS_BITMAPS_LOADER_ID,
                    trailerBitmapBytesBundle,
                    castBitmapBytesLoaderCallbacks()).forceLoad();
        } else {
            getSupportLoaderManager().restartLoader(TRAILERS_BITMAPS_LOADER_ID,
                    trailerBitmapBytesBundle,
                    castBitmapBytesLoaderCallbacks()).forceLoad();
        }

    }

    // Helper Method to start Loader for Trailers Bitmap loading
    private void loadCastDetailsIntoDatabase(){

        // Parsing data into bundle
        /*Bundle castEntriesBundle = new Bundle();
        castEntriesBundle.putBoolean(CAST_ENTRIES_QUERY_EXTRA,true );*/

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Boolean> castEntriesLoader = loaderManager.getLoader(CAST_ENTRIES_LOADER_ID);

        // Check if loader is available already
        if (castEntriesLoader == null) {
            getSupportLoaderManager().initLoader(CAST_ENTRIES_LOADER_ID,
                    null,
                    loadCastDetailsIntoDatabaseLoaderCallbacks()).forceLoad();
        } else {
            getSupportLoaderManager().restartLoader(CAST_ENTRIES_LOADER_ID,
                    null,
                    loadCastDetailsIntoDatabaseLoaderCallbacks()).forceLoad();
        }

    }

    /*
       HELPER METHOD TO SETUP RECYCLERVIEW - Favorite Movies Details & Normal Movie Details
   */

    //Helper Method to set RecyclerView for Cast List
    private void setCastRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),0,false);

        recyclerView_casts.setLayoutManager(linearLayoutManager);
        recyclerView_casts.setHasFixedSize(true);
        castListAdapter = new CastListAdapter(castDetails,castDetails.size(), fromFavorite);
        recyclerView_casts.setAdapter(castListAdapter);
    }

    //Helper Method to set RecyclerView for Movie Trailers
    private void setMovieTrailersRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),0,false);

        recyclerView_trailers.setLayoutManager(linearLayoutManager);
        recyclerView_trailers.setHasFixedSize(true);
        trailersAdapter = new TrailersAdapter(movieTrailers, movieTrailers.size(), this, fromFavorite);
        recyclerView_trailers.setAdapter(trailersAdapter);
    }

    //Helper Method to set RecyclerView for Movie Reviews
    private void setMovieReviewsRecyclerView() {

        if (movieReviews.size()>0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), 1, false);
            recyclerView_reviews.setLayoutManager(linearLayoutManager);
            recyclerView_reviews.setHasFixedSize(true);
            reviewsAdapter = new ReviewsAdapter(movieReviews, movieReviews.size(), this);
            textView_total_reviews.setText(String.valueOf(movieReviews.size()));
            recyclerView_reviews.setAdapter(reviewsAdapter);
        }else {
            textView_total_reviews.setText(R.string.no_reviews);
            imageButton_more_reviews.setVisibility(View.GONE);
        }
    }

    //Helper Method to set UI Components using MovieDetails2 object
    private void setUIComponentsUsingMovieDetails2(MovieDetail2 data) {

        if (null == data){
            return;
        }
        if (data.getGenres() != null) {
            //Displaying genres from MovieDetail2
            for (int i = 0; i < data.getGenres().size(); i++) {
                textView_generes.append(data.getGenres().get(i) + " ");
            }

        } else {
            textView_generes.setText(R.string.no_data_available);
        }

        if (data.getTagline() != null && !data.getTagline().equals("")) {
            //Displaying Tagline from MovieDetail2
            textView_tagline.setText(data.getTagline());
        } else {
            textView_tagline.setText(R.string.no_tagline_available);
        }
    }

     /*
       HELPER METHOD FOR UTILS - Favorite Movies Details & Normal Movie Details
   */

    // Helper Method to get the Display width
    public int getDisplaySize(){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return pxToDp(displayMetrics.widthPixels);
    }

    // Helper Metho to get the px to dp
    public int pxToDp(int px){
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    //Helper Method to convert Bitmap into ByteArray
    public static byte[] getBitmapAsByteArray(Bitmap bitmap){
        if (bitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            return outputStream.toByteArray();
        }
        return null;
    }

    //Helper Method to convert Byte into String
    public static String getByteAsString(byte[] bitmap){
        if (bitmap != null) {
            return Base64.encodeToString(bitmap, Base64.DEFAULT);
        }
        return null;
    }

    // Helper Method to show a Network error Toast message
    private void showToastMessage(int drawableID, int messageID) {


        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup)findViewById(R.id.customToast));
        TextView textView = layout.findViewById(R.id.toast_txt);
        textView.setText(messageID);
        ImageView imageView = layout.findViewById(R.id.toast_img);
        imageView.setBackgroundResource(drawableID);


        if (mToast != null){
            mToast.cancel();
            mToast = new Toast(this);
            mToast.setGravity(Gravity.BOTTOM, 0, 0);
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.setView(layout);
            mToast.show();
        }else {
            mToast = new Toast(this);
            mToast.setGravity(Gravity.BOTTOM, 0, 0);
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.setView(layout);
            mToast.show();
        }
    }

    /*
        HELPER METHOD TO LOAD DATA INTO DATABASE - Normal Movie Details
    */
    private void loadReviewsIntoDatabase() {
        final List<ReviewsEntry> reviewsEntry = new ArrayList<>();
        for (int i=0; i<movieReviews.size(); i++){
            ReviewsEntry entry = new ReviewsEntry(movieID, movieReviews.get(i).getAuthor(),
                                                            movieReviews.get(i).getContent(),
                                                            movieReviews.get(i).getReview_url());
            reviewsEntry.add(entry);
        }

        movieDatabase.movieDao().insertReviews(reviewsEntry);
    }

    private void loadTrailersIntoDatabase() {
        final List<TrailerEntry> trailerEntries = new ArrayList<>();
        if (trailerBitmapBytes != null &&  trailerBitmapBytes.size() > 0) {
            for (int i = 0; i < movieTrailers.size(); i++) {

                if (trailerBitmapBytes.get(i) != null) {
                    TrailerEntry entry = new TrailerEntry(movieID, movieTrailers.get(i).getVideo_id(),
                            movieTrailers.get(i).getKey(), movieTrailers.get(i).getName(),
                            movieTrailers.get(i).getSite(), movieTrailers.get(i).getType(),
                            trailerBitmapBytes.get(i));
                    trailerEntries.add(entry);
                } else {
                    TrailerEntry entry = new TrailerEntry(movieID, movieTrailers.get(i).getVideo_id(),
                            movieTrailers.get(i).getKey(), movieTrailers.get(i).getName(),
                            movieTrailers.get(i).getSite(), movieTrailers.get(i).getType(),
                            null);
                    trailerEntries.add(entry);
                }
            }
        }

        movieDatabase.movieDao().insertTrailers(trailerEntries);
    }

   private void loadCastsIntoDatabase() {
        final List<CastEntry> castEntries = new ArrayList<>();
        if(castBitmapBytes != null && castBitmapBytes.size() > 0) {
            for (int i = 0; i < castDetails.size(); i++) {
                if (castBitmapBytes.get(i) != null) {
                    CastEntry entry = new CastEntry(movieID,
                            castDetails.get(i).getName(),
                            castDetails.get(i).getCharacter(),
                            castDetails.get(i).getGender(),
                            castDetails.get(i).getOrder(),
                            castBitmapBytes.get(i));
                    castEntries.add(entry);
                } else {
                    CastEntry entry = new CastEntry(movieID,
                            castDetails.get(i).getName(),
                            castDetails.get(i).getCharacter(),
                            castDetails.get(i).getGender(),
                            castDetails.get(i).getOrder(),
                            null);
                    castEntries.add(entry);
                }
            }
        }

       movieDatabase.movieDao().insertCasts(castEntries);

    }

    private void loadMovieDetailsIntoDatabase() {
        Bitmap bitmap_poster = imageView_poster.getDrawingCache();
        Bitmap bitmap_backDrop = imageView_backdrop.getDrawingCache();
        final MovieEntry movieEntry = new MovieEntry(movieID, movieDetail.getOriginalTitle(), language,
                movieDetail.getReleaseDate(), movieDetail.getOverView(), movieDetail2.getTagline(),
                movieDetail2.getGenres(), movieDetail.getVoteAverage(),
                getBitmapAsByteArray(bitmap_poster), getBitmapAsByteArray(bitmap_backDrop));

        movieDatabase.movieDao().insertMovie(movieEntry);
    }
}