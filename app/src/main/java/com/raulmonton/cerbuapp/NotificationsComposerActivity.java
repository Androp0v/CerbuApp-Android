package com.raulmonton.cerbuapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class NotificationsComposerActivity extends AppCompatActivity {

    private class sendMessageAsync extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                sendMsg((String)objects[0], (String) objects[1]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private static void sendMsg(String title, String body) throws IOException, JSONException {
        String url = "https://fcm.googleapis.com/fcm/send";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "key=AAAAdiT0Mic:APA91bE1TOsm13Pzu-I6TUKVg-0Mq60uvm2rV9OPUZaOexKmmkiLN_Mboa6lvyjyDgyEkY-inxTdTKR7N05Y3I2d4OymP3Ge7O7XMfbFUTNorsH9shl6y_6iMvMW6NWFOnkq-iWBMEUA");

        JSONObject msg = new JSONObject();
        msg.put("title",title);
        msg.put("body",body);

        JSONObject parent = new JSONObject();

        parent.put("to", "/topics/all");
        parent.put("content_available", true);
        parent.put("notification", msg);

        con.setDoOutput(true);
        OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
        os.write(parent.toString());
        os.flush();
        os.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + parent.toString());
        System.out.println("Response Code : " + responseCode+" "+con.getResponseMessage());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_composer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView titleView = findViewById(R.id.inputTitle);
        final TextView messageView = findViewById(R.id.inputMessage);
        Button sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String title = titleView.getText().toString();
                String message = messageView.getText().toString();

                Map<String,Object> taskMap = new HashMap<>();
                taskMap.put("Title", title);
                taskMap.put("Message", message);

                DatabaseReference ref = database.getReference("Notifications");
                ref.push().setValue(taskMap);

                new sendMessageAsync().execute(title,message);

                finish();

            }
        });

    }
}
