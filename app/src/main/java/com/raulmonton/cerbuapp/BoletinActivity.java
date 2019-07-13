package com.raulmonton.cerbuapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

public class BoletinActivity extends AppCompatActivity {

    AnimationDrawable boletinAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boletin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView boletinTop = findViewById(R.id.boletinTop);
        boletinTop.setImageResource(R.drawable.boletin_animation);
        boletinAnimation = (AnimationDrawable) boletinTop.getDrawable();
        boletinAnimation.start();
    }
}
