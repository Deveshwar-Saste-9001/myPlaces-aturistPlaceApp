package com.example.myplaces;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myplaces.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final int SPLASH_DELAY = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        Paper.init(this);

        // Handler to add a delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUserStatus();
            }
        }, SPLASH_DELAY);








//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//
//
//                // Check if user information is already saved
//                if (Paper.book().contains("user")) {
//                    Users savedUser = Paper.book().read("user");
//                    autoLogin(savedUser);
//                } else {
//                    navigateToLogin();
//                }
//                Intent intent =new Intent(MainActivity.this,MainMenuActivity.class);
//                startActivity(intent);
//            }
//        },5000);


    }



    private void checkUserStatus() {
        // Check if a Firebase user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already logged in, navigate to home page
            navigateToMain();
        } else {
            // Check if user information is saved in Paper
            if (Paper.book().contains("user")) {
                Users savedUser = Paper.book().read("user");
                loginWithSavedCredentials(savedUser);
            } else {
                navigateToLogin();
            }
        }
    }

    private void loginWithSavedCredentials(Users user) {
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            navigateToMain();
                        } else {
                            navigateToLogin();
                        }
                    }
                });
    }

    private void navigateToLogin() {
        startActivity(new Intent(MainActivity.this, MainMenuActivity.class));
        finish();
    }

    private void navigateToMain() {
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
        finish();
    }

}