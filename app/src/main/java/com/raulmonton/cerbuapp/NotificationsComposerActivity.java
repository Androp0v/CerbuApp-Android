package com.raulmonton.cerbuapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class NotificationsComposerActivity extends AppCompatActivity {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

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

                finish();

            }
        });

    }
}
