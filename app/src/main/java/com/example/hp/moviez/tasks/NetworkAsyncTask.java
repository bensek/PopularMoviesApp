package com.example.hp.moviez.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.hp.moviez.adapters.MoviesAdapter;
import com.example.hp.moviez.models.GridItem;
import com.example.hp.moviez.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NetworkAsyncTask extends AsyncTask<String, Void, Integer> {
    private static String LOG_TAG = NetworkAsyncTask.class.getSimpleName();
    private String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w300/";
    private Context context;
    private ArrayList<GridItem> mGridImages;
    private MoviesAdapter moviesAdapter;


    public NetworkAsyncTask(Context context, MoviesAdapter moviesAdapter, ArrayList<GridItem> gridItems) {
        this.context = context;
        this.moviesAdapter = moviesAdapter;
        this.mGridImages = gridItems;
    }

    @Override
    protected Integer doInBackground(String... params) {
        Integer result = 1;
        HttpURLConnection urlConnection1 = null;
        BufferedReader reader = null;
        String JSONString = "";

        try {
            // Build the URL for the API and with other parameters if necessary

            URL url = new URL(params[0]);

            // Create the request, and open the connection

            urlConnection1 = (HttpURLConnection) url.openConnection();
            urlConnection1.setRequestMethod("GET");
            urlConnection1.connect();

            // Read the inputStream into a String

            InputStream inputStream = urlConnection1.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));


            // Add a new line to the sting to facilitate debugging

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;// InputStream was empty
            }
            JSONString = buffer.toString();
            Log.v(LOG_TAG, "MainString" + JSONString);
            inputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection1 != null) {
                urlConnection1.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("NetworkAsyncTask", "Error closing stream", e);
                }
            }
        }
        if (JSONString != null) {
           parseString(JSONString);
            result = 1;
        } else {
               Toast.makeText(this.context, "Cannot connect to the internet. Please try again", Toast.LENGTH_SHORT).show();
        }
        return result;

       
    }

    @Override
    protected void onPostExecute(Integer result) {

            moviesAdapter.setGridImages(mGridImages);

    }

    private void parseString(String JSONString) {
        try {
            JSONObject baseObject = new JSONObject(JSONString);
            JSONArray moviesArray = baseObject.getJSONArray("results");
            GridItem item;
            int isFavorite = 0;

            int length = moviesArray.length() - 2;
            for (int j = 0; j < length; j++) {
                item = new GridItem();
                JSONObject movie = moviesArray.getJSONObject(j);

                String movieId = String.valueOf(movie.getInt("id"));
                item.setId(movieId);
                Log.v("dfs", "idm" + movieId);

                String posterPath = movie.getString("poster_path");
                String realPosterPath = new StringBuilder(posterPath).deleteCharAt(0).toString();
                Uri imageUri = Uri.parse(BASE_IMAGE_URL).buildUpon()
                        .appendPath(realPosterPath).build();

                String imageUrl = imageUri.toString();
                item.setImage(imageUrl);

                String movieTitle = movie.getString("original_title");
                item.setTitle(movieTitle);

                String releaseYear = movie.getString("release_date");
                item.setYear(releaseYear);

                String movieRating = String.valueOf(movie.getDouble("vote_average"));
                item.setRating(movieRating);
                Log.v("--", movieRating + "movieRatingAsyn");


                String moviePlot = movie.getString("overview");
                item.setOverview(moviePlot);

                item.setIsFavorite(isFavorite);

                mGridImages.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
