package com.raulmonton.cerbuapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MenuActivity extends AppCompatActivity {

    Bitmap finalBitmap;
    ImageView menuContent;

    private PdfRenderer mPdfRenderer;
    private PdfRenderer.Page mPdfPage;

    private Integer globalPageNumber = 0;

    private class loadImageAsync extends AsyncTask {

        @Override
        protected Boolean doInBackground(Object[] objects) {
            LoadImageFromWebOperations((String) objects[0], (Integer) objects[1]);
            return true;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            menuContent.setImageBitmap(finalBitmap);
        }

    }

    void LoadImageFromWebOperations(String url, Integer pageNumber) {
        try {
            InputStream inputStream = (InputStream) new URL(url).getContent();
            FileOutputStream output;

            File outputFile = new File(getCacheDir(), "menu.pdf");

            output = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            int size;
            // Just copy the entire contents of the file
            while ((size = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, size);
            }
            inputStream.close();
            output.close();

            ParcelFileDescriptor fileDescriptor =
                    ParcelFileDescriptor.open(outputFile,
                            ParcelFileDescriptor.MODE_READ_ONLY);
            mPdfRenderer = new PdfRenderer(fileDescriptor);

            if (pageNumber < 0){
                pageNumber = 0;
                globalPageNumber = pageNumber;
            }else{
                if (pageNumber > mPdfRenderer.getPageCount() - 1){
                    pageNumber = mPdfRenderer.getPageCount() - 1;
                    globalPageNumber = pageNumber;
                }
            }

            mPdfPage = mPdfRenderer.openPage(pageNumber);

            Bitmap bitmap = Bitmap.createBitmap(mPdfPage.getWidth(),
                    mPdfPage.getHeight(),
                    Bitmap.Config.ARGB_8888);

            mPdfPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

            finalBitmap = bitmap;


        } catch (Exception e) {
            Log.e("MyTAG", e.toString());
            Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
            Bitmap bitmap = Bitmap.createBitmap(1, 1, conf);
            finalBitmap = bitmap;
        }
    }

    @SuppressLint({"SetJavaScriptEnabled","ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        menuContent = findViewById(R.id.menuContent);
        new loadImageAsync().execute("https://cerbuna.unizar.es/sites/cerbuna.unizar.es/files/users/temporales/menu.pdf",0);

        menuContent.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    if (event.getX() > menuContent.getWidth()/2){
                        globalPageNumber += 1;
                        new loadImageAsync().execute("https://cerbuna.unizar.es/sites/cerbuna.unizar.es/files/users/temporales/menu.pdf", globalPageNumber);
                    }else{
                        globalPageNumber -= 1;
                        new loadImageAsync().execute("https://cerbuna.unizar.es/sites/cerbuna.unizar.es/files/users/temporales/menu.pdf", globalPageNumber);
                    }
                }
                return false;
            }
        });
    }
}
