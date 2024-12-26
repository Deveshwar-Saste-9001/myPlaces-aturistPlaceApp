package com.example.myplaces.Fragments;
import static android.content.Intent.getIntent;
import static com.example.myplaces.DatabaseFiles.DatabaseCodes.MyFavlists;
import static com.example.myplaces.DatabaseFiles.DatabaseCodes.loadFavPlacesData;
import static com.example.myplaces.DatabaseFiles.DatabaseCodes.loadFragmentData;
import static com.example.myplaces.DatabaseFiles.DatabaseCodes.loadWishList;
import static com.example.myplaces.DatabaseFiles.DatabaseCodes.loadedCategoriesNames;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myplaces.Adaptors.MiniPlacesAdaptor;
import com.example.myplaces.Adaptors.PlacesAdaptor;
import com.example.myplaces.AddPlaces;
import com.example.myplaces.CategoryActivity;
import com.example.myplaces.DatabaseFiles.DatabaseCodes;
import com.example.myplaces.Models.PlacesListModel;
import com.example.myplaces.Models.PlacesModel;
import com.example.myplaces.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;

public class MyPlacesFragment extends Fragment {
    public MyPlacesFragment() {
        // Required empty public constructor
    }
    public static RecyclerView placesRecyclerViewNew;
    public static MiniPlacesAdaptor Favadapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_places, container, false);
        placesRecyclerViewNew = view.findViewById(R.id.placesRecyclerviewNew);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(getContext());
        LayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        placesRecyclerViewNew.setLayoutManager(LayoutManager);
        if( FirebaseAuth.getInstance().getCurrentUser()!=null){
            if(MyFavlists.size()==0){
                loadWishList(getActivity(), true);

            }else{
                Favadapter = new MiniPlacesAdaptor(DatabaseCodes.lists.get(0).getPlacesModelList(), placesRecyclerViewNew);
                placesRecyclerViewNew.setAdapter(Favadapter);
                Favadapter.notifyDataSetChanged();
            }
        }
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();


    }
}