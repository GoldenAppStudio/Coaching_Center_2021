package com.goldenapp.questionhub;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GooglePayTestActivity extends AppCompatActivity {

    String TAG = "main";
    final int UPI_PAYMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_pay_test);

        getSupportActionBar().setTitle("Payment Authentication");
        Button send = findViewById(R.id.send);
        TextView product_name = findViewById(R.id.product_name);
        TextView product_price = findViewById(R.id.product_price);
        TextView product_details = findViewById(R.id.product_details);


        product_name.setText(getIntent().getStringExtra("video_title"));
        product_price.setText(getIntent().getStringExtra("video_price"));
       // product_details.setText("" + getIntent().getStringExtra("product_name") + " was uploader" + getIntent().getStringExtra(""));

        send.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(GooglePayTestActivity.this, R.style.MyDialogTheme);
            builder.setMessage("Are you sure you want to process payment of Rs " + getIntent().getStringExtra("video_price") + "?")
                    .setTitle("Continue?")
                    .setCancelable(false)
                    // ranbirsharma127033@oksbi
                    .setPositiveButton("Continue", (dialog, id) -> {
                        payUsingUpi(getIntent().getStringExtra("video_title"), "9728507946@ybl",
                                "Paid Course", getIntent().getStringExtra("video_price"));
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        });
    }

    public void payUsingUpi(String name, String upiId, String note, String amount) {
        Log.e("main ", "name " + name + "--up--" + upiId + "--" + note + "--" + amount);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                //.appendQueryParameter("mc", "")
                //.appendQueryParameter("tid", "02125412")
                //.appendQueryParameter("tr", "25584584")
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                //.appendQueryParameter("refUrl", "blueapp")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        // check if intent resolves
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(GooglePayTestActivity.this, "No UPI/Payment app found, please install one (i.e Paytm, Google Pay, PhonePe) to continue", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response " + resultCode);

        if (requestCode == UPI_PAYMENT) {
            if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                if (data != null) {
                    String trxt = data.getStringExtra("response");
                    Log.e("UPI", "onActivityResult: " + trxt);
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add(trxt);
                    upiPaymentDataOperation(dataList);
                } else {
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
            } else {
                //when user simply back without payment
                Log.e("UPI", "onActivityResult: " + "Return data is null");
                ArrayList<String> dataList = new ArrayList<>();
                dataList.add("nothing");
                upiPaymentDataOperation(dataList);
            }
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(GooglePayTestActivity.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String[] response = str.split("&");
            for (String s : response) {
                String[] equalStr = s.split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(GooglePayTestActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: " + approvalRefNo);
                video_purchased();
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(GooglePayTestActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: " + approvalRefNo);
            } else {
                Toast.makeText(GooglePayTestActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: " + approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(GooglePayTestActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable();
        }
        return false;
    }

    public void video_purchased() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("users");
        db.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("purchased_courses")
                .child(db.push().getKey())
                .child("video_id")
                .setValue(getIntent().getStringExtra("video_id"));
    }
}