package com.example.hp.moviez.data;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";
    public static final int DATABASE_VERSION = 1;


    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesContract.MoviesEntry.TABLE_NAME + " (" +
                MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " TEXT UNIQUE NOT NULL, "+
                MoviesContract.MoviesEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_RUNTIME + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_RATING + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                " UNIQUE (" + MoviesContract.MoviesEntry.COLUMN_MOVIE_ID+ ") ON CONFLICT REPLACE " + ");";

        final String CREATE_TRAILERS_TABLE = "CREATE TABLE " + MoviesContract.TrailersEntry.TABLE_NAME + " (" +
                MoviesContract.TrailersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.TrailersEntry.COLUMN_MOVIE_KEY + " INTEGER NOT NULL, " +
                MoviesContract.TrailersEntry.COLUMN_TRAILER_NAME + " TEXT NOT NULL, " +
                MoviesContract.TrailersEntry.COLUMN_YOUTUBE_URL + " TEXT NOT NULL, " +
                MoviesContract.TrailersEntry.COLUMN_IMAGE_URL + " TEXT NOT NULL, " +
                //SETTING UP THE MOVIE_KEY COL AS THE FOREIGN KEY TO THE MOVIES TABLE
                " FOREIGN KEY (" + MoviesContract.TrailersEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
                MoviesContract.MoviesEntry.TABLE_NAME + " (" + MoviesContract.MoviesEntry._ID + "), " +
                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + MoviesContract.TrailersEntry.COLUMN_YOUTUBE_URL + ") ON CONFLICT REPLACE " + ");";

        final String CREATE_REVIEWS_TABLE = "CREATE TABLE " + MoviesContract.ReviewsEntry.TABLE_NAME + " ( " +
                MoviesContract.ReviewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                MoviesContract.ReviewsEntry.COLUMN_MOVIE_KEY + " INTEGER NOT NULL, " +
                MoviesContract.ReviewsEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                MoviesContract.ReviewsEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + MoviesContract.ReviewsEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
                MoviesContract.MoviesEntry.TABLE_NAME + " (" + MoviesContract.MoviesEntry._ID + "), " +
                " UNIQUE (" + MoviesContract.ReviewsEntry.COLUMN_CONTENT +  ") ON CONFLICT REPLACE " + ");";

        db.execSQL(CREATE_MOVIES_TABLE);
        db.execSQL(CREATE_TRAILERS_TABLE);
        db.execSQL(CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // this method deletes that the table when the version is changed
        //incase you want to backup your data without delete just leave out these two lines
        db.execSQL(" DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " + MoviesContract.TrailersEntry.TABLE_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " + MoviesContract.ReviewsEntry.TABLE_NAME);
    }
}
