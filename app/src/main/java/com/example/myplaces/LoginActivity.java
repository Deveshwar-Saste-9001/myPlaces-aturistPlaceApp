package com.example.myplaces;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText loginid, loginPassword;
    private Button signin;
    private TextView backBtn,signup;
    private TextView forgetPassword;
    private Dialog loadingDialog;
    private CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login); loadingDialog = new Dialog(LoginActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loginid = findViewById(R.id.userEmail);
        loginPassword = findViewById(R.id.userPass);
        signup = findViewById(R.id.joinus_loginpage);
        signin = findViewById(R.id.SignInbtn_login);
        backBtn=findViewById(R.id.backbtn);
        rememberMe=findViewById(R.id.checkBox);
        forgetPassword = findViewById(R.id.forget_pass);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent forgetPasswordIntent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
//                startActivity(forgetPasswordIntent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RegIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(RegIntent);
                finish();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(loginid.getText())) {
                    if (!TextUtils.isEmpty(loginPassword.getText())) {
                        LoginUser(loginid.getText().toString(), loginPassword.getText().toString());
                    } else {
                        loginPassword.requestFocus();
                        Toast.makeText(LoginActivity.this, "Password Field can't be empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    loginid.requestFocus();
                    Toast.makeText(LoginActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void LoginUser(String email, String Password) {
        loadingDialog.show();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "Login Successfuly", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                    finish();
                } else {
                    loadingDialog.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}