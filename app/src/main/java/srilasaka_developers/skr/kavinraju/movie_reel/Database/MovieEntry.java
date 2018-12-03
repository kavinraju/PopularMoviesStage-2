package srilasaka_developers.skr.kavinraju.movie_reel.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
@Entity(tableName = "movies")
public class MovieEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "movie_id")
    private int movieId;
    @ColumnInfo(name = "movie_title")
    private String movieTitle;
    @ColumnInfo(name = "language")
    private String movie_language;
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    private String overView;
    private String tagline;
    private ArrayList<String> genres;
    @ColumnInfo(name = "vote_avg")
    private double voteAverage;
    private byte[] poster;
    @ColumnInfo(name = "back_drop")
    private byte[] backDrop;

    @Ignore
    public MovieEntry(int movieId, String movieTitle, String movie_language, String releaseDate, String overView, String tagline, ArrayList<String> genres, double voteAverage, byte[] poster, byte[] backDrop) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movie_language = movie_language;
        this.releaseDate = releaseDate;
        this.overView = overView;
        this.tagline = tagline;
        this.genres = genres;
        this.voteAverage = voteAverage;
        this.poster = poster;
        this.backDrop = backDrop;
    }


    public MovieEntry(int id, int movieId, String movieTitle, String movie_language, String releaseDate, String overView, String tagline, ArrayList<String> genres, double voteAverage, byte[] poster, byte[] backDrop) {
        this.id = id;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movie_language = movie_language;
        this.releaseDate = releaseDate;
        this.overView = overView;
        this.tagline = tagline;
        this.genres = genres;
        this.voteAverage = voteAverage;
        this.poster = poster;
        this.backDrop = backDrop;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovie_language() {
        return movie_language;
    }

    public void setMovie_language(String movie_language) {
        this.movie_language = movie_language;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public byte[] getPoster() {
        return poster;
    }

    public void setPoster(byte[] poster) {
        this.poster = poster;
    }

    public byte[] getBackDrop() {
        return backDrop;
    }

    public void setBackDrop(byte[] backDrop) {
        this.backDrop = backDrop;
    }


}
