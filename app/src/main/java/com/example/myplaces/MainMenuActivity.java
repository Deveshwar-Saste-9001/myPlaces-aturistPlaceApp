package com.example.myplaces;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainMenuActivity extends AppCompatActivity {
    private  Button singInBtn;
    private TextView joinusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);
        singInBtn=findViewById(R.id.signin_mainmenu);
        joinusText=findViewById(R.id.joinus_mainmenu);


        singInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinPage=new Intent(MainMenuActivity.this,LoginActivity.class);
                startActivity(signinPage);


            }
        });
        joinusText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent joinus=new Intent(MainMenuActivity.this,RegistrationActivity.class);
                startActivity(joinus);
            }
        });









    }
}