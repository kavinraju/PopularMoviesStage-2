package com.popularmovies_stage2.kavinraju.popularmovies.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.popularmovies_stage2.kavinraju.popularmovies.Data_Model.MovieDetail;
import com.popularmovies_stage2.kavinraju.popularmovies.Database.MovieEntry;
import com.popularmovies_stage2.kavinraju.popularmovies.R;

import java.util.List;

public class FavoriteMoviesAdapter extends RecyclerView.Adapter<FavoriteMoviesAdapter.FavoriteMoviesViewHolder> {

    private List<MovieEntry> movieEntries;
    private int totalMovies;
    private int lastPosition = -1;
    final private MovieTileClickListener movieTileClickListener;
    private Context context;

    public FavoriteMoviesAdapter(List<MovieEntry> movieEntries, int totalMovies, MovieTileClickListener movieTileClickListener) {
        this.movieEntries = movieEntries;
        this.totalMovies = totalMovies;
        this.movieTileClickListener = movieTileClickListener;

    }



    public interface MovieTileClickListener{

        void onMovieTitleClick(View view, int clickedMoviePosition);
    }

    @NonNull
    @Override
    public FavoriteMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutID = R.layout.recyclerview_card_home_activity;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutID , parent , false);

        return new FavoriteMoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteMoviesViewHolder holder, int position) {

        if (movieEntries.size() > 0){

            byte[] poster = movieEntries.get(position).getPoster();
            Bitmap bitmap_poster = BitmapFactory.decodeByteArray(poster, 0, poster.length);
            holder.imageView_poster.setImageBitmap(bitmap_poster);
            holder.textView.setText(String.valueOf(movieEntries.get(position).getVoteAverage()));


            if (position > lastPosition){
                Animation animation = AnimationUtils.loadAnimation(context,R.anim.recycler_view_bot_to_up);
                holder.itemView.setAnimation(animation);
                lastPosition = holder.getAdapterPosition();
            }
        }
    }

    @Override
    public int getItemCount() {
        return totalMovies;
    }

    class FavoriteMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView_poster;
        ImageView imageView_favorite;
        TextView textView;

        FavoriteMoviesViewHolder(View itemView) {
            super(itemView);
            imageView_poster = itemView.findViewById(R.id.img_poster_recycerView_Card_HomeActivity);
            imageView_favorite = itemView.findViewById(R.id.img_favorite_recycerView_Card_HomeActivity);
            textView = itemView.findViewById(R.id.tv_rating_recycerView_Card_HomeActivity);

            imageView_poster.setOnClickListener(this);
            imageView_favorite.setOnClickListener(this);
            imageView_favorite.setBackgroundResource(R.drawable.favorite_primary_24dp);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.img_poster_recycerView_Card_HomeActivity:
                case R.id.img_favorite_recycerView_Card_HomeActivity:
                    movieTileClickListener.onMovieTitleClick(v, this.getPosition());
                    break;
            }
        }
    }

    public void removeMovieEntry(MovieEntry movieEntry, int removedItemPosition){
        movieEntries.remove(movieEntry);
        notifyItemRemoved(removedItemPosition);
    }
}
