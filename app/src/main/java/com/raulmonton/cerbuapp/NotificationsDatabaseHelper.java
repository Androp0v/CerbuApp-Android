package com.raulmonton.cerbuapp;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NotificationsDatabaseHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "notificationsDatabase.db";
    private static String DB_PATH = "";
    private static int database_version = 1;
    private static String database_name = "database";

    private SQLiteDatabase myDataBase;


    public NotificationsDatabaseHelper(Context context) {
        super(context, database_name, null, database_version);
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("MyTag", "onCreate() called");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i("MyTag", "onUpgrade() called");
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(!dbExist){
            SQLiteDatabase db = this.getReadableDatabase();
            db.execSQL("CREATE TABLE \"notifications\" (\"id\" INTEGER PRIMARY KEY AUTOINCREMENT,\"titles\" TEXT,\"messages\" TEXT);");
        }
    }

    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null;
    }


    public void openDataBase() throws SQLException {

        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

}
