package com.polytron.mylauncher.screens.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.polytron.mylauncher.R;
import com.polytron.mylauncher.adapters.AppsDrawerAdapter;

public class AppsDrawerFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    GridLayoutManager gridLayoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apps_drawer, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.appDrawer_recylerView);

        adapter = new AppsDrawerAdapter(getContext(), getActivity());

//        layoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager = new GridLayoutManager(getContext(),4);


        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}