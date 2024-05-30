package com.example.myplaces.DatabaseFiles;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myplaces.Adaptors.CategoryAdapter;
import com.example.myplaces.Adaptors.PlacesAdaptor;
import com.example.myplaces.Models.CategoryModel;
import com.example.myplaces.Models.PlacesListModel;
import com.example.myplaces.Models.PlacesModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DatabaseCodes {

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public static String currentUserName, currentUserEmail, currentProfileImage;

    public static List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();
    public static List<PlacesListModel> lists = new ArrayList<>();
    public static List<String> loadedCategoriesNames = new ArrayList<>();
    public static String email,username ,location;

    public static void loadCategories(final RecyclerView categoryRecyclerView, final Context context) {
        categoryModelList.clear();
        firebaseFirestore.collection("Category").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(), documentSnapshot.get("categoryName").toString()));
                            }
                            CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList);
                            categoryRecyclerView.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public static void loadFragmentData(final RecyclerView homePageRecyclerView, final Context context, final int index, String categoryName) {

        firebaseFirestore.collection("Category")
                .document(categoryName).collection("PLACES").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                List<PlacesModel> placesModelList = new ArrayList<>();
                                long no_of_products = (long) documentSnapshot.get("no_of_places");
                                for (long x = 1; x < no_of_products + 1; x++) {

                                        placesModelList.add(new PlacesModel(documentSnapshot.get("places_ID_" + x).toString(),
                                                true
                                                , ""
                                                , " "
                                                , " "
                                                , " "
                                                , " "
                                                , ""
                                                , ""
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
                                lists.add(index, new PlacesListModel(placesModelList));

                            }
                            PlacesAdaptor placesAdaptor = new PlacesAdaptor(lists.get(index).getPlacesModelList(), homePageRecyclerView);
                            homePageRecyclerView.setAdapter(placesAdaptor);
                            placesAdaptor.notifyDataSetChanged();
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}
