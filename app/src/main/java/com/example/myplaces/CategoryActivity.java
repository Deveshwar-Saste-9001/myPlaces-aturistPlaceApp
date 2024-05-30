package com.example.myplaces;

import static com.example.myplaces.DatabaseFiles.DatabaseCodes.loadedCategoriesNames;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myplaces.Adaptors.PlacesAdaptor;
import com.example.myplaces.DatabaseFiles.DatabaseCodes;
import com.example.myplaces.Models.PlacesListModel;
import com.example.myplaces.Models.PlacesModel;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView placesRecyclerViewNew;
    private Button requestNewplace;
    private String categoryTitle;
    private PlacesAdaptor adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.categorytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        categoryTitle = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(categoryTitle);

        requestNewplace = findViewById(R.id.addplaceBtn);
        placesRecyclerViewNew = findViewById(R.id.placesRecyclerviewNew);

        categoryTitle = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(categoryTitle);
        requestNewplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addplaceIntent = new Intent(CategoryActivity.this, AddPlaces.class);
                addplaceIntent.putExtra("Category", categoryTitle);
                startActivity(addplaceIntent);
            }
        });
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        LayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        placesRecyclerViewNew.setLayoutManager(LayoutManager);

//        adapter = new PlacesAdaptor(homePageModelFakeList);


        int listPosition = 0;
        for (int x = 0; x < loadedCategoriesNames.size(); x++) {
            if (loadedCategoriesNames.get(x).equals(categoryTitle.toUpperCase())) {
                listPosition = x;
            }
        }
        if (listPosition == 0) {
            loadedCategoriesNames.add(categoryTitle.toUpperCase());
            DatabaseCodes.lists.add(DatabaseCodes.loadedCategoriesNames.size() - 1, new PlacesListModel(new ArrayList<PlacesModel>()));
            //adapter = new HomePageAdapter(lists.get(loadedCategoriesNames.size()-1));
            DatabaseCodes.loadFragmentData(placesRecyclerViewNew, this, DatabaseCodes.loadedCategoriesNames.size() - 1, categoryTitle.toUpperCase());
        } else {
            adapter = new PlacesAdaptor(DatabaseCodes.lists.get(listPosition).getPlacesModelList(), placesRecyclerViewNew);
            placesRecyclerViewNew.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }


//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CategoryActivity.this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        placesRecyclerViewNew.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
