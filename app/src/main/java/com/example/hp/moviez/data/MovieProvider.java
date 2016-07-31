package com.example.hp.moviez.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by HP on 7/22/2016.
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private  MoviesDbHelper mMovieHelper;

    static final int MOVIES = 100;
    static final int MOVIES_WITH_ID = 101;
    static final int TRAILERS_WITH_MOVIE = 102;
    static final int REVIEWS_WITH_MOVIE = 103;
    static final int TRAILERS = 200;
    static final int REVIEWS = 300;

    private static final SQLiteQueryBuilder sMoviesQueryBuilder;

    static {
        sMoviesQueryBuilder = new SQLiteQueryBuilder();

        // Defining inner joins for my tables.
        //trailers INNER JOIN movies ON trailers.movie_key = movies._ID
        //reviews INNER JOIN movies ON reviews.movie_key = movies._ID

        sMoviesQueryBuilder.setTables(
                MoviesContract.TrailersEntry.TABLE_NAME + " INNER JOIN " + MoviesContract.MoviesEntry.TABLE_NAME +
                    " ON " + MoviesContract.TrailersEntry.TABLE_NAME +
                    "." + MoviesContract.TrailersEntry.COLUMN_MOVIE_KEY +
                    " = " + MoviesContract.MoviesEntry.TABLE_NAME +
                    "." + MoviesContract.MoviesEntry._ID);

        sMoviesQueryBuilder.setTables(
                MoviesContract.ReviewsEntry.TABLE_NAME + " INNER JOIN " + MoviesContract.MoviesEntry.TABLE_NAME +
                        " ON " + MoviesContract.ReviewsEntry.TABLE_NAME +
                        " . " + MoviesContract.ReviewsEntry.COLUMN_MOVIE_KEY +
                        " = " + MoviesContract.MoviesEntry.TABLE_NAME +
                        " . " + MoviesContract.MoviesEntry._ID);
    }

    //Defining the several selection clauses for each table

    //movies.movie_id = ?
    private static final String sMovieIdSelection =
            MoviesContract.MoviesEntry.TABLE_NAME +
                    "." + MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " = ? ";

    //setting the various args of the queryy;
    private Cursor getTrailersByMovieId(Uri uri, String [] projection, String sortOrder){
        String movieId = MoviesContract.TrailersEntry.getMovieIdFromUri(uri);

        String[] selectionArgs = new String[]{movieId};
        String selection = sMovieIdSelection;

        return sMoviesQueryBuilder.query(mMovieHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }
    private Cursor getMovieById(Uri uri, String[] projection, String sortOrder){
        String movieId = MoviesContract.MoviesEntry.getMovieIdFromUri(uri);

        String[] selectionArgs = new String[]{movieId};
        String selection = sMovieIdSelection;

        return sMoviesQueryBuilder.query(mMovieHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    // For the Reviews table;
    private Cursor getReviewsByMovieId(Uri uri, String [] projection, String sortOrder){
        String movieId = MoviesContract.ReviewsEntry.getMovieIdFromUri(uri);

        String[] selectionArgs = new String[]{movieId};
        String selection = sMovieIdSelection;

        return sMoviesQueryBuilder.query(mMovieHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }



    // Building the UriMatcher
    static UriMatcher buildUriMatcher(){

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MoviesContract.PATH_MOVIE, MOVIES);
        matcher.addURI(authority, MoviesContract.PATH_REVIEWS, REVIEWS);
        matcher.addURI(authority, MoviesContract.PATH_TRAILERS, TRAILERS);
        matcher.addURI(authority, MoviesContract.PATH_MOVIE + "/*", MOVIES_WITH_ID);
        matcher.addURI(authority, MoviesContract.PATH_TRAILERS + "/*", TRAILERS_WITH_MOVIE);
        matcher.addURI(authority, MoviesContract.PATH_REVIEWS + "/*", REVIEWS_WITH_MOVIE);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mMovieHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        //Using the Uri Matcher to determine the URI type
        final int match = sUriMatcher.match(uri);

        switch(match){

            case MOVIES:
                return MoviesContract.MoviesEntry.CONTENT_TYPE;

            case MOVIES_WITH_ID:
                return MoviesContract.MoviesEntry.CONTENT_ITEM_TYPE;

            case TRAILERS_WITH_MOVIE:
                return MoviesContract.TrailersEntry.CONTENT_TYPE;

            case REVIEWS_WITH_MOVIE:
                return MoviesContract.ReviewsEntry.CONTENT_TYPE;

            case TRAILERS:
                return MoviesContract.TrailersEntry.CONTENT_TYPE;

            case REVIEWS:
                return MoviesContract.ReviewsEntry.CONTENT_TYPE;

            default:
                throw  new UnsupportedOperationException("Unknown uri " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;
        switch(sUriMatcher.match(uri)) {

            //"movies/*"
            case MOVIES_WITH_ID: {
                retCursor = getMovieById(uri, projection, sortOrder);
                break;
            }

            // "movies"
            case MOVIES: {
                retCursor = mMovieHelper.getReadableDatabase().query(
                        MoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            // "trailers"
            case TRAILERS: {
                retCursor = mMovieHelper.getReadableDatabase().query(
                        MoviesContract.TrailersEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            // "reviews"
            case REVIEWS: {
                retCursor = mMovieHelper.getReadableDatabase().query(
                        MoviesContract.ReviewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            //"trailers/*"
            case TRAILERS_WITH_MOVIE: {
                retCursor = getTrailersByMovieId(uri, projection, sortOrder);
                break;
            }

            //"reviews/*"
            case REVIEWS_WITH_MOVIE: {
                retCursor = getReviewsByMovieId(uri, projection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);

        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }



    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mMovieHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case MOVIES: {
                long _id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MoviesContract.MoviesEntry.buildMovieUriWith_Id(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case TRAILERS: {
                long _id = db.insert(MoviesContract.TrailersEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MoviesContract.TrailersEntry.buildTrailerUriWith_Id(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case REVIEWS: {
                long _id = db.insert(MoviesContract.ReviewsEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MoviesContract.ReviewsEntry.buildReviewUriWith_Id(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMovieHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int deletedRows;

        if(selection == null) selection = "1";
        switch (match){
            case MOVIES:
                deletedRows = db.delete(
                        MoviesContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case TRAILERS:
                deletedRows = db.delete(
                        MoviesContract.TrailersEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case REVIEWS:
                deletedRows = db.delete(
                        MoviesContract.ReviewsEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (deletedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMovieHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int updatedRows;

        switch (match){
            case MOVIES:
                updatedRows = db.update(MoviesContract.MoviesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            case TRAILERS:
                updatedRows = db.update(MoviesContract.TrailersEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            case REVIEWS:
                updatedRows = db.update(MoviesContract.ReviewsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (updatedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updatedRows;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mMovieHelper.getWritableDatabase();
        final int match  = sUriMatcher.match(uri);

        switch (match){
            case TRAILERS: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.TrailersEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case REVIEWS: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.ReviewsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public void shutdown() {
        mMovieHelper.close();
        super.shutdown();
    }
}
