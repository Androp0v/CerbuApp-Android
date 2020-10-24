package com.raulmonton.cerbuapp;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CapacityDetails extends BottomSheetDialogFragment {

    double fractionNumber = 0;
    int maxCapacity = 0;
    String roomName;

    ProgressBar progressBar;

    String salaPolivalenteTimeout = "3 horas";
    String salaDeLecturaTimeout = "6 horas";
    String bibliotecaTimeout = "6 horas";

    private int getProgressBarColor(Double fractionNumber){
        if (fractionNumber < 0.3){
            return getResources().getColor(R.color.progressbarGREEN, this.getContext().getTheme());
        }else if (fractionNumber >= 0.3 && fractionNumber < 0.6){
            return getResources().getColor(R.color.progressbarYELLOW, this.getContext().getTheme());
        }else if (fractionNumber >= 0.6 && fractionNumber < 0.8){
            return getResources().getColor(R.color.progressbarORANGE, this.getContext().getTheme());
        }else if (fractionNumber >= 0.8){
            return getResources().getColor(R.color.progressbarRED, this.getContext().getTheme());
        }else{
            return getResources().getColor(R.color.colorSeparator, this.getContext().getTheme());
        }
    }

    private void animateProgressBar(){
        // Set initial progressbar progress
        progressBar.setProgress((int) (fractionNumber*100));

        // Set progressbar colors
        progressBar.setProgressTintList(ColorStateList.valueOf(getProgressBarColor(fractionNumber)));

        // Progressbar animation
        CapacityActivity.ProgressBarAnimation anim;
        anim = new CapacityActivity.ProgressBarAnimation(progressBar,
                0,
                (int)(fractionNumber*100));
        anim.setDuration((long) (1000*fractionNumber));
        progressBar.startAnimation(anim);
    }

    public CapacityDetails(double fractionNumber, int maxCapacity, String roomName) {
        this.fractionNumber = fractionNumber;
        this.maxCapacity = maxCapacity;
        this.roomName = roomName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.bottom_sheet_capacity_details, container, false);
        TextView sheetTitle = rootView.findViewById(R.id.sheetTitle);
        TextView description = rootView.findViewById(R.id.timeoutTextView);
        progressBar = rootView.findViewById(R.id.detailsProgressbar);

        // Set sheet title
        sheetTitle.setText((int)(fractionNumber*maxCapacity)  + " de " +  maxCapacity + " personas");

        // Set sheet description
        SpannableString boldString;
        String baseString = "Número aproximado. Las entradas aquí caducan automáticamente después de ";
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        switch(roomName) {
            case "Sala Polivalente":
                boldString = new SpannableString(baseString + salaPolivalenteTimeout + ".");
                break;
            case "Sala de Lectura":
                boldString = new SpannableString(baseString + salaDeLecturaTimeout + ".");
                break;
            case "Biblioteca":
                boldString = new SpannableString(baseString + bibliotecaTimeout + ".");
                break;
            default:
                boldString = new SpannableString("?? minutos");
        }
        boldString.setSpan(boldSpan,72,boldString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        description.setText(boldString);

        // Animate progressbar
        Log.e("FRACTION NUMBER", String.valueOf(fractionNumber));
        animateProgressBar();

        return rootView;
    }
}