package srilasaka_developers.skr.kavinraju.movie_reel.Database;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;

public class Converters {

    @TypeConverter
    public static ArrayList<String> toArrayListGenres(String genres){

        String[] genresArray = genres.split(" ");
        return new ArrayList<>(Arrays.asList(genresArray));
    }

    @TypeConverter
    public static String toStringGenres(ArrayList<String> arrayListGenres){
        StringBuilder genres = new StringBuilder();
        for (String genre:arrayListGenres){
            genres.append(genre).append(" ");
        }
        return genres.toString();
    }
}
