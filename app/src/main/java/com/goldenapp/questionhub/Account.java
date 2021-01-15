package com.goldenapp.questionhub;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Account extends Fragment {
    public Account() {
        // Required empty public constructor
    }

    ImageView image;
    private TextView name, father, mother, phone, nationality, state, district, village;
    private TextView aadhar, dob, email, gender, tenth, twelve, user_name;
    private String USER_ID;
    RelativeLayout no_batch_layout, batch_list_layout;

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
        ScrollView userBatchView = getView().findViewById(R.id.user_batch_view);
        ScrollView userDetailsView = getView().findViewById(R.id.user_details_view);
        FloatingActionButton fb = getView().findViewById(R.id.user_edit);
        TextView personal = getView().findViewById(R.id.user_personal);
        TextView batches = getView().findViewById(R.id.user_batches);
        TextView details = getView().findViewById(R.id.user_details);
        user_name = getView().findViewById(R.id.user_name);
        ImageView user_image = getView().findViewById(R.id.user_image);
        no_batch_layout = getView().findViewById(R.id.no_batch_layout);
        batch_list_layout = getView().findViewById(R.id.batch_list_layout);
        List<String> batch_id_list = new ArrayList<>();

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
            userBatchView.setVisibility(View.GONE);
            userDetailsView.setVisibility(View.GONE);
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
            userBatchView.setVisibility(View.VISIBLE);
            userDetailsView.setVisibility(View.GONE);
            fb.setVisibility(View.GONE);

            batches.setTypeface(Typeface.DEFAULT_BOLD);
            details.setTypeface(Typeface.DEFAULT);
            personal.setTypeface(Typeface.DEFAULT);
            personal.setTextColor(Color.parseColor("#888888"));
            details.setTextColor(Color.parseColor("#888888"));
            batches.setTextColor(Color.parseColor("#000000"));
        });

        details.setOnClickListener(v -> {
            userBatchView.setVisibility(View.GONE);
            userDetailsView.setVisibility(View.VISIBLE);
            userPersonalView.setVisibility(View.GONE);
            fb.setVisibility(View.GONE);

            details.setTypeface(Typeface.DEFAULT_BOLD);
            batches.setTypeface(Typeface.DEFAULT);
            personal.setTypeface(Typeface.DEFAULT);
            personal.setTextColor(Color.parseColor("#888888"));
            details.setTextColor(Color.parseColor("#000000"));
            batches.setTextColor(Color.parseColor("#888888"));
        });

        // Check if batch is already added or not ...
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.child("batches").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    no_batch_layout.setVisibility(View.GONE);
                    batch_list_layout.setVisibility(View.VISIBLE);
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        batch_id_list.add(ds.child("batch_id").getValue().toString());
                    }
                } else {
                    no_batch_layout.setVisibility(View.VISIBLE);
                    batch_list_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Batch Recycler View Stuff ...
        DatabaseReference databaseReference;
        List<_BatchClass> list = new ArrayList<>();
        RecyclerView recyclerView;
        final _BatchRecycler[] adapter = new _BatchRecycler[1];

        recyclerView = getView().findViewById(R.id.batch_list_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SkeletonScreen skeletonScreen = Skeleton.bind(recyclerView)
                .adapter(adapter[0])
                .load(R.layout.batch_list_recycler)
                .show();

        databaseReference = FirebaseDatabase.getInstance().getReference("batches/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if(batch_id_list.contains(dataSnapshot.child("batch_id").getValue().toString())) {
                        _BatchClass batchClass = dataSnapshot.getValue(_BatchClass.class);
                        list.add(batchClass);
                    }
                }

                Collections.reverse(list);
                adapter[0] = new _BatchRecycler(getActivity(), list, getFragmentManager(), Account.this);
                recyclerView.setAdapter(adapter[0]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
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

class _BatchRecycler extends RecyclerView.Adapter<_BatchRecycler.ViewHolder> {

    View view;
    Context context;
    List<_BatchClass> MainImageUploadInfoList;
    FragmentManager fragmentManager;
    Fragment fragment;

    public _BatchRecycler(Context context, List<_BatchClass> TempList, FragmentManager fragmentManager, Fragment fragment) {
        this.MainImageUploadInfoList = TempList;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.batch_list_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final _BatchClass batchClass = MainImageUploadInfoList.get(position);
        holder.name.setText(batchClass.getBatch_name());

        holder.created_by.setText(String.format("Batch created by: %s", batchClass.getCreated_by()));
        holder.id.setText(String.format("Batch ID: %s", batchClass.getBatch_id()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BatchActivity.class);
            intent.putExtra("batch_id", batchClass.getBatch_id());
            intent.putExtra("batch_name", batchClass.getBatch_name());
            context.startActivity(intent);
        });

        holder.remove_batch.setOnClickListener(view1 -> {
            new AlertDialog.Builder(context)
                    .setTitle("Are you sure?")
                    .setMessage("You are about to remove this batch from your profile. Continue?")
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton("Yes",
                            (dialog, which) -> {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/batches/");
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                            if (ds.child("batch_id").getValue().toString().equals(batchClass.getBatch_id())) {
                                                databaseReference.child(Objects.requireNonNull(ds.getKey())).removeValue();
                                                fragmentManager.beginTransaction().detach(fragment).attach(fragment).commit();
                                                Toast.makeText(context, "Batch removed from profile", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }).create().show();
        });
    }

    @Override
    public int getItemCount() {
        return MainImageUploadInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, details, created_by, id;
        ImageView remove_batch;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.batch_name);
            created_by = itemView.findViewById(R.id.batch_created_by);
            id = itemView.findViewById(R.id.batch_id_text);
            remove_batch = itemView.findViewById(R.id.remove_batch);
        }
    }
}


class _BatchClass {
    private String batch_name;
    private String batch_id;
    private String created_by;

    public _BatchClass() {
        //empty constructor needed
    }

    public _BatchClass(String batch_name,
                      String created_by, String batch_id, String content) {
        this.batch_name = batch_name;
        this.created_by = created_by;
        this.batch_id = batch_id;
    }

    public String getBatch_name() {
        return batch_name;
    }

    public String getBatch_id() {
        return batch_id;
    }

    public String getCreated_by() {
        return created_by;
    }

}
