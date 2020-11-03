package com.raulmonton.cerbuapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static com.raulmonton.cerbuapp.MainActivity.MyPREFERENCES;
import static android.Manifest.permission.CAMERA;

public class CapacityActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;

    private static final String SALA_POLIVALENTE_QR = "gNc98aMN";
    private static final String SALA_DE_LECTURA_QR = "tHWL45Pg";
    private static final String BIBLIOTECA_QR = "gFJtN3HS";
    private static final String GIMNASIO_QR = "ATPi0bjz";
    private static final String OUT_QR = "SVOWE1Kd";

    // Handler thread to update the UI
    HandlerThread handlerThread;
    Handler mainHandler;

    // Other variables
    private double salaPolivalenteFractionNumber = 0.0;
    private double salaDeLecturaFractionNumber = 0.0;
    private double bibliotecaFractionNumber = 0.0;
    private double gimnasioFractionNumber = 0.0;

    private double salaPolivalenteFractionNumberOld = 0.0;
    private double salaDeLecturaFractionNumberOld = 0.0;
    private double bibliotecaFractionNumberOld = 0.0;
    private double gimnasioFractionNumberOld = 0.0;

    private int salaPolivalenteMaxCapacity = 0;
    private int salaDeLecturaMaxCapacity = 0;
    private int bibliotecaMaxCapacity = 0;
    private int gimnasioMaxCapacity = 0;

    // Database
    private DatabaseReference checkInDatabase;

    // UI elements
    private ProgressBar salaPolivalenteProgressbar;
    private ProgressBar salaDeLecturaProgressbar;
    private ProgressBar bibliotecaProgressbar;
    private ProgressBar gimnasioProgressbar;

    private TextView salaPolivalenteDescription;
    private TextView salaDeLecturaDescription;
    private TextView bibliotecaDescription;
    private TextView gimnasioDescription;

    private FloatingActionButton qrScanButton;

    private Chip salaPolivalenteChip;
    private Chip salaDeLecturaChip;
    private Chip bibliotecaChip;
    private Chip gimnasioChip;

    // Local deltas
    private float salaPolivalenteLocalUpdate = 0;
    private float salaDeLecturaLocalUpdate = 0;
    private float bibliotecaLocalUpdate = 0;
    private float gimnasioLocalUpdate = 0;

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

    private String getDescriptionString(Double fractionNumber, int maxCapacity){

        if (maxCapacity == 0){
            return "Ocupación desconocida";
        }

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
        // Set initial progressbar progress (TO-DO: This may not do anything)
        salaPolivalenteProgressbar.setProgress((int) (salaPolivalenteFractionNumber*100));
        salaDeLecturaProgressbar.setProgress((int) (salaDeLecturaFractionNumber*100));
        bibliotecaProgressbar.setProgress((int) (bibliotecaFractionNumber*100));
        gimnasioProgressbar.setProgress((int) (gimnasioFractionNumber*100));

        // Set progressbar colors
        salaPolivalenteProgressbar.setProgressTintList(ColorStateList.valueOf(getProgressBarColor(salaPolivalenteFractionNumber)));
        salaDeLecturaProgressbar.setProgressTintList(ColorStateList.valueOf(getProgressBarColor(salaDeLecturaFractionNumber)));
        bibliotecaProgressbar.setProgressTintList(ColorStateList.valueOf(getProgressBarColor(bibliotecaFractionNumber)));
        gimnasioProgressbar.setProgressTintList(ColorStateList.valueOf(getProgressBarColor(gimnasioFractionNumber)));

        // Set descriptions
        salaPolivalenteDescription.setText(getDescriptionString(salaPolivalenteFractionNumber, salaPolivalenteMaxCapacity));
        salaDeLecturaDescription.setText(getDescriptionString(salaDeLecturaFractionNumber, salaDeLecturaMaxCapacity));
        bibliotecaDescription.setText(getDescriptionString(bibliotecaFractionNumber, bibliotecaMaxCapacity));
        gimnasioDescription.setText(getDescriptionString(gimnasioFractionNumber, gimnasioMaxCapacity));

        // Progressbar animation
        ProgressBarAnimation anim;
        if (salaPolivalenteMaxCapacity != 0){
            anim = new ProgressBarAnimation(salaPolivalenteProgressbar,
                    (int)(salaPolivalenteFractionNumberOld*100),
                    (int)(salaPolivalenteFractionNumber*100 + salaPolivalenteLocalUpdate/salaPolivalenteMaxCapacity*100));
        } else {
            anim = new ProgressBarAnimation(salaPolivalenteProgressbar,
                    (int)(salaPolivalenteFractionNumberOld*100),
                    (int)(salaPolivalenteFractionNumber*100));
        }
        anim.setDuration((long) (1000*(Math.abs(salaPolivalenteFractionNumber - salaPolivalenteFractionNumberOld))));
        salaPolivalenteProgressbar.startAnimation(anim);

        if (salaDeLecturaMaxCapacity != 0){
            anim = new ProgressBarAnimation(salaDeLecturaProgressbar,
                    (int)(salaDeLecturaFractionNumberOld*100),
                    (int)(salaDeLecturaFractionNumber*100 + salaDeLecturaLocalUpdate/salaDeLecturaMaxCapacity*100));
        } else {
            anim = new ProgressBarAnimation(salaDeLecturaProgressbar,
                    (int)(salaDeLecturaFractionNumberOld*100),
                    (int)(salaDeLecturaFractionNumber*100));
        }
        anim.setDuration((long) (1000*(Math.abs(salaDeLecturaFractionNumber - salaDeLecturaFractionNumberOld))));
        salaDeLecturaProgressbar.startAnimation(anim);

        if (bibliotecaMaxCapacity != 0){
            anim = new ProgressBarAnimation(bibliotecaProgressbar,
                    (int)(bibliotecaFractionNumberOld*100),
                    (int)(bibliotecaFractionNumber*100 + bibliotecaLocalUpdate/bibliotecaMaxCapacity*100));
        } else {
            anim = new ProgressBarAnimation(bibliotecaProgressbar,
                    (int)(bibliotecaFractionNumberOld*100),
                    (int)(bibliotecaFractionNumber*100));
        }
        anim.setDuration((long) (1000*(Math.abs(bibliotecaFractionNumber - bibliotecaFractionNumberOld))));
        bibliotecaProgressbar.startAnimation(anim);

        if (gimnasioMaxCapacity != 0){
            anim = new ProgressBarAnimation(gimnasioProgressbar,
                    (int)(gimnasioFractionNumberOld*100),
                    (int)(gimnasioFractionNumber*100 + gimnasioLocalUpdate/gimnasioMaxCapacity*100));
        } else {
            anim = new ProgressBarAnimation(gimnasioProgressbar,
                    (int)(gimnasioFractionNumberOld*100),
                    (int)(gimnasioFractionNumber*100));
        }
        anim.setDuration((long) (1000*(Math.abs(gimnasioFractionNumber - gimnasioFractionNumberOld))));
        gimnasioProgressbar.startAnimation(anim);

        // Save new fraction numbers
        salaPolivalenteFractionNumberOld = salaPolivalenteFractionNumber;
        salaDeLecturaFractionNumberOld = salaDeLecturaFractionNumber;
        bibliotecaFractionNumberOld = bibliotecaFractionNumber;
        gimnasioFractionNumberOld = gimnasioFractionNumber;

        // Setup info buttons
        Button salaPolivalenteButton = findViewById(R.id.salaPolivalenteInfo);
        setUpDetailsButton(salaPolivalenteButton,"Sala Polivalente");

        Button salaDeLecturaButton = findViewById(R.id.salaDeLecturaInfo);
        setUpDetailsButton(salaDeLecturaButton,"Sala de Lectura");

        Button bibliotecaButton = findViewById(R.id.bibliotecaInfo);
        setUpDetailsButton(bibliotecaButton,"Biblioteca");

        Button gimnasioButton = findViewById(R.id.gimnasioInfo);
        setUpDetailsButton(gimnasioButton,"Gimnasio");
    }

    public void showQRDialog(CapacityActivity activity){

        // Ask for permission to access camera for QR code scanning
        if (!checkPermission()){
            requestPermission();
        }else{
            // Show QR scanning interface
            final QRScanSheet dialog = new QRScanSheet(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.capacityActivity = activity;
            dialog.setContentView(R.layout.bottom_sheet_qrscan);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }

    }

    private void deleteLocalUpdates(){
        salaPolivalenteLocalUpdate = 0;
        salaDeLecturaLocalUpdate = 0;
        bibliotecaLocalUpdate = 0;
        gimnasioLocalUpdate = 0;
    }

    private void exitAllRooms(){
        // Make all location chips invisible
        CapacityActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                salaPolivalenteChip.setVisibility(View.INVISIBLE);
                salaDeLecturaChip.setVisibility(View.INVISIBLE);
                bibliotecaChip.setVisibility(View.INVISIBLE);
                gimnasioChip.setVisibility(View.INVISIBLE);
            }
        });

        // Retrieve userID from preferences
        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String userID = preferences.getString("userID","userID_DEFAULT");

        // Show action in toast message
        CapacityActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Saliendo", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete the check-in from the database
        checkInDatabase.child(userID).setValue(null);

        // Set a negative local update in the current room just left
        if (salaPolivalenteLocalUpdate == 1) { salaPolivalenteLocalUpdate = -1; }
        if (salaDeLecturaLocalUpdate == 1) { salaDeLecturaLocalUpdate = -1; }
        if (bibliotecaLocalUpdate == 1) { bibliotecaLocalUpdate = -1; }
        if (gimnasioLocalUpdate == 1) { gimnasioLocalUpdate = -1; }

        //Animate the change
        CapacityActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animateProgressBars();
            }
        });
    }

    public void handleQRDialogResult(String qrCode){

        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String userID = preferences.getString("userID","userID_DEFAULT");
        String QRString;

        // Make all location chips invisible
        CapacityActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                salaPolivalenteChip.setVisibility(View.INVISIBLE);
                salaDeLecturaChip.setVisibility(View.INVISIBLE);
                bibliotecaChip.setVisibility(View.INVISIBLE);
                gimnasioChip.setVisibility(View.INVISIBLE);
            }
        });

        // Create Runnable to update UI
        Runnable updateChip;

        // Make visible only the current location
        switch (qrCode) {
            case SALA_POLIVALENTE_QR:
                QRString = "SalaPolivalente";

                updateChip = new Runnable() {
                    @Override
                    public void run() {
                        salaPolivalenteChip.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Entrando a la Sala Polivalente", Toast.LENGTH_SHORT).show();
                    }
                };
                CapacityActivity.this.runOnUiThread(updateChip);

                deleteLocalUpdates();
                salaPolivalenteLocalUpdate = 1;
                break;
            case SALA_DE_LECTURA_QR:
                QRString = "SalaDeLectura";

                updateChip = new Runnable() {
                    @Override
                    public void run() {
                        salaDeLecturaChip.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Entrando a la Sala de Lectura", Toast.LENGTH_SHORT).show();
                    }
                };
                CapacityActivity.this.runOnUiThread(updateChip);

                deleteLocalUpdates();
                salaDeLecturaLocalUpdate = 1;
                break;
            case BIBLIOTECA_QR:
                QRString = "Biblioteca";

                updateChip = new Runnable() {
                    @Override
                    public void run() {
                        bibliotecaChip.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Entrando a la Biblioteca", Toast.LENGTH_SHORT).show();
                    }
                };
                CapacityActivity.this.runOnUiThread(updateChip);

                deleteLocalUpdates();
                bibliotecaLocalUpdate = 1;
                break;
            case GIMNASIO_QR:
                QRString = "Gimnasio";

                updateChip = new Runnable() {
                    @Override
                    public void run() {
                        gimnasioChip.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Entrando al Gimnasio", Toast.LENGTH_SHORT).show();
                    }
                };
                CapacityActivity.this.runOnUiThread(updateChip);

                deleteLocalUpdates();
                gimnasioLocalUpdate = 1;
                break;
            case OUT_QR:
                exitAllRooms();
                return;
            default:
                // Gracefully ignore unknown QR codes and exit function
                Toast errorToast = Toast.makeText(getApplicationContext(), "Error leyendo el QR", (int) 1.0);
                errorToast.getView().setBackgroundColor(Color.rgb(255,59,48));
                errorToast.show();
                return;
        }

        // Prepare data to be uploaded to the database
        String finalQRString = QRString;
        Double finalEpoch = (double) (System.currentTimeMillis() / (double) 1000);

        // Update the database check-in
        checkInDatabase.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if user exists
                if (snapshot.getValue() == null){
                    // If user doesn't exist, add it
                    checkInDatabase.child(userID + "/Room").setValue(finalQRString);
                    checkInDatabase.child(userID + "/Time").setValue(finalEpoch);
                } else {
                    // If user exists, update it
                    checkInDatabase.child(userID + "/Room").setValue(finalQRString);
                    checkInDatabase.child(userID + "/Time").setValue(finalEpoch);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gracefully ignore unknown QR codes and exit function
                Toast errorToast = Toast.makeText(getApplicationContext(), "Sin conexión", (int) 1.0);
                errorToast.getView().setBackgroundColor(Color.rgb(255,59,48));
                errorToast.show();
            }
        });

        // Animate local changes
        CapacityActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animateProgressBars();
            }
        });

    }

    private void setUpDetailsButton(Button infoButton, final String roomName){

        final double fractionNumber;
        final int maxCapacity;

        switch (roomName){
            case "Sala Polivalente":
                fractionNumber = salaPolivalenteFractionNumber;
                maxCapacity = salaPolivalenteMaxCapacity;
                break;
            case "Sala de Lectura":
                fractionNumber = salaDeLecturaFractionNumber;
                maxCapacity = salaDeLecturaMaxCapacity;
                break;
            case "Biblioteca":
                fractionNumber = bibliotecaFractionNumber;
                maxCapacity = bibliotecaMaxCapacity;
                break;
            case "Gimnasio":
                fractionNumber = gimnasioFractionNumber;
                maxCapacity = gimnasioMaxCapacity;
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

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(CapacityActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if (shouldShowRequestPermissionRationale(CAMERA)) {
                showMessageOKCancel("Necesitas aceptar los permisos de la cámara para poder escanear los códigos QR.",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
                        }
                    });
            }
        } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showQRDialog(CapacityActivity.this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capacity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set Firebase RealtimeDatabase reference
        checkInDatabase = FirebaseDatabase.getInstance().getReference().child("Capacities/Check-ins/");
        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String userID = preferences.getString("userID","userID_DEFAULT");

        // Retrieve the user check-in ONLY, not the whole check-in database
        checkInDatabase.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null){
                    String currentRoom = snapshot.child("Room").getValue().toString();
                    switch (currentRoom){
                        case "SalaPolivalente":
                            salaPolivalenteChip.setVisibility(View.VISIBLE);
                            break;
                        case "SalaDeLectura":
                            salaDeLecturaChip.setVisibility(View.VISIBLE);
                            break;
                        case "Biblioteca":
                            bibliotecaChip.setVisibility(View.VISIBLE);
                            break;
                        case "Gimnasio":
                            gimnasioChip.setVisibility(View.VISIBLE);
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Get a handler that can be used to post to the main thread
        //mainHandler = new Handler(Looper.getMainLooper());
        handlerThread = new HandlerThread("ChipUpdater");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        mainHandler = new Handler(looper);

        // UI elements
        salaPolivalenteProgressbar = findViewById(R.id.salaPolivalenteProgressbar);
        salaDeLecturaProgressbar = findViewById(R.id.salaDeLecturaProgressbar);
        bibliotecaProgressbar = findViewById(R.id.bibliotecaProgressbar);
        gimnasioProgressbar = findViewById(R.id.gimnasioProgressbar);

        salaPolivalenteDescription = findViewById(R.id.salaPolivalenteTextView);
        salaDeLecturaDescription = findViewById(R.id.salaDeLecturaTextView);
        bibliotecaDescription = findViewById(R.id.bibliotecaTextView);
        gimnasioDescription = findViewById(R.id.gimnasioTextView);

        qrScanButton = findViewById(R.id.qrScanFAB);

        salaPolivalenteChip = findViewById(R.id.salaPolivalenteChip);
        salaDeLecturaChip = findViewById(R.id.salaDeLecturaChip);
        bibliotecaChip = findViewById(R.id.bibliotecaChip);
        gimnasioChip = findViewById(R.id.gimnasioChip);

        // Make chips invisible by default
        salaPolivalenteChip.setVisibility(View.INVISIBLE);
        salaDeLecturaChip.setVisibility(View.INVISIBLE);
        bibliotecaChip.setVisibility(View.INVISIBLE);
        gimnasioChip.setVisibility(View.INVISIBLE);

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

        // Setup actions for close buttons in chips
        salaPolivalenteChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitAllRooms();
            }
        });
        salaDeLecturaChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitAllRooms();
            }
        });
        bibliotecaChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitAllRooms();
            }
        });
        gimnasioChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitAllRooms();
            }
        });

        // Observe for changes in the database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Capacities/Count");

        databaseReference.limitToFirst(10).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    if (Objects.equals(postSnapshot.getKey(), "SalaPolivalente")){
                        Room room = postSnapshot.getValue(Room.class);
                        if ((room != null ? room.Max : -1) == -1){
                            salaPolivalenteFractionNumber = -1;
                        }else{
                            salaPolivalenteMaxCapacity = room.Max;
                            salaPolivalenteFractionNumber = ((float) room.Current)/((float) room.Max);
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
                    }else if (Objects.equals(postSnapshot.getKey(), "Gimnasio")){
                        Room room = postSnapshot.getValue(Room.class);
                        if ((room != null ? room.Max : -1) == -1){
                            gimnasioFractionNumber = -1;
                        }else{
                            gimnasioMaxCapacity = room.Max;
                            gimnasioFractionNumber = ((float) room.Current)/((float) room.Max);
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

        //LOG ONLY TO-DO: DELETE
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handleQRDialogResult(SALA_POLIVALENTE_QR);
            }
        }, 200);


    }
}