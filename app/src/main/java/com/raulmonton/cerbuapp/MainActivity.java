package com.raulmonton.cerbuapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;

public class MainActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "AppPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Enable notifications by subscribing to topic: "all"
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("FiltersActive", false);

        editor.putBoolean("adjuntos", false);
        editor.putBoolean("favoritos", false);

        editor.putBoolean("male", true);
        editor.putBoolean("female", true);
        editor.putBoolean("NBothers", true);

        editor.putBoolean("100s", true);
        editor.putBoolean("200s", true);
        editor.putBoolean("300s", true);
        editor.putBoolean("400s", true);

        editor.apply();


        ImageView orlaImageView = findViewById(R.id.orlaImageView);
        orlaImageView.setClickable(true);
        orlaImageView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View V) {

                Intent intent = new Intent(V.getContext(), TabActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        LinearLayout boletinLayout = findViewById(R.id.boletinLayout);
        boletinLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent boletinIntent = new Intent(MainActivity.this, BoletinActivity.class);
                startActivity(boletinIntent);
            }
        });

        LinearLayout barcodeLayout = findViewById(R.id.barcodeLayout);
        barcodeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent barcodeIntent = new Intent(MainActivity.this, BarcodeActivity.class);
                startActivity(barcodeIntent);
            }
        });

        LinearLayout menuLayout = findViewById(R.id.menuLayout);
        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuIntent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(menuIntent);
            }
        });

        LinearLayout magazineLayout = findViewById(R.id.magazineLayout);
        magazineLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent magazineIntent = new Intent(MainActivity.this, MagazineActivity.class);
                startActivity(magazineIntent);
            }
        });

        LinearLayout notificationsLayout = findViewById(R.id.notificationLayout);
        notificationsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuIntent = new Intent(MainActivity.this, NotificationsActivity.class);
                startActivity(menuIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent) ;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
