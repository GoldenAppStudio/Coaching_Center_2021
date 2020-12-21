package com.goldenappstudio.coachinginstitutes2020;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ethanhua.skeleton.BuildConfig;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.goldenappstudio.coachinginstitutes2020.MainActivity.fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empty public constructor

    }

    ViewFlipper viewFlipper;
    CardView mCardViewShare;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewFlipper = getView().findViewById(R.id.view_flipper);
        viewFlipper.setInAnimation(this.getContext(), android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this.getContext(), android.R.anim.slide_out_right);
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.startFlipping();

        DatabaseReference databaseReference;
        List<TrendingVideo> list = new ArrayList<>();
        RecyclerView recyclerView;
        final TrendingVideoRecycle[] adapter = new TrendingVideoRecycle[1];
        CardView complete_your_profile_card, free_study_material;
        complete_your_profile_card = getView().findViewById(R.id.complete_your_profile_card);
        free_study_material = getView().findViewById(R.id.free_study_material);

        TextView textView = getView().findViewById(R.id.RAND_1);
        mCardViewShare = getView().findViewById(R.id.cardViewShare);

        mCardViewShare.setOnClickListener(view1 -> {
            try {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Download Coaching Institute application for preparation of govt jobs and learning. Download Link\n" + "https://play.google.com/store/apps/details?id=com.goldenappstudio.coachinginstitutes2020");
                shareIntent.setType("text/plane");
                startActivity(Intent.createChooser(shareIntent, "Share Coaching App via"));
            } catch (Exception e) {
                //e.toString();
            }

        });

        recyclerView = getView().findViewById(R.id.trending_video_thumb_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        SkeletonScreen skeletonScreen = Skeleton.bind(recyclerView)
                .adapter(adapter[0])
                .load(R.layout.trending_video_recycler)
                .show();

        complete_your_profile_card.setOnClickListener(v -> {
            FragmentChangeListener fc = (FragmentChangeListener) getActivity();
            assert fc != null;
            fc.replaceFragment(new Account());
        });

        free_study_material.setOnClickListener(v -> {
            FragmentChangeListener fc = (FragmentChangeListener) getActivity();
            assert fc != null;
            fc.replaceFragment(new Store());
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("store/videos/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getChildren() == null) {
                    textView.setVisibility(View.GONE);
                } else {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        TrendingVideo trendingVideo = dataSnapshot.getValue(TrendingVideo.class);
                        list.add(trendingVideo);
                        skeletonScreen.hide();
                    }

                    Collections.reverse(list);
                    adapter[0] = new TrendingVideoRecycle(getActivity(), list);
                    recyclerView.setAdapter(adapter[0]);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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
        holder.title.setText(trendingVideo.getVideo_title());
        // Check if video is already purchased ...
        DatabaseReference db = FirebaseDatabase.getInstance()
                .getReference("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        db.child("purchased_courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    if (trendingVideo.getVideo_price().isEmpty() || trendingVideo.getVideo_price().equals("0")) {
                        holder.price.setText("Free");
                    } else {
                        holder.price.setText(String.format("Rs %s", trendingVideo.getVideo_price()));
                    }
                } else {
                    int a = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.child("video_id").getValue().toString().equals(trendingVideo.getVideo_id())) {
                            holder.price.setText("PURCHASED");
                            a = 1;
                        }
                    }
                    if (a == 0) {
                        if (trendingVideo.getVideo_price().isEmpty() || trendingVideo.getVideo_price().equals("0")) {
                            holder.price.setText("Free");
                        } else {
                            holder.price.setText(String.format("Rs %s", trendingVideo.getVideo_price()));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.upload_time.setText(trendingVideo.getVideo_upload_time());
        holder.duration.setText(trendingVideo.getVideo_duration());
        holder.author.setText(String.format("By %s", trendingVideo.getVideo_teacher()));
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl("gs://coaching-institute-project.appspot.com/store/videos/" + trendingVideo.getVideo_id() + ".mp4");

        gsReference.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(context).load(uri.toString()).into(holder.image)).addOnFailureListener(exception -> {
            // Handle any errors
        });

        holder.itemView.setOnClickListener(v -> {
            if (trendingVideo.getVideo_price().equals("0") || trendingVideo.getVideo_price().isEmpty()) {
                Intent intent = new Intent(v.getContext(), VideoPlayer.class);
                intent.putExtra("video_title", trendingVideo.getVideo_title());
                intent.putExtra("video_id", trendingVideo.getVideo_id());
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                db.child("purchased_courses").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Intent intent = new Intent(v.getContext(), GooglePayTestActivity.class);
                            intent.putExtra("video_title", trendingVideo.getVideo_title());
                            intent.putExtra("video_price", trendingVideo.getVideo_price());
                            intent.putExtra("video_id", trendingVideo.getVideo_id());
                            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            int a = 0;
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                if (ds.child("video_id").getValue().toString().equals(trendingVideo.getVideo_id())) {
                                    a = 1;
                                    Intent intent = new Intent(v.getContext(), VideoPlayer.class);
                                    intent.putExtra("video_title", trendingVideo.getVideo_title());
                                    intent.putExtra("video_id", trendingVideo.getVideo_id());
                                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                }
                            }
                            if (a == 0) {
                                Intent intent = new Intent(v.getContext(), GooglePayTestActivity.class);
                                intent.putExtra("video_title", trendingVideo.getVideo_title());
                                intent.putExtra("video_price", trendingVideo.getVideo_price());
                                intent.putExtra("video_id", trendingVideo.getVideo_id());
                                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });
    }

    @Override
    public int getItemCount() {
        return Math.min(MainImageUploadInfoList.size(), 5);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, price, duration, upload_time, author;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.trending_video_title);
            price = itemView.findViewById(R.id.trending_video_price);
            image = itemView.findViewById(R.id.trending_video_thumb_recycle);
            author = itemView.findViewById(R.id.trending_video_author);
            duration = itemView.findViewById(R.id.duration_of_trending_video);
            upload_time = itemView.findViewById(R.id.trending_video_upload_time);
        }
    }
}

class TrendingVideo {
    private String video_title, video_description, video_price, video_teacher, video_duration;
    private String video_id, video_upload_time;

    public TrendingVideo() {
        //empty constructor needed
    }

    public TrendingVideo(String video_title, String video_description, String video_upload_time,
                         String video_teacher, String video_price, String video_id, String video_duration) {
        this.video_title = video_title;
        this.video_teacher = video_teacher;
        this.video_price = video_price;
        this.video_id = video_id;
        this.video_description = video_description;
        this.video_upload_time = video_upload_time;
        this.video_duration = video_duration;
    }

    public String getVideo_title() {
        return video_title;
    }

    public String getVideo_description() {
        return video_description;
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getVideo_duration() {
        return video_duration;
    }

    public String getVideo_price() {
        return video_price;
    }

    public String getVideo_teacher() {
        return video_teacher;
    }

    public String getVideo_upload_time() {
        return video_upload_time;
    }
}
