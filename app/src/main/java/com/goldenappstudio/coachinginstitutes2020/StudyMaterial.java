package com.goldenappstudio.coachinginstitutes2020;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class StudyMaterial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_material);

        Button previous = findViewById(R.id.fsm_previous_button);
        Button next = findViewById(R.id.fsm_next_button);
        TextView title = findViewById(R.id.fsm_title);
        TextView details = findViewById(R.id.fsm_details);
        TextView content = findViewById(R.id.fsm_content);
    }
}