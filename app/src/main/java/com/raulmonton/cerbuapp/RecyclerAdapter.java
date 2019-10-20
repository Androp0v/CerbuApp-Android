package com.raulmonton.cerbuapp;

import android.content.Context;
import android.content.SharedPreferences;
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
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import static com.raulmonton.cerbuapp.MainActivity.MyPREFERENCES;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Data> List;
    private DatabaseHelper databaseHelper;
    private List<Data> FilteredList;
    private OnRowListener myOnRowListener;

    private ItemFilter mFilter = new ItemFilter();

    public String cleanString(String rawString){
        rawString = rawString.replace("á", "a");
        rawString = rawString.replace("é", "e");
        rawString = rawString.replace("í", "i");
        rawString = rawString.replace("ó", "o");
        rawString = rawString.replace("ú", "u");
        rawString = rawString.replace("ü", "u");

        return rawString;
    }

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

            if (rowData.getLiked() == 1) {
                holder.attributes.setImageResource(R.drawable.ic_becario_hot);
            }else{
                holder.attributes.setImageResource(R.drawable.ic_becario);
            }

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
            String nameText2 = rowData.getName() + rowData.getSurname_1() + rowData.getSurname_2();
            nameText2 = nameText2.replace(" ","");
            nameText2 = nameText2.toLowerCase();
            nameText2 = nameText2.replace("á", "a");
            nameText2 = nameText2.replace("é", "e");
            nameText2 = nameText2.replace("í", "i");
            nameText2 = nameText2.replace("ó", "o");
            nameText2 = nameText2.replace("ú", "u");
            nameText2 = nameText2.replace("ü", "u");
            nameText2 = nameText2.replace("ñ", "n");
            nameText2 = nameText2.replace("-", "");
            resID = res.getIdentifier(nameText2 , "drawable", context.getPackageName());
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

            filterString = filterString.replace("á", "a");
            filterString = filterString.replace("é", "e");
            filterString = filterString.replace("í", "i");
            filterString = filterString.replace("ó", "o");
            filterString = filterString.replace("ú", "u");
            filterString = filterString.replace("ü", "u");

            String[] splitedFilterStrings = filterString.split(" ");

            FilterResults results = new FilterResults();

            ArrayList<Data> Datas = new ArrayList<Data>(List.size());
            String filterableName;
            String filterableSurname_1;
            String filterableSurname_2;
            String filterableCareer;
            String filterableBeca;
            boolean approvedFlag;

            for (int i = 0; i < List.size(); i++){

                filterableName = List.get(i).getName().toLowerCase();
                filterableSurname_1 = List.get(i).getSurname_1().toLowerCase();
                filterableSurname_2 = List.get(i).getSurname_2().toLowerCase();
                filterableCareer = List.get(i).getCareer().toLowerCase();

                try{
                    filterableBeca = List.get(i).getBeca().toLowerCase();
                    filterableBeca = cleanString(filterableBeca.replace("á", "a"));
                }catch(Exception e){
                    filterableBeca = "";
                }

                filterableName = cleanString(filterableName);
                filterableSurname_1 = cleanString(filterableSurname_1);
                filterableSurname_2 = cleanString(filterableSurname_2);
                filterableCareer = cleanString(filterableCareer);


                approvedFlag = false;

                for (int j = 0; j < splitedFilterStrings.length; j++){
                    if (filterableName.startsWith(splitedFilterStrings[j])
                            || filterableSurname_1.startsWith(splitedFilterStrings[j])
                            || filterableSurname_2.startsWith(splitedFilterStrings[j])
                            || filterableCareer.startsWith(splitedFilterStrings[j])
                            || filterableBeca.startsWith(splitedFilterStrings[j])) {

                        approvedFlag = true;

                    }else{

                        approvedFlag = false;
                        String tmpString = splitedFilterStrings[j];

                        for(int k = 1; k < j+1; k++){

                            tmpString = splitedFilterStrings[j-k] + " " + tmpString;

                            if (filterableName.startsWith(tmpString)
                                    || filterableSurname_1.startsWith(tmpString)
                                    || filterableSurname_2.startsWith(tmpString)
                                    || filterableCareer.startsWith(tmpString)
                                    || filterableBeca.startsWith(tmpString)){

                                approvedFlag = true;
                                break;
                            }
                        }
                    }

                    final SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    boolean showRooms = preferences.getBoolean("showRooms", false);

                    if (showRooms){
                        if (List.get(i).getRoom().toLowerCase().startsWith(splitedFilterStrings[j])){
                            approvedFlag = true;
                        }
                    }

                    if (!approvedFlag){
                        break;
                    }

                }

                if (approvedFlag){
                    Datas.add(List.get(i));
                }

            }


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