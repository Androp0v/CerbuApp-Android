package com.raulmonton.cerbuapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.raulmonton.cerbuapp.ui.main.PlaceholderFragment;
import com.raulmonton.cerbuapp.ui.main.SectionsPagerAdapter;

import java.util.List;

import static com.raulmonton.cerbuapp.MainActivity.MyPREFERENCES;

public class TabActivity extends AppCompatActivity{

    static final int REQUEST_CODE = 1;
    SectionsPagerAdapter sectionsPagerAdapter;
    TabLayout tabs;
    FloatingActionButton fab;

    public final void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sectionsPagerAdapter = new SectionsPagerAdapter(this,
                getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        //To hide the keyboard on tab change, clean search field
        tabs.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Hide keyboard
                hideKeyboard();

                //Clean search, instantiate item if fragment not yet created
                try {
                    PlaceholderFragment fragment = (PlaceholderFragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                    fragment.mSearchView.setIconified(true);
                    fragment.mSearchView.setIconified(true);
                }catch (Exception e){
                    //In case it's going back from settings activity, viewpager would be null
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fab = findViewById(R.id.fab);
        SharedPreferences preferences;
        preferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        boolean filtersActive = preferences.getBoolean("FiltersActive", false);

        if (filtersActive){
            fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)));
        }else{
            fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorBeca)));
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TabActivity.this,FiltersActivity.class);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            //Refresh all fragments
            sectionsPagerAdapter = new SectionsPagerAdapter(this,
                    getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

            final ViewPager viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(sectionsPagerAdapter);
            tabs.setupWithViewPager(viewPager);

            SharedPreferences preferences;
            preferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            boolean filtersActive = preferences.getBoolean("FiltersActive", false);

            if (filtersActive){
                fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)));
            }else{
                fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorBeca)));
            }
        }
    }
}