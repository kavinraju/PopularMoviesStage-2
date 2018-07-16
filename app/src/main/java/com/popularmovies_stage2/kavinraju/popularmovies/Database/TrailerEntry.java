package com.popularmovies_stage2.kavinraju.popularmovies.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/*
        1. Define Trailer Entry class for Table Contents
        2. Define DAO (Queries) for that.
        3. Add in the entities in the Database Class
        4. Define a funtion in NetworkUtils to ge the Bitmap of the Thumnail
        5. Create a loader to load all the images as Bitmap in a List
        6. Now on taping the favorite button Trailers data has to be added in the database and then required while viewing in Offline.
        7. Re-Query the data to display on UI using the Adapter (add bool in so as to know whether is form Fav. Movie or Main List)
 */
@Entity(tableName = "trailers")
public class TrailerEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "movie_id")
    private int movieId;
    @ColumnInfo(name = "video_id")
    private String videoId;
    private String key;
    private String name;
    private String site;
    private String type;
    @ColumnInfo(name = "video_thumbnail")
    private byte[] videoThumbnail;

    @Ignore
    public TrailerEntry(int movieId, String videoId, String key, String name, String site, String type, byte[] videoThumbnail) {
        
        this.movieId = movieId;
        this.videoId = videoId;
        this.key = key;
        this.name = name;
        this.site = site;
        this.type = type;
        this.videoThumbnail = videoThumbnail;
    }



    public TrailerEntry(int id, int movieId, String videoId, String key, String name, String site, String type, byte[] videoThumbnail) {
        this.id = id;
        this.movieId = movieId;
        this.videoId = videoId;
        this.key = key;
        this.name = name;
        this.site = site;
        this.type = type;
        this.videoThumbnail = videoThumbnail;
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


    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public byte[] getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(byte[] videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }
}

