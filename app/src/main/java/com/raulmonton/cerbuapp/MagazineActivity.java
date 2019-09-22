package com.raulmonton.cerbuapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MagazineActivity extends AppCompatActivity {

    private PdfRenderer mPdfRenderer;
    private PdfRenderer.Page mPdfPage;
    private ImageView mainPage;
    Integer globalPageNumber = 0;

    void copyToLocalCache(File outputFile, String filename) throws IOException {
        if (!outputFile.exists()) {
            InputStream input = getApplicationContext().getAssets().open(filename);
            FileOutputStream output;
            output = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            int size;
            // Just copy the entire contents of the file
            while ((size = input.read(buffer)) != -1) {
                output.write(buffer, 0, size);
            }
            input.close();
            output.close();
        }
    }

    // Display a page from the PDF on an ImageView

    void openPdfWithAndroidSDK(ImageView imageView, int pageNumber) throws IOException {
        // Copy sample.pdf from 'res/raw' folder into local cache so PdfRenderer can handle it
        File fileCopy = new File(getCacheDir(), "patiointerior.pdf");
        copyToLocalCache(fileCopy, "patiointerior.pdf");

        // We will get a page from the PDF file by calling openPage
        ParcelFileDescriptor fileDescriptor =
                ParcelFileDescriptor.open(fileCopy,
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

        // Create a new bitmap and render the page contents on to it
        Bitmap bitmap = Bitmap.createBitmap(mPdfPage.getWidth(),
                mPdfPage.getHeight(),
                Bitmap.Config.ARGB_8888);
        mPdfPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        // Set the bitmap in the ImageView so we can view it
        imageView.setImageBitmap(bitmap);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazine);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mainPage = findViewById(R.id.magazineImage);

        try {
            openPdfWithAndroidSDK(mainPage, globalPageNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainPage.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    if (event.getX() > mainPage.getWidth()/2){
                        try {
                            globalPageNumber += 1;
                            openPdfWithAndroidSDK(mainPage, globalPageNumber);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            globalPageNumber -= 1;
                            openPdfWithAndroidSDK(mainPage, globalPageNumber);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPdfPage != null) {
            mPdfPage.close();
        }
        if (mPdfRenderer != null) {
            mPdfRenderer.close();
        }
    }
}
