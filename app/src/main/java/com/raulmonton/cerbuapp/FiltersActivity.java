package com.raulmonton.cerbuapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.CompoundButton;
import android.widget.Switch;
import static com.raulmonton.cerbuapp.MainActivity.MyPREFERENCES;

public class FiltersActivity extends AppCompatActivity {

    Switch adjuntosSwitch;
    Switch favoritesSwitch;

    Switch maleSwitch;
    Switch femaleSwitch;
    Switch NBothersSwitch;

    Switch oneSwitch;
    Switch twoSwitch;
    Switch threeSwitch;
    Switch fourSwitch;

    SharedPreferences preferences;

    void filtersDefaults(){
        if (!adjuntosSwitch.isChecked()
                && !favoritesSwitch.isChecked()
                && maleSwitch.isChecked()
                && femaleSwitch.isChecked()
                && NBothersSwitch.isChecked()
                && oneSwitch.isChecked()
                && twoSwitch.isChecked()
                && threeSwitch.isChecked()
                && fourSwitch.isChecked()){

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("FiltersActive",false);
            editor.apply();
        }else{
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("FiltersActive",true);
            editor.apply();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        adjuntosSwitch = findViewById(R.id.adjuntosSwitch);
        favoritesSwitch = findViewById(R.id.favoritesSwitch);

        maleSwitch = findViewById(R.id.maleSwitch);
        femaleSwitch = findViewById(R.id.femaleSwitch);
        NBothersSwitch = findViewById(R.id.NBOthersSwitch);

        oneSwitch = findViewById(R.id.oneSwitch);
        twoSwitch = findViewById(R.id.twoSwitch);
        threeSwitch = findViewById(R.id.threeSwitch);
        fourSwitch = findViewById(R.id.fourSwitch);

        boolean adjuntosSavedState = preferences.getBoolean("adjuntos", false);
        boolean favoritesSavedState = preferences.getBoolean("favoritos", false);

        boolean maleSavedState = preferences.getBoolean("male", true);
        boolean femaleSavedState = preferences.getBoolean("female", true);
        boolean NBothersSavedState = preferences.getBoolean("NBothers", true);

        boolean oneSavedState = preferences.getBoolean("100s", true);
        boolean twoSavedState = preferences.getBoolean("200s", true);
        boolean threeSavedState = preferences.getBoolean("300s", true);
        boolean fourSavedState = preferences.getBoolean("400s", true);

        //Set the buttons states
        {
            if (adjuntosSavedState) {
                adjuntosSwitch.setChecked(true);
            } else {
                adjuntosSwitch.setChecked(false);
            }
            if (favoritesSavedState) {
                favoritesSwitch.setChecked(true);
            } else {
                favoritesSwitch.setChecked(false);
            }

            if (maleSavedState) {
                maleSwitch.setChecked(true);
            } else {
                maleSwitch.setChecked(false);
            }
            if (femaleSavedState) {
                femaleSwitch.setChecked(true);
            } else {
                femaleSwitch.setChecked(false);
            }
            if (NBothersSavedState) {
                NBothersSwitch.setChecked(true);
            } else {
                NBothersSwitch.setChecked(false);
            }

            if (oneSavedState) {
                oneSwitch.setChecked(true);
            } else {
                oneSwitch.setChecked(false);
            }
            if (twoSavedState) {
                twoSwitch.setChecked(true);
            } else {
                twoSwitch.setChecked(false);
            }
            if (threeSavedState) {
                threeSwitch.setChecked(true);
            } else {
                threeSwitch.setChecked(false);
            }
            if (fourSavedState) {
                fourSwitch.setChecked(true);
            } else {
                fourSwitch.setChecked(false);
            }
        }

        //Listeners
        {
            adjuntosSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                    if (bChecked) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("adjuntos", true);
                        editor.apply();
                    } else {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("adjuntos", false);
                        editor.apply();
                    }
                    filtersDefaults();
                }
            });
            favoritesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                    if (bChecked) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("favoritos", true);
                        editor.apply();
                    } else {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("favoritos", false);
                        editor.apply();
                    }
                    filtersDefaults();
                }
            });

            maleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                    if (bChecked) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("male", true);
                        editor.apply();
                    } else {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("male", false);
                        editor.apply();
                    }
                    filtersDefaults();
                }
            });
            femaleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                    if (bChecked) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("female", true);
                        editor.apply();
                    } else {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("female", false);
                        editor.apply();
                    }
                    filtersDefaults();
                }
            });
            NBothersSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                    if (bChecked) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("NBothers", true);
                        editor.apply();
                    } else {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("NBothers", false);
                        editor.apply();
                    }
                    filtersDefaults();
                }
            });

            oneSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                    if (bChecked) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("100s", true);
                        editor.apply();
                    } else {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("100s", false);
                        editor.apply();
                    }
                    filtersDefaults();
                }
            });
            twoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                    if (bChecked) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("200s", true);
                        editor.apply();
                    } else {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("200s", false);
                        editor.apply();
                    }
                    filtersDefaults();
                }
            });
            threeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                    if (bChecked) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("300s", true);
                        editor.apply();
                    } else {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("300s", false);
                        editor.apply();
                    }
                    filtersDefaults();
                }
            });
            fourSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                    if (bChecked) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("400s", true);
                        editor.apply();
                    } else {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("400s", false);
                        editor.apply();
                    }
                    filtersDefaults();
                }
            });
        }



    }

}