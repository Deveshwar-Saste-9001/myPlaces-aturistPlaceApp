package com.example.myplaces.Fragments;


import static com.example.myplaces.DatabaseFiles.DatabaseCodes.categoryModelList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myplaces.Adaptors.CategoryAdapter;
import com.example.myplaces.DatabaseFiles.DatabaseCodes;
import com.example.myplaces.R;
import com.google.android.material.shape.CornerFamily;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView categoryRecyclerview;
    private CategoryAdapter categoryAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        categoryRecyclerview = view.findViewById(R.id.categoryRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerview.setLayoutManager(linearLayoutManager);
        if (categoryModelList.size() == 0) {
            DatabaseCodes.loadCategories(categoryRecyclerview, getContext());
        } else {
            categoryAdapter = new CategoryAdapter(categoryModelList);
            categoryAdapter.notifyDataSetChanged();
        }
        categoryRecyclerview.setAdapter(categoryAdapter);

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
