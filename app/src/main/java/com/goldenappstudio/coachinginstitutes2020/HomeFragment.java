package com.goldenappstudio.coachinginstitutes2020;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
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
public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatabaseReference databaseReference;
        ProgressDialog progressDialog;
        List<TrendingVideo> list = new ArrayList<>();
        RecyclerView recyclerView;
        final TrendingVideoRecycle[] adapter = new TrendingVideoRecycle[1];

        recyclerView = getView().findViewById(R.id.trending_video_thumb_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Data from Database");
        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("treanding_videos/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TrendingVideo subService = dataSnapshot.getValue(TrendingVideo.class);
                    list.add(subService);
                }

                adapter[0] = new TrendingVideoRecycle(getActivity(), list);
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

class TrendingVideoRecycle extends RecyclerView.Adapter<TrendingVideoRecycle.ViewHolder> {

    View view;
    Context context;
    List<TrendingVideo> MainImageUploadInfoList;
    public static String SUB_SERVICE_UID;

    public TrendingVideoRecycle(Context context, List<TrendingVideo> TempList) {
        this.MainImageUploadInfoList = TempList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trending_video_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final TrendingVideo trendingVideo = MainImageUploadInfoList.get(position);
        holder.title.setText(trendingVideo.getTitle());
        holder.price.setText(trendingVideo.getPrice());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl("gs://coaching-institute-project.appspot.com/video_courses/videos/file_example_MP4_480_1_5MG.mp4");

        gsReference.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(context).load(uri.toString()).into(holder.image)).addOnFailureListener(exception -> {
            // Handle any errors
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), VideoPlayer.class);
            SUB_SERVICE_UID = trendingVideo.getUID();
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return MainImageUploadInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, price;
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.trending_video_title);
            price = itemView.findViewById(R.id.trending_video_price);
            image = itemView.findViewById(R.id.trending_video_thumb_recycle);
        }
    }
}

class TrendingVideo {
    private String title;
    private String UID;
    private String teacher;
    private String price;

    public TrendingVideo() {
        //empty constructor needed
    }

    public TrendingVideo(String title, String UID, String teacher, String price) {
        this.title = title;
        this.UID = UID;
        this.price = price;
        this.teacher = teacher;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getUID() {
        return UID;
    }
}
