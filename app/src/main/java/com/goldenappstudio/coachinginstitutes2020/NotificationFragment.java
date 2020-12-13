package com.goldenappstudio.coachinginstitutes2020;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

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
        List<Notification> list = new ArrayList<>();
        RecyclerView recyclerView;
        final NotificationRecycler[] adapter = new NotificationRecycler[1];

        recyclerView = getView().findViewById(R.id.notification_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SkeletonScreen skeletonScreen = Skeleton.bind(recyclerView)
                .adapter(adapter[0])
                .load(R.layout.notification_recycler)
                .show();

        databaseReference = FirebaseDatabase.getInstance().getReference("notifications/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Notification subService = dataSnapshot.getValue(Notification.class);
                    list.add(subService);
                }

                Collections.reverse(list);
                adapter[0] = new NotificationRecycler(getActivity(), list);
                recyclerView.setAdapter(adapter[0]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void onSaveInstanceState() {
    }
}

class NotificationRecycler extends RecyclerView.Adapter<NotificationRecycler.ViewHolder> {

    View view;
    Context context;
    List<Notification> MainImageUploadInfoList;

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

        holder.itemView.setOnClickListener(v -> {
            Dialog myDialog = new Dialog(context);
            myDialog.setContentView(R.layout.show_popup);
            TextView title = myDialog.findViewById(R.id.notification_title_popup);
            TextView content = myDialog.findViewById(R.id.notification_content_popup);

            title.setText(notification.getTitle());
            content.setText(notification.getContent());

            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
        });
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
    private String notification_uid;
    private String content;

    public Notification() {
        //empty constructor needed
    }

    public Notification(String title, String time,
                        String teacher, String notification_uid, String content) {
        this.title = title;
        this.time = time;
        this.teacher = teacher;
        this.notification_uid = notification_uid;
        this.content = content;
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

    public String getNotification_uid() {
        return notification_uid;
    }

    public String getContent() {
        return content;
    }
}

