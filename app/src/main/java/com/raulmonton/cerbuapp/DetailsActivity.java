package com.raulmonton.cerbuapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.raulmonton.cerbuapp.MainActivity.MyPREFERENCES;

import java.io.IOException;

public class DetailsActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private boolean changedFlag = false;
    private Data rowData;
    private CoordinatorLayout coordinatorLayout;
    private LinearLayout rootLayout;

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
        rootLayout = findViewById(R.id.rootLayout);

        rowData = getIntent().getExtras().getParcelable("itemData");

        String nameText = rowData.getName() + " " + rowData.getSurname_1();
        String nameText2 = rowData.getName() + " " + rowData.getSurname_1() + rowData.getSurname_2();

        ImageView becaImage = findViewById(R.id.becaView);

        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView roomTextView = findViewById(R.id.roomTextView);

        nameTextView.setText(rowData.getName() + " " + rowData.getSurname_1() + " " + rowData.getSurname_2());

        TextView careerTextView = findViewById(R.id.careerTextView);

        if (rowData.getBeca() != null && !rowData.getBeca().isEmpty()){
            careerTextView.setText(rowData.getCareer() + " | " + rowData.getBeca());
            becaImage.setImageResource(R.drawable.ic_becario_big);
            roomTextView.setText("Habitación: " + rowData.getRoom());
        }else{
            careerTextView.setText(rowData.getCareer());
            becaImage.setImageResource(0);

            final SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            boolean showRooms = preferences.getBoolean("showRooms", false);

            if (showRooms){
                roomTextView.setText("Habitación: " + rowData.getRoom());
            }else{
                roomTextView.setText("");
            }
        }

        final ImageView setFavorite = findViewById(R.id.setFavorite);

        if (rowData.getLiked() == 1) {
            setFavorite.setImageResource(R.drawable.ic_favorites);
        }else{
            setFavorite.setImageResource(R.drawable.ic_favorites_unselected);
            if (rowData.getName().equals("Raúl") && rowData.getSurname_1().equals("Montón")){
                setFavorite.setImageResource(R.drawable.ic_favorites_unselected_white);
            }
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
                    if (rowData.getName().equals("Raúl") && rowData.getSurname_1().equals("Montón")){
                        setFavorite.setImageResource(R.drawable.ic_favorites_unselected_white);
                    }
                    rowData.setLiked(0);
                    databaseHelper.setLikedOnDatabase(rowData.getId(), 0);

                }else{
                    setFavorite.setImageResource(R.drawable.ic_favorites);
                    rowData.setLiked(1);
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

        nameText2 = nameText2.replace(" ","");
        nameText2 = nameText2.toLowerCase();
        nameText2 = nameText2.replace("á", "a");
        nameText2 = nameText2.replace("é", "e");
        nameText2 = nameText2.replace("í", "i");
        nameText2 = nameText2.replace("ó", "o");
        nameText2 = nameText2.replace("ú", "u");
        nameText2 = nameText2.replace("ü", "u");
        nameText2 = nameText2.replace("ñ", "n");
        nameText2 = nameText2.replace("-", "");

        String nameTextHRes = nameText.concat("hres");
        String nameTextHRes2 = nameText2.concat("hres");

        int resID = res.getIdentifier(nameTextHRes , "drawable", getPackageName());

        myImage.setImageResource(resID);

        if (resID == 0){
            resID = res.getIdentifier(nameTextHRes2 , "drawable", getPackageName());
            myImage.setImageResource(resID);
        }
        if (resID == 0){
            resID = res.getIdentifier(nameText , "drawable", getPackageName());
            myImage.setImageResource(resID);
        }
        if (resID == 0){
            resID = res.getIdentifier(nameText2 , "drawable", getPackageName());
            myImage.setImageResource(resID);
        }
        if (resID == 0) {
            myImage.setImageResource(R.drawable.nohres);
        }

        if (rowData.getName().equals("Raúl") && rowData.getSurname_1().equals("Montón")){
            rootLayout.setBackgroundColor(Color.BLACK);
            nameTextView.setTextColor(Color.WHITE);
            careerTextView.setTextColor(Color.WHITE);
            roomTextView.setTextColor(Color.WHITE);
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRaulBackground));
        }
    }
}
