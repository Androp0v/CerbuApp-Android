package com.raulmonton.cerbuapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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
import java.util.Map;
import java.util.TreeMap;

import static com.raulmonton.cerbuapp.MainActivity.MyPREFERENCES;

/*@IgnoreExtraProperties
class Notification {
    public String Title;
    public String Message;

    public Notification() {

    }

}*/

public class NotificationsActivity extends AppCompatActivity {

    List<String> titleList = new ArrayList<>();
    List<String> messageList = new ArrayList<>();
    NotificationsRecyclerAdapter adapter;
    RecyclerView recyclerView;

    private DatabaseReference mDatabase;

    private void collectNotifications(DataSnapshot users) {

        titleList.clear();
        messageList.clear();

        //iterate through each user, ignoring their UID
        for (DataSnapshot contactSnapshot: users.getChildren()) {
            //Get fields and append to list
            titleList.add((String) contactSnapshot.child("Title").getValue());
            messageList.add((String) contactSnapshot.child("Message").getValue());
        }

        Collections.reverse(titleList);
        Collections.reverse(messageList);

        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        boolean showNotifs = preferences.getBoolean("showNotifs", false);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent composerIntent = new Intent(NotificationsActivity.this, NotificationsComposerActivity.class);
                startActivity(composerIntent);
            }
        });

        if (!showNotifs){
            fab.setVisibility(View.GONE);
        }

        recyclerView = findViewById(R.id.notificationsRecyclerView);
        adapter = new NotificationsRecyclerAdapter(this, titleList, messageList);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Query recentNotifications = mDatabase.child("Notifications").limitToLast(15).orderByKey();

        recentNotifications.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectNotifications(dataSnapshot);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}
