package com.raulmonton.cerbuapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    // UI elements
    private ProgressBar comedorProgressbar;
    private ProgressBar salaDeLecturaProgressbar;
    private ProgressBar bibliotecaProgressbar;

    private TextView comedorDescription;
    private TextView salaDeLecturaDescription;
    private TextView bibliotecaDescription;

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

    private void animateProgressBars(){
        // Set initial progressbar progress
        comedorProgressbar.setProgress((int) (comedorFractionNumber*100));
        salaDeLecturaProgressbar.setProgress((int) (salaDeLecturaFractionNumber*100));
        bibliotecaProgressbar.setProgress((int) (bibliotecaFractionNumber*100));

        // Set initial progressbar colors
        comedorProgressbar.setProgressTintList(ColorStateList.valueOf(getProgressBarColor(comedorFractionNumber)));
        salaDeLecturaProgressbar.setProgressTintList(ColorStateList.valueOf(getProgressBarColor(salaDeLecturaFractionNumber)));
        bibliotecaProgressbar.setProgressTintList(ColorStateList.valueOf(getProgressBarColor(bibliotecaFractionNumber)));

        // Set initial descriptions
        comedorDescription.setText(getDescriptionString(comedorFractionNumber));
        salaDeLecturaDescription.setText(getDescriptionString(salaDeLecturaFractionNumber));
        bibliotecaDescription.setText(getDescriptionString(bibliotecaFractionNumber));
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

        // Initial UI
        animateProgressBars();

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
                            comedorFractionNumber = ((float) room.Current)/((float) room.Max);
                        }
                    }else if (Objects.equals(postSnapshot.getKey(), "SalaDeLectura")){
                        Room room = postSnapshot.getValue(Room.class);
                        if ((room != null ? room.Max : -1) == -1){
                            salaDeLecturaFractionNumber = -1;
                        }else{
                            salaDeLecturaFractionNumber = ((float) room.Current)/((float) room.Max);
                        }
                    }else if (Objects.equals(postSnapshot.getKey(), "Biblioteca")){
                        Room room = postSnapshot.getValue(Room.class);
                        if ((room != null ? room.Max : -1) == -1){
                            bibliotecaFractionNumber = -1;
                        }else{
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