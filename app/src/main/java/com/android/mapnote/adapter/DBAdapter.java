package com.android.mapnote.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/* Used this Class form the code given in class and we added some more functionality */


// a user-defined helper class to access an SQLite database
public class DBAdapter {

    // String constants for the columns of a database table
    static final String KEY_ROWID = "_id";
    static final String KEY_LOCATION  = "location";
    static final String KEY_CODE = "code";
    static final String KEY_ITEM = "item";
    static final String TAG       = "DBAdapter"; // for LogCat

    // String constants for database name, database version, and table name
    static final String DATABASE_NAME = "MapNoteDB";
    static final String DATABASE_TABLE = "reminders";
    static final int DATABASE_VERSION = 2;

    // SQL statement for creating a database schema
    static final String DATABASE_CREATE =
            "create table reminders ( _id integer primary key autoincrement, " +
                    "location text not null, item text not null, code text not null );";

    final Context context;   // context for database access

    DatabaseHelper dBHelper; // a private static nested class

    SQLiteDatabase db;

    public DBAdapter( Context ctx )
    {
        this.context = ctx;

        dBHelper = new DatabaseHelper( context ); // private static nested class
    }

    // static nested class: create and upgrade a database
    // SQLiteOpenHelper: database creation and version management
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper( Context ctx )
        {
            super( ctx, DATABASE_NAME, null, DATABASE_VERSION );
        }

        @Override
        // onCreate() is called when the database is created for the first time.
        public void onCreate( SQLiteDatabase db )
        {
            try {
                db.execSQL( DATABASE_CREATE ); // create the database schema

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.i( TAG, "Upgrading database from version " + oldVersion + " to " +
                    newVersion + ", which will destroy all old data");

            db.execSQL("DROP TABLE IF EXISTS reminders");

            onCreate( db ); // invoke SQLiteOpenHelper's onCreate() method
        }
    }// end DatabseHelper


    //--- 1. open the database of reminders ---
    public DBAdapter open() throws SQLException
    {
        db = dBHelper.getWritableDatabase();
        return this;
    }

    //--- 2. closes the database of reminders ---
    public void close()
    {
        dBHelper.close();
    }

    //--- 3. insert a reminder into the database ---
    //       - ContentValues: key/value pairs
    public long insertReminders(String location, String item, String code)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put( KEY_LOCATION, location );
        initialValues.put( KEY_ITEM, item );
        initialValues.put( KEY_CODE, code );

        return db.insert( DATABASE_TABLE, null, initialValues );
    }

    //--- 4. deletes a particular reminder from the database ---
    public boolean deleteReminders(String location)
    {
        return db.delete( DATABASE_TABLE, KEY_LOCATION + " = ?", new String[] { location }) > 0;
    }

    //---5. retrieves all the reminders from the database ---
    //      - Cursor object: a pointer to the result set of the query
    public Cursor getAllReminders()
    {
        return db.query( DATABASE_TABLE,
                new String[] { KEY_ROWID, KEY_LOCATION, KEY_ITEM, KEY_CODE },
                null, null, null, null, null);
    }

    //--- 6. retrieve a particular reminder from the database ---
    //       - Cursor object: a pointer to the result set of the query
    public Cursor getReminder(String location) throws SQLException
    {
        Cursor mCursor =
                db.query( true,
                        DATABASE_TABLE,
                        null,
                        "location=?",
                        new String[] {location},
                        null, null, null, null);

        if ( mCursor != null ) { mCursor.moveToFirst(); } // move the cursor to the first row

        return mCursor;
    }

    //--- 7. retrieve all locations from the database ---
    //       - Cursor object: a pointer to the result set of the query
    public Cursor getLocations() throws SQLException
    {
        Cursor mCursor =
                db.rawQuery("SELECT DISTINCT location FROM reminders", null);

        if ( mCursor != null ) { mCursor.moveToFirst(); } // move the cursor to the first row

        return mCursor;
    }


    //--- 8. retrieve all locations and geodata from the database ---
    //       - Cursor object: a pointer to the result set of the query
    public Cursor getLocationsAndGeo() throws SQLException
    {
        Cursor mCursor =
                db.rawQuery("SELECT DISTINCT location, code FROM reminders", null);

        if ( mCursor != null ) { mCursor.moveToFirst(); } // move the cursor to the first row

        return mCursor;
    }

} // end DBAdapter