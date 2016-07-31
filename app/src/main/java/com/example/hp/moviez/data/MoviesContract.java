package com.example.hp.moviez.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by HP on 7/19/2016.
 */
public class MoviesContract {

    // Content authority is the name of the ContentProvider

    public static final String CONTENT_AUTHORITY = "com.example.hp.moviez";

    // Base Uri for all URI's for this provider

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // All the possible paths for my Uri

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_TRAILERS = "trailers";
    public static final String PATH_REVIEWS = "reviews";


    public static  final class MoviesEntry implements BaseColumns{
        // BaseColumns adds for us the _ID column automatically

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        //queries a movie with a specific movieId
        public static Uri buildMovieUri(String id){
                return CONTENT_URI.buildUpon().appendPath(id).build();
        }
        public static Uri buildMovieUriWith_Id(long _id){
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }

        // Getting the MovieId from the Uri
        public static String getMovieIdFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }

        public static final String TABLE_NAME = "movie";

        //This is our foreign key
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_NAME = "title";

        public static final String COLUMN_POSTER_PATH = "poster_path";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_RUNTIME = "runtime";

        public static final String COLUMN_RATING = "vote_average";

        public static final String COLUMN_OVERVIEW = "overview";

    }

    public static final class TrailersEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;


        public static Uri buildTrailerUriWith_Id(long _id){
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }
        // Helps query trailers for a given movie using its id
        public static Uri buildTrailersUri(String id){
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        // Getting the MovieId from the Uri
        public static String getMovieIdFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }


        public static final String TABLE_NAME = "trailers";

        public static final String COLUMN_MOVIE_KEY = "movie_id";

        public static final String COLUMN_TRAILER_NAME = "name";

        public static final String COLUMN_YOUTUBE_URL = "key";

        public static final String COLUMN_IMAGE_URL = "image_url";

    }

    public static final class ReviewsEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;

        public static Uri buildReviewUriWith_Id(long _id){
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }
        //Queries reviews for a given movie by passing its id
        public static final Uri buildReviewsUri(String id){
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }
        // Getting the MovieId from the Uri
        public static String getMovieIdFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }

        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_MOVIE_KEY = "movie_id";

        public static final String COLUMN_AUTHOR = "author";

        public static final String COLUMN_CONTENT = "content";

    }










}
