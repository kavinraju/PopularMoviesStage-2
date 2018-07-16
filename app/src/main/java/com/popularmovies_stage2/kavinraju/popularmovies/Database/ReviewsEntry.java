package com.popularmovies_stage2.kavinraju.popularmovies.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "reviews")
public class ReviewsEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "movie_id")
    private int movieID;
    private String author;
    private String content;

    @Ignore
    public ReviewsEntry(int movieID, String author, String content, String reviewURL) {
        this.movieID = movieID;
        this.author = author;
        this.content = content;
        this.reviewURL = reviewURL;
    }

    public ReviewsEntry(int id, int movieID, String author, String content, String reviewURL) {
        this.id = id;
        this.movieID = movieID;
        this.author = author;
        this.content = content;
        this.reviewURL = reviewURL;
    }

    @ColumnInfo(name = "review_url")
    private String reviewURL;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReviewURL() {
        return reviewURL;
    }

    public void setReviewURL(String reviewURL) {
        this.reviewURL = reviewURL;
    }

}
