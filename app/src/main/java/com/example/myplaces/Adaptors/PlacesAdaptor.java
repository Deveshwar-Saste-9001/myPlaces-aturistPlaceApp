package com.example.myplaces.Adaptors;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myplaces.Models.PlacesModel;
import com.example.myplaces.PlaceDetailsActivity;
import com.example.myplaces.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
public class PlacesAdaptor extends RecyclerView.Adapter<PlacesAdaptor.ViewHolder> {
    private List<PlacesModel> placesModelList;
    private RecyclerView placeRecyclerView;
    public PlacesAdaptor(List<PlacesModel> placesModelList, RecyclerView placeRecyclerView) {
        this.placesModelList = placesModelList;
        this.placeRecyclerView = placeRecyclerView;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.placesitemview, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String image = placesModelList.get(position).getPlace_Image();
        String name = placesModelList.get(position).getPlaceName();
        String state = placesModelList.get(position).getPlace_state();
        String country = placesModelList.get(position).getPlace_country();
        holder.setPlaceData(image, name, state, country, position);
    }
    @Override
    public int getItemCount() {
        return placesModelList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView placeImage;
        private TextView placetitle, placeRating, placeState, placeCountry, adderUser;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeImage = itemView.findViewById(R.id.placeImageView);
            placetitle = itemView.findViewById(R.id.placeTitletext);
            placeState = itemView.findViewById(R.id.placeState);
            placeCountry = itemView.findViewById(R.id.placeCountry);
            placeRating = itemView.findViewById(R.id.tv_product_rating_miniView_wish1);
            adderUser = itemView.findViewById(R.id.placeFoundBy);
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
    }
}
