package com.raulmonton.cerbuapp;

import android.content.Context;
import android.content.res.Resources;
import android.database.SQLException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.raulmonton.cerbuapp.Data;
import com.raulmonton.cerbuapp.DatabaseHelper;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Data> List;
    private DatabaseHelper databaseHelper;
    private List<Data> FilteredList;
    private OnRowListener myOnRowListener;

    private ItemFilter mFilter = new ItemFilter();

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name;
        public TextView career;
        public ImageView attributes;
        public ImageView icon;

        OnRowListener myOnRowListener;

        public MyViewHolder(View itemView,OnRowListener myOnRowListener) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            career = itemView.findViewById(R.id.career);
            attributes = itemView.findViewById(R.id.attributes);
            icon = itemView.findViewById(R.id.detailedImageView);
            this.myOnRowListener = myOnRowListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            myOnRowListener.onRowClick(getAdapterPosition());
        }
    }
    public RecyclerAdapter(Context context, ArrayList<Data> List, DatabaseHelper dbhelper, OnRowListener myOnRowListener){
        this.context = context;
        this.List = List;
        this.databaseHelper = dbhelper;
        this.FilteredList = List;
        this.myOnRowListener = myOnRowListener;
    }

    public interface OnRowListener{
        void onRowClick(int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_content, parent, false);

        return new MyViewHolder(itemView, myOnRowListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        Data rowData = FilteredList.get(position);
        String displayName = rowData.getName() + " " + rowData.getSurname_1() + " " + rowData.getSurname_2();
        holder.name.setText(displayName);

        if (rowData.getBeca() != null && !rowData.getBeca().isEmpty()){
            holder.career.setText(rowData.getCareer() + " | " + rowData.getBeca());
            holder.attributes.setImageResource(R.drawable.ic_becario);
        }else{
            holder.career.setText(rowData.getCareer());
            holder.attributes.setImageDrawable(null);

            if (rowData.getLiked() == 1) {
                holder.attributes.setImageResource(R.drawable.ic_favorites);
            }else{
                holder.attributes.setImageDrawable(null);
            }

        }


        Resources res = context.getResources();
        String nameText = rowData.getName() + rowData.getSurname_1();

        nameText = nameText.replace(" ","");
        nameText = nameText.toLowerCase();
        nameText = nameText.replace("á", "a");
        nameText = nameText.replace("é", "e");
        nameText = nameText.replace("í", "i");
        nameText = nameText.replace("ó", "o");
        nameText = nameText.replace("ú", "u");
        nameText = nameText.replace("ü", "u");
        nameText = nameText.replace("ñ", "n");
        nameText = nameText.replace("-", "");

        int resID = res.getIdentifier(nameText , "drawable", context.getPackageName());
        holder.icon.setImageResource(resID);

        if (resID == 0){
            resID = res.getIdentifier(nameText , "drawable", context.getPackageName());
            holder.icon.setImageResource(resID);
        }
        if (resID == 0) {
            holder.icon.setImageResource(R.drawable.nohres);
        }
    }

    public Data getItem(int position) {
        return FilteredList.get(position);
    }

    @Override
    public int getItemCount() {
        if (FilteredList == null){
            return 0;
        }
        else{
            return FilteredList.size();
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence searchSequence) {

            String filterString = searchSequence.toString().toLowerCase();

            FilterResults results = new FilterResults();

            ArrayList<Data> Datas;
            databaseHelper = new DatabaseHelper(context);

            try {
                databaseHelper.createDataBase();
            } catch (IOException ioe) {
                throw new Error("Unable to create database");
            }
            try {
                databaseHelper.openDataBase();
            }catch(SQLException sqle){
                throw sqle;
            }

            Datas = databaseHelper.queryDatabase("TEST");

            results.values = Datas;
            results.count = Datas.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence searchSequence, FilterResults filterResults) {
            FilteredList = (ArrayList<Data>) filterResults.values;
            notifyDataSetChanged();
        }
    }

    public void changeItemLiked(int id, int liked){
        for (int i = 0; i < FilteredList.size();i++){
            if (FilteredList.get(i).getId() == id){
                FilteredList.get(i).setLiked(liked);
                break;
            }
        }
    }



}