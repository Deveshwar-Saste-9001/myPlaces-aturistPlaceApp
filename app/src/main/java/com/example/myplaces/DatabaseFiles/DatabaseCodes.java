package com.example.myplaces.DatabaseFiles;
import static com.example.myplaces.Fragments.MyPlacesFragment.Favadapter;
import static com.example.myplaces.Fragments.MyPlacesFragment.placesRecyclerViewNew;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myplaces.Adaptors.CategoryAdapter;
import com.example.myplaces.Adaptors.MiniPlacesAdaptor;
import com.example.myplaces.Adaptors.PlacesAdaptor;
import com.example.myplaces.Models.CategoryModel;
import com.example.myplaces.Models.PlacesListModel;
import com.example.myplaces.Models.PlacesModel;
import com.example.myplaces.PlaceDetailsActivity;
import com.example.myplaces.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DatabaseCodes {

    public static final String LOG_TAG = DatabaseCodes.class.getName();
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static String currentUserName, currentUserEmail, currentProfileImage;
    public static List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();
    public static List<PlacesListModel> lists = new ArrayList<>();
    public static List<PlacesListModel> MyFavlists = new ArrayList<>();
    public static List<PlacesModel> wishListModelList = new ArrayList<>();
    public static List<String> WishListlist = new ArrayList<>();
    public static List<String> loadedCategoriesNames = new ArrayList<>();
    public static String email,username ,location,role;
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
    public static void loadFavPlacesData(final RecyclerView homePageRecyclerView, final Context context) {
        firebaseFirestore.collection("USERS")
                .document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                List<PlacesModel> placesModelList = new ArrayList<>();
                                long no_of_places = (long) documentSnapshot.get("list_size");
                                for (long x = 0; x < no_of_places; x++) {
                                    placesModelList.add(new PlacesModel(documentSnapshot.get("place_id_" + x).toString(),
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
                                MyFavlists.add(0,new PlacesListModel(placesModelList));
                            }
                            Favadapter = new MiniPlacesAdaptor(MyFavlists.get(0).getPlacesModelList(), placesRecyclerViewNew);
                            placesRecyclerViewNew.setAdapter(Favadapter);
                            Favadapter.notifyDataSetChanged();
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    public static void loadWishList(final Context context, final boolean loadProductData) {
        MyFavlists.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_FAV")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                                WishListlist.add(task.getResult().get("place_id_" + x).toString());
                                if (DatabaseCodes.MyFavlists.contains(PlaceDetailsActivity.placeId)) {
                                    PlaceDetailsActivity.ADDEDTOWISHLIST = true;
                                    if (PlaceDetailsActivity.addtoWishList != null) {
                                        PlaceDetailsActivity.addtoWishList.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorPrimary));
                                    }
                                } else {
                                    PlaceDetailsActivity.ADDEDTOWISHLIST = false;
                                    if (PlaceDetailsActivity.addtoWishList != null) {
                                        PlaceDetailsActivity.addtoWishList.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                    }
                                }
                                if (loadProductData) {
                                    wishListModelList.clear();
                                    final String productId = task.getResult().get("place_id_" + x).toString();
                                    firebaseFirestore.collection("PLACES").document(productId)
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        final DocumentSnapshot documentSnapshot = task.getResult();
                                                        wishListModelList.add(new PlacesModel(productId,
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
                                                                                        , ""));

                                                        MyFavlists.add(0,new PlacesListModel(wishListModelList));
                                                                            Favadapter = new MiniPlacesAdaptor(MyFavlists.get(0).getPlacesModelList(), placesRecyclerViewNew);
                                                                            placesRecyclerViewNew.setAdapter(Favadapter);
                                                                            Favadapter.notifyDataSetChanged();
                                                                        } else {
                                                                            String error = task.getException().getMessage();
                                                                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });


                                                    } else {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                    }


                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    public static void removeFromWishList(final int index, final Context context) {

        final String removedProductID = WishListlist.get(index);
        WishListlist.remove(index);
        Map<String, Object> updateWishList = new HashMap<>();
        for (int x = 0; x < WishListlist.size(); x++) {
            updateWishList.put("place_id_" + x, WishListlist.get(x));
        }
        updateWishList.put("list_size", (long) WishListlist.size());
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_FAV")
                .set(updateWishList).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (wishListModelList.size() != 0) {
                                wishListModelList.remove(index);
                            }
                            PlaceDetailsActivity.ADDEDTOWISHLIST = false;
                            Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            if (PlaceDetailsActivity.addtoWishList != null) {
                                PlaceDetailsActivity.addtoWishList.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorPrimary));
                            }
                            WishListlist.add(index, removedProductID);
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                        MyFavlists.add(0,new PlacesListModel(wishListModelList));
                        PlaceDetailsActivity.running_Wishlist_query = false;
                    }
                });
    }

}
