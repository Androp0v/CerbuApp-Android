package com.raulmonton.cerbuapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    List<String> titleList;
    List<String> messageList;
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

        NotificationsDatabaseHelper notificationsDatabaseHelper = new NotificationsDatabaseHelper(NotificationsActivity.this);

        titleList = notificationsDatabaseHelper.getAllTitles();
        messageList = notificationsDatabaseHelper.getAllMessages();

        adapter = new NotificationsRecyclerAdapter(this, titleList, messageList, notificationsDatabaseHelper);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}
