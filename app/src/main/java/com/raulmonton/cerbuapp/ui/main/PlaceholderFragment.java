package com.raulmonton.cerbuapp.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import android.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raulmonton.cerbuapp.Data;
import com.raulmonton.cerbuapp.DatabaseHelper;
import com.raulmonton.cerbuapp.DetailsActivity;
import com.raulmonton.cerbuapp.InterfaceLike;
import com.raulmonton.cerbuapp.R;
import com.raulmonton.cerbuapp.RecyclerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PlaceholderFragment extends Fragment implements RecyclerAdapter.OnRowListener, InterfaceLike {

    public SearchView mSearchView;

    public RecyclerAdapter adapter;
    RecyclerView recyclerView;
    DatabaseHelper databaseHelper;
    private ArrayList<Data> DataList;

    public static PlaceholderFragment newInstance(ArrayList<Data> Datas, int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("ArrayData", Datas);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onRowClick(int position){
        //DataList.get(position);
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        Data detailedData = adapter.getItem(position);
        intent.putExtra("itemData", detailedData);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tab, container, false);

        Bundle extras = getArguments();
        DataList  = extras.getParcelableArrayList("ArrayData");

        recyclerView = root.findViewById(R.id.recyclerView);

        adapter = new RecyclerAdapter(getContext(), DataList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tab, menu);

        MenuItem mSearch = menu.findItem(R.id.app_bar_search);

        mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Buscar");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    adapter.getFilter().filter("");
                } else {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {
            if(resultCode == Activity.RESULT_OK){

                int id = data.getIntExtra("id", -1);
                int liked = data.getIntExtra("liked", 0);

                List<Fragment> fragmentList = getFragmentManager().getFragments();
                for (Fragment fragment : fragmentList) {
                    ((PlaceholderFragment)fragment).onLikedChanged(id, liked);
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //No need to do anything
            }
        }
    }

    @Override
    public void onLikedChanged(int id, int liked) {
        adapter.changeItemLiked(id, liked);
        adapter.notifyDataSetChanged();
    }
}