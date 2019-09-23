package com.raulmonton.cerbuapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.SQLException;
import android.os.Bundle;

import java.io.IOException;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        NotificationsDatabaseHelper notificationsDatabaseHelper = new NotificationsDatabaseHelper(getApplicationContext());

        try {
            notificationsDatabaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            notificationsDatabaseHelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }
    }
}
