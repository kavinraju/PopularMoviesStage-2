package srilasaka_developers.skr.kavinraju.movie_reel.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import srilasaka_developers.skr.kavinraju.movie_reel.Data_Model.MovieDetail;
import srilasaka_developers.skr.kavinraju.movie_reel.R;
import srilasaka_developers.skr.kavinraju.movie_reel.Utils.NetworkUtils;


import java.util.ArrayList;
import java.util.List;

public class MovieListAdapter_HomeActivity extends RecyclerView.Adapter<MovieListAdapter_HomeActivity.MovieList_ViewHolder> {

    private ArrayList<MovieDetail> movieDetails;
    private int totalMovies;
    private int lastPosition = -1;
    private List<Integer> favMovies = new ArrayList<>();
    final private MovieTileClickListener movieTileClickListener;
    private Context context;

    public MovieListAdapter_HomeActivity(ArrayList<MovieDetail> movieDetails, int totalMovies, List<Integer> favMovies, MovieTileClickListener movieTileClickListener) {
        this.movieDetails = movieDetails;
        this.totalMovies = totalMovies;
        this.favMovies = favMovies;
        this.movieTileClickListener = movieTileClickListener;

    }



    public interface MovieTileClickListener{

        void onMovieTitleClick(View view, int clickedMoviePosition);
    }

    @NonNull
    @Override
    public MovieListAdapter_HomeActivity.MovieList_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        int layoutID = R.layout.recyclerview_card_home_activity;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutID , parent , false);


        return new MovieList_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter_HomeActivity.MovieList_ViewHolder holder, int position) {

        if (movieDetails.size() > 0) {
            String path = movieDetails.get(position).getPoster_path();
            String url = NetworkUtils.buildURL_for_Image(path, false).toString();

            RequestOptions options = new RequestOptions()
                    .error(R.drawable.error_bk)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH);

            Glide.with(context)
                    .load(url)
                    .apply(options)
                    .into(holder.imageView_poster);

            if (favMovies!= null && movieDetails !=null) {
                if (favMovies.contains(movieDetails.get(position).getId())) {
                    holder.imageView_favorite.setBackgroundResource(R.drawable.favorite_primary_24dp);
                    movieDetails.get(position).setFavorite(true);
                } else {
                    holder.imageView_favorite.setBackgroundResource(R.drawable.favorite_border_primary_24dp);
                }
            }else {
                if (favMovies == null){
                    Log.d("NULL", "fav");
                }else if (movieDetails == null){
                    Log.d("NULL", "fav");
                }
            }

            ViewCompat.setTransitionName(holder.imageView_poster, movieDetails.get(position).getOriginalTitle());
            holder.textView.setText(String.valueOf(movieDetails.get(position).getVoteAverage()));
            Log.d("onBindViewHolder",url);

            if (position > lastPosition){
                Animation animation = AnimationUtils.loadAnimation(context,R.anim.recycler_view_bot_to_up);
                holder.itemView.setAnimation(animation);
                lastPosition = holder.getAdapterPosition();
                Log.d("animation- adapter posi", String.valueOf(holder.getAdapterPosition()));
                Log.d("animation - posi", String.valueOf(holder.getPosition()));
            }
        }

    }


    @Override
    public int getItemCount() {
        return totalMovies;
    }

    public class
    MovieList_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView_poster;
        ImageView imageView_favorite;
        TextView textView;

        MovieList_ViewHolder(View itemView) {
            super(itemView);

            imageView_poster = itemView.findViewById(R.id.img_poster_recycerView_Card_HomeActivity);
            imageView_favorite = itemView.findViewById(R.id.img_favorite_recycerView_Card_HomeActivity);
            textView = itemView.findViewById(R.id.tv_rating_recycerView_Card_HomeActivity);

            imageView_poster.setOnClickListener(this);
            imageView_favorite.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.img_poster_recycerView_Card_HomeActivity:
                    movieTileClickListener.onMovieTitleClick(v,this.getPosition());
                    break;
            }

        }
    }

    public void addMovies(ArrayList<MovieDetail> movies){

        movieDetails.addAll(movies);
        totalMovies = movieDetails.size();
        notifyItemInserted(totalMovies - movies.size());
        notifyDataSetChanged();
    }

    public void cleatMovieData(){
        int size = movieDetails.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                movieDetails.remove(0);
            }
            notifyItemRangeRemoved(0,size);
        }
    }
}


