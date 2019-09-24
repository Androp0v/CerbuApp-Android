package com.raulmonton.cerbuapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class NotificationsDatabaseHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "notificationsDatabase.db";
    private static String DB_PATH = "";
    private static int database_version = 1;
    private static String database_name = "database";

    private String TABLE_NAME = "notificationsDatabase";
    private String COLUMN_ID = "id";
    private String COLUMN_TITLES = "titles";
    private String COLUMN_MESSAGES = "messages";

    private SQLiteDatabase myDataBase;

    public NotificationsDatabaseHelper(Context context) {
        super(context, database_name, null, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //myDataBase = getReadableDatabase();
        myDataBase = sqLiteDatabase;
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, titles TEXT, messages TEXT)";
        myDataBase.execSQL(CREATE_TABLE);
        //myDataBase = sqLiteDatabase;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i("MyTag", "onUpgrade() called");
    }


    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    public List<String> getAllMessages(){

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " +
                COLUMN_ID + " DESC";
        myDataBase = this.getReadableDatabase();

        if (myDataBase != null){
            Cursor cursor = myDataBase.rawQuery(selectQuery, null);

            List<String> messages = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    messages.add(cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGES)));
                } while (cursor.moveToNext());
            }

            myDataBase.close();

            return  messages;
        }
            return new ArrayList<>();
    }

    public List<String> getAllTitles(){

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " +
                COLUMN_ID + " DESC";
        myDataBase = this.getReadableDatabase();

        if (myDataBase != null){
            Cursor cursor = myDataBase.rawQuery(selectQuery, null);

            List<String> messages = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    messages.add(cursor.getString(cursor.getColumnIndex(COLUMN_TITLES)));
                } while (cursor.moveToNext());
            }

            myDataBase.close();

            return  messages;
        }

        return new ArrayList<>();

    }

    public void addMessage(String title, String message){
        myDataBase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLES, title);
        cv.put(COLUMN_MESSAGES, message);
        myDataBase.insert(TABLE_NAME, null, cv);
        myDataBase.close();
    }

}
