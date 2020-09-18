package com.raulmonton.cerbuapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static com.raulmonton.cerbuapp.R.id.comedorProgressbar;

public class CapacityActivity extends AppCompatActivity {

    private double comedorFractionNumber = 0.0;
    private double salaDeLecturaFractionNumber = 0.0;
    private double bibliotecaFractionNumber = 0.0;

    private double comedorFractionNumberOld = 0.0;
    private double salaDeLecturaFractionNumberOld = 0.0;
    private double bibliotecaFractionNumberOld = 0.0;

    private int comedorMaxCapacity = 0;
    private int salaDeLecturaMaxCapacity = 0;
    private int bibliotecaMaxCapacity = 0;

    // UI elements
    private ProgressBar comedorProgressbar;
    private ProgressBar salaDeLecturaProgressbar;
    private ProgressBar bibliotecaProgressbar;

    private TextView comedorDescription;
    private TextView salaDeLecturaDescription;
    private TextView bibliotecaDescription;

    private FloatingActionButton qrScanButton;

    private Chip comedorChip;
    private Chip salaDeLecturaChip;
    private Chip bibliotecaChip;

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

    private String getDescriptionString(Double fractionNumber){
        if (fractionNumber < 0.3){
            return "Vacío o casi vacío";
        }else if (fractionNumber >= 0.3 && fractionNumber < 0.6){
            return "Ocupación moderada o baja";
        }else if (fractionNumber >= 0.6 && fractionNumber < 0.8){
            return "Ocupación moderada o alta";
        }else if (fractionNumber >= 0.8){
            return "Lleno o casi lleno";
        }else{
            return "Ocupación desconocida";
        }
    }

    private static class Room {

        public int Current;
        public int Max;

        public Room() {
            // Default constructor required for calls to DataSnapshot.getValue(Room.class)
        }

        public Room(int Current, int Max) {
            this.Current = Current;
            this.Max = Max;
        }

    }

    static class ProgressBarAnimation extends Animation {
        private ProgressBar progressBar;
        private float from;
        private float  to;

        public ProgressBarAnimation(ProgressBar progressBar, float from, float to) {
            super();
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            progressBar.setProgress((int) value);
        }

    }

    private void animateProgressBars(){
        // Set initial progressbar progress
        comedorProgressbar.setProgress((int) (comedorFractionNumber*100));
        salaDeLecturaProgressbar.setProgress((int) (salaDeLecturaFractionNumber*100));
        bibliotecaProgressbar.setProgress((int) (bibliotecaFractionNumber*100));

        // Set progressbar colors
        comedorProgressbar.setProgressTintList(ColorStateList.valueOf(getProgressBarColor(comedorFractionNumber)));
        salaDeLecturaProgressbar.setProgressTintList(ColorStateList.valueOf(getProgressBarColor(salaDeLecturaFractionNumber)));
        bibliotecaProgressbar.setProgressTintList(ColorStateList.valueOf(getProgressBarColor(bibliotecaFractionNumber)));

        // Set descriptions
        comedorDescription.setText(getDescriptionString(comedorFractionNumber));
        salaDeLecturaDescription.setText(getDescriptionString(salaDeLecturaFractionNumber));
        bibliotecaDescription.setText(getDescriptionString(bibliotecaFractionNumber));

        // Progressbar animation
        ProgressBarAnimation anim;
        anim = new ProgressBarAnimation(comedorProgressbar,
                (int)(comedorFractionNumberOld*100),
                (int)(comedorFractionNumber*100));
        anim.setDuration((long) (1000*(Math.abs(comedorFractionNumber - comedorFractionNumberOld))));
        comedorProgressbar.startAnimation(anim);

        anim = new ProgressBarAnimation(salaDeLecturaProgressbar,
                (int)(salaDeLecturaFractionNumberOld*100),
                (int)(salaDeLecturaFractionNumber*100));
        anim.setDuration((long) (1000*(Math.abs(salaDeLecturaFractionNumber - salaDeLecturaFractionNumberOld))));
        salaDeLecturaProgressbar.startAnimation(anim);

        anim = new ProgressBarAnimation(bibliotecaProgressbar,
                (int)(bibliotecaFractionNumberOld*100),
                (int)(bibliotecaFractionNumber*100));
        anim.setDuration((long) (1000*(Math.abs(bibliotecaFractionNumber - bibliotecaFractionNumberOld))));
        bibliotecaProgressbar.startAnimation(anim);

        // Save new fraction numbers
        comedorFractionNumberOld = comedorFractionNumber;
        salaDeLecturaFractionNumberOld = salaDeLecturaFractionNumber;
        bibliotecaFractionNumberOld = bibliotecaFractionNumber;

        // Setup info buttons
        Button comedorButton = findViewById(R.id.comedorInfo);
        setUpDetailsButton(comedorButton,"Comedor");

        Button salaDeLecturaButton = findViewById(R.id.salaDeLecturaInfo);
        setUpDetailsButton(salaDeLecturaButton,"Sala de Lectura");

        Button bibliotecaButton = findViewById(R.id.bibliotecaInfo);
        setUpDetailsButton(bibliotecaButton,"Biblioteca");
    }

    public void showQRDialog(Activity activity){

        final QRScanSheet dialog = new QRScanSheet(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.bottom_sheet_qrscan);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    private void setUpDetailsButton(Button infoButton, final String roomName){

        final double fractionNumber;
        final int maxCapacity;

        switch (roomName){
            case "Comedor":
                fractionNumber = comedorFractionNumber;
                maxCapacity = comedorMaxCapacity;
                break;
            case "Sala de Lectura":
                fractionNumber = salaDeLecturaFractionNumber;
                maxCapacity = salaDeLecturaMaxCapacity;
                break;
            case "Biblioteca":
                fractionNumber = bibliotecaFractionNumber;
                maxCapacity = bibliotecaMaxCapacity;
                break;
            default:
                fractionNumber = -1;
                maxCapacity = -1;
        }

        infoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        CapacityDetails bottomSheetFragment = new CapacityDetails(fractionNumber,maxCapacity,roomName);
                        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capacity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // UI elements
        comedorProgressbar = findViewById(R.id.comedorProgressbar);
        salaDeLecturaProgressbar = findViewById(R.id.salaDeLecturaProgressbar);
        bibliotecaProgressbar = findViewById(R.id.bibliotecaProgressbar);

        comedorDescription = findViewById(R.id.comedorTextView);
        salaDeLecturaDescription = findViewById(R.id.salaDeLecturaTextView);
        bibliotecaDescription = findViewById(R.id.bibliotecaTextView);

        qrScanButton = findViewById(R.id.qrScanFAB);

        comedorChip = findViewById(R.id.comedorChip);
        salaDeLecturaChip = findViewById(R.id.salaDeLecturaChip);
        bibliotecaChip = findViewById(R.id.bibliotecaChip);

        // Make chips invisible by default
        comedorChip.setVisibility(View.INVISIBLE);
        salaDeLecturaChip.setVisibility(View.INVISIBLE);
        bibliotecaChip.setVisibility(View.INVISIBLE);

        // Initial progressbar UI
        animateProgressBars();

        // Setup FAB

        qrScanButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        showQRDialog(CapacityActivity.this);
                    }
                });

        // Observe for changes in the database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Capacities/Count");

        databaseReference.limitToFirst(10).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if (Objects.equals(postSnapshot.getKey(), "Comedor")){
                        Room room = postSnapshot.getValue(Room.class);
                        if ((room != null ? room.Max : -1) == -1){
                            comedorFractionNumber = -1;
                        }else{
                            comedorMaxCapacity = room.Max;
                            comedorFractionNumber = ((float) room.Current)/((float) room.Max);
                        }
                    }else if (Objects.equals(postSnapshot.getKey(), "SalaDeLectura")){
                        Room room = postSnapshot.getValue(Room.class);
                        if ((room != null ? room.Max : -1) == -1){
                            salaDeLecturaFractionNumber = -1;
                        }else{
                            salaDeLecturaMaxCapacity = room.Max;
                            salaDeLecturaFractionNumber = ((float) room.Current)/((float) room.Max);
                        }
                    }else if (Objects.equals(postSnapshot.getKey(), "Biblioteca")){
                        Room room = postSnapshot.getValue(Room.class);
                        if ((room != null ? room.Max : -1) == -1){
                            bibliotecaFractionNumber = -1;
                        }else{
                            bibliotecaMaxCapacity = room.Max;
                            bibliotecaFractionNumber = ((float) room.Current)/((float) room.Max);
                        }
                    }
                }
                animateProgressBars();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Firebase Error", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });



    }
}