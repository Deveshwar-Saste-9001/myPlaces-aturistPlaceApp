package com.example.myplaces;

import static com.example.myplaces.Fragments.MyPlacesFragment.Favadapter;
import static com.example.myplaces.Fragments.MyPlacesFragment.placesRecyclerViewNew;

import android.os.Bundle;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myplaces.Adaptors.MiniPlacesAdaptor;
import com.example.myplaces.Models.PlacesListModel;
import com.example.myplaces.Models.PlacesModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchPlacesActivity extends AppCompatActivity {
    private SearchView searchplacesvies;
    private RecyclerView searchplacesRecyclerView;
    private TextView noplacestextview;
    MyAdapter adapter;
    List<PlacesModel> placesModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_places);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Places");


        searchplacesvies=findViewById(R.id.searchplacessearchview);
        searchplacesRecyclerView=findViewById(R.id.searchplacesrecyclerview);
        noplacestextview=findViewById(R.id.noplacefoundtext);

        searchplacesRecyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchplacesRecyclerView.setLayoutManager(layoutManager);
       placesModelList=new ArrayList<>();
        adapter = new MyAdapter(placesModelList, searchplacesRecyclerView);
        searchplacesRecyclerView.setAdapter(adapter);
        searchplacesvies.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                placesModelList.clear();
                String[] tags=query.toLowerCase().split(" ");
                for(String tag:tags){
                    FirebaseFirestore.getInstance().collection("PLACES").whereArrayContains("tags",tag).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        placesModelList.clear();
                                        for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()) {
                                            placesModelList.add(new PlacesModel(documentSnapshot.getId().toString(),
                                                    true
                                                    , documentSnapshot.get("place_Name").toString(),
                                                    ""+documentSnapshot.get("place_address").toString() + " " + documentSnapshot.getString("place_state").toString() + " " + documentSnapshot.get("place_country").toString() + " " + documentSnapshot.get("place_pincode").toString(),
                                                    documentSnapshot.get("place_Image").toString(),
                                                    documentSnapshot.get("place_description").toString(),
                                                    documentSnapshot.get("place_state").toString(),
                                                    documentSnapshot.get("place_country").toString(),
                                                    documentSnapshot.get("place_pincode").toString()
                                                    , 0.0
                                                    , 0.0
                                                    , 0
                                                    , 0
                                                    , 0
                                                    , 0
                                                    , 0
                                                    , ""
                                            ));

                                        }
                                        adapter = new MyAdapter(placesModelList,searchplacesRecyclerView);
                                        searchplacesRecyclerView.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(SearchPlacesActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.notifyDataSetChanged();
                return false;
            }
        });

    }
    class MyAdapter extends MiniPlacesAdaptor implements Filterable{

        public MyAdapter(List<PlacesModel> placesModelList, RecyclerView placeRecyclerView) {
            super(placesModelList, placeRecyclerView);
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {



                    return null;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
notifyDataSetChanged();
                }
            };
        }
    }
}