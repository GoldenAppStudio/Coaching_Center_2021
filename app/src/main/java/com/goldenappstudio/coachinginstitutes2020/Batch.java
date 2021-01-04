package com.goldenappstudio.coachinginstitutes2020;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class Batch extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RelativeLayout no_batch_layout, batch_list_layout;
    Dialog myDialog;

    public Batch() {
        // Required empty public constructor
    }

    public static Batch newInstance(String param1, String param2) {
        Batch fragment = new Batch();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_batch, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button view_store = getView().findViewById(R.id.view_store);
        FloatingActionButton add_batch = getView().findViewById(R.id.add_batch);
        no_batch_layout = getView().findViewById(R.id.no_batch_layout);
        batch_list_layout = getView().findViewById(R.id.batch_list_layout);
        List<String> batch_id_list = new ArrayList<>();

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

        view_store.setOnClickListener(view1 -> {
            FragmentChangeListener fc = (FragmentChangeListener) getActivity();
            assert fc != null;
            fc.replaceFragment(new Store());
        });

        add_batch.setOnClickListener(view3 -> {
            myDialog = new Dialog(getContext());
            myDialog.setContentView(R.layout.add_batch_popup);
            EditText batch_id = myDialog.findViewById(R.id.batch_id);

            CircularProgressButton btn = myDialog.findViewById(R.id.btn_id);
            btn.setOnClickListener(view1 -> {
                btn.startAnimation();

                DatabaseReference db = FirebaseDatabase.getInstance().getReference("batches/");

                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Toast.makeText(getContext(), "Batch not found", Toast.LENGTH_SHORT).show();
                            batch_id.setText("");
                            btn.revertAnimation();
                        } else {
                            int a = 0;
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                if (ds.child("batch_id").getValue().toString().equals(batch_id.getText().toString())) {
                                    a = 1;
                                    add_batch_to_user(batch_id.getText().toString());
                                }
                            }
                            if (a == 0) {
                                Toast.makeText(getContext(), "Batch not found", Toast.LENGTH_SHORT).show();
                                batch_id.setText("");
                                btn.revertAnimation();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            });

            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            myDialog.show();
        });

        // Batch Recycler View Stuff ...
        DatabaseReference databaseReference;
        List<BatchClass> list = new ArrayList<>();
        RecyclerView recyclerView;
        final BatchRecycler[] adapter = new BatchRecycler[1];

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
                    if (batch_id_list.contains(dataSnapshot.child("batch_id").getValue().toString())) {
                        BatchClass batchClass = dataSnapshot.getValue(BatchClass.class);
                        list.add(batchClass);
                    }
                }

                Collections.reverse(list);
                adapter[0] = new BatchRecycler(getActivity(), list, getFragmentManager(), Batch.this);
                recyclerView.setAdapter(adapter[0]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void add_batch_to_user(String batch_id) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference
                .child("batches")
                .child(reference.child("batches").push().getKey())
                .child("batch_id")
                .setValue(batch_id);

        myDialog.dismiss();
        // Reload current fragment
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }
}


class BatchRecycler extends RecyclerView.Adapter<BatchRecycler.ViewHolder> {

    View view;
    Context context;
    List<BatchClass> MainImageUploadInfoList;
    FragmentManager fragmentManager;
    Fragment fragment;

    public BatchRecycler(Context context, List<BatchClass> TempList, FragmentManager fragmentManager, Fragment fragment) {
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

        final BatchClass batchClass = MainImageUploadInfoList.get(position);
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


class BatchClass {
    private String batch_name;
    private String batch_id;
    private String created_by;

    public BatchClass() {
        //empty constructor needed
    }

    public BatchClass(String batch_name,
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
