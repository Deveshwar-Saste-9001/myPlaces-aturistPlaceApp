package com.example.myplaces.Adaptors;

import static com.example.myplaces.DatabaseFiles.DatabaseCodes.firebaseFirestore;
import static com.example.myplaces.DatabaseFiles.DatabaseCodes.wishListModelList;
import static com.example.myplaces.PlaceDetailsActivity.addtoWishList;
import static com.example.myplaces.PlaceDetailsActivity.placeId;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myplaces.DatabaseFiles.DatabaseCodes;
import com.example.myplaces.Models.PlacesModel;
import com.example.myplaces.PlaceDetailsActivity;
import com.example.myplaces.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiniPlacesAdaptor extends RecyclerView.Adapter<MiniPlacesAdaptor.ViewHolder>    {
        private List<PlacesModel> placesModelList;
        private RecyclerView placeRecyclerView;
        public MiniPlacesAdaptor(List < PlacesModel > placesModelList, RecyclerView placeRecyclerView) {
        this.placesModelList = placesModelList;
        this.placeRecyclerView = placeRecyclerView;
    }
        @NonNull
        @Override
        public MiniPlacesAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item_view_mini, parent, false);
        return new MiniPlacesAdaptor.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String image = placesModelList.get(position).getPlace_Image();
        String name = placesModelList.get(position).getPlaceName();
        String state = placesModelList.get(position).getPlace_state();
        String country = placesModelList.get(position).getPlace_country();
        holder.setPlaceData(image, name, state, country, position);
        holder.placeid=placesModelList.get(position).getPlacesId();
    }
        @Override
        public int getItemCount () {
        return placesModelList.size();
    }
        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView placeImage;
            public String placeid;
            public  boolean ADDEDTOWISHLIST = false;
            private FloatingActionButton addToFav;
            private TextView placetitle, placeRating, placeState, placeCountry, adderUser;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                placeImage = itemView.findViewById(R.id.placeImageView);
                placetitle = itemView.findViewById(R.id.placeTitletext);
                placeState = itemView.findViewById(R.id.placeState);
                placeCountry = itemView.findViewById(R.id.placeCountry);
                placeRating = itemView.findViewById(R.id.tv_product_rating_miniView_wish1);
                adderUser = itemView.findViewById(R.id.placeFoundBy);
                addToFav=itemView.findViewById(R.id.addTofavBtn);

            }
            public void setPlaceData(String ImageUri, final String placetitleText, String placeStateText, String placeCountyText, int position) {
                for (final PlacesModel model : placesModelList) {
                    if (!model.getPlacesId().isEmpty()) {
                        if (model.getPlaceName().isEmpty()) {
                            FirebaseFirestore.getInstance().collection("PLACES").document(model.getPlacesId())
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                model.setPlace_Image(task.getResult().getString("place_Image"));
                                                model.setPlaceName(task.getResult().getString("place_Name"));
                                                model.setPlace_state(task.getResult().getString("place_state"));
                                                model.setPlace_country(task.getResult().getString("place_country"));
                                                if (placesModelList.indexOf(model) == placesModelList.size() - 1) {
                                                    if (placeRecyclerView.getAdapter() != null) {
                                                        placeRecyclerView.getAdapter().notifyDataSetChanged();
                                                    }
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                }
                Glide.with(itemView.getContext()).load(ImageUri).into(placeImage);
                placetitle.setText(placetitleText);
                placeState.setText(placeStateText);
                placeCountry.setText(placeCountyText);
                if (!placetitleText.isEmpty()) {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent viewPlaceIntent = new Intent(itemView.getContext(), PlaceDetailsActivity.class);
                            viewPlaceIntent.putExtra("placeId", placetitleText);
                            itemView.getContext().startActivity(viewPlaceIntent);
                        }
                    });
                }


            }
          private void addfav(){
                        if (ADDEDTOWISHLIST) {
                            int index = DatabaseCodes.WishListlist.indexOf(placeId);
                            DatabaseCodes.removeFromWishList(index, itemView.getContext());
                            addToFav.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                        } else {
                            addToFav.setSupportImageTintList(itemView.getResources().getColorStateList(R.color.colorPrimaryDark));
                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("place_id_" + String.valueOf(DatabaseCodes.WishListlist.size()), placeId);
                            addProduct.put("list_size", (long) DatabaseCodes.WishListlist.size() + 1);

                            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                                    .document("MY_FAV").update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                if (wishListModelList.size() != 0) {

                                                    wishListModelList.add(new PlacesModel(placeId,
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
                                                ADDEDTOWISHLIST = true;
                                                DatabaseCodes.WishListlist.add(placeId);
                                                Toast.makeText(itemView.getContext(), "Product Added to wishlist successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                addtoWishList.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                                String error = task.getException().getMessage();
                                                Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                    });
                        }
                    }
                }


    }










