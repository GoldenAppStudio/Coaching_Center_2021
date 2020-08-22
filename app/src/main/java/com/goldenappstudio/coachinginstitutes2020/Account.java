package com.goldenappstudio.coachinginstitutes2020;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class Account extends Fragment {
    public Account() {
        // Required empty public constructor
    }

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

        personal.setOnClickListener(v -> {
            userPersonalView.setVisibility(View.VISIBLE);
            fb.setVisibility(View.GONE);

            personal.setTypeface(Typeface.DEFAULT_BOLD);
            details.setTypeface(Typeface.DEFAULT);
            batches.setTypeface(Typeface.DEFAULT);
            personal.setTextColor(Color.parseColor("#000000"));
            details.setTextColor(Color.parseColor("#888888"));
            batches.setTextColor(Color.parseColor("#888888"));
        });

        batches.setOnClickListener(v -> {
            userPersonalView.setVisibility(View.GONE);
            fb.setVisibility(View.VISIBLE);

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

    public void onSaveInstanceState() {
    }
}
