package com.raulmonton.cerbuapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.pdf.PdfRenderer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class BoletinActivity extends AppCompatActivity {

    AnimationDrawable boletinAnimation;
    ImageView boletinContent;
    Bitmap finalBitmap;

    private PdfRenderer mPdfRenderer;
    private PdfRenderer.Page mPdfPage;

    private class loadImageAsync extends AsyncTask {

        @Override
        protected Boolean doInBackground(Object[] objects) {
            LoadImageFromWebOperations((String) objects[0]);
            return true;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            boletinContent.setImageBitmap(finalBitmap);
        }

    }

    void LoadImageFromWebOperations(String url) {
        try {
            InputStream inputStream = (InputStream) new URL(url).getContent();
            FileOutputStream output;

            File outputFile = new File(getCacheDir(), "boletin.pdf");

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

            mPdfPage = mPdfRenderer.openPage(0);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boletin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView boletinTop = findViewById(R.id.boletinTop);
        boletinContent = findViewById(R.id.boletinContent);

        boletinTop.setImageResource(R.drawable.boletin_animation);
        boletinAnimation = (AnimationDrawable) boletinTop.getDrawable();
        boletinAnimation.start();

        new loadImageAsync().execute("https://drive.google.com/uc?id=1raUqZ-P8zY1DiKIGwCvN8K65NFnaJk8Y");

    }
}
