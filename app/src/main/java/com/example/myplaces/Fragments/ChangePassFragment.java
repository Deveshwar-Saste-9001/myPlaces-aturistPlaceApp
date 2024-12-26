package com.example.myplaces.Fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myplaces.DatabaseFiles.DatabaseCodes;
import com.example.myplaces.LoginActivity;
import com.example.myplaces.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassFragment extends Fragment {
    private EditText newPasstext,conNewPasstext,currentPassword;
    private Button doneBtn;
    public ChangePassFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_change_password, container, false);
        newPasstext=view.findViewById(R.id.newPass);
        conNewPasstext=view.findViewById(R.id.conPass);
        doneBtn=view.findViewById(R.id.changePassword);
        currentPassword=view.findViewById(R.id.currentPassword);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePass();
            }
        });







        return view;
    }

    private void ChangePass(){
        if (!TextUtils.isEmpty(newPasstext.getText())) {
            if (!TextUtils.isEmpty(conNewPasstext.getText())) {
                if (newPasstext.getText().equals(conNewPasstext.getText())){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(DatabaseCodes.email, currentPassword.getText().toString());

// Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(newPasstext.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "Password updated");
                                                } else {
                                                    Log.d(TAG, "Error password not updated");
                                                }
                                            }
                                        });
                                    } else {
                                        Log.d(TAG, "Error auth failed");
                                    }
                                }
                            });
                }else{
                    conNewPasstext.requestFocus();
                    Toast.makeText(getContext(), "Password doesn't Matched", Toast.LENGTH_SHORT).show();
                }

            } else {
                conNewPasstext.requestFocus();
                Toast.makeText(getContext(), "Password Field can't be empty", Toast.LENGTH_SHORT).show();
            }
        } else {
            newPasstext.requestFocus();
            Toast.makeText(getContext(), "Password Field can't be empty", Toast.LENGTH_SHORT).show();

        }




    }
}