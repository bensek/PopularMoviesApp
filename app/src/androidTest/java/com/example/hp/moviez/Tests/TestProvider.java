//package com.example.hp.moviez.Tests;
//
//import android.content.ComponentName;
//import android.content.ContentValues;
//import android.content.pm.PackageManager;
//import android.content.pm.ProviderInfo;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.test.AndroidTestCase;
//import android.util.Log;
//
//import com.example.hp.moviez.data.MovieProvider;
//import com.example.hp.moviez.data.MoviesContract;
//import com.example.hp.moviez.data.MoviesDbHelper;
//
///**
// * Created by HP on 7/22/2016.
// */
//public class TestProvider extends AndroidTestCase {
//
//    MovieProvider movieProvider;
//
//    // This method deletes the entire db and tables using the content provider
//    // It also finds weather they have been deleted by querying the db again
//    public void deleteAllRecordsInProvider(){
//        mContext.getContentResolver().delete(
//                MoviesContract.MoviesEntry.CONTENT_URI,
//                null,
//                null
//        );
//        mContext.getContentResolver().delete(
//                MoviesContract.TrailersEntry.CONTENT_URI,
//                null,
//                null
//        );
//
//        Cursor cursor = mContext.getContentResolver().query(
//                MoviesContract.MoviesEntry.CONTENT_URI,
//                null,
//                null,
//                null,
//                null
//        );
//        assertEquals("Error: Records not deleted from the MoviesTable", 0, cursor.getCount());
//        cursor.close();
//    }
//
//    public void deleteAllRecords(){
//        deleteAllRecordsInProvider();
//    }
//
//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();
//        deleteAllRecords();
//    }
//
//    public void testProviderRegistry(){
//        PackageManager pm = mContext.getPackageManager();
//
//        ComponentName componentName = new ComponentName(mContext.getPackageName(),
//                MovieProvider.class.getName());
//
//        try{
//            // Fetch the provider info using the component name from the PackageManager
//            // This throws an exception if the provider isn't registered.
//            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
//
//            // Make sure that the registered authority matches the authority from the Contract.
//            assertEquals("Error: MovieProvider registered with authority: " + providerInfo.authority +
//                            " instead of authority: " + MoviesContract.CONTENT_AUTHORITY,
//                    providerInfo.authority, MoviesContract.CONTENT_AUTHORITY);
//
//        } catch (PackageManager.NameNotFoundException e) {
//            // the provider isn't registered correctly.
//            assertTrue("Error: MovieProvider not registered at " + mContext.getPackageName(),
//                    false);
//        }
//    }
//
//

//    public void testGetType(){
//        // content://com.example.hp.moviez/movies/
//        String type = mContext.getContentResolver().getType(MoviesContract.MoviesEntry.CONTENT_URI);
//
//        assertEquals("error : the MoviesEntry Content Uri should return Dir type ",
//                MoviesContract.MoviesEntry.CONTENT_TYPE, type);
//
//        String testMovieId = "56983";
//        //content://com.example.hp.moviez/movies/56983
//        type = mContext.getContentResolver().getType(
//                MoviesContract.MoviesEntry.buildMovieUri(testMovieId));
//
//        assertEquals("Error : the MoviesEntry CONTENT_URI with movieId should return MoviesEntry.CONTENT_ITEM_TYPE ",
//                MoviesContract.MoviesEntry.CONTENT_ITEM_TYPE, type);
//
//        // Make a test for the posterspath
////        type = mContext.getContentResolver().getType()
//
//
//        //content://com.example.hp.moviez/trailers
//        type = mContext.getContentResolver().getType(MoviesContract.TrailersEntry.CONTENT_URI);
//
//        assertEquals("Error : the TrailersEntry CONTENT_URI should return CONTENT_TYPE",
//                MoviesContract.TrailersEntry.CONTENT_TYPE, type);
//
//        //content://com.example.hp.moviez/trailers/id
//        type = mContext.getContentResolver().getType(
//                MoviesContract.TrailersEntry.buildTrailersUri(testMovieId));
//
//        assertEquals("Error: TrailersEntry CONTENT_URI with id should return DIR type",
//                MoviesContract.TrailersEntry.CONTENT_TYPE, type);
//
//        //content://com.example.hp.moviez/reviews
//        type = mContext.getContentResolver().getType(MoviesContract.ReviewsEntry.CONTENT_URI);
//
//        assertEquals("Error : the TrailersEntry CONTENT_URI should return CONTENT_TYPE",
//                MoviesContract.ReviewsEntry.CONTENT_TYPE, type);
//
//        //content://com.example.hp.moviez/trailers/id
//        type = mContext.getContentResolver().getType(
//                MoviesContract.ReviewsEntry.buildReviewsUri(testMovieId));
//
//        assertEquals("Error: TrailersEntry CONTENT_URI with id should return DIR type",
//                MoviesContract.ReviewsEntry.CONTENT_TYPE, type);
//    }
//
//    public void testBasicMoviesQuery(){
//        // First insert the records into the database
//        MoviesDbHelper moviesDbHelper = new MoviesDbHelper(mContext);
//        SQLiteDatabase db = moviesDbHelper.getWritableDatabase();
//
//        ContentValues testValues = TestUtilities.createMoviesTestValues();
//        long movieWordId  = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, testValues);
//        assertTrue("Unable to Insert values into MoviesTable", movieWordId != -1);
//
//        db.close();
//
//        //Test the basic content provider query
//        Cursor movieCursor = mContext.getContentResolver().query(
//                MoviesContract.MoviesEntry.CONTENT_URI,
//                null,
//                null,
//                null,
//                null);
//        movieCursor.moveToFirst();
//        //Validating the queried cursor with the inserted values
//        TestUtilities.validateCursor("Test BasicMoviesQueries", movieCursor, testValues);
//
//    }
//
//    // Testing the basic query for trailers and reviews table
//    public void testBasicTrailersQuery() {
//
//        // get a reference of your database
//        MoviesDbHelper moviesDbHelper = new MoviesDbHelper(mContext);
//        SQLiteDatabase db = moviesDbHelper.getWritableDatabase();
//
//        ContentValues testValues = TestUtilities.createMoviesTestValues();
//        long moviesRowId = TestUtilities.insertMovieValues(mContext);
//
//        //create ContentValues for the Trailers table to be inserted
//        ContentValues trailerValues = TestUtilities.createTrailersValues(moviesRowId);
//
//
//        //insert the trailers values into the trailer table
//        long trailersRowId = db.insert(MoviesContract.TrailersEntry.TABLE_NAME, null, trailerValues);
//        assertTrue("Unable to Insert TrailersEntry into the Database", trailersRowId != -1);
//
//        //Query the entered values
//        Cursor trailerCursor = mContext.getContentResolver().query(
//                MoviesContract.TrailersEntry.CONTENT_URI,
//                null,
//                null,
//                null,
//                null);
//        Log.d("dcncs", "test " + trailerCursor.getCount());
//        // moving the cursor to the first valid db row
//        assertTrue("Error: No values returned from the query", trailerCursor.moveToFirst());
//
//        // Validate the returned cursor values with the inserted ContentValues
//        TestUtilities.validateRow("Validation failed", trailerCursor, trailerValues);
//    }
//
//    public void testBasicReviewsQuery() {
//
//        // get a reference of your database
//        MoviesDbHelper moviesDbHelper = new MoviesDbHelper(mContext);
//        SQLiteDatabase db = moviesDbHelper.getWritableDatabase();
//
//        ContentValues testValues = TestUtilities.createMoviesTestValues();
//        long moviesRowId = TestUtilities.insertMovieValues(mContext);
//
//        //create ContentValues for the Trailers table to be inserted
//        ContentValues reviewValues = TestUtilities.createReviewsValues(moviesRowId);
//
//        //insert the trailers values into the trailer table
//        long reviewsRowId = db.insert(MoviesContract.ReviewsEntry.TABLE_NAME, null, reviewValues);
//        assertTrue("Unable to Insert ReviewsEntry into the Database", reviewsRowId != -1);
//
//        //Query the entered values
//        Cursor reviewCursor = mContext.getContentResolver().query(
//                MoviesContract.ReviewsEntry.CONTENT_URI,
//                null,
//                null,
//                null,
//                null);
//        Log.d("d", "RevCursor  = " + reviewCursor.getCount() + " = values inserted :"+ reviewValues.size());
//        // moving the cursor to the first valid db row
//        assertTrue("Error: No values returned from the query", reviewCursor.moveToFirst());
//
//        // Validate the returned cursor values with the inserted ContentValues
//        TestUtilities.validateRow("Validation failed", reviewCursor, reviewValues);
//    }
//
//    public void testInsertReadProvider(){
//        ContentValues testValues = TestUtilities.createMoviesTestValues();
//
//        //First get a content observer for our insert using the content resolver
//        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
//        mContext.getContentResolver().registerContentObserver(MoviesContract.MoviesEntry.CONTENT_URI, true, tco);
//        Uri movieUri = mContext.getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, testValues);
//
//        tco.waitForNotificationOrFail();
//        mContext.getContentResolver().unregisterContentObserver(tco);
//
//        long movieRowId = ContentUris.parseId(movieUri);
//
//        //Verying if the row is returned
//        assertTrue("Error: Insertion of movie failed", movieRowId != -1);
//
//        //If the data was inserted try to query it and see
//        Cursor cursor = mContext.getContentResolver().query(
//                MoviesContract.MoviesEntry.CONTENT_URI,
//                null,
//                null,
//                null,
//                null);
//       // cursor.moveToFirst();
//
//        TestUtilities.validateCursor("testInsertReadProvider. Error validating MoviesEntry.",
//                cursor, testValues);
//
//        //If the valiation works now insert Trailers and maybe Reviews
//        ContentValues trailerValues = TestUtilities.createTrailersValues(3);//movieRowId);
//        ContentValues reviewsValues = TestUtilities.createReviewsValues(3);//movieRowId);
//
//        tco = TestUtilities.getTestContentObserver();
//        mContext.getContentResolver().registerContentObserver(MoviesContract.TrailersEntry.CONTENT_URI, true, tco);
//        mContext.getContentResolver().registerContentObserver(MoviesContract.ReviewsEntry.CONTENT_URI, true, tco);
//
//        Uri trailerInsertUri = mContext.getContentResolver()
//                .insert(MoviesContract.TrailersEntry.CONTENT_URI, trailerValues);
//        assertTrue("Error inserting into trailerstable",trailerInsertUri != null);
//
//        Uri reviewsInsertUri = mContext.getContentResolver()
//                .insert(MoviesContract.ReviewsEntry.CONTENT_URI, reviewsValues);
//        assertTrue("Error while inserting into reviewstable",reviewsInsertUri != null);
//
//        //checking whether the content observer was called
//        tco.waitForNotificationOrFail();
//        mContext.getContentResolver().unregisterContentObserver(tco);
//
//        //Query the inserted row
//        Cursor trailerCursor = mContext.getContentResolver().query(
//                MoviesContract.TrailersEntry.CONTENT_URI,
//                null,
//                null,
//                null,
//                null);
//
//        //trailerCursor.moveToFirst();
//
//        TestUtilities.validateCursor("testInserting .error validating TrailersEntry insert.",
//                trailerCursor, trailerValues);
//
//        Cursor reviewCursor = mContext.getContentResolver().query(
//                MoviesContract.ReviewsEntry.CONTENT_URI,
//                null,
//                null,
//                null,
//                null);
//        //reviewCursor.moveToFirst();
//
//        TestUtilities.validateCursor("testInserting .error validating ReviewsEntry insert.",
//                reviewCursor, reviewsValues);
//
//
//        // validating that our two joins are perfectly working
//
//    }


// Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the delete functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
//    public void testDeleteRecords() {
//        testInsertReadProvider();
//
//        // Register a content observer for our movie delete.
//        TestUtilities.TestContentObserver movieObserver = TestUtilities.getTestContentObserver();
//        mContext.getContentResolver().registerContentObserver(MoviesContract.MoviesEntry.CONTENT_URI, true, movieObserver);
//
//        // Register a content observer for our trailer delete.
//        TestUtilities.TestContentObserver trailerObserver = TestUtilities.getTestContentObserver();
//        mContext.getContentResolver().registerContentObserver(MoviesContract.TrailersEntry.CONTENT_URI, true, trailerObserver);
//
//        deleteAllRecordsInProvider();
//
//        // Students: If either of these fail, you most-likely are not calling the
//        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
//        // delete.  (only if the insertReadProvider is succeeding)
//        movieObserver.waitForNotificationOrFail();
//        trailerObserver.waitForNotificationOrFail();
//
//        mContext.getContentResolver().unregisterContentObserver(movieObserver);
//        mContext.getContentResolver().unregisterContentObserver(trailerObserver);
//    }

//    private static final int BULK_INSERT_RECORDS_TO_INSERT = 4;
//    static ContentValues[] createBulkInsertTrailerValues(long movieRowId){
//
//        ContentValues[] contentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];
//
//        for(int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++){
//            ContentValues trailerValues = new ContentValues();
//            trailerValues.put(MoviesContract.TrailersEntry.COLUMN_MOVIE_KEY, movieRowId);
//            trailerValues.put(MoviesContract.TrailersEntry.COLUMN_TRAILER_NAME, "Official Teaser");
//            trailerValues.put(MoviesContract.TrailersEntry.COLUMN_YOUTUBE_URL, "www.youtube.com/watch?v=SUXWAEX2jlg" + i);
//            trailerValues.put(MoviesContract.TrailersEntry.COLUMN_IMAGE_URL, "http://img.youtube.com/vi/f_9WTRhdsSE/default.jpg");
//            contentValues[i] = trailerValues;
//        }
//        return contentValues;
//    }
//    static ContentValues[] createBulkInsertReviewsValues(long movieRowId){
//        ContentValues[] contentValues2 = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];
//
//        for(int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++){
//            ContentValues reviewsValues = new ContentValues();
//            reviewsValues.put(MoviesContract.ReviewsEntry.COLUMN_MOVIE_KEY, movieRowId);
//            reviewsValues.put(MoviesContract.ReviewsEntry.COLUMN_AUTHOR, "Ochieng");
//            reviewsValues.put(MoviesContract.ReviewsEntry.COLUMN_CONTENT, "It was a great movie" + i);
//            contentValues2[i] = reviewsValues;
//        }
//        return contentValues2;
//    }
//
//
// // BulkInsert ContentProvider function.
//    public void testBulkInsert() {
//        // first, let's create a movie
//        ContentValues testValues = TestUtilities.createMoviesTestValues();
//        Uri movieUri = mContext.getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, testValues);
//        long movieRowId = ContentUris.parseId(movieUri);
//
//        // Verify we got a row back.
//        assertTrue("A movie has been inserted into the movie table",movieRowId != -1);
//
//        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
//        // the round trip.
//
//        // A cursor is your primary interface to the query results.
//        Cursor cursor = mContext.getContentResolver().query(
//                MoviesContract.MoviesEntry.CONTENT_URI,
//                null, // leaving "columns" null just returns all the columns.
//                null, // cols for "where" clause
//                null, // values for "where" clause
//                null  // sort order
//        );
//
//        TestUtilities.validateCursor("testBulkInsert. Error validating MoviesEntry.",
//                cursor, testValues);
//
//        // Now we can bulkInsert some weather.  In fact, we only implement BulkInsert for trailers and reviews
//        // entries.  With ContentProviders, you really only have to implement the features you
//        // use, after all.
//        ContentValues[] bulkInsertContentValues = createBulkInsertTrailerValues(movieRowId);
//        ContentValues[] bulkInsertRevContentValues = createBulkInsertReviewsValues(movieRowId);
//
//        // Register a content observer for our bulk insert.
//        TestUtilities.TestContentObserver trailerObserver = TestUtilities.getTestContentObserver();
//        mContext.getContentResolver().registerContentObserver(MoviesContract.TrailersEntry.CONTENT_URI, true, trailerObserver);
//
//        TestUtilities.TestContentObserver reviewObserver = TestUtilities.getTestContentObserver();
//        mContext.getContentResolver().registerContentObserver(MoviesContract.ReviewsEntry.CONTENT_URI, true, reviewObserver);
//
//        int insertCount = mContext.getContentResolver().bulkInsert(MoviesContract.TrailersEntry.CONTENT_URI, bulkInsertContentValues);
//        int insertCount2 = mContext.getContentResolver().bulkInsert(MoviesContract.ReviewsEntry.CONTENT_URI, bulkInsertRevContentValues);
//
//        // Students:  If this fails, it means that you most-likely are not calling the
//        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
//        // ContentProvider method.
//        trailerObserver.waitForNotificationOrFail();
//        mContext.getContentResolver().unregisterContentObserver(trailerObserver);
//
//        reviewObserver.waitForNotificationOrFail();
//        mContext.getContentResolver().unregisterContentObserver(reviewObserver);
//
//        assertEquals(BULK_INSERT_RECORDS_TO_INSERT, insertCount);
//        assertEquals(BULK_INSERT_RECORDS_TO_INSERT , insertCount2 );
//
//        // A cursor is your primary interface to the query results.
//        Cursor trailerCursor = mContext.getContentResolver().query(
//                MoviesContract.TrailersEntry.CONTENT_URI,
//                null,
//                null,
//                null,
//                MoviesContract.TrailersEntry._ID + " ASC"
//        );
//
//        Cursor reviewCursor = mContext.getContentResolver().query(
//                MoviesContract.ReviewsEntry.CONTENT_URI,
//                null,
//                null,
//                null,
//                MoviesContract.ReviewsEntry._ID + " ASC"
//        );
//
//        // we should have as many records in the database as we've inserted
//        assertEquals( BULK_INSERT_RECORDS_TO_INSERT, trailerCursor.getCount());
//        assertEquals( BULK_INSERT_RECORDS_TO_INSERT, reviewCursor.getCount());
//
//        // and let's make sure they match the ones we created
//        trailerCursor.moveToFirst();
//        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, trailerCursor.moveToNext() ) {
//            TestUtilities.validateRow("testBulkInsert.  Error validating MovieEntry " + i,
//                    trailerCursor, bulkInsertContentValues[i]);
//        }
//
//        reviewCursor.moveToFirst();
//        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, reviewCursor.moveToNext() ) {
//            TestUtilities.validateRow("testBulkInsert.  Error validating MovieEntry " + i,
//                    reviewCursor, bulkInsertRevContentValues[i]);
//        }
//        trailerCursor.close();
//        reviewCursor.close();
//    }
//}


