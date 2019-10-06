package com.raulmonton.cerbuapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@IgnoreExtraProperties
class Notification {
    public String Title;
    public String Message;

    public Notification() {

    }

}

public class NotificationsActivity extends AppCompatActivity {

    List<String> titleList = new ArrayList<>();
    List<String> messageList = new ArrayList<>();
    List<Notification> notificationList = new ArrayList<>();
    NotificationsRecyclerAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.notificationsRecyclerView);
        adapter = new NotificationsRecyclerAdapter(this, titleList, messageList);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Notifications")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Notification currentNotification = document.toObject(Notification.class);
                                titleList.add(currentNotification.Title);
                                messageList.add(currentNotification.Message);
                                //notificationList.add(currentNotification);
                            }
                        } else {
                            Log.e("MyTAG", "Error getting documents.", task.getException());
                        }

                        //Collections.sort(notificationList);
                        adapter.notifyDataSetChanged();
                    }
                });


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}
