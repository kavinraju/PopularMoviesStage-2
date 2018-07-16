package com.popularmovies_stage2.kavinraju.popularmovies.Data_Model;

import java.io.Serializable;

public class MovieReviews implements Serializable{

    private int movie_id;
    private String review_id;
    private int page;
    private int total_pages;
    private int total_results;
    private String author;
    private String content;
    private String review_url;

    public MovieReviews() {
    }

    public MovieReviews(int movie_id, String review_id, int page, int total_pages, int total_results, String author, String content, String review_url) {
        this.movie_id = movie_id;
        this.review_id = review_id;
        this.page = page;
        this.total_pages = total_pages;
        this.total_results = total_results;
        this.author = author;
        this.content = content;
        this.review_url = review_url;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getReview_id() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
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

    public String getReview_url() {
        return review_url;
    }

    public void setReview_url(String review_url) {
        this.review_url = review_url;
    }
}
