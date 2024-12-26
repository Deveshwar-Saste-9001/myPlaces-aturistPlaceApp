package com.example.myplaces;


import static com.example.myplaces.DatabaseFiles.DatabaseCodes.MyFavlists;
import static com.example.myplaces.DatabaseFiles.DatabaseCodes.firebaseFirestore;
import static com.example.myplaces.DatabaseFiles.DatabaseCodes.wishListModelList;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myplaces.DatabaseFiles.DatabaseCodes;
import com.example.myplaces.Models.PlacesListModel;
import com.example.myplaces.Models.PlacesModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class PlaceDetailsActivity extends AppCompatActivity{
    private Button getDirectionBtn, donatebtn;
    private ImageView placeImageFull;
    private Dialog loadingDialog;
    private TextView placeTitle, placeAddress, placeRatingmini, placetotalRatingmini, placeDecription;
    private ProgressBar roadProgress, sefetyProgress, touristProgress, adventureProgress, costProgress;
    private RecyclerView reviewRecyclerView;

    private RatingBar ratingBar;
    private EditText review;
    public static String placeId;
    private String locationurl;
    private Spinner mSpinnerLanguageTo;
    private Button mButtonTranslate;
    double newLat;
    double newLong;
    public static FloatingActionButton addtoWishList;

    public static boolean ADDEDTOWISHLIST = false;
    public static boolean running_Wishlist_query = false;
    ArrayList<String> arrayList = new ArrayList<>();
    String LanguageName;
    private int oldLang=FirebaseTranslateLanguage.EN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.Viewplacetoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getDirectionBtn = findViewById(R.id.getDirectionBtn);
        placeImageFull = findViewById(R.id.placeimagefull);

        mSpinnerLanguageTo = (Spinner) findViewById(R.id.spinner_language_to);
        mButtonTranslate = (Button) findViewById(R.id.button_translate);
        //submitReviewBtn = findViewById(R.id.submitReviewBtn);
        donatebtn=findViewById(R.id.Donateow);
        placeTitle = findViewById(R.id.placeTitleFull);
        placeAddress = findViewById(R.id.placeAddressFull);
        placeRatingmini = findViewById(R.id.tv_product_rating_miniView_wish1);
        placeDecription = findViewById(R.id.placeDecriptiontext);
        roadProgress = findViewById(R.id.roadprogressbar);
        sefetyProgress = findViewById(R.id.sefetyProgressbar);
        touristProgress = findViewById(R.id.toristProgressbar);
        adventureProgress = findViewById(R.id.AdventureProgressbar);
        costProgress = findViewById(R.id.costProgressbar);
        loadingDialog = new Dialog(PlaceDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        placeId = getIntent().getStringExtra("placeId");



        addtoWishList = (FloatingActionButton) findViewById(R.id.placewishlist);
        addtoWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (!running_Wishlist_query) {
                        running_Wishlist_query = true;
                        if (ADDEDTOWISHLIST) {
                            int index = DatabaseCodes.WishListlist.indexOf(placeId);
                            DatabaseCodes.removeFromWishList(index, PlaceDetailsActivity.this);
                            addtoWishList.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                        } else {
                            addtoWishList.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimaryDark));
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
                                                Toast.makeText(PlaceDetailsActivity.this, "Product Added to wishlist successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                addtoWishList.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                                String error = task.getException().getMessage();
                                                Toast.makeText(PlaceDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                            }
                                            running_Wishlist_query = false;

                                        }
                                    });
                        }
                    }
                }

        });
        donatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PlaceDetailsActivity.this,DonateActivity.class);
                startActivity(intent);
            }
        });
        FirebaseFirestore.getInstance().collection("PLACES").document(placeId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) { Toolbar toolbar = (Toolbar) findViewById(R.id.categorytoolbar);

                if (task.isSuccessful()) {
                    if (task.getResult().get("place_Image").toString()!=null){
                       Glide.with(PlaceDetailsActivity.this).load(task.getResult().get("place_Image").toString()).into(placeImageFull);
                    }
                    placeTitle.setText(task.getResult().get("place_Name").toString());
                    placeAddress.setText(task.getResult().get("place_address").toString() + " " + task.getResult().get("place_state").toString() + " " + task.getResult().get("place_country").toString() + " " + task.getResult().get("place_pincode").toString());
                    placeDecription.setText(task.getResult().get("place_description").toString());
                    roadProgress.setProgress(Integer.parseInt(String.valueOf(task.getResult().getLong("road_condition"))));
                    sefetyProgress.setProgress(Integer.parseInt(String.valueOf(task.getResult().getLong("sefety_level"))));
                    touristProgress.setProgress(Integer.parseInt(String.valueOf(task.getResult().getLong("tourist_level"))));
                    costProgress.setProgress(Integer.parseInt(String.valueOf(task.getResult().getLong("cost_level"))));
                    adventureProgress.setProgress(Integer.parseInt(String.valueOf(task.getResult().getLong("adventure_level"))));
                    locationurl=task.getResult().get("location_url").toString();
                    getSupportActionBar().setTitle(placeTitle.getText());
                    if (DatabaseCodes.WishListlist.contains(placeId)) {
                        ADDEDTOWISHLIST = true;
                        addtoWishList.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimaryDark));
                    } else {
                        ADDEDTOWISHLIST = false;
                        addtoWishList.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#d6d6d6")));
                    }

                }
                loadingDialog.dismiss();
            }
        });
        getDirectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlaceDetailsActivity.this, locationurl, Toast.LENGTH_SHORT).show();
                Uri gmmIntentUri = Uri.parse(locationurl.toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                   startActivity(mapIntent);
            }
        });
        arrayList.add("hindi");
        arrayList.add("english");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,                         android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerLanguageTo.setAdapter(arrayAdapter);
        mSpinnerLanguageTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LanguageName = parent.getItemAtPosition(position).toString();
              //  Toast.makeText(parent.getContext(), "Selected: " + LanguageName,          Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });


        mButtonTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLanguageCode(LanguageName);
            }

        });
    }
    private void getLanguageCode(String language) {
        int langCode;
        switch (language){
            case "hindi":
                langCode = FirebaseTranslateLanguage.HI;
                //mSourceLang.setText("Hindi");
                break;
            case "english":
                langCode = FirebaseTranslateLanguage.EN;
                //mSourceLang.setText("Arabic");

                break;
            default:
                langCode = 0;
        }

        translateText(langCode);
    }

    private void translateText(int langCode) {
        //mTranslatedText.setText("Translating..");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                //from language
                .setSourceLanguage(oldLang)
                // to language
                .setTargetLanguage(langCode)
                .build();

        final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                .getTranslator(options);

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .build();


        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                translator.translate(placeTitle.getText().toString()).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        placeTitle.setText(s);
                    }
                });
                translator.translate(placeAddress.getText().toString()).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        placeAddress.setText(s);
                    }
                });
                translator.translate(placeDecription.getText().toString()).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        placeDecription.setText(s);
                    }
                });
                translator.translate(getDirectionBtn.getText().toString()).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        getDirectionBtn.setText(s);
                    }
                });
                translator.translate(donatebtn.getText().toString()).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        donatebtn.setText(s);
                    }
                });
                oldLang=langCode;
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