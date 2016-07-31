package com.example.hp.moviez.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.moviez.models.GridItem;
import com.example.hp.moviez.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by HP on 7/7/2016.
 */
public class MoviesAdapter extends ArrayAdapter<GridItem> {

    private static final String LOG_TAG = MoviesAdapter.class.getSimpleName();
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<GridItem> GridImages = new ArrayList<GridItem>();

    static class ViewHolder{
        ImageView imageView;
        TextView textView;
    }

    // This constructor takes in the the id of the item layout and the list of data to be operated on

    public MoviesAdapter(Context mContext, int layoutResourceId, ArrayList<GridItem> GridImages) {
        super(mContext, layoutResourceId, GridImages);
        this.mContext = mContext;
        this.layoutResourceId = layoutResourceId;
        this.GridImages = GridImages;
    }

    //Updates the gridImages on the GridView and refreshes them

    public void setGridImages(ArrayList<GridItem> GridImages) {
        this.GridImages = GridImages;
        notifyDataSetChanged();
    }

    // This is where it all goes down, the method gets each Item and gives it its respective data


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;


        if(row == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) row.findViewById(R.id.grid_view_image);
            holder.textView = (TextView) row.findViewById(R.id.grid_view_text);
            row.setTag(holder);

        }else{
            holder = (ViewHolder) row.getTag();
        }

        GridItem item = GridImages.get(position);
        Picasso.with(mContext).load(item.getImage()).into(holder.imageView);
        holder.textView.setText(item.getTitle());

        return  row;
    }

}
