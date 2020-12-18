
package com.goldenappstudio.coachinginstitutes2020;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private Button submit;
    ImageView image;
    private EditText name, father, mother, phone, nationality, state, district, village;
    private EditText aadhar, dob, email, gender, tenth, twelve;
    private String USER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name = findViewById(R.id.name_edit);
        father = findViewById(R.id.father_name_edit);
        mother = findViewById(R.id.mother_name_edit);
        phone = findViewById(R.id.phone_edit);
        nationality = findViewById(R.id.nationality_edit);
        state = findViewById(R.id.state_edit);
        district = findViewById(R.id.district_edit);
        village = findViewById(R.id.village_edit);
        dob = findViewById(R.id.dob_edit);
        email = findViewById(R.id.email_edit);
        gender = findViewById(R.id.gender_edit);
        aadhar = findViewById(R.id.aadhar_no_edit);
        tenth =  findViewById(R.id.tenth_marks_edit);
        twelve = findViewById(R.id.twelth_marks_edit);
        submit = findViewById(R.id.submit_edit);

        fetch_data();

        getSupportActionBar().setTitle("Register Info");

        submit.setOnClickListener(v -> {
            if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Toast.makeText(this, "Data added/updated", Toast.LENGTH_SHORT).show();
                DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("users/" + USER_ID);

                firebaseDatabase.child("UID").setValue(USER_ID);

                if(name.getText().toString().equals("")) firebaseDatabase.child("name").setValue("N/A");
                else firebaseDatabase.child("name").setValue(name.getText().toString());
                if(father.getText().toString().equals("")) firebaseDatabase.child("fathers_name").setValue("N/A");
                else firebaseDatabase.child("fathers_name").setValue(father.getText().toString());
                if(mother.getText().toString().equals("")) firebaseDatabase.child("mothers_name").setValue("N/A");
                else firebaseDatabase.child("mothers_name").setValue(mother.getText().toString());
                if(phone.getText().toString().equals("")) firebaseDatabase.child("phone").setValue("N/A");
                else firebaseDatabase.child("phone").setValue(phone.getText().toString());
                if(nationality.getText().toString().equals("")) firebaseDatabase.child("nationality").setValue("N/A");
                else firebaseDatabase.child("nationality").setValue(nationality.getText().toString());
                if(state.getText().toString().equals("")) firebaseDatabase.child("state").setValue("N/A");
                else firebaseDatabase.child("state").setValue(state.getText().toString());
                if(district.getText().toString().equals("")) firebaseDatabase.child("district").setValue("N/A");
                else firebaseDatabase.child("district").setValue(district.getText().toString());
                if(village.getText().toString().equals("")) firebaseDatabase.child("village").setValue("N/A");
                else firebaseDatabase.child("village").setValue(village.getText().toString());
                if(dob.getText().toString().equals("")) firebaseDatabase.child("dob").setValue("N/A");
                else firebaseDatabase.child("dob").setValue(dob.getText().toString());
                if(email.getText().toString().equals("")) firebaseDatabase.child("email").setValue("N/A");
                else firebaseDatabase.child("email").setValue(email.getText().toString());
                if(gender.getText().toString().equals("")) firebaseDatabase.child("gender").setValue("N/A");
                else firebaseDatabase.child("gender").setValue(gender.getText().toString());
                if(aadhar.getText().toString().equals("")) firebaseDatabase.child("aadhar").setValue("N/A");
                else firebaseDatabase.child("aadhar").setValue(aadhar.getText().toString());
                if(tenth.getText().toString().equals("")) firebaseDatabase.child("tenth").setValue("N/A");
                else firebaseDatabase.child("tenth").setValue(tenth.getText().toString());
                if(twelve.getText().toString().equals("")) firebaseDatabase.child("twelve").setValue("N/A");
                else firebaseDatabase.child("twelve").setValue(twelve.getText().toString());

            } else {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(this, "Please login first!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetch_data() {
        USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("users/" + USER_ID);

        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if(snapshot.child("name").getValue().toString().equals("N/A")) name.setText("");
                    else name.setText(snapshot.child("name").getValue().toString());
                    if(snapshot.child("fathers_name").getValue().toString().equals("N/A")) father.setText("");
                    else father.setText(snapshot.child("fathers_name").getValue().toString());
                    if(snapshot.child("mothers_name").getValue().toString().equals("N/A")) mother.setText("");
                    else mother.setText(snapshot.child("mothers_name").getValue().toString());
                    if(snapshot.child("phone").getValue().toString().equals("N/A")) phone.setText("");
                    else phone.setText(snapshot.child("phone").getValue().toString());
                    if(snapshot.child("dob").getValue().toString().equals("N/A")) dob.setText("");
                    else dob.setText(snapshot.child("dob").getValue().toString());
                    if(snapshot.child("email").getValue().toString().equals("N/A")) email.setText("");
                    else email.setText(snapshot.child("email").getValue().toString());
                    if(snapshot.child("gender").getValue().toString().equals("N/A")) gender.setText("");
                    else gender.setText(snapshot.child("gender").getValue().toString());
                    if(snapshot.child("aadhar").getValue().toString().equals("N/A")) aadhar.setText("");
                    else aadhar.setText(snapshot.child("aadhar").getValue().toString());
                    if(snapshot.child("nationality").getValue().toString().equals("N/A")) nationality.setText("");
                    else nationality.setText(snapshot.child("nationality").getValue().toString());
                    if(snapshot.child("state").getValue().toString().equals("N/A")) state.setText("");
                    else state.setText(snapshot.child("state").getValue().toString());
                    if(snapshot.child("district").getValue().toString().equals("N/A")) district.setText("");
                    else district.setText(snapshot.child("district").getValue().toString());
                    if(snapshot.child("village").getValue().toString().equals("N/A")) village.setText("");
                    else village.setText(snapshot.child("village").getValue().toString());
                    if(snapshot.child("tenth").getValue().toString().equals("N/A")) tenth.setText("");
                    else tenth.setText(snapshot.child("tenth").getValue().toString());
                    if(snapshot.child("twelve").getValue().toString().equals("N/A")) twelve.setText("");
                    else twelve.setText(snapshot.child("twelve").getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
