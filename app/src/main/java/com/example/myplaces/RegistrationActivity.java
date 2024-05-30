package com.example.myplaces;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    private Dialog loadingDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private TextInputEditText emailValue, nameValue;
    private TextInputEditText mobileValue;
    private TextInputEditText passwordValue;
    private TextInputEditText confPassword;
    private DatabaseReference RootRef;
    public static boolean disableRegCloseBtn = false;
    private Button signup, backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        nameValue = findViewById(R.id.user_name_reg);
        mobileValue = findViewById(R.id.userMobile_reg);
        emailValue = findViewById(R.id.userEmail_reg);
        passwordValue = findViewById(R.id.user_password_reg);
        confPassword = findViewById(R.id.com_user_password_reg);
        signup = findViewById(R.id.SignupBtn_reg);
        backbtn = findViewById(R.id.backBtn_reg);

        loadingDialog = new Dialog(RegistrationActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void CreateAccount() {
        String name = nameValue.getText().toString();
        String mobile = "+91"+mobileValue.getText().toString();
        String email = emailValue.getText().toString();
        String password = passwordValue.getText().toString();
        String confPasswordValue = confPassword.getText().toString();

        if (TextUtils.isEmpty(name)) {
            nameValue.requestFocus();
            Toast.makeText(this, "Enter your name...", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(mobile)) {
            mobileValue.requestFocus();
            Toast.makeText(this, "Enter your mobile number...", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(email)) {
            emailValue.requestFocus();
            Toast.makeText(this, "Enter your email...", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(password)) {
            passwordValue.requestFocus();
            Toast.makeText(this, "Enter your password...", Toast.LENGTH_SHORT).show();

        } else if(TextUtils.isEmpty(confPasswordValue)){
            confPassword.requestFocus();
            Toast.makeText(this, "Enter confirm password...", Toast.LENGTH_SHORT).show();
        }else if(!confPasswordValue.equals(password)){
            confPassword.requestFocus();
            Toast.makeText(this, "your password is missed match...", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingDialog.show();
            ValidatePhoneNumber(name, mobile, password, email);
        }

    }
    private void ValidatePhoneNumber(final String Name, final String Mobile, final String Password, final String Email) {

        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(Mobile).exists())) {
                    final HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("Mobile", Mobile);
                    userdataMap.put("Name", Name);
                    userdataMap.put("Email", Email);
                    userdataMap.put("Password", Password);
                    userdataMap.put("Role", "User");

                    CheckEmailAndPassword(Mobile, Name, Email, Password);


                } else {
                    Toast.makeText(RegistrationActivity.this, "this mobile number" + Mobile + "alredy exist", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Please try again using another number", Toast.LENGTH_SHORT).show();
                    mobileValue.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void CheckEmailAndPassword(final String Mobile, final String Name, final String Email, final String Password) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
        if (emailValue.getText().toString().matches(emailPattern)) {
            firebaseAuth.createUserWithEmailAndPassword(emailValue.getText().toString(), passwordValue.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final HashMap<String, Object> userdataMap = new HashMap<>();
                                userdataMap.put("Mobile", Mobile);
                                userdataMap.put("Name", Name);
                                userdataMap.put("Email", Email);
                                userdataMap.put("profile", "");
                                userdataMap.put("id", firebaseAuth.getUid());
                                userdataMap.put("Password", Password);
                                userdataMap.put("Role", "User");

                                firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                        .set(userdataMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    CollectionReference UserDataReference = firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("USER_DATA");

//////////////////MAPS
                                                    Map<String, Object> notificationMap = new HashMap<>();
                                                    notificationMap.put("list_size", (long) 0);

                                                    //////
                                                    final List<String> documentsNames = new ArrayList<>();
                                                    documentsNames.add("MY_NOTIFICATIONS");

                                                    List<Map<String, Object>> documentsFields = new ArrayList<>();
                                                    documentsFields.add(notificationMap);

                                                    for (int x = 0; x < documentsNames.size(); x++) {
                                                        final int finalX = x;
                                                        UserDataReference.document(documentsNames.get(x))
                                                                .set(documentsFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            if (finalX == documentsNames.size() - 1) {
                                                                                RootRef.child("Users").child(Mobile).updateChildren(userdataMap)
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if (task.isSuccessful()) {
//
                                                                                                    if (disableRegCloseBtn) {
                                                                                                        Toast.makeText(RegistrationActivity.this, "Congratulation your account created", Toast.LENGTH_SHORT).show();
                                                                                                        disableRegCloseBtn = false;
                                                                                                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                                                                                        startActivity(intent);
                                                                                                        finish();
                                                                                                    } else {
                                                                                                        Toast.makeText(RegistrationActivity.this, "Congratulation your account created", Toast.LENGTH_SHORT).show();
                                                                                                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                                                                                        startActivity(intent);
                                                                                                        finish();
                                                                                                        disableRegCloseBtn = false;
                                                                                                    }
                                                                                                    loadingDialog.dismiss();
                                                                                                    nameValue.setText("");
                                                                                                    mobileValue.setText("");
                                                                                                    passwordValue.setText("");
                                                                                                    emailValue.setText("");
                                                                                                } else {
                                                                                                    loadingDialog.dismiss();
                                                                                                    disableRegCloseBtn = false;
                                                                                                    Toast.makeText(RegistrationActivity.this, "Network Error Please Try Again", Toast.LENGTH_SHORT).show();
                                                                                                }

                                                                                            }
                                                                                        });
                                                                            }
                                                                        } else {
                                                                            loadingDialog.dismiss();
                                                                            Toast.makeText(RegistrationActivity.this, "Network Error Please Try Again", Toast.LENGTH_SHORT).show();

                                                                        }


                                                                    }
                                                                });
                                                    }

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(RegistrationActivity.this, "Network Error Please Try Again", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                            } else {
                                loadingDialog.dismiss();
                                String error = task.getException().getMessage();
                                Toast.makeText(RegistrationActivity.this, error, Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

        } else {
            loadingDialog.dismiss();
            Toast.makeText(RegistrationActivity.this, "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (FirebaseAuth.getInstance().getUid() != null) {
            FirebaseAuth.getInstance().signOut();
        }
        nameValue.setText("");
        mobileValue.setText("");
        emailValue.setText("");
        passwordValue.setText("");

        Intent homeIntent = new Intent(RegistrationActivity.this, MainMenuActivity.class);
        startActivity(homeIntent);
        finish();
    }




}