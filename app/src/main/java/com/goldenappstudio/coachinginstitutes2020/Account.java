package com.goldenappstudio.coachinginstitutes2020;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Account extends Fragment {
    public Account() {
        // Required empty public constructor
    }

    ImageView image;
    private TextView name, father, mother, phone, nationality, state, district, village;
    private TextView aadhar, dob, email, gender, tenth, twelve, user_name;
    private String USER_ID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ScrollView userPersonalView = getView().findViewById(R.id.user_personal_view);
        FloatingActionButton fb = getView().findViewById(R.id.user_edit);
        TextView personal = getView().findViewById(R.id.user_personal);
        TextView batches = getView().findViewById(R.id.user_batches);
        TextView details = getView().findViewById(R.id.user_details);
        user_name = getView().findViewById(R.id.user_name);
        ImageView user_image = getView().findViewById(R.id.user_image);

        name = getView().findViewById(R.id.name_show);
        father = getView().findViewById(R.id.father_name_show);
        mother = getView().findViewById(R.id.mother_name_show);
        phone = getView().findViewById(R.id.phone_show);
        nationality = getView().findViewById(R.id.nationality_show);
        state = getView().findViewById(R.id.state_show);
        district = getView().findViewById(R.id.district_show);
        village = getView().findViewById(R.id.village_show);
        dob = getView().findViewById(R.id.dob_show);
        email = getView().findViewById(R.id.email_show);
        gender = getView().findViewById(R.id.gender_show);
        aadhar = getView().findViewById(R.id.aadhar_show);
        tenth = getView().findViewById(R.id.tenth_mark_show);
        twelve = getView().findViewById(R.id.twelve_mark_show);

        fb.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), RegistrationActivity.class);
            startActivity(intent);
        });

        fetch_data();

        personal.setOnClickListener(v -> {
            userPersonalView.setVisibility(View.VISIBLE);
            fb.setVisibility(View.VISIBLE);

            personal.setTypeface(Typeface.DEFAULT_BOLD);
            details.setTypeface(Typeface.DEFAULT);
            batches.setTypeface(Typeface.DEFAULT);
            personal.setTextColor(Color.parseColor("#000000"));
            details.setTextColor(Color.parseColor("#888888"));
            batches.setTextColor(Color.parseColor("#888888"));
        });

        batches.setOnClickListener(v -> {
            userPersonalView.setVisibility(View.GONE);
            fb.setVisibility(View.GONE);

            batches.setTypeface(Typeface.DEFAULT_BOLD);
            details.setTypeface(Typeface.DEFAULT);
            personal.setTypeface(Typeface.DEFAULT);
            personal.setTextColor(Color.parseColor("#888888"));
            details.setTextColor(Color.parseColor("#888888"));
            batches.setTextColor(Color.parseColor("#000000"));
        });

        details.setOnClickListener(v -> {
            userPersonalView.setVisibility(View.GONE);
            fb.setVisibility(View.GONE);

            details.setTypeface(Typeface.DEFAULT_BOLD);
            batches.setTypeface(Typeface.DEFAULT);
            personal.setTypeface(Typeface.DEFAULT);
            personal.setTextColor(Color.parseColor("#888888"));
            details.setTextColor(Color.parseColor("#000000"));
            batches.setTextColor(Color.parseColor("#888888"));
        });
    }

    public void onSaveInstanceState() {}

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
