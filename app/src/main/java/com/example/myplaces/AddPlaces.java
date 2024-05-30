package com.example.myplaces;

import static android.app.PendingIntent.getActivity;
import static android.os.Build.VERSION;
import static android.os.Build.VERSION_CODES;

import static com.example.myplaces.R.id.selectImage;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddPlaces extends AppCompatActivity {
    private ActivityResultLauncher<Intent> resultLauncher;
    private SeekBar roadCondition, sefetylevel, touristlevel, adventurelevel, costlevel;
    private TextView roadvalue, sefetyvalue, touristvalue, adventurevalue, costvalue;
    private int roadProgress = 0, sefetyProgress = 0, touristProgress = 0, adventureProgress = 0, costProgress = 0;
    private ImageView placeImage;
    private Uri imageuri;
    private double longitude, latitude;
    private boolean updatePhoto = false;
    private EditText longitudetext,latitudetext,placeName, placeAddress, placeDiscription, placestate, placeContry, placepincode;
    private CheckBox hostelCheck, geusthouseCheck, campingspotCheck, otherCheck;
    private Button AddrequestBtn,checkloc,selectImage;
    private Dialog loadingDialog;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_places);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Place");
        roadCondition = findViewById(R.id.roadConditionseek);
        roadvalue = findViewById(R.id.roadvluetext);
        sefetylevel = findViewById(R.id.sefetyLevelseek);
        sefetyvalue = findViewById(R.id.sefetyvluetext);
        touristlevel = findViewById(R.id.toristLevelSeek);
        touristvalue = findViewById(R.id.touristvluetext);
        adventurelevel = findViewById(R.id.adventureLevelSeek);
        adventurevalue = findViewById(R.id.adventurevluetext);
        costlevel = findViewById(R.id.costLevelSeek);
        costvalue = findViewById(R.id.costvluetext);
        placeImage = findViewById(R.id.placeImageSelect);
        placeName = findViewById(R.id.placetitlenew);
        selectImage=findViewById(R.id.selectImage);
        longitudetext=findViewById(R.id.Longitude);
        latitudetext=findViewById(R.id.latitude);
        placeAddress = findViewById(R.id.placelocaladdress);
        placeDiscription = findViewById(R.id.placeDescription);
        hostelCheck = findViewById(R.id.hostelcheck);
        geusthouseCheck = findViewById(R.id.Guesthousecheck);
        campingspotCheck = findViewById(R.id.CampingSpotcheck);
        otherCheck = findViewById(R.id.Othercheck);
        placestate = findViewById(R.id.placelocalState);
        placeContry = findViewById(R.id.placelocalContry);
        placepincode = findViewById(R.id.placelocalpincode);
        AddrequestBtn = findViewById(R.id.requestPlaceBtnnew);
        checkloc=findViewById(R.id.checkLoc);
        loadingDialog = new Dialog(AddPlaces.this);
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        categoryName = getIntent().getStringExtra("Category");

//        findViewById(R.id.open_place_picker_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new PlacePicker.IntentBuilder()
//                        .setLatLong(19.8753, 75.34425)
//                        .showLatLong(true)
//                        .setMapRawResourceStyle(R.raw.map_style)
//                        .setMapZoom(9)
//                        .setMapType(MapType.NORMAL)
//                        .build(Main2Activity.this);
//                startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);
//            }
//        });
        /////seek Bar code start

        checkloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latitude =Double.parseDouble(latitudetext.getText().toString());
                longitude = Double.parseDouble(longitudetext.getText().toString());
                Geocoder geocoder = new Geocoder(AddPlaces.this, Locale.getDefault());
                //List<Address> addresses =geocoder.getFromLocation(latitude, longitude, 1);

                try {
                    List < Address > addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    
                    //txt_paddress.setText(address);
                    placeContry.setText(country);
                    placestate.setText(state);
                    placepincode.setText(postalCode);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        roadCondition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                roadProgress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                roadvalue.setText(roadProgress + "/" + roadCondition.getMax());
            }
        });
        sefetylevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                sefetyProgress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sefetyvalue.setText(sefetyProgress + "/" + roadCondition.getMax());
            }
        });
        touristlevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                touristProgress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                touristvalue.setText(touristProgress + "/" + roadCondition.getMax());
            }
        });
        adventurelevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                adventureProgress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                adventurevalue.setText(adventureProgress + "/" + roadCondition.getMax());
            }
        });
        costlevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                costProgress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                costvalue.setText(costProgress + "/" + roadCondition.getMax());
            }
        });
        //////seek Bar  code end
        registerResult();
      selectImage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent=new Intent(MediaStore.ACTION_PICK_IMAGES);
              resultLauncher.launch(intent);
          }
      });
        AddrequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(placestate.getText())) {
                    if (!TextUtils.isEmpty(placeName.getText())) {
                        if (!TextUtils.isEmpty(placeAddress.getText())) {
                            if (!TextUtils.isEmpty(placeDiscription.getText())) {
                                addPlace();
                            } else {
                                placeDiscription.requestFocus();
                                Toast.makeText(AddPlaces.this, "please add place decription", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            placeAddress.requestFocus();
                            Toast.makeText(AddPlaces.this, "please add place address", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        placeName.requestFocus();
                        Toast.makeText(AddPlaces.this, "please add place decription", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddPlaces.this, "Please select location of place", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void addPlace() {
        if (updatePhoto) {
            loadingDialog.show();
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
            String datefinal = df.format(c);
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("places/" + placeName.getText().toString() + datefinal + ".jpg");
            if (imageuri != null) {
                Glide.with(AddPlaces.this).asBitmap().load(imageuri).into(new ImageViewTarget<Bitmap>(placeImage) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = storageReference.putBytes(data);
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                imageuri = task.getResult();
                                                Map<String, Object> updatedata = new HashMap<>();
                                                updatedata.put("verified", (boolean) true);
                                                updatedata.put("place_Image", task.getResult().toString());
                                                updatedata.put("place_Name", placeName.getText().toString());
                                                updatedata.put("place_state", placestate.getText().toString());
                                                updatedata.put("place_country", placeContry.getText().toString());
                                                updatedata.put("place_pincode", placepincode.getText().toString());
                                                updatedata.put("place_latitude", (double) latitude);
                                                updatedata.put("place_longitude", (double) longitude);
                                                updatedata.put("place_address", placeAddress.getText().toString());
                                                updatedata.put("place_description", placeDiscription.getText().toString());
                                                updatedata.put("road_condition", roadProgress);
                                                updatedata.put("sefety_level", sefetyProgress);
                                                updatedata.put("tourist_level", touristProgress);
                                                updatedata.put("adventure_level", adventureProgress);
                                                updatedata.put("cost_level", costProgress);
                                                updatedata.put("Category", categoryName);
                                                String hostel = "";
                                                String guesthouse = "";
                                                String campingspot = "";
                                                String other = "";
                                                if (hostelCheck.isChecked()) {
                                                    hostel = "hostel";
                                                }
                                                if (geusthouseCheck.isChecked()) {
                                                    guesthouse = "guesthouse";
                                                }
                                                if (campingspotCheck.isChecked()) {
                                                    campingspot = "camping spot";
                                                }
                                                if (otherCheck.isChecked()) {
                                                    other = "other";
                                                }
                                                updatedata.put("places_to_stay", hostel + " " + guesthouse + " " + campingspot + " " + other);

                                                FirebaseFirestore.getInstance().collection("PLACES").document(placeName.getText().toString())
                                                        .set(updatedata).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            FirebaseFirestore.getInstance().collection("Category").document(categoryName).collection("PLACES").document(categoryName + "PLACES").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                    long placeseslistSize = (long) documentSnapshot.get("no_of_places");
                                                                    Map<String, Object> categoryPlace = new HashMap<>();
                                                                    categoryPlace.put("no_of_places", (long) (placeseslistSize + 1));
                                                                    categoryPlace.put("places_ID_" + (placeseslistSize + 1), placeName.getText().toString());
                                                                    categoryPlace.put("verified_" + (placeseslistSize + 1), true);
                                                                    FirebaseFirestore.getInstance().collection("Category").document(categoryName).collection("PLACES").document(categoryName + "PLACES").update(categoryPlace);
                                                                }
                                                            });
                                                            Toast.makeText(AddPlaces.this, "Your Request submited successfully", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            String error = task.getException().getMessage();
                                                            Toast.makeText(AddPlaces.this, error, Toast.LENGTH_SHORT).show();
                                                        }
                                                        loadingDialog.dismiss();
                                                    }
                                                });

                                            } else {
                                                loadingDialog.dismiss();
                                                String error = task.getException().getMessage();
                                                Toast.makeText(AddPlaces.this, error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    loadingDialog.dismiss();
                                    String error = task.getException().getMessage();
                                    Toast.makeText(AddPlaces.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                    }
                });
            }


        }


    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//            super.onActivityResult(requestCode, resultCode, data);
//            if (requestCode == 1) {
//                if (resultCode == Activity.RESULT_OK) {
//                    if (data != null) {
//                        imageuri = data.getData();
//                        updatePhoto = true;
//                        Glide.with(AddPlaces.this).load(imageuri).apply(new RequestOptions().placeholder(R.drawable.ic_local_library_black_24dp)).into(placeImage);
//
//                    } else {
//                        Toast.makeText(AddPlaces.this, "Image not found!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//    }
    private void registerResult(){
        resultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                try {


                    imageuri = o.getData().getData();
                    updatePhoto = true;
                    Glide.with(AddPlaces.this).load(imageuri).apply(new RequestOptions().placeholder(R.drawable.ic_local_library_black_24dp)).into(placeImage);
                }catch (Exception e){

                }
            }
        });
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
