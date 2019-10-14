package com.raulmonton.cerbuapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import static com.raulmonton.cerbuapp.MainActivity.MyPREFERENCES;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Switch orderSwitch = findViewById(R.id.orderSwitch);
        final Switch roomSwitch = findViewById(R.id.roomsSwitch);
        final Switch notifsSwitch = findViewById(R.id.notifSwitch);
        final ImageView lockImage = findViewById(R.id.lockImageView);
        final ImageView lockImageNotifs = findViewById(R.id.lockNotifImageView);

        final SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        final boolean becaUnlocked = preferences.getBoolean("becaUnlocked", false);
        final boolean notifsUnlocked = preferences.getBoolean("notifsUnlocked", false);
        boolean switchSavedState = preferences.getBoolean("nameFirst", true);
        boolean roomSwitchSavedState = preferences.getBoolean("showRooms", false);
        boolean notifsSwitchSavedState = preferences.getBoolean("showNotifs", false);

        if (switchSavedState){
            orderSwitch.setChecked(false);
        }
        else {
            orderSwitch.setChecked(true);
        }

        if (roomSwitchSavedState){
            roomSwitch.setChecked(true);
        }
        else {
            roomSwitch.setChecked(false);
        }

        if (notifsSwitchSavedState){
            notifsSwitch.setChecked(true);
        }
        else {
            notifsSwitch.setChecked(false);
        }

        if (becaUnlocked){
            roomSwitch.setEnabled(true);
            lockImage.setImageResource(R.drawable.ic_lock_open);
            lockImage.setClickable(false);
        }
        else {
            roomSwitch.setEnabled(false);
            lockImage.setImageResource(R.drawable.ic_lock);
        }

        if (notifsUnlocked){
            notifsSwitch.setEnabled(true);
            lockImageNotifs.setImageResource(R.drawable.ic_lock_open);
            lockImageNotifs.setClickable(false);
        }
        else {
            notifsSwitch.setEnabled(false);
            lockImageNotifs.setImageResource(R.drawable.ic_lock);
        }

        orderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {

                if (bChecked) {

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("nameFirst",false);
                    editor.apply();

                } else {

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("nameFirst",true);
                    editor.apply();

                }
            }
        });

        roomSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {

                if (bChecked) {

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("showRooms",true);
                    editor.apply();

                } else {

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("showRooms",false);
                    editor.apply();

                }
            }
        });

        notifsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {

                if (bChecked) {

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("showNotifs",true);
                    editor.apply();

                } else {

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("showNotifs",false);
                    editor.apply();

                }
            }
        });

        lockImage.setClickable(true);
        lockImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!becaUnlocked){

                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    builder.setMessage("Introduce la contraseña").setTitle("Contraseña");

                    final EditText input = new EditText(SettingsActivity.this);
                    input.setPadding(24,24,24,24);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builder.setView(input);

                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String inputText = input.getText().toString();
                            if (inputText.equals("Tungsteno")){
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("becaUnlocked",true);
                                editor.apply();
                                roomSwitch.setEnabled(true);
                                lockImage.setImageResource(R.drawable.ic_lock_open);
                            }
                        }
                    });

                    builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
                }
        });

        lockImageNotifs.setClickable(true);
        lockImageNotifs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!notifsUnlocked){

                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    builder.setMessage("Introduce la contraseña").setTitle("Contraseña");

                    final EditText input = new EditText(SettingsActivity.this);
                    input.setPadding(24,24,24,24);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builder.setView(input);

                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String inputText = input.getText().toString();
                            if (inputText.equals("Tungsteno")){
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("notifsUnlocked",true);
                                editor.apply();
                                notifsSwitch.setEnabled(true);
                                lockImageNotifs.setImageResource(R.drawable.ic_lock_open);
                            }
                        }
                    });

                    builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

}