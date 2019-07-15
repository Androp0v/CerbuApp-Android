package com.raulmonton.cerbuapp.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.raulmonton.cerbuapp.Data;
import com.raulmonton.cerbuapp.DatabaseHelper;
import com.raulmonton.cerbuapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.raulmonton.cerbuapp.MainActivity.MyPREFERENCES;


public class SectionsPagerAdapter extends FragmentStatePagerAdapter{

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_0, R.string.tab_text_1,
            R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4, R.string.tab_text_5};

    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm, int behavior) {
        super(fm, behavior);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        DatabaseHelper databaseHelper;
        ArrayList<Data> Datas;

        databaseHelper = new DatabaseHelper(mContext);

        try {
            databaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            databaseHelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }


        if (position == 0) {
            Datas = databaseHelper.getAllData();
        } else {
            Datas = databaseHelper.getPromData(position);
        }

        return PlaceholderFragment.newInstance(Datas, position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 6;
    }

}