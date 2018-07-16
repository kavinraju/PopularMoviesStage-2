package com.popularmovies_stage2.kavinraju.popularmovies.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.popularmovies_stage2.kavinraju.popularmovies.Data_Model.CastDetails;
import com.popularmovies_stage2.kavinraju.popularmovies.Data_Model.MovieTrailers;
import com.popularmovies_stage2.kavinraju.popularmovies.MovieDetailsActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NetworkUtils {


    private static String BASE_URL_THE_MOVIE_DB = "https://api.themoviedb.org/3";  // This is the base URL
    private static String MOVIE = "movie";   // This is used as path for movie
    private static String API_KEY = "api_key";  // This is used as parameter key
    private static String LANGUAGE = "language";    // This is used as parameter key
    private static String LANGUAGES = "languages";    // This is used as parameter key
    private static String CONFIGURATION = "configuration";  // This is used as parameter key
    private static String PAGE = "page";    // This is used as parameter key
    private static String CREDITS = "credits";    // This is used as parameter key
    private static String REVIEWS = "reviews";    // This is used as parameter key
    private static String VIDEOS = "videos";    // This is used as parameter key
    private static String api_key = "YOUR_API_KEY"; // This is used as parameter value of api_key
    private static String language_english = "en-US";   // This is used as parameter value of language
    private static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p";
    private static String W_185 = "w185";
    private static String W_500 = "w780";

    private static String BASE_URL_YOUTUBE = "http://www.youtube.com";  // This is the base URL for youtube
    private static String BASE_URL_YOUTUBE_THUMBNAIL ="https://img.youtube.com";  // This is the base URL for youtube thumbnail
    private static String WATCH = "watch";   // This is used as path for watch
    private static String V = "v";   // This is used as query parameter for key
    private static String VI = "vi";   // This is used as parameter
    private static String JPG = "0.jpg"; // This is used as parameter



    /*
        Rubric
        <In a background thread, app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu>
     */
    // Helper Method to build URL for List of Movies using Movie Rating Type ( Sort Order ) and Page Num.
    public static URL buildURL_for_MoivesList(String movie_rating_type, int page){

        /*
        My intension is to generate URL of these types -
                https://api.themoviedb.org/3/movie/popular?api_key=<<api_key>>&language=en-US&page=1
                https://api.themoviedb.org/3/movie/top_rated?api_key=<<api_key>>&language=en-US&page=1
         */
        Uri uri = Uri.parse(BASE_URL_THE_MOVIE_DB).buildUpon() /* https://api.themoviedb.org/3 */
                    .appendPath(MOVIE)  /* /movie */
                    .appendPath(movie_rating_type)  /* /popular */
                    .appendQueryParameter(API_KEY , api_key)  /* ?api_key=<<api_key>> */
                    .appendQueryParameter(LANGUAGE , language_english)  /* &language=en-US */
                    .appendQueryParameter(PAGE , String.valueOf(page))  /* &page=1 */
                    .build();
        URL url = null;

        try{
            url = new URL(uri.toString());
            Log.d("NetworkUtils: URL: " , url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    // Helper Method to build URL for Image
    public static URL buildURL_for_Image(String path, boolean backdrop){

        /*
        We do this because by default path be like “ /nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg ”.
        Hence if we build URL with this we will have a extra space character %20 in the URL which leads to error.
        */

        path = path.replace("/","");

        Uri uri;

        if (backdrop){

            uri = Uri.parse(BASE_IMAGE_URL).buildUpon()
                    .appendPath(W_500) // getting image of size w500
                    .appendPath(path)
                    .build();

        }else {
            uri = Uri.parse(BASE_IMAGE_URL).buildUpon()
                    .appendPath(W_185)  // getting image of size w185
                    .appendPath(path)
                    .build();
        }

        URL url = null;
        try{
            url = new URL(uri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    // Helper Method to build URL for Movie Detail using passed Movie ID
    public static URL buildURL_for_MovieDetail(int id){

        Uri uri = Uri.parse(BASE_URL_THE_MOVIE_DB).buildUpon()
                        .appendPath(MOVIE)
                        .appendPath(String.valueOf(id))
                        .appendQueryParameter(API_KEY , api_key)
                        .appendQueryParameter(LANGUAGE , language_english)
                        .build();
        URL url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    // Helper Method to build URL for lists of Language
    public static URL buildURL_for_movieLanguages(){

        Uri uri = Uri.parse(BASE_URL_THE_MOVIE_DB).buildUpon()
                .appendPath(CONFIGURATION)
                .appendPath(LANGUAGES)
                .appendQueryParameter(API_KEY , api_key)
                .build();
        URL url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    // Helper Method to build URL for lists of Cast Details
    public static URL buildURL_for_castDetails(int movieID){

        Uri uri = Uri.parse(BASE_URL_THE_MOVIE_DB).buildUpon()
                .appendPath(MOVIE)
                .appendPath(String.valueOf(movieID))
                .appendPath(CREDITS)
                .appendQueryParameter(API_KEY , api_key)
                .build();
        URL url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    // Helper Method to build URL for movie reviews
    public static URL buildURL_for_movieReviews(int movieID){

        Uri uri = Uri.parse(BASE_URL_THE_MOVIE_DB).buildUpon()
                    .appendPath(MOVIE)
                    .appendPath(String.valueOf(movieID))
                    .appendPath(REVIEWS)
                    .appendQueryParameter(API_KEY , api_key)
                    .build();
        URL url = null;
        try{
            url = new URL(uri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    // Helper Method to build URL for movie trailers
    public static URL buildURL_for_movieTrailers(int movieID){

        Uri uri = Uri.parse(BASE_URL_THE_MOVIE_DB).buildUpon()
                    .appendPath(MOVIE)
                    .appendPath(String.valueOf(movieID))
                    .appendPath(VIDEOS)
                    .appendQueryParameter(API_KEY, api_key)
                    .build();
        URL url = null;
        try{
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    // Helper Method to build URL for Video Thumbnail
    public static URL buildURL_for_videoThumbnail(String key){

        Uri uri = Uri.parse(BASE_URL_YOUTUBE_THUMBNAIL).buildUpon()
                .appendPath(VI)
                .appendPath(key)
                .appendPath(JPG)
                .build();
        URL url = null;
        try{
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    // Helper Method to build URL for YouTube Link
    public static Uri buildURL_for_youtube_link(String key){

        /* URL url = null;
        try{
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }*/
        return Uri.parse(BASE_URL_YOUTUBE).buildUpon()
                    .appendPath(WATCH)
                    .appendQueryParameter(V,key)
                    .build();
    }

    // Helper Method to get Cast Bitmaps
    public static List<Bitmap> getCastsBitmap(ArrayList<CastDetails> castDetails){

        List<Bitmap> castPicBitmap = new ArrayList<>();
        for (int i = 0; i < castDetails.size(); i++){
            String path = castDetails.get(i).getProfile_path();
            String img_url = NetworkUtils.buildURL_for_Image(path, false).toString();
            try {
                URL url = new URL(img_url);
                InputStream inputStream = url.openConnection().getInputStream();
                if (inputStream != null) {
                    castPicBitmap.add(BitmapFactory.decodeStream(inputStream));
                }else {
                    castPicBitmap.add(null);
                    Log.d("bitmap","null");
                }
            } catch(IOException e) {
                System.out.println(e);
                castPicBitmap.add(null);
                Log.d("bitmap","null");
            }
        }
        return castPicBitmap;
    }

    // Helper Method to get Trailers Bitmaps
    public static List<Bitmap> getTrailersBitmap(ArrayList<MovieTrailers> movieTrailers){

        List<Bitmap> trailerPicBitmaps = new ArrayList<>();
        for (int i = 0; i < movieTrailers.size(); i++){
            String img_url = NetworkUtils.buildURL_for_videoThumbnail(movieTrailers.get(i).getKey()).toString();
            try {
                URL url = new URL(img_url);
                InputStream inputStream = url.openConnection().getInputStream();
                if (inputStream != null) {
                    trailerPicBitmaps.add(BitmapFactory.decodeStream(inputStream));
                }

            } catch(IOException e) {
                System.out.println(e);
                trailerPicBitmaps.add(null);
                Log.d("bitmap","null");
            }
        }
        return trailerPicBitmaps;
    }
        // Helper Method to get responce from the passed URL
    public static String getResponceFromURl(URL url) throws IOException {

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                            .url(url)
                            .build();

        Response response = okHttpClient.newCall(request).execute();

        assert response.body() != null;
        return response.body().string();
    }

}