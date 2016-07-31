package com.example.hp.moviez.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.moviez.models.Trailer;
import com.example.hp.moviez.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 7/13/2016.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {

    private List<Trailer> trailerList = new ArrayList<>();
    private Context context;



    public class TrailerHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{
        public TextView trailerName, trailerNumber, trailerKey;
        public ImageView trailerImage;
        public TrailerHolder(View view){
            super(view);
            trailerName = (TextView) view.findViewById(R.id.trailernm_text_view);
            trailerImage = (ImageView) view.findViewById(R.id.trailer_image_view);
            trailerImage.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            Trailer trailer = trailerList.get(getAdapterPosition());

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(trailer.getTrailerUrl()));
                    context.startActivity(intent);
        }
    }

    public TrailerAdapter(List<Trailer> trailerList, Context c){
        this.trailerList = trailerList;
        this.context = c;
    }
    public void setTrailers(List<Trailer> trailerList) {
        this.trailerList = trailerList;
        notifyDataSetChanged();
    }

    @Override
    public TrailerAdapter.TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View itemView = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.recycler_row_item, parent, false);

        return new TrailerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerHolder holder, int position) {
        Trailer item = trailerList.get(position);
        holder.trailerName.setText(item.getTrailerName());
    }

    @Override
    public int getItemCount() {
            return trailerList.size();
    }
}
