package com.example.hp.moviez.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.hp.moviez.R;
import com.example.hp.moviez.models.GridItem;
import com.example.hp.moviez.models.Trailer;
import com.example.hp.moviez.adapters.TrailerAdapter;

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
 * Created by HP on 7/15/2016.
 */
public class FetchTrailers extends AsyncTask<String, Void, Integer>{
    private String LOG_TAG = FetchTrailers.class.getSimpleName();
    private String API_PARAM = "api_key";
    public String API_KEY;

    private String VIDEOS_PATH = "videos";
    private Context context;
    private GridItem itemClicked;
    private static List<Trailer> mytrailerList = new ArrayList<>();
    private TrailerAdapter tAdapter;
    RecyclerView recyclerView;



    public FetchTrailers(Context context, GridItem item, RecyclerView rv){
        this.context = context;
        this.itemClicked = item;
        this.recyclerView = rv;
        API_KEY = context.getResources().getString(R.string.my_movie_db_key);
    }

    @Override
    protected Integer doInBackground(String... params) {
        Integer result = 1;
        HttpURLConnection urlConnection2 = null;
        BufferedReader reader2 = null;
        String trailerJsonStr = null;
        String movieId = itemClicked.getId() ;


        try {
            final String BASE_URL = "http://api.themoviedb.org/3/movie";

            Uri trailerUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(movieId)
                    .appendPath(VIDEOS_PATH)
                    .appendQueryParameter(API_PARAM, API_KEY)
                    .build();

            URL urlTrailer = new URL(trailerUri.toString());

            urlConnection2 = (HttpURLConnection) urlTrailer.openConnection();
            urlConnection2.setRequestMethod("GET");
            urlConnection2.connect();

            // Read the input stream into a String
            InputStream inputStream2 = urlConnection2.getInputStream();
            StringBuffer buffer2 = new StringBuffer();
            if (inputStream2 == null) {
                return null;
            }

            reader2 = new BufferedReader(new InputStreamReader(inputStream2));

            String line;
            while ((line = reader2.readLine()) != null) {
                buffer2.append(line + "\n");
            }

            if (buffer2.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            trailerJsonStr = buffer2.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection2 != null) {
                urlConnection2.disconnect();
            }
            if (reader2 != null) {
                try {
                    reader2.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            mytrailerList = parseTrailerJSON(trailerJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (result == 1) {
            tAdapter = new TrailerAdapter(mytrailerList, context);
            LinearLayoutManager horizontalLM = new LinearLayoutManager(context.getApplicationContext(),
                    LinearLayoutManager.HORIZONTAL, false);

            recyclerView.setLayoutManager(horizontalLM);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(tAdapter);
        }
    }

    public List<Trailer> parseTrailerJSON(String trailerJson) throws JSONException {
        // Parse out the youtube key for the movie and add it to the itemr
        String trailerUrl = null;
        String trailerName = null;
        String trailerImageUrl = null;
        String key;
        final String BASE_YOUTUBE_URL = "http://www.youtube.com";
        final String BASE_YOUTUBE_THUMBNAIL = "http://img.youtube.com";

        JSONArray array = null;
            JSONObject object1 = new JSONObject(trailerJson);
            array = object1.getJSONArray("results");
            List<Trailer> trailerList = new ArrayList<>(array.length());
            Trailer trailerObj;
            for(int i =0; i < array.length(); i++) {


                JSONObject object2 = array.getJSONObject(i);
                trailerName = object2.getString("name");
                key = object2.getString("key");
                Uri youtubeUri = Uri.parse(BASE_YOUTUBE_URL).buildUpon()
                                    .appendPath("watch")
                                    .appendQueryParameter("v", key)
                                    .build();
                trailerUrl = youtubeUri.toString();

                Uri imageUri = Uri.parse(BASE_YOUTUBE_THUMBNAIL).buildUpon()
                        .appendPath("vi")
                        .appendPath(key)
                        .appendPath(i + ".jpg")
                        .build();
                trailerImageUrl = imageUri.toString();

                trailerObj = new Trailer(trailerName, trailerUrl, trailerImageUrl);

                trailerList.add(trailerObj);
            }
        itemClicked.setTrailersList(trailerList);
        return trailerList;
    }


}
