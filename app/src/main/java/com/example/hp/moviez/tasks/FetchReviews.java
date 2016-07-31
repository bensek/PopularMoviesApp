package com.example.hp.moviez.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.hp.moviez.R;
import com.example.hp.moviez.adapters.ReviewsAdapter;
import com.example.hp.moviez.models.Review;
import com.example.hp.moviez.models.GridItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 7/17/2016.
 */
public class FetchReviews extends AsyncTask<String, Void, List<Review>> {
    private String LOG_TAG = FetchReviews.class.getSimpleName();
    private String API_PARAM = "api_key";
    private String REVIEWS_PATH = "reviews";
    private String API_KEY;

    private Context context;
    private GridItem itemClicked;
    private List<Review> myReviewsList = new ArrayList<>();
    private RecyclerView recyclerViewList;
    ReviewsAdapter rAdapter;

    public FetchReviews(Context c, GridItem item, RecyclerView rv) {
        this.context = c;
        this.itemClicked = item;
        this.recyclerViewList = rv;
        API_KEY = c.getResources().getString(R.string.my_movie_db_key);
    }

    @Override
    protected List<Review> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String reviewsJsonStr = null;
        String movieId = itemClicked.getId();


        try {
            final String BASE_URL = "http://api.themoviedb.org/3/movie";

            Uri reviewUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(movieId)
                    .appendPath(REVIEWS_PATH)
                    .appendQueryParameter(API_PARAM, API_KEY)
                    .build();

            URL urlReview = new URL(reviewUri.toString());

            urlConnection = (HttpURLConnection) urlReview.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            reviewsJsonStr = buffer.toString();
            Log.v(LOG_TAG, "reviewJson" + reviewsJsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            myReviewsList = parseReviewJSON(reviewsJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return myReviewsList;
    }

    @Override
    protected void onPostExecute(List<Review> rl)  {
       rAdapter = new ReviewsAdapter(myReviewsList);
        LinearLayoutManager verticalLM = new LinearLayoutManager(context.getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);

        recyclerViewList.setLayoutManager(verticalLM);
        recyclerViewList.setItemAnimator(new DefaultItemAnimator());
       recyclerViewList.setAdapter(rAdapter);
    }

    public List<Review> parseReviewJSON (String reviewJson) throws JSONException {
            // Parse out the youtube key for the movie and add it to the itemr
            String author = null;
            String desc = null;

            JSONArray array = null;
            JSONObject object1 = new JSONObject(reviewJson);
            array = object1.getJSONArray("results");
            List<Review> reviewsList = new ArrayList<>(array.length());
            Review trailerObj;
            for (int i = 0; i < array.length(); i++) {


                JSONObject object2 = array.getJSONObject(i);
                author = object2.getString("author");
                desc = object2.getString("content");

                trailerObj = new Review(author, desc);

                reviewsList.add(trailerObj);

            }
            itemClicked.setReviewsList(reviewsList);
            return reviewsList;
    }
}