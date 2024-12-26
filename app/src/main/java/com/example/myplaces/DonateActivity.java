package com.example.myplaces;

import static com.example.myplaces.DatabaseFiles.DatabaseCodes.firebaseFirestore;
import static com.example.myplaces.DatabaseFiles.DatabaseCodes.username;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DonateActivity extends AppCompatActivity {
    private TextInputEditText AmountText;
    private Button donateNowBtn;
    private Button backBtn;
    private TextView usernamePament;

    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_donate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.categorytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Donate");
        AmountText=findViewById(R.id.donateNowAmount);
        donateNowBtn=findViewById(R.id.donatenow_btn);
        backBtn=findViewById(R.id.donateBackbtn);
        usernamePament=findViewById(R.id.usernamePament);
usernamePament.setText("Welcome "+ username);
        donateNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(AmountText.getText())){
                    String Amount=AmountText.getText().toString();
upi(Amount);
                }

            }
        });
backBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
    }
});
    }


    private void upi(String Amount) {
        String upi = "9958499222@ptyes";
        String names = "Trip Treasure";
        String note = "purchase";
        payUsingUpi(Amount, upi, names, note);
    }

    private void payUsingUpi(String amount, String upi, String name, String note) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upi)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        upiPayIntent.setPackage(GOOGLE_PAY_PACKAGE_NAME);

        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, 0);
        } else {
            Toast.makeText(DonateActivity.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if ((resultCode == Activity.RESULT_OK) || resultCode == 11) {
                    if (data != null) {
                        String trtx = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trtx);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trtx);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }


    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        String str = data.get(0);
        Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
        String paymentCancel = "";
        if (str == null) str = "discard";
        String status = "";
        String approvalRefNo = "";
        String response[] = str.split("&");
        for (int i = 0; i < response.length; i++) {
            String equalStr[] = response[i].split("=");
            if (equalStr.length >= 2) {
                if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                    status = equalStr[1].toLowerCase();
                } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                    approvalRefNo = equalStr[1];
                }
            } else {
                paymentCancel = "Payment cancelled by user";
            }
        }
        if (status.equals("success")) {
            Toast.makeText(DonateActivity.this, "Transaction successful", Toast.LENGTH_SHORT).show();
            Log.d("UPI", "responseStr: " + approvalRefNo);


            final Map<String, Object> updateStatus = new HashMap<>();
            updateStatus.put("Payment_Status", "paid");
            updateStatus.put("Transaction_ID", approvalRefNo);
            updateStatus.put("Order_Status", "Ordered");
//            firebaseFirestore.collection("ORDERS").document(order_id).update(updateStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//                        Map<String, Object> userOrder = new HashMap<>();
//                        userOrder.put("order_id", order_id);
//                        userOrder.put("time", FieldValue.serverTimestamp());
//                        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS")
//                                .document(order_id).set(userOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            showConfirmationLayout();
//                                        } else {
//                                            Toast.makeText(DeliveryActivity.this, "failed to update user's OrderList", Toast.LENGTH_LONG).show();
//                                        }
//                                    }
//                                });
//                    }
//
//                }
//            });
        } else if ("Payment cancelled by user".equals(paymentCancel)) {
            Toast.makeText(DonateActivity.this, "Payment cancel by user", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(DonateActivity.this, "Transaction failed please try again!", Toast.LENGTH_SHORT).show();
        }

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