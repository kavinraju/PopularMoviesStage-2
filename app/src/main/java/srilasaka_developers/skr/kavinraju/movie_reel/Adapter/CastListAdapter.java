package srilasaka_developers.skr.kavinraju.movie_reel.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import srilasaka_developers.skr.kavinraju.movie_reel.Data_Model.CastDetails;
import srilasaka_developers.skr.kavinraju.movie_reel.R;
import srilasaka_developers.skr.kavinraju.movie_reel.Utils.NetworkUtils;

import java.util.ArrayList;

public class CastListAdapter extends RecyclerView.Adapter<CastListAdapter.CastViewHolder>{

    private ArrayList<CastDetails> castDetails;
    private int totalCasts;
    private Context context;
    private boolean isFromFav;

    public CastListAdapter(ArrayList<CastDetails> castDetails, int totalCasts, boolean isFromFav){
        this.castDetails = castDetails;
        this.totalCasts = totalCasts;
        this.isFromFav = isFromFav;
    }
    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutID = R.layout.recyclerview_cast_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutID , parent , false);


        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {

            if (castDetails.size() > 0) {

                holder.textView_name.setText(castDetails.get(position).getName());
                holder.textView_character.setText(castDetails.get(position).getCharacter());

                if (isFromFav){

                    RequestOptions options = new RequestOptions()
                            .error(R.drawable.error_bk)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH);

                    Glide.with(context)
                            .load(castDetails.get(position).getProfilePic())
                            .apply(options)
                            .into(holder.imageView_cast);

                }else {
                    String path = castDetails.get(position).getProfile_path();
                    String url = NetworkUtils.buildURL_for_Image(path, false).toString();

                    RequestOptions options = new RequestOptions()
                            .error(R.drawable.error_bk)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH);

                    Glide.with(context)
                            .load(url)
                            .apply(options)
                            .into(holder.imageView_cast);
                }
            }
    }

    @Override
    public int getItemCount() {
        return totalCasts;
    }

    class CastViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView_cast;
        TextView textView_name, textView_character;

        CastViewHolder(View itemView) {
            super(itemView);
            imageView_cast = itemView.findViewById(R.id.recyclerview_castlist_imageView);
            textView_name  = itemView.findViewById(R.id.recyclerview_castlist_textView_name);
            textView_character  = itemView.findViewById(R.id.recyclerview_castlist_textView_character);

            // Cache is enabled so as to obtain Bitmap later while saving in Database
            imageView_cast.setDrawingCacheEnabled(true);

        }
    }
}
