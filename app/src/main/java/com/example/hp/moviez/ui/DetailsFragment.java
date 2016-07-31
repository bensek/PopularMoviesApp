package com.example.hp.moviez.ui;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.moviez.R;
import com.example.hp.moviez.adapters.ReviewsAdapter;
import com.example.hp.moviez.adapters.TrailerAdapter;
import com.example.hp.moviez.tasks.FetchReviews;
import com.example.hp.moviez.tasks.FetchTrailers;
import com.example.hp.moviez.models.GridItem;
import com.example.hp.moviez.models.Review;
import com.example.hp.moviez.models.Trailer;
import com.example.hp.moviez.data.MoviesContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class DetailsFragment extends Fragment {


    private TextView titleText;
    private TextView yearText;
    private ImageView imageView;
    private TextView overviewText;
    private TextView ratingText;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewList;
    private Button favButton;
    private TrailerAdapter tAdapter;
    private ReviewsAdapter rAdapter;



    public DetailsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        titleText = (TextView) rootView.findViewById(R.id.movie_title);
        yearText = (TextView) rootView.findViewById(R.id.movie_realease_year_text);
        ratingText = (TextView) rootView.findViewById(R.id.movie_rating_text);
        overviewText = (TextView) rootView.findViewById(R.id.movie_plot_text);
        imageView = (ImageView) rootView.findViewById(R.id.movie_image_view);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerViewList = (RecyclerView) rootView.findViewById(R.id.recycler_view_list);
        recyclerViewList.setLayoutManager(new LinearLayoutManager(getContext()));
        favButton = (Button) rootView.findViewById(R.id.btn_favorite);

        final GridItem item = getActivity().getIntent().getExtras().getParcelable("movie");
        long _ID = item.get_ID();

        titleText.setText(Html.fromHtml(item.getTitle()));
        yearText.setText(Html.fromHtml(item.getYear()));
        overviewText.setText(Html.fromHtml(item.getOverview()));
        ratingText.setText(item.getRating());
        Picasso.with(getActivity()).load(item.getImage()).into(imageView);


        if (item.isFavorite() != 1) {
            // Execute the trailers async task
            FetchTrailers fetchTrailers = new FetchTrailers(getContext(), item, recyclerView);
            fetchTrailers.execute();
            //Fetch the reviews
            FetchReviews fetchReviews = new FetchReviews(getContext(), item, recyclerViewList);
            fetchReviews.execute();
        } else {
            // LOAD MOVIE TRAILERS AND REVIEWS FROM THE DB:

            Cursor trailerCursor = getContext().getContentResolver().query(
                    MoviesContract.TrailersEntry.CONTENT_URI,
                    null,
                    MoviesContract.TrailersEntry.COLUMN_MOVIE_KEY + "=?",
                    new String[]{String.valueOf(_ID)},
                    null
            );
            List<Trailer> trailers = new ArrayList<>(trailerCursor.getCount());

            if (trailerCursor != null) {
                Trailer trailer;
                trailerCursor.moveToFirst();
                for (int i = 0; i < trailerCursor.getCount(); i++) {

                    String name = trailerCursor.getString(trailerCursor.getColumnIndex(MoviesContract.TrailersEntry.COLUMN_TRAILER_NAME));
                    String youtubeUrl = trailerCursor.getString(trailerCursor.getColumnIndex(MoviesContract.TrailersEntry.COLUMN_YOUTUBE_URL));
                    String imageUrl = trailerCursor.getString(trailerCursor.getColumnIndex(MoviesContract.TrailersEntry.COLUMN_IMAGE_URL));
                    trailer = new Trailer(name, youtubeUrl, imageUrl);
                    trailers.add(trailer);
                }
                Log.v("TAG", "TrailersFav " + trailers.size() + trailerCursor.getCount());
                tAdapter = new TrailerAdapter(trailers, getContext());
                LinearLayoutManager horizontalLM = new LinearLayoutManager(getActivity().getApplicationContext(),
                        LinearLayoutManager.HORIZONTAL, false);

                recyclerView.setLayoutManager(horizontalLM);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(tAdapter);
            }

            Cursor reviewCursor = getContext().getContentResolver().query(
                    MoviesContract.ReviewsEntry.CONTENT_URI,
                    null,
                    MoviesContract.ReviewsEntry.COLUMN_MOVIE_KEY + "=?",
                    new String[]{String.valueOf(_ID)},
                    null
            );
            List<Review> reviews = new ArrayList<>(reviewCursor.getCount());

            if (reviewCursor != null) {
                Review review;
                reviewCursor.moveToFirst();
                for (int i = 0; i < reviewCursor.getCount(); i++) {

                    String author = reviewCursor.getString(reviewCursor.getColumnIndex(MoviesContract.ReviewsEntry.COLUMN_AUTHOR));
                    String content = reviewCursor.getString(reviewCursor.getColumnIndex(MoviesContract.ReviewsEntry.COLUMN_CONTENT));
                    review = new Review(author, content);
                    reviews.add(review);
                }

                rAdapter = new ReviewsAdapter(reviews);
                LinearLayoutManager verticalLM = new LinearLayoutManager(getContext(),
                        LinearLayoutManager.VERTICAL, false);

                recyclerViewList.setLayoutManager(verticalLM);
                recyclerViewList.setItemAnimator(new DefaultItemAnimator());
                recyclerViewList.setAdapter(rAdapter);
            }

        }
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.isFavorite() == 0) {
                    item.setIsFavorite(1);
                    Toast.makeText(getContext(), "Marked as favorite", Toast.LENGTH_SHORT).show();
                    addFavoritedMovie(item);
                } else {
                    Toast.makeText(getContext(), "Already a favorite", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    public void addFavoritedMovie(GridItem item) {
        long movieRowId;

        ContentValues movieValues = new ContentValues();

        movieValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, item.getId());
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_NAME, item.getTitle());
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, item.getImage());
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, item.getYear());
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_RUNTIME, "120min");
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_RATING, item.getRating());
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, item.getOverview());

        Uri insertedUri = getContext().getContentResolver().insert(
                MoviesContract.MoviesEntry.CONTENT_URI,
                movieValues);
        movieRowId = ContentUris.parseId(insertedUri);


        List<Trailer> trailerList = item.getTrailersList();
        Vector<ContentValues> trVector = new Vector<ContentValues>(trailerList.size());
        Trailer trailer = new Trailer();
        for (int i = 0; i < trailerList.size(); i++) {
            trailer = trailerList.get(i);
            ContentValues trailerValues = new ContentValues();

            trailerValues.put(MoviesContract.TrailersEntry.COLUMN_MOVIE_KEY, movieRowId);
            trailerValues.put(MoviesContract.TrailersEntry.COLUMN_TRAILER_NAME, trailer.getTrailerName());
            trailerValues.put(MoviesContract.TrailersEntry.COLUMN_YOUTUBE_URL, trailer.getTrailerUrl());
            trailerValues.put(MoviesContract.TrailersEntry.COLUMN_IMAGE_URL, trailer.getTrailerImageUrl());

            trVector.add(trailerValues);
        }
        int inserted = 0;
        if (trVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[trailerList.size()];
            trVector.toArray(cvArray);
            inserted = getContext().getContentResolver().bulkInsert(
                    MoviesContract.TrailersEntry.CONTENT_URI, cvArray);

        }
        List<Review> reviewList = item.getReviewsList();
        Vector<ContentValues> revVector = new Vector<ContentValues>(reviewList.size());
        Review review = new Review();
        for (int i = 0; i < reviewList.size(); i++) {

            ContentValues reviewValues = new ContentValues();

            reviewValues.put(MoviesContract.ReviewsEntry.COLUMN_MOVIE_KEY, movieRowId);
            reviewValues.put(MoviesContract.ReviewsEntry.COLUMN_AUTHOR, review.getAuthor());
            reviewValues.put(MoviesContract.ReviewsEntry.COLUMN_CONTENT, review.getReviewDesc());

            revVector.add(reviewValues);
        }
        int insertedReviews = 0;
        if (revVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[reviewList.size()];
            revVector.toArray(cvArray);
            insertedReviews = getContext().getContentResolver().bulkInsert(
                    MoviesContract.ReviewsEntry.CONTENT_URI, cvArray
            );
        }
    }
}

