package com.raulmonton.cerbuapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ProgressBar;

import static com.raulmonton.cerbuapp.R.id.comedorProgressbar;

public class CapacityActivity extends AppCompatActivity {

    private double comedorFractionNumber = 0.8;
    private double salaDeLecturaFractionNumber = 0.2;
    private double bibliotecaFractionNumber = 0.6;

    private int getProgressBarColor(Double fractionNumber){
        if (fractionNumber < 0.3){
            return getResources().getColor(R.color.progressbarGREEN, getTheme());
        }else if (fractionNumber >= 0.3 && fractionNumber < 0.6){
            return getResources().getColor(R.color.progressbarYELLOW, getTheme());
        }else if (fractionNumber >= 0.6 && fractionNumber < 0.8){
            return getResources().getColor(R.color.progressbarORANGE, getTheme());
        }else if (fractionNumber >= 0.8){
            return getResources().getColor(R.color.progressbarRED, getTheme());
        }else{
            return getResources().getColor(R.color.colorSeparator, getTheme());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capacity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ProgressBar comedorProgressbar = findViewById(R.id.comedorProgressbar);
        ProgressBar salaDeLecturaProgressbar = findViewById(R.id.salaDeLecturaProgressbar);
        ProgressBar bibliotecaProgressbar = findViewById(R.id.bibliotecaProgressbar);

        // Set initial progressbar progress
        comedorProgressbar.setProgress((int) (comedorFractionNumber*100));
        salaDeLecturaProgressbar.setProgress((int) (salaDeLecturaFractionNumber*100));
        bibliotecaProgressbar.setProgress((int) (bibliotecaFractionNumber*100));

        // Set initial progressbar colors
        comedorProgressbar.setProgressTintList(ColorStateList.valueOf(getProgressBarColor(comedorFractionNumber)));
        salaDeLecturaProgressbar.setProgressTintList(ColorStateList.valueOf(getProgressBarColor(salaDeLecturaFractionNumber)));
        bibliotecaProgressbar.setProgressTintList(ColorStateList.valueOf(getProgressBarColor(bibliotecaFractionNumber)));

    }
}