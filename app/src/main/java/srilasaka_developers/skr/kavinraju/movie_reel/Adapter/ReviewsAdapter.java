package srilasaka_developers.skr.kavinraju.movie_reel.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import srilasaka_developers.skr.kavinraju.movie_reel.Data_Model.MovieReviews;
import srilasaka_developers.skr.kavinraju.movie_reel.R;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private ArrayList<MovieReviews> movieReviews;
    private int totalReviews;
    private boolean noReviews = false;
    private Context context;

    public ReviewsAdapter(ArrayList<MovieReviews> movieReviews, int totalReviews, Context context) {
        this.movieReviews = movieReviews;
        this.totalReviews = totalReviews;
        this.context = context;
    }



    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context  = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutID = R.layout.recyclerview_reviews_list_item;
        View view = inflater.inflate(layoutID,parent,false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {

        if (movieReviews != null){
            holder.textView_author.setText(movieReviews.get(position).getAuthor());
            holder.textView_content.setText(movieReviews.get(position).getContent());
        }

    }

    @Override
    public int getItemCount() {
        return totalReviews;
    }

    class ReviewsViewHolder extends RecyclerView.ViewHolder {

        TextView textView_author, textView_content;

        ReviewsViewHolder(View itemView) {
            super(itemView);
            textView_author = itemView.findViewById(R.id.recylerview_reviews_tv_author);
            textView_content = itemView.findViewById(R.id.recyclerView_reviews_tv_content);
        }
    }
}
