package com.goldenappstudio.coachinginstitutes2020;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FreeStudyMaterial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_study_material);

        TextView title = findViewById(R.id.fsm_title_0);
        TextView details = findViewById(R.id.fsm_details_0);
        TextView content = findViewById(R.id.fsm_content_0);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference(getIntent().getStringExtra("fsm_reference"));
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                title.setText(snapshot.child("fsm_title").getValue().toString());
                details.setText(String.format("%s | %s", snapshot.child("fsm_teacher").getValue().toString(), snapshot.child("fsm_upload_time").getValue().toString()));
                content.setText(snapshot.child("fsm_content").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }
}