package srilasaka_developers.skr.kavinraju.movie_reel.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "casts")
public class CastEntry {


    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "movie_id")
    private int movieId;
    private String name;
    private String character;
    private int gender;
    private int order;
    @ColumnInfo(name = "profile_pic")
    private byte[] profilePic;

    @Ignore
    public CastEntry(int movieId, String name, String character, int gender, int order, byte[] profilePic) {
        this.movieId = movieId;
        this.name = name;
        this.character = character;
        this.gender = gender;
        this.order = order;
        this.profilePic = profilePic;
    }


    public CastEntry(int id, int movieId, String name, String character, int gender, int order, byte[] profilePic) {
        this.id = id;
        this.movieId = movieId;
        this.name = name;
        this.character = character;
        this.gender = gender;
        this.order = order;
        this.profilePic = profilePic;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }
}
