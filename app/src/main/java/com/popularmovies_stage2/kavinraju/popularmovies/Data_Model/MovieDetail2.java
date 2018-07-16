package com.popularmovies_stage2.kavinraju.popularmovies.Data_Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MovieDetail2 implements Parcelable {

    private ArrayList<String> genres;
    private String original_language;
    private String tagline;

    public MovieDetail2(){

    }

    public MovieDetail2(ArrayList<String> genres, String original_language, String tagline) {
        this.genres = genres;
        this.original_language = original_language;
        this.tagline = tagline;
    }

    private MovieDetail2(Parcel in) {
        genres = in.createStringArrayList();
        original_language = in.readString();
        tagline = in.readString();
    }

    public static final Creator<MovieDetail2> CREATOR = new Creator<MovieDetail2>() {
        @Override
        public MovieDetail2 createFromParcel(Parcel in) {
            return new MovieDetail2(in);
        }

        @Override
        public MovieDetail2[] newArray(int size) {
            return new MovieDetail2[size];
        }
    };

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(genres);
        dest.writeString(original_language);
        dest.writeString(tagline);
    }


    public static final Parcelable.Creator<MovieDetail2> creator = new Parcelable.Creator<MovieDetail2>() {
        @Override
        public MovieDetail2 createFromParcel(Parcel source) {
            return new MovieDetail2(source);
        }

        @Override
        public MovieDetail2[] newArray(int size) {
            return new MovieDetail2[size];
        }
    };
}
