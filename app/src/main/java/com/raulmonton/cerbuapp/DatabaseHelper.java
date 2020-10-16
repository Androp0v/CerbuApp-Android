package com.raulmonton.cerbuapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.raulmonton.cerbuapp.MainActivity.MyPREFERENCES;

public class DatabaseHelper extends SQLiteOpenHelper{
    private static int database_version = 1;
    private static String database_name = "database";
    private static String table_name = "colegiales";
    private static String column_id = "_id";
    private static String column_names = "names";
    private static String column_surnames_1 = "surnames_1";
    private static String column_surnames_2 = "surnames_2";
    private static String column_careers = "careers";
    private static String column_promotions = "promotions";
    private static String column_rooms = "rooms";
    private static String column_becas = "becas";
    private static String column_likes = "likes";
    private static String column_floors = "floors";
    private static String column_genders = "gender";


    private static String DB_NAME = "database.db";
    private static String DB_PATH = "";

    private final Context myContext;
    private SQLiteDatabase myDataBase;


    public DatabaseHelper(Context context) {
        super(context, database_name, null, database_version);
        this.myContext = context;
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("MyTag", "onCreate() called");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i("MyTag", "onUpgrade() called");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(sqLiteDatabase);
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(!dbExist){

            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
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

    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

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


    public ArrayList<Data> getAllData() {
        ArrayList<Data> Datas = new ArrayList<>();

        SharedPreferences preferences = myContext.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        boolean nameFirstSetting = preferences.getBoolean("nameFirst", true);

        String selectQuery;

        if (nameFirstSetting){
            selectQuery = "SELECT  * FROM " + table_name + " ORDER BY " + column_names + " COLLATE UNICODE";
        }else{
            selectQuery = "SELECT  * FROM " + table_name + " ORDER BY " + column_surnames_1 + " COLLATE UNICODE";
        }

        Cursor cursor = myDataBase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Data data = new Data();
                data.setId(cursor.getInt(cursor.getColumnIndex(column_id)));
                data.setName(cursor.getString(cursor.getColumnIndex(column_names)));
                data.setSurname_1(cursor.getString(cursor.getColumnIndex(column_surnames_1)));
                data.setSurname_2(cursor.getString(cursor.getColumnIndex(column_surnames_2)));
                data.setCareer(cursor.getString(cursor.getColumnIndex(column_careers)));
                data.setPromotion(cursor.getInt(cursor.getColumnIndex(column_promotions)));
                data.setRoom(cursor.getString(cursor.getColumnIndex(column_rooms)));
                data.setBeca(cursor.getString(cursor.getColumnIndex(column_becas)));
                data.setLiked(cursor.getInt(cursor.getColumnIndex(column_likes)));
                data.setFloor(cursor.getInt(cursor.getColumnIndex(column_floors)));
                data.setGender(cursor.getInt(cursor.getColumnIndex(column_genders)));

                Datas.add(data);
            } while (cursor.moveToNext());
        }
        // close db connection
        cursor.close();
        // return notes list
        return Datas;
    }

    public ArrayList<Data> getPromData(int promotion) {
        ArrayList<Data> Datas = new ArrayList<>();

        SharedPreferences preferences = myContext.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        boolean nameFirstSetting = preferences.getBoolean("nameFirst", true);

        String selectQuery;

        if (nameFirstSetting){
            selectQuery = "SELECT  * FROM " + table_name + " WHERE " + column_promotions + " = " + promotion + " ORDER BY " + column_names + " COLLATE UNICODE";
        }else{
            selectQuery = "SELECT  * FROM " + table_name + " WHERE " + column_promotions + " = " + promotion + " ORDER BY " + column_surnames_1 + " COLLATE UNICODE";
        }

        Cursor cursor = myDataBase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Data data = new Data();
                data.setId(cursor.getInt(cursor.getColumnIndex(column_id)));
                data.setName(cursor.getString(cursor.getColumnIndex(column_names)));
                data.setSurname_1(cursor.getString(cursor.getColumnIndex(column_surnames_1)));
                data.setSurname_2(cursor.getString(cursor.getColumnIndex(column_surnames_2)));
                data.setCareer(cursor.getString(cursor.getColumnIndex(column_careers)));
                data.setPromotion(cursor.getInt(cursor.getColumnIndex(column_promotions)));
                data.setRoom(cursor.getString(cursor.getColumnIndex(column_rooms)));
                data.setBeca(cursor.getString(cursor.getColumnIndex(column_becas)));
                data.setLiked(cursor.getInt(cursor.getColumnIndex(column_likes)));
                data.setFloor(cursor.getInt(cursor.getColumnIndex(column_floors)));
                data.setGender(cursor.getInt(cursor.getColumnIndex(column_genders)));

                Datas.add(data);
            } while (cursor.moveToNext());
        }
        // close db connection
        cursor.close();
        // return notes list
        return Datas;
    }

    public ArrayList<Data> getMaxPromData(int promotion) {
        ArrayList<Data> Datas = new ArrayList<>();

        SharedPreferences preferences = myContext.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        boolean nameFirstSetting = preferences.getBoolean("nameFirst", true);

        String selectQuery;

        if (nameFirstSetting){
            selectQuery = "SELECT  * FROM " + table_name + " WHERE " + column_promotions + " >= " + promotion + " ORDER BY " + column_names + " COLLATE UNICODE";
        }else{
            selectQuery = "SELECT  * FROM " + table_name + " WHERE " + column_promotions + " >= " + promotion + " ORDER BY " + column_surnames_1 + " COLLATE UNICODE";
        }

        Cursor cursor = myDataBase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Data data = new Data();
                data.setId(cursor.getInt(cursor.getColumnIndex(column_id)));
                data.setName(cursor.getString(cursor.getColumnIndex(column_names)));
                data.setSurname_1(cursor.getString(cursor.getColumnIndex(column_surnames_1)));
                data.setSurname_2(cursor.getString(cursor.getColumnIndex(column_surnames_2)));
                data.setCareer(cursor.getString(cursor.getColumnIndex(column_careers)));
                data.setPromotion(cursor.getInt(cursor.getColumnIndex(column_promotions)));
                data.setRoom(cursor.getString(cursor.getColumnIndex(column_rooms)));
                data.setBeca(cursor.getString(cursor.getColumnIndex(column_becas)));
                data.setLiked(cursor.getInt(cursor.getColumnIndex(column_likes)));
                data.setFloor(cursor.getInt(cursor.getColumnIndex(column_floors)));
                data.setGender(cursor.getInt(cursor.getColumnIndex(column_genders)));

                Datas.add(data);
            } while (cursor.moveToNext());
        }
        // close db connection
        cursor.close();
        // return notes list
        return Datas;
    }


    public void setLikedOnDatabase(int id, int liked) {

        String selectQuery = "UPDATE " + table_name + " SET " + column_likes + " = " + liked + " WHERE " + column_id + " = " + id;
        myDataBase.execSQL(selectQuery);
    }
}