package com.goldenappstudio.coachinginstitutes2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Size;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactUs extends AppCompatActivity {

    DatabaseReference databaseReference;
    ListView mListView;

    int[] images = {
            R.drawable.avatar,
            R.drawable.email,
            R.drawable.phone_sp_profile,
            R.drawable.address,
            R.drawable.description
    };

    String[] text = new String[]{};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        getSupportActionBar().setTitle("Contact Us");
        mListView = findViewById(R.id.list_view_sp_profile);

        SpeedDialView speedDialView = findViewById(R.id.speedDial);
        speedDialView.addActionItem(new
                SpeedDialActionItem.Builder(R.id.email_id, R.drawable.email)
                .setFabBackgroundColor(Color.RED)
                .setFabImageTintColor(Color.WHITE).create());
        speedDialView.addActionItem(new
                SpeedDialActionItem.Builder(R.id.phone_id, R.drawable.phone_sp_profile)
                .setFabBackgroundColor(Color.GREEN)
                .setFabImageTintColor(Color.WHITE).create());
        speedDialView.addActionItem(new
                SpeedDialActionItem.Builder(R.id.address_id, R.drawable.address)
                .setFabBackgroundColor(Color.BLUE)
                .setFabImageTintColor(Color.WHITE).create());

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("contacts");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                text = new String[]{
                        snapshot.child("name").getValue().toString(),
                        snapshot.child("email").getValue().toString(),
                        "+91 " + snapshot.child("phone").getValue().toString(),
                        snapshot.child("address").getValue().toString(),
                        snapshot.child("description").getValue().toString(),
                };

                CustomList adapter = new
                        CustomList(ContactUs.this, text, images);
                mListView = findViewById(R.id.list_view_sp_profile);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener((parent, view, position, id) -> {
                });

                speedDialView.setOnActionSelectedListener(actionItem -> {
                    switch (actionItem.getId()) {
                        case R.id.email_id:
                            if (snapshot.child("email").exists() && !snapshot.child("email").getValue().toString().equals("")) {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{snapshot.child("email").getValue().toString()});
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject...");
                                intent.putExtra(Intent.EXTRA_TEXT, "Body of the content here...");
                                intent.putExtra(Intent.EXTRA_CC, "mailcc@gmail.com");
                                intent.setType("text/html");
                                intent.setPackage("com.google.android.gm");
                                startActivity(Intent.createChooser(intent, "Send mail"));
                            } else {
                                Toast.makeText(ContactUs.this, "No email is provided at the moment. Please try again.", Toast.LENGTH_LONG).show();
                                speedDialView.close();
                            }
                            return true;
                        case R.id.phone_id:
                            if (snapshot.child("phone").exists() && !snapshot.child("phone").getValue().toString().equals("")) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + snapshot.child("phone").getValue().toString()));
                                startActivity(intent);
                            } else {
                                Toast.makeText(ContactUs.this, "Phone no is not available at the moment. Please try again.", Toast.LENGTH_LONG).show();
                                speedDialView.close();
                            }
                            return true;
                        case R.id.address_id:
                            if (snapshot.child("lat").exists() && snapshot.child("long").exists()
                                    && !snapshot.child("lat").getValue().toString().equals("")
                                    && !snapshot.child("long").getValue().toString().equals("")) {
                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                        Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
                                startActivity(intent);
                            } else {
                                Toast.makeText(ContactUs.this, "Address is not available at the moment. Please try later.", Toast.LENGTH_LONG).show();
                                speedDialView.close();
                            }
                            return true;
                    }
                    return false;
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}