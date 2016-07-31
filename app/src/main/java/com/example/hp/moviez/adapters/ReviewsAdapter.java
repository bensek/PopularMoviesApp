package com.example.hp.moviez.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hp.moviez.models.Review;
import com.example.hp.moviez.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 7/17/2016.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewHolder> {

    private List<Review> reviewsList = new ArrayList<>();

    public class ReviewHolder extends RecyclerView.ViewHolder {
        public TextView authorView, reviewView;

        public ReviewHolder(View view) {
            super(view);
            authorView = (TextView) view.findViewById(R.id.review_person_name);
            reviewView = (TextView) view.findViewById(R.id.review_desc);
        }
    }

    public ReviewsAdapter(List<Review> revList) {
        this.reviewsList = revList;
    }
    public void setReviewsList(List<Review> rev) {
        this.reviewsList = rev;
        notifyDataSetChanged();
    }

    @Override
    public ReviewsAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_review_item, parent, false);

        return new ReviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ReviewHolder holder, int position) {
        Review item = reviewsList.get(position);
        holder.authorView.setText(item.author);
        holder.reviewView.setText(item.reviewDesc);
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }
}

