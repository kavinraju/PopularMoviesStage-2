package com.popularmovies_stage2.kavinraju.popularmovies.Data_Model;


import android.os.Parcel;
import android.os.Parcelable;

public class MovieDetail implements Parcelable {

    private int page;
    private int id;
    private double voteAverage;
    private String originalTitle;
    private String original_language;
    private String releaseDate;
    private String overView;
    private String poster_path;
    private String backDrop_path;
    private int totalPages;
    private boolean favorite;


    public  MovieDetail(){

    }

    public MovieDetail(int page, int id, double voteAverage,  String originalTitle, String original_language, String releaseDate, String overView, String poster_path, String backDrop_path, int totalPages, boolean favorite) {
        this.page = page;
        this.id = id;
        this.voteAverage = voteAverage;
        this.originalTitle = originalTitle;
        this.original_language = original_language;
        this.releaseDate = releaseDate;
        this.overView = overView;
        this.poster_path = poster_path;
        this.backDrop_path = backDrop_path;
        this.totalPages = totalPages;
        this.favorite = favorite;
    }


    private MovieDetail(Parcel in) {
        page = in.readInt();
        id = in.readInt();
        voteAverage = in.readDouble();
        originalTitle = in.readString();
        original_language = in.readString();
        releaseDate = in.readString();
        overView = in.readString();
        poster_path = in.readString();
        backDrop_path = in.readString();
        totalPages = in.readInt();
    }

    public static final Creator<MovieDetail> CREATOR = new Creator<MovieDetail>() {
        @Override
        public MovieDetail createFromParcel(Parcel in) {
            return new MovieDetail(in);
        }

        @Override
        public MovieDetail[] newArray(int size) {
            return new MovieDetail[size];
        }
    };

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }


    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackDrop_path() {
        return backDrop_path;
    }

    public void setBackDrop_path(String backDrop_path) {
        this.backDrop_path = backDrop_path;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeInt(id);
        dest.writeDouble(voteAverage);
        dest.writeString(originalTitle);
        dest.writeString(original_language);
        dest.writeString(releaseDate);
        dest.writeString(overView);
        dest.writeString(poster_path);
        dest.writeString(backDrop_path);
        dest.writeInt(totalPages);
    }

    public static final Parcelable.Creator<MovieDetail> creator = new Parcelable.Creator<MovieDetail>() {
        @Override
        public MovieDetail createFromParcel(Parcel source) {
            return new MovieDetail(source);
        }

        @Override
        public MovieDetail[] newArray(int size) {
            return new MovieDetail[size];
        }
    };
}
