package com.popularmovies_stage2.kavinraju.popularmovies.Data_Model;

import java.io.Serializable;

public class MovieTrailers implements Serializable{

    private int movie_id;
    private int size;
    private String video_id;
    private String key;
    private String name;
    private String site;
    private String type;
    private byte[] videoThumbnail;

    public MovieTrailers() {
    }

    public MovieTrailers(int movie_id, int size, String video_id, String key, String name, String site, String type, byte[] videoThumbnail) {
        this.movie_id = movie_id;
        this.video_id = video_id;
        this.size = size;
        this.key = key;
        this.name = name;
        this.site = site;
        this.type = type;
        this.videoThumbnail = videoThumbnail;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(byte[] videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }
}
