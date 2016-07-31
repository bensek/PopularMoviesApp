package com.example.hp.moviez.ui;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.hp.moviez.BuildConfig;
import com.example.hp.moviez.R;
import com.example.hp.moviez.adapters.MoviesAdapter;
import com.example.hp.moviez.models.GridItem;
import com.example.hp.moviez.data.MoviesContract;
import com.example.hp.moviez.tasks.NetworkAsyncTask;
import java.util.ArrayList;


public class MoviesFragment extends Fragment{

    private GridView gridView;
    private MoviesAdapter adapter;
    private ArrayList<GridItem> mGridImages;
    private String API_PARAM = "api_key";
    private String API_KEY = getResources().getString(R.string.my_movie_db_key);
    private String BASE_DISCOVER_MOVIES_URL = "http://api.themoviedb.org/3/discover/movie";
    private String BASE_URL_POPULAR_MOVIES = "http://api.themoviedb.org/3/movie/popular";
    private String BASE_URL_TOP_RATED_MOVIES = "http://api.themoviedb.org/3/movie/top_rated";

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);


        gridView = (GridView) rootView.findViewById(R.id.grid_view_items);

        // initialize with empty data;
        mGridImages = new ArrayList<>();
        adapter =new MoviesAdapter(getActivity(), R.layout.grid_view_item, mGridImages);
        gridView.setAdapter(adapter);

        // When no sort type is selected query the discover movies
        NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(getContext(), adapter, mGridImages);
        String popularUrl = (Uri.parse(BASE_DISCOVER_MOVIES_URL).buildUpon()
                .appendQueryParameter(API_PARAM, API_KEY )
                .build()).toString();
        networkAsyncTask.execute(popularUrl

        );


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Getting the item at the clicked position
                GridItem item = (GridItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("movie", item);
                getActivity().startActivity(intent);
            }
        });
    return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movies_frag, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_most_popular:
                adapter.clear();
                adapter.notifyDataSetChanged();
                new NetworkAsyncTask(getContext(), adapter, mGridImages)
                        .execute( (Uri.parse(BASE_URL_POPULAR_MOVIES).buildUpon()
                            .appendQueryParameter(API_PARAM, API_KEY)
                            .build()).toString());
                break;
            case R.id.action_top_rated:
                adapter.clear();
                adapter.notifyDataSetChanged();
                new NetworkAsyncTask(getContext(), adapter, mGridImages)
                        .execute((Uri.parse(BASE_URL_TOP_RATED_MOVIES).buildUpon()
                            .appendQueryParameter(API_PARAM, API_KEY )
                            .build()).toString());

                break;
            case R.id.action_favorites:
                new QueryFavorites(getContext(), R.layout.grid_view_item, gridView).execute();

        }
        return false;
    }


    private class QueryFavorites extends AsyncTask<Void, Void, ArrayList<GridItem>>{
        GridItem mGridItem = new GridItem();
        private ArrayList<GridItem> movieItemsList = null;
        private MoviesAdapter moviesAdapter;
        private Context context;
        private int layoutResId;
        private GridView gridView;
        public QueryFavorites(Context c, int LayoutId, GridView grid){
            this.context = c;
            this.layoutResId = LayoutId;
            this.gridView = grid;
        }
        final String[] FAV_COLUMNS = new String[]{
                MoviesContract.MoviesEntry._ID,
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID,
                MoviesContract.MoviesEntry.COLUMN_NAME,
                MoviesContract.MoviesEntry.COLUMN_POSTER_PATH,
                MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE,
                MoviesContract.MoviesEntry.COLUMN_RUNTIME,
                MoviesContract.MoviesEntry.COLUMN_RATING,
                MoviesContract.MoviesEntry.COLUMN_OVERVIEW
        };
        private  final int INDEX_ID = 0;
        private  final int INDEX_MOVIE_ID =1;
        private  final int INDEX_NAME = 2;
        private final int INDEX_POSTER_PATH = 3;
        private  final int INDEX_RELEASE_DATE = 4;
        private  final int INDEX_RUNTIME = 5;
        private  final int INDEX_RATING = 6;
        private  final int INDEX_OVERVIEW = 7;

        @Override
        protected ArrayList<GridItem> doInBackground(Void... params) {

            Cursor cursor = getContext().getContentResolver().query(
                    MoviesContract.MoviesEntry.CONTENT_URI,
                    FAV_COLUMNS,
                    null,
                    null,
                    MoviesContract.MoviesEntry._ID + " ASC"
            );
            if(cursor != null){
                cursor.moveToFirst();
                long _ID = 0;

                movieItemsList = new ArrayList<GridItem>();

                while(!cursor.isAfterLast()){
                    mGridItem = new GridItem();
                    mGridItem.setIsFavorite(1);
                    mGridItem.setOverview(cursor.getString(INDEX_OVERVIEW));
                    mGridItem.setRating(cursor.getString(INDEX_RATING));
                    mGridItem.setTitle(cursor.getString(INDEX_NAME));
                    mGridItem.setYear(cursor.getString(INDEX_RELEASE_DATE));
                    mGridItem.setId(cursor.getString(INDEX_MOVIE_ID));
                    mGridItem.setImage(cursor.getString(INDEX_POSTER_PATH));
                    mGridItem.set_ID(Long.valueOf(cursor.getInt(INDEX_ID)));
                    movieItemsList.add(mGridItem);
                    cursor.moveToNext();

                }
            }else{
                Toast.makeText(getActivity(), "No Favorites were added", Toast.LENGTH_SHORT).show();
            }
            return movieItemsList;
        }
        @Override
        protected void onPostExecute(ArrayList<GridItem> list) {
            // this method enables us update the UI after downloading the data
            if (list != null) {
                MoviesAdapter favAdapter = new MoviesAdapter(getContext(), layoutResId, movieItemsList );
                gridView.setAdapter(favAdapter);
                favAdapter.setGridImages(movieItemsList);
            } else {
                Toast.makeText(getContext(), "No Favorites Movies Found", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
