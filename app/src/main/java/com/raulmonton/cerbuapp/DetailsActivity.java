package com.raulmonton.cerbuapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private boolean changedFlag = false;
    private Data rowData;
    private CoordinatorLayout coordinatorLayout;

    @Override
    public void onBackPressed() {
        if (changedFlag){
            Intent returnIntent = new Intent();
            returnIntent.putExtra("id", rowData.getId());
            returnIntent.putExtra("liked", rowData.getLiked());
            setResult(RESULT_OK, returnIntent);
        }else{
            setResult(RESULT_CANCELED, new Intent());
        }
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        rowData = getIntent().getExtras().getParcelable("itemData");

        String nameText = rowData.getName() + " " + rowData.getSurname_1();

        TextView nameTextView = findViewById(R.id.nameTextView);
        nameTextView.setText(rowData.getName() + " " + rowData.getSurname_1() + " " + rowData.getSurname_2());

        TextView careerTextViewBeca = findViewById(R.id.careerTextViewBeca);
        TextView becaTextViewBeca = findViewById(R.id.becaTextViewBeca);
        TextView careerTextView = findViewById(R.id.careerTextView);

        if (rowData.getBeca() != null && !rowData.getBeca().isEmpty()){
            careerTextView.setText(" | ");
            careerTextViewBeca.setText(rowData.getCareer());
            becaTextViewBeca.setText(rowData.getBeca());
            nameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_becario, 0);
        }else{
            careerTextView.setText(rowData.getCareer());
            becaTextViewBeca.setText("");
            careerTextViewBeca.setText("");
        }

        final ImageView setFavorite = findViewById(R.id.setFavorite);

        if (rowData.getLiked() == 1) {
            setFavorite.setImageResource(R.drawable.ic_favorites);
        }else{
            setFavorite.setImageResource(R.drawable.ic_favorites_unselected);
        }

        setFavorite.setClickable(true);

        setFavorite.setOnClickListener(new View.OnClickListener() {

            public void onClick(View V) {

                databaseHelper = new DatabaseHelper(DetailsActivity.this);

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

                if (rowData.getLiked() == 1) {
                    setFavorite.setImageResource(R.drawable.ic_favorites_unselected);
                    rowData.setLiked(0);
                    //Snackbar snackbar = Snackbar.make(coordinatorLayout,"Eliminado de favoritos",Snackbar.LENGTH_SHORT);
                    //snackbar.show();
                    databaseHelper.setLikedOnDatabase(rowData.getId(), 0);

                }else{
                    setFavorite.setImageResource(R.drawable.ic_favorites);
                    rowData.setLiked(1);
                    //Snackbar snackbar = Snackbar.make(coordinatorLayout,"Añadido a favoritos",Snackbar.LENGTH_SHORT);
                    //snackbar.show();
                    databaseHelper.setLikedOnDatabase(rowData.getId(), 1);
                }

                changedFlag = true;

                ObjectAnimator animX = ObjectAnimator.ofFloat(setFavorite, "scaleX", 0.8f, 1.0f);
                ObjectAnimator animY = ObjectAnimator.ofFloat(setFavorite, "scaleY", 0.8f, 1.0f);
                AnimatorSet animSetXY = new AnimatorSet();
                animSetXY.playTogether(animX, animY);

                animSetXY.setDuration(500);
                animSetXY.setInterpolator(new BounceInterpolator());
                animSetXY.start();

            }
        });

        ImageView myImage = findViewById(R.id.detailedImageView);

        Resources res = getResources();

        nameText = nameText.replace(" ","");
        nameText = nameText.toLowerCase();
        nameText = nameText.replace("á", "a");
        nameText = nameText.replace("é", "e");
        nameText = nameText.replace("í", "i");
        nameText = nameText.replace("ó", "o");
        nameText = nameText.replace("ú", "u");
        nameText = nameText.replace("ü", "u");
        nameText = nameText.replace("ñ", "n");
        nameText = nameText.replace("-", "");

        String nameTextHRes = nameText.concat("hres");
        int resID = res.getIdentifier(nameTextHRes , "drawable", getPackageName());
        myImage.setImageResource(resID);

        if (resID == 0){
            resID = res.getIdentifier(nameText , "drawable", getPackageName());
            myImage.setImageResource(resID);
        }
        if (resID == 0) {
            myImage.setImageResource(R.drawable.nohres);
        }
    }
}
