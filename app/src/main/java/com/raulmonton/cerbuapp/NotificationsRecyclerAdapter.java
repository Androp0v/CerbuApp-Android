package com.raulmonton.cerbuapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationsRecyclerAdapter extends RecyclerView.Adapter<NotificationsRecyclerAdapter.MyViewHolder>{
    private Context context;
    private List<String> titleList;
    private List<String> messageList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView message;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.Title);
            message = view.findViewById(R.id.Message);
        }
    }

    public NotificationsRecyclerAdapter(Context context, List<String> titles, List<String> messages){
        this.context = context;
        this.titleList = titles;
        this.messageList = messages;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notifications_recycler_content, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        String title = titleList.get(position);
        String message = messageList.get(position);

        holder.title.setText(title);
        holder.message.setText(message);
        holder.message.setLinksClickable(true);
    }

    @Override
    public int getItemCount() {
        if (titleList != null) {
            return titleList.size();
        }else{
            return 0;
        }
    }
}
