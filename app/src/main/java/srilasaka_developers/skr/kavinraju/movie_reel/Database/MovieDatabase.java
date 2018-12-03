package srilasaka_developers.skr.kavinraju.movie_reel.Database;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

@Database(entities = {MovieEntry.class, CastEntry.class, TrailerEntry.class, ReviewsEntry.class}, version = 1 , exportSchema = false)
@TypeConverters(Converters.class)
public abstract class MovieDatabase extends RoomDatabase{

    private static final String LOG_TAG = MovieDatabase.class.getSimpleName();
    private static final Object OBJECT = new Object();
    private static final String DATABASE_NAME = "favmovies";
    private static MovieDatabase movieDatabase;

    public static MovieDatabase getInstance(Context context){
        if (movieDatabase == null){
            synchronized (OBJECT){
                Log.d(LOG_TAG, "Creating new Database");
                movieDatabase = Room.databaseBuilder(context.getApplicationContext(),
                                    MovieDatabase.class,
                                    MovieDatabase.DATABASE_NAME)
                                    .build();
            }
        }
        //Log.d(LOG_TAG, "Return exsisting Database");
        return movieDatabase;
    }

    public abstract MovieDao movieDao();


    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
