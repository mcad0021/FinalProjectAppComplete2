package com.example.tdog1.finalprojectapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/** database helper class of movie*/
public class MovieDatabaseHelper extends SQLiteOpenHelper {  /** inherit from SQLiteOpenHelper class*/
    /** class variables*/
    private static String DATABASE_NAME = "Favorites.db";
    private static int VERSION_NUM = 1;
    private static final String KEY_TITLE = "title";
    private static String TABLE_NAME = "Favorties";


    /** initialize the MovieDatabaseHelper class*/
    MovieDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /** call the oncreate method when MovieDatabaseHelper object is used in another class*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("MovieDatabaseHelper", "Calling onCreate");  /** tell me in debug im in oncreate*/
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + KEY_TITLE + " TEXT PRIMARY KEY );");  /** execute the sql*/
    }
    /** when updating the db*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("MovieDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);  /** tell me the new version and old version of db*/
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);  /**clear database of the table name*/
        onCreate(db);
    }

    public boolean addData(String item) {  /**add data to the database*/
        SQLiteDatabase db = this.getWritableDatabase();  /**ensure the database is writeable*/
        ContentValues contentValues = new ContentValues();  /** create contentvalue object to be stored in database*/
        contentValues.put(KEY_TITLE, item);  /** set database object to the movie title*/
        long result = 0;
        result = db.insertWithOnConflict(TABLE_NAME, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);  /** insert the content values into table and delete duplicate info*/

        if (result == -1) {
            return true;
        }
        else {
            return false;
        }
}

    public Cursor getData(){  /** used to get the data*/
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;  /** sql query code to process data info*/
        Cursor data = db.rawQuery(query, null); /** query the db to get data when trying to access it*/
        return data;
    }


}
