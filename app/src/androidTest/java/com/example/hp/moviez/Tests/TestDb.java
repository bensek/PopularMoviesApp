//package com.example.hp.moviez.Tests;
//
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.test.AndroidTestCase;
//
//import com.example.hp.moviez.data.MoviesContract;
//import com.example.hp.moviez.data.MoviesDbHelper;
//
//import java.util.HashSet;
//
///**
// * Created by HP on 7/20/2016.
// */
//public class TestDb extends AndroidTestCase {
//
//
//    // Since we want each test to start with a clean slate
//    void deleteTheDatabase() {
//        mContext.deleteDatabase(MoviesDbHelper.DATABASE_NAME);
//    }
//
//    /*
//       This function gets called before each test is executed to delete the database.  This makes
//       sure that we always have a clean test.
//    */
//    public void setUp() {
//        deleteTheDatabase();
//    }
//
//    public void testCreateDb() throws Throwable {
//        // build a HashSet of all of the table names we wish to look for
//        // Note that there will be another table in the DB that stores the
//        // Android metadata (db version information)
//        final HashSet<String> tableNameHashSet = new HashSet<String>();
//        tableNameHashSet.add(MoviesContract.MoviesEntry.TABLE_NAME);
//        tableNameHashSet.add(MoviesContract.TrailersEntry.TABLE_NAME);
//        tableNameHashSet.add(MoviesContract.ReviewsEntry.TABLE_NAME);
//
//        mContext.deleteDatabase(MoviesDbHelper.DATABASE_NAME);
//        SQLiteDatabase db = new MoviesDbHelper(
//                this.mContext).getWritableDatabase();
//        assertEquals(true, db.isOpen());
//
//        // have we created the tables we want?
//        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
//
//        assertTrue("Error: This means that the database has not been created correctly",
//                c.moveToFirst());
//
//        // verify that the tables have been created
//        do {
//            tableNameHashSet.remove(c.getString(0));
//        } while( c.moveToNext() );
//
//        // if this fails, it means that your database doesn't contain both the location entry
//        // and weather entry tables
//        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
//                tableNameHashSet.isEmpty());
//
//        // now, do our tables contain the correct columns?
//        c = db.rawQuery("PRAGMA table_info(" + MoviesContract.MoviesEntry.TABLE_NAME + ")",
//                null);
//
//        assertTrue("Error: This means that we were unable to query the database for table information.",
//                c.moveToFirst());
//
//        // Build a HashSet of all of the column names we want to look for
//        final HashSet<String> moviesColumnHashSet = new HashSet<String>();
//        moviesColumnHashSet.add(MoviesContract.MoviesEntry._ID);
//        moviesColumnHashSet.add(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID);
//        moviesColumnHashSet.add(MoviesContract.MoviesEntry.COLUMN_NAME);
//        moviesColumnHashSet.add(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH);
//        moviesColumnHashSet.add(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE);
//        moviesColumnHashSet.add(MoviesContract.MoviesEntry.COLUMN_RUNTIME);
//        moviesColumnHashSet.add(MoviesContract.MoviesEntry.COLUMN_RATING);
//        moviesColumnHashSet.add(MoviesContract.MoviesEntry.COLUMN_OVERVIEW);
//
//
//        int columnNameIndex = c.getColumnIndex("name");
//        do {
//            String columnName = c.getString(columnNameIndex);
//            moviesColumnHashSet.remove(columnName);
//        } while(c.moveToNext());
//
//        // if this fails, it means that your database doesn't contain all of the required location
//        // entry columns
//        assertTrue("Error: The database doesn't contain all of the required location entry columns",
//                moviesColumnHashSet.isEmpty());
//        db.close();
//    }
//
//
//
//    public long testMoviesTable() {
//        MoviesDbHelper moviesDbHelper = new MoviesDbHelper(mContext);
//        SQLiteDatabase db = moviesDbHelper.getWritableDatabase();
//        // Creating Content Values for inserting.
//        ContentValues testValues = TestUtilities.createMoviesTestValues();
//
//        //Inserting the testValues into the db which retruns the row id
//        long moviesRowId = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, testValues);
//        assertTrue("No Values where inserted in the movies table", moviesRowId != -1);
//
//        //Quering the db to receive the cursor of moviesRowId
//
//        Cursor cursor = db.query(
//                MoviesContract.MoviesEntry.TABLE_NAME,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null);
//
//        // Move the cursor to the next row, the method returns true
//
//        assertTrue("Error: There are no entries in the query ", cursor.moveToFirst() );
//
//        // Validating the data inserted with those returned in the cursor
//
//        TestUtilities.validateRow("Error: Location Query Validation failed", cursor, testValues);
//        assertFalse("Error: More than one movie returned from movies query", cursor.moveToNext());
//
//        //closing the curso and database
//        cursor.close();
//        db.close();
//
//        return moviesRowId;
//    }
//
//    public void testTrailersTable(){
//
//        //first get the id of the the movie entered
//        long moviesRowId = testMoviesTable();
//
//        // get a reference of your database
//        MoviesDbHelper moviesDbHelper = new MoviesDbHelper(mContext);
//        SQLiteDatabase db = moviesDbHelper.getWritableDatabase();
//
//        //create ContentValues for the Trailers table to be inserted
//        ContentValues trailerValues = TestUtilities.createTrailersValues(moviesRowId);
//
//        //insert the trailers values into the trailer table
//        long trailersRowId = db.insert(MoviesContract.TrailersEntry.TABLE_NAME, null, trailerValues);
//        assertTrue(trailersRowId != -1);
//
//        //Query the entered values
//        Cursor trailerCursor = db.query(
//                MoviesContract.TrailersEntry.TABLE_NAME,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null);
//
//        // moving the cursor to the first valid db row
//        assertTrue("Error: No values returned from the query", trailerCursor.moveToFirst());
//
//        // Validate the returned cursor values with the inserted ContentValues
//        TestUtilities.validateRow("Validation failed", trailerCursor, trailerValues);
//
//        // move the cursor to demonstrate that there is only one record in the table
//        assertFalse( "Error: More than one record returned from trailer query",
//                trailerCursor.moveToNext() );
//
//        trailerCursor.close();
//        db.close();
//    }
//
//    public void testReviewsTable(){
//
//        //first get the id of the the movie entered
//        long moviesRowId = testMoviesTable();
//
//        // get a reference of your database
//        MoviesDbHelper moviesDbHelper = new MoviesDbHelper(mContext);
//        SQLiteDatabase db = moviesDbHelper.getWritableDatabase();
//
//        //create ContentValues for the Trailers table to be inserted
//        ContentValues reviewValues = TestUtilities.createReviewsValues(moviesRowId);
//
//        //insert the trailers values into the trailer table
//        long reviewsRowId = db.insert(MoviesContract.ReviewsEntry.TABLE_NAME, null, reviewValues);
//        assertTrue(reviewsRowId != -1);
//
//        //Query the entered values
//        Cursor reviewCursor = db.query(
//                MoviesContract.ReviewsEntry.TABLE_NAME,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null);
//
//        // moving the cursor to the first valid db row
//        assertTrue("Error: No values returned from the query", reviewCursor.moveToFirst());
//
//        // Validate the returned cursor values with the inserted ContentValues
//        TestUtilities.validateRow("Validation failed", reviewCursor, reviewValues);
//
//        // move the cursor to demonstrate that there is only one record in the table
//        assertFalse( "Error: More than one record returned from trailer query",
//                reviewCursor.moveToNext() );
//
//        reviewCursor.close();
//        db.close();
//    }
//}
