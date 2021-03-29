package com.raulmonton.cerbuapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.core.content.res.ResourcesCompat;

import java.lang.ref.WeakReference;

public class LoadImageAsync extends AsyncTask<Data,Integer,Drawable> {

    private WeakReference<ImageView> icon;
    WeakReference<Context> context;

    LoadImageAsync(ImageView icon, Context context){
        this.icon = new WeakReference<>(icon);
        this.context = new WeakReference<>(context);
    }

    @Override
    protected Drawable doInBackground(Data[] rowDataRaw) {

        Data rowData = rowDataRaw[0];

        Resources res = context.get().getResources();
        String nameText = rowData.getName() + rowData.getSurname_1();

        nameText = nameText.replace(" ","");
        nameText = nameText.replace("-", "");
        nameText = nameText.toLowerCase();
        nameText = nameText.replace("á", "a");
        nameText = nameText.replace("é", "e");
        nameText = nameText.replace("í", "i");
        nameText = nameText.replace("ó", "o");
        nameText = nameText.replace("ú", "u");
        nameText = nameText.replace("ü", "u");
        nameText = nameText.replace("ñ", "n");
        nameText = nameText.replace("ç", "c");

        int resID = res.getIdentifier(nameText , "drawable", context.get().getPackageName());

        if (resID == 0){
            String nameText2 = rowData.getName() + rowData.getSurname_1() + rowData.getSurname_2();
            nameText2 = nameText2.replace(" ","");
            nameText2 = nameText2.replace("-", "");
            nameText2 = nameText2.toLowerCase();
            nameText2 = nameText2.replace("á", "a");
            nameText2 = nameText2.replace("é", "e");
            nameText2 = nameText2.replace("í", "i");
            nameText2 = nameText2.replace("ó", "o");
            nameText2 = nameText2.replace("ú", "u");
            nameText2 = nameText2.replace("ü", "u");
            nameText2 = nameText2.replace("ñ", "n");
            nameText2 = nameText2.replace("ç", "c");
            resID = res.getIdentifier(nameText2 , "drawable", context.get().getPackageName());
        }
        if (resID == 0) {
            resID = res.getIdentifier("no","drawable",context.get().getPackageName());
        }

        return ResourcesCompat.getDrawable(context.get().getResources(), resID, null);
    }

    @Override
    protected void onPostExecute(Drawable resID) {
        super.onPostExecute(resID);
        icon.get().setImageDrawable(resID);
    }
}