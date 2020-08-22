package com.goldenappstudio.coachinginstitutes2020;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {


    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatabaseReference databaseReference;
        ProgressDialog progressDialog;
        List<Notification> list = new ArrayList<>();
        RecyclerView recyclerView;
        final NotificationRecycler[] adapter = new NotificationRecycler[1];

        recyclerView = getView().findViewById(R.id.notification_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Data from Database");
        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("notifications/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Notification subService = dataSnapshot.getValue(Notification.class);
                    list.add(subService);
                }

                adapter[0] = new NotificationRecycler(getActivity(), list);
                recyclerView.setAdapter(adapter[0]);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

    }

    public void onSaveInstanceState() {
    }
}

class NotificationRecycler extends RecyclerView.Adapter<NotificationRecycler.ViewHolder> {

    View view;
    Context context;
    List<Notification> MainImageUploadInfoList;
    public static String NOTIFICATION_UID;

    public NotificationRecycler(Context context, List<Notification> TempList) {
        this.MainImageUploadInfoList = TempList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Notification notification = MainImageUploadInfoList.get(position);
        holder.title.setText(notification.getTitle());
        holder.details.setText(String.format("by %s (%s)", notification.getTeacher(), notification.getTime()));

        /*holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), VideoPlayer.class);
            SUB_SERVICE_UID = trendingVideo.getUID();
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });*/
    }

    @Override
    public int getItemCount() {
        return MainImageUploadInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, details;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notification_title);
            details = itemView.findViewById(R.id.notification_details);
        }
    }
}

class Notification {
    private String title;
    private String time;
    private String teacher;

    public Notification() {
        //empty constructor needed
    }

    public Notification(String title, String time, String teacher) {
        this.title = title;
        this.time = time;
        this.teacher = teacher;
    }

    public String getTitle() {
        return title;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getTime() {
        return time;
    }
}

