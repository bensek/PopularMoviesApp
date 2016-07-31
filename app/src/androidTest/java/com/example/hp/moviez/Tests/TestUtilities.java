//package com.example.hp.moviez.Tests;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.ContentObserver;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.net.Uri;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.test.AndroidTestCase;
//
//import com.example.hp.moviez.data.MoviesContract;
//import com.example.hp.moviez.data.MoviesDbHelper;
//import com.example.hp.moviez.utils.PollingCheck;
//
//import java.util.Map;
//import java.util.Set;
//
///**
// * Created by HP on 7/20/2016.
// */
//public class TestUtilities extends AndroidTestCase{
//
//
//    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
//        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
//        validateRow(error, valueCursor, expectedValues);
//        valueCursor.close();
//    }
//
//
//    // validating a given row with the inserted content values
//
//    static void validateRow (String error, Cursor cursor, ContentValues realValues){
//
//        Set<Map.Entry<String, Object>> valueSet = realValues.valueSet();
//        for(Map.Entry<String, Object> entry : valueSet){
//            String colName = entry.getKey();
//            int index = cursor.getColumnIndex(colName);
//            assertFalse("Column " + colName + "not found. " + error, index == -1);
//            String realValue = entry.getValue().toString();
//            assertEquals("Value " + entry.getValue().toString() + "doesnt match the real value " +
//            realValue + ". "+ error, realValue, cursor.getString(index));
//        }
//    }
//
//
//
//
//// creating contentvalues for the movies table
//    static ContentValues createMoviesTestValues(){
//        ContentValues testValues = new ContentValues();
//        testValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, "8795");
//        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME, "Wrath of Titans");
//        testValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, "http://");
//        testValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, "2010");
//        testValues.put(MoviesContract.MoviesEntry.COLUMN_RUNTIME, "110");
//        testValues.put(MoviesContract.MoviesEntry.COLUMN_RATING, "8.9");
//        testValues.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, "Here comes");
//
//        return testValues;
//    }
//
//    // Creating the ContentValues for the TrailersTable;
//    static  ContentValues createTrailersValues(long foreignKey){
//        ContentValues trailerValues = new ContentValues();
//        trailerValues.put(MoviesContract.TrailersEntry.COLUMN_MOVIE_KEY, foreignKey);
//        trailerValues.put(MoviesContract.TrailersEntry.COLUMN_TRAILER_NAME, "Official Trailer");
//        trailerValues.put(MoviesContract.TrailersEntry.COLUMN_YOUTUBE_URL, "http://www.youtube");
//        trailerValues.put(MoviesContract.TrailersEntry.COLUMN_IMAGE_URL, "http://");
//
//
//        return trailerValues;
//    }
//
//    // Creating the ContentValues for the TrailersTable;
//    static  ContentValues createReviewsValues(long foreignKey){
//        ContentValues reviewValues = new ContentValues();
//        reviewValues.put(MoviesContract.ReviewsEntry.COLUMN_MOVIE_KEY, foreignKey);
//        reviewValues.put(MoviesContract.ReviewsEntry.COLUMN_AUTHOR, "ven");
//        reviewValues.put(MoviesContract.ReviewsEntry.COLUMN_CONTENT, "great movie");
//
//        return reviewValues;
//    }
//
//    static long insertMovieValues(Context context) {
//        // insert our test records into the database
//        MoviesDbHelper dbHelper = new MoviesDbHelper(context);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues testValues = TestUtilities.createMoviesTestValues();
//
//
//        long moviesRowId = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, testValues);
//
//        // Verify we got a row back.
//        assertTrue("Error: Failure to insert Movie values in movies table", moviesRowId != -1);
//
//        return moviesRowId;
//    }
//
//    /*
//       Students: The functions we provide inside of TestProvider use this utility class to test
//       the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
//       CTS tests.
//
//       Note that this only tests that the onChange function is called; it does not test that the
//       correct Uri is returned.
//    */
//    static class TestContentObserver extends ContentObserver {
//        final HandlerThread mHT;
//        boolean mContentChanged;
//
//        static TestContentObserver getTestContentObserver() {
//            HandlerThread ht = new HandlerThread("ContentObserverThread");
//            ht.start();
//            return new TestContentObserver(ht);
//        }
//
//        private TestContentObserver(HandlerThread ht) {
//            super(new Handler(ht.getLooper()));
//            mHT = ht;
//        }
//
//        // On earlier versions of Android, this onChange method is called
//        @Override
//        public void onChange(boolean selfChange) {
//            onChange(selfChange, null);
//        }
//
//        @Override
//        public void onChange(boolean selfChange, Uri uri) {
//            mContentChanged = true;
//        }
//
//        public void waitForNotificationOrFail() {
//            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
//            // It's useful to look at the Android CTS source for ideas on how to test your Android
//            // applications.  The reason that PollingCheck works is that, by default, the JUnit
//            // testing framework is not running on the main Android application thread.
//            new PollingCheck(5000) {
//                @Override
//                protected boolean check() {
//                    return mContentChanged;
//                }
//            }.run();
//            mHT.quit();
//        }
//    }
//
//    static TestContentObserver getTestContentObserver() {
//        return TestContentObserver.getTestContentObserver();
//    }
//
//
//
//
//
//
//}
