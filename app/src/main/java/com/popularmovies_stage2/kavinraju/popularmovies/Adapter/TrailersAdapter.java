package com.popularmovies_stage2.kavinraju.popularmovies.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.popularmovies_stage2.kavinraju.popularmovies.Data_Model.MovieTrailers;
import com.popularmovies_stage2.kavinraju.popularmovies.R;
import com.popularmovies_stage2.kavinraju.popularmovies.Utils.NetworkUtils;

import java.util.ArrayList;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {


    private ArrayList<MovieTrailers> movieTrailers;
    private int totalTrailers;
    private boolean noTrailer = false;
    private Context context;
    final private OnTrailerTileClickListener onTrailerTileClickListener;
    private boolean isFromFav;

    public interface OnTrailerTileClickListener{
        void onTrailerClickListener(String key);
    }

    public TrailersAdapter(ArrayList<MovieTrailers> movieTrailers, int totalTrailers, OnTrailerTileClickListener onTrailerTileClickListener, boolean isFromFav) {
        this.movieTrailers = movieTrailers;
        this.totalTrailers = totalTrailers;
        this.onTrailerTileClickListener = onTrailerTileClickListener;
        if (totalTrailers == 0){
            noTrailer = true;
            this.totalTrailers = 1;
        }
        this.isFromFav = isFromFav;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context  = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutID = R.layout.recyclerview_trailers_list_item;
        View view = inflater.inflate(layoutID,parent,false);
        return new TrailerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {

        RequestOptions options = new RequestOptions()
                .error(R.drawable.error_bk)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        // If from fav and has trailer
        if (isFromFav && !noTrailer){


                Glide.with(context)
                        .load(movieTrailers.get(position).getVideoThumbnail())
                        .apply(options)
                        .into(holder.imageView_thunbnail);

            // If not from fav and has trailer
        }else if (!isFromFav && !noTrailer) {

            String url = NetworkUtils.buildURL_for_videoThumbnail(movieTrailers.get(position).getKey()).toString();

            Glide.with(context)
                    .load(url)
                    .apply(options)
                    .into(holder.imageView_thunbnail);
        }else {
            holder.cardView.setVisibility(View.GONE);
            holder.textView_no_trailer_available.setVisibility(View.VISIBLE);
            }


    }

    @Override
    public int getItemCount() {
        return totalTrailers;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView_thunbnail;
        ImageButton imageButton_play;
        TextView textView_no_trailer_available;
        CardView cardView;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            imageView_thunbnail = itemView.findViewById(R.id.recylerview_trailers_img_thumbnail);
            imageButton_play = itemView.findViewById(R.id.recylerview_trailers_img_btn_play);
            cardView = itemView.findViewById(R.id.recyclerView_trailers_cardview);
            textView_no_trailer_available = itemView.findViewById(R.id.recylerview_trailers_tv_no_trailer_available);

            textView_no_trailer_available.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);


            imageView_thunbnail.setOnClickListener(this);
            imageButton_play.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.recylerview_trailers_img_thumbnail:
                case R.id.recylerview_trailers_img_btn_play:
                    onTrailerTileClickListener.onTrailerClickListener(movieTrailers.get(this.getPosition()).getKey());
                    break;
            }

        }
    }
}
