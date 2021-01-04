package com.goldenappstudio.coachinginstitutes2020;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class Store extends Fragment {
    public Store() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button video_button = view.findViewById(R.id.store_video_button);
        Button pdf_button = view.findViewById(R.id.store_pdf_button);
        Button fsm_button = view.findViewById(R.id.store_fsm_button);
        LinearLayout video_layout = view.findViewById(R.id.store_video_layout);
        LinearLayout pdf_layout = view.findViewById(R.id.store_pdf_layout);
        LinearLayout fsm_layout = view.findViewById(R.id.store_fsm_layout);

        video_button.setOnClickListener(view1 -> {
            video_button.setBackgroundColor(Color.parseColor("#7579e7"));
            pdf_button.setBackgroundColor(Color.parseColor("#bbbbbb"));
            fsm_button.setBackgroundColor(Color.parseColor("#bbbbbb"));

            video_button.setTextColor(Color.parseColor("#ffffff"));
            pdf_button.setTextColor(Color.parseColor("#000000"));
            fsm_button.setTextColor(Color.parseColor("#000000"));

            video_layout.setVisibility(View.VISIBLE);
            pdf_layout.setVisibility(View.GONE);
            fsm_layout.setVisibility(View.GONE);
        });
        pdf_button.setOnClickListener(view1 -> {
            video_button.setBackgroundColor(Color.parseColor("#bbbbbb"));
            pdf_button.setBackgroundColor(Color.parseColor("#7579e7"));
            fsm_button.setBackgroundColor(Color.parseColor("#bbbbbb"));

            video_button.setTextColor(Color.parseColor("#000000"));
            pdf_button.setTextColor(Color.parseColor("#ffffff"));
            fsm_button.setTextColor(Color.parseColor("#000000"));

            video_layout.setVisibility(View.GONE);
            pdf_layout.setVisibility(View.VISIBLE);
            fsm_layout.setVisibility(View.GONE);
        });
        fsm_button.setOnClickListener(view1 -> {
            video_button.setBackgroundColor(Color.parseColor("#bbbbbb"));
            pdf_button.setBackgroundColor(Color.parseColor("#bbbbbb"));
            fsm_button.setBackgroundColor(Color.parseColor("#7579e7"));

            video_button.setTextColor(Color.parseColor("#000000"));
            pdf_button.setTextColor(Color.parseColor("#000000"));
            fsm_button.setTextColor(Color.parseColor("#ffffff"));

            video_layout.setVisibility(View.GONE);
            pdf_layout.setVisibility(View.GONE);
            fsm_layout.setVisibility(View.VISIBLE);
        });

        DatabaseReference databaseReference;
        List<InterestingVideo> list = new ArrayList<>();
        RecyclerView recyclerView;
        final InterestVideoRecycle[] adapter = new InterestVideoRecycle[1];

        DatabaseReference reference;
        List<MoreVideo> list1 = new ArrayList<>();
        RecyclerView recyclerView1;
        final MoreVideoRecycler[] adapter1 = new MoreVideoRecycler[1];

        DatabaseReference pdf_reference;
        List<Pdfs> pdf_list = new ArrayList<>();
        RecyclerView pdf_recyclerview;
        final PdfRecycler[] pdf_adapter = new PdfRecycler[1];

        DatabaseReference fsm_reference;
        List<Fsm> fsm_list = new ArrayList<>();
        RecyclerView fsm_recyclerview;
        final FsmRecycler[] fsm_adapter = new FsmRecycler[1];

        recyclerView = getView().findViewById(R.id.interest_video_thumb_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));

        recyclerView1 = getView().findViewById(R.id.more_video_thumb_recycle);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        pdf_recyclerview = getView().findViewById(R.id.store_pdf_recyclerview);
        pdf_recyclerview.setHasFixedSize(true);
        pdf_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        fsm_recyclerview = getView().findViewById(R.id.store_fsm_recyclerview);
        fsm_recyclerview.setHasFixedSize(true);
        fsm_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        SkeletonScreen skeletonScreen = Skeleton.bind(recyclerView)
                .adapter(adapter[0])
                .load(R.layout.interested_in_recycler)
                .show();

        SkeletonScreen skeletonScreen1 = Skeleton.bind(recyclerView1)
                .adapter(adapter1[0])
                .load(R.layout.more_video_recycler)
                .show();

        SkeletonScreen skeletonScreen2 = Skeleton.bind(pdf_recyclerview)
                .adapter(pdf_adapter[0])
                .load(R.layout.store_pdf_recycler)
                .show();

        SkeletonScreen skeletonScreen3 = Skeleton.bind(fsm_recyclerview)
                .adapter(fsm_adapter[0])
                .load(R.layout.store_fsm_recycler)
                .show();

        databaseReference = FirebaseDatabase.getInstance().getReference("store/videos/");
        reference = FirebaseDatabase.getInstance().getReference("store/videos/");
        pdf_reference = FirebaseDatabase.getInstance().getReference("store/pdfs/");
        fsm_reference = FirebaseDatabase.getInstance().getReference("store/fsm/");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    InterestingVideo interestingVideo = dataSnapshot.getValue(InterestingVideo.class);
                    list.add(interestingVideo);
                }

                adapter[0] = new InterestVideoRecycle(getActivity(), list);
                recyclerView.setAdapter(adapter[0]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MoreVideo moreVideo = dataSnapshot.getValue(MoreVideo.class);
                    list1.add(moreVideo);
                }

                Collections.reverse(list1);
                adapter1[0] = new MoreVideoRecycler(getActivity(), list1);
                recyclerView1.setAdapter(adapter1[0]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        pdf_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Pdfs pdfs = dataSnapshot.getValue(Pdfs.class);
                    pdf_list.add(pdfs);
                }

                Collections.reverse(pdf_list);
                pdf_adapter[0] = new PdfRecycler(getActivity(), pdf_list);
                pdf_recyclerview.setAdapter(pdf_adapter[0]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        fsm_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Fsm fsm = dataSnapshot.getValue(Fsm.class);
                    fsm_list.add(fsm);
                }

                Collections.reverse(fsm_list);
                fsm_adapter[0] = new FsmRecycler(getActivity(), fsm_list);
                fsm_recyclerview.setAdapter(fsm_adapter[0]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}

class InterestVideoRecycle extends RecyclerView.Adapter<InterestVideoRecycle.ViewHolder> {

    View view;
    Context context;
    List<InterestingVideo> MainImageUploadInfoList;
    public static String SUB_SERVICE_UID;

    public InterestVideoRecycle(Context context, List<InterestingVideo> TempList) {
        this.MainImageUploadInfoList = TempList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interested_in_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final InterestingVideo interestingVideo = MainImageUploadInfoList.get(position);
        holder.title.setText(interestingVideo.getVideo_title());
        holder.teacher.setText(interestingVideo.getVideo_teacher());
        holder.upload_time.setText(interestingVideo.getVideo_upload_time());

        DatabaseReference db = FirebaseDatabase.getInstance()
                .getReference("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        db.child("purchased_courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    if (interestingVideo.getVideo_price().isEmpty() || interestingVideo.getVideo_price().equals("0")) {
                        holder.price.setText("Free");
                        if(interestingVideo.getStrike_price() == null) {
                            holder.price_.setText("");
                        } else {
                            holder.price_.setPaintFlags(holder.price_.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            holder.price_.setText(String.format("₹%s", interestingVideo.getStrike_price()));
                        }
                    } else {
                        holder.price.setText(String.format("₹%s", interestingVideo.getVideo_price()));
                        if(interestingVideo.getStrike_price() == null) {
                            holder.price_.setText("");
                        } else {
                            holder.price_.setPaintFlags(holder.price_.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            holder.price_.setText(String.format("₹%s", interestingVideo.getStrike_price()));
                        }                    }
                } else {
                    int a = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.child("video_id").getValue().toString().equals(interestingVideo.getVideo_id())) {
                            holder.price.setText("PURCHASED");
                            a = 1;
                        }
                    }
                    if (a == 0) {
                        if (interestingVideo.getVideo_price().isEmpty() || interestingVideo.getVideo_price().equals("0")) {
                            holder.price.setText("Free");
                            if(interestingVideo.getStrike_price() == null) {
                                holder.price_.setText("");
                            } else {
                                holder.price_.setPaintFlags(holder.price_.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                holder.price_.setText(String.format("₹%s", interestingVideo.getStrike_price()));
                            }
                        } else {
                            holder.price.setText(String.format("₹%s", interestingVideo.getVideo_price()));
                            if(interestingVideo.getStrike_price() == null) {
                                holder.price_.setText("");
                            } else {
                                holder.price_.setPaintFlags(holder.price_.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                holder.price_.setText(String.format("₹%s", interestingVideo.getStrike_price()));
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl("gs://coaching-institute-project.appspot.com/store/videos/" + interestingVideo.getVideo_id() + ".mp4");

        gsReference.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(context).load(uri.toString()).into(holder.image)).addOnFailureListener(exception -> {
            // Handle any errors
        });

        holder.itemView.setOnClickListener(v -> {
            if (interestingVideo.getVideo_price().equals("0") || interestingVideo.getVideo_price().isEmpty()) {
                Intent intent = new Intent(v.getContext(), VideoPlayer.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("video_title", interestingVideo.getVideo_title());
                intent.putExtra("video_price", interestingVideo.getVideo_price());
                intent.putExtra("video_id", interestingVideo.getVideo_id());
                context.startActivity(intent);
            } else {
                db.child("purchased_courses").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Intent intent = new Intent(v.getContext(), GooglePayTestActivity.class);
                            intent.putExtra("video_title", interestingVideo.getVideo_title());
                            intent.putExtra("video_price", interestingVideo.getVideo_price());
                            intent.putExtra("video_id", interestingVideo.getVideo_id());
                            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            int a = 0;
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                if (ds.child("video_id").getValue().toString().equals(interestingVideo.getVideo_id())) {
                                    a = 1;
                                    Intent intent = new Intent(v.getContext(), VideoPlayer.class);
                                    intent.putExtra("video_title", interestingVideo.getVideo_title());
                                    intent.putExtra("video_id", interestingVideo.getVideo_id());
                                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                }
                            }
                            if (a == 0) {
                                Intent intent = new Intent(v.getContext(), GooglePayTestActivity.class);
                                intent.putExtra("video_title", interestingVideo.getVideo_title());
                                intent.putExtra("video_price", interestingVideo.getVideo_price());
                                intent.putExtra("video_id", interestingVideo.getVideo_id());
                                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(MainImageUploadInfoList.size(), 5);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, price, price_, teacher, upload_time;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.interst_video_title);
            price = itemView.findViewById(R.id.interest_video_price);
            image = itemView.findViewById(R.id.interest_video_thumb_recycle);
            teacher = itemView.findViewById(R.id.interest_video_teacher);
            upload_time = itemView.findViewById(R.id.interest_video_upload_time);
            price_ = itemView.findViewById(R.id.interest_video_price_);
        }
    }
}

class InterestingVideo {
    private String video_title, video_description, video_price, video_teacher;
    private String video_id, video_upload_time, video_duration, strike_price;

    public InterestingVideo() {
        //empty constructor needed
    }

    public InterestingVideo(String video_title, String video_description, String video_upload_time,
                            String video_teacher, String video_price,
                            String video_id, String video_duration, String strike_price) {
        this.video_title = video_title;
        this.video_teacher = video_teacher;
        this.video_price = video_price;
        this.video_id = video_id;
        this.video_description = video_description;
        this.video_upload_time = video_upload_time;
        this.video_duration = video_duration;
        this.strike_price = strike_price;
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

    public String getStrike_price() {
        return strike_price;
    }
}

class MoreVideoRecycler extends RecyclerView.Adapter<MoreVideoRecycler.ViewHolder> {

    View view;
    Context context;
    List<MoreVideo> MainImageUploadInfoList;
    public static String SUB_SERVICE_UID;

    public MoreVideoRecycler(Context context, List<MoreVideo> TempList) {
        this.MainImageUploadInfoList = TempList;
        this.context = context;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_video_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final MoreVideo moreVideo = MainImageUploadInfoList.get(position);
        holder.title.setText(moreVideo.getVideo_title());
        DatabaseReference db = FirebaseDatabase.getInstance()
                .getReference("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        db.child("purchased_courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    if (moreVideo.getVideo_price().isEmpty() || moreVideo.getVideo_price().equals("0")) {
                        holder.price.setText("Free");
                        if(moreVideo.getStrike_price() == null) {
                            holder.price_.setText("");
                        } else {
                            holder.price_.setPaintFlags(holder.price_.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            holder.price_.setText(String.format("₹%s", moreVideo.getStrike_price()));
                        }
                    } else {
                        holder.price.setText(String.format("₹%s", moreVideo.getVideo_price()));
                        if(moreVideo.getStrike_price() == null) {
                            holder.price_.setText("");
                        } else {
                            holder.price_.setPaintFlags(holder.price_.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            holder.price_.setText(String.format("₹%s", moreVideo.getStrike_price()));
                        }                    }
                } else {
                    int a = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.child("video_id").getValue().toString().equals(moreVideo.getVideo_id())) {
                            holder.price.setText("PURCHASED");
                            a = 1;
                        }
                    }
                    if (a == 0) {
                        if (moreVideo.getVideo_price().isEmpty() || moreVideo.getVideo_price().equals("0")) {
                            holder.price.setText("Free");
                            if(moreVideo.getStrike_price() == null) {
                                holder.price_.setText("");
                            } else {
                                holder.price_.setPaintFlags(holder.price_.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                holder.price_.setText(String.format("₹%s", moreVideo.getStrike_price()));
                            }
                        } else {
                            holder.price.setText(String.format("₹%s", moreVideo.getVideo_price()));
                            if(moreVideo.getStrike_price() == null) {
                                holder.price_.setText("");
                            } else {
                                holder.price_.setPaintFlags(holder.price_.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                holder.price_.setText(String.format("₹%s", moreVideo.getStrike_price()));
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.teacher.setText(moreVideo.getVideo_teacher());
        holder.upload_time.setText(moreVideo.getVideo_upload_time());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl("gs://coaching-institute-project.appspot.com/store/videos/" + moreVideo.getVideo_id() + ".mp4");

        gsReference.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(context).load(uri.toString()).into(holder.image)).addOnFailureListener(exception -> {
            // Handle any errors
        });

        holder.itemView.setOnClickListener(v -> {
            if (moreVideo.getVideo_price().equals("0") || moreVideo.getVideo_price().isEmpty()) {
                Intent intent = new Intent(v.getContext(), VideoPlayer.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("video_title", moreVideo.getVideo_title());
                intent.putExtra("video_price", moreVideo.getVideo_price());
                intent.putExtra("video_id", moreVideo.getVideo_id());
                context.startActivity(intent);
            } else {
                db.child("purchased_courses").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Intent intent = new Intent(v.getContext(), GooglePayTestActivity.class);
                            intent.putExtra("video_title", moreVideo.getVideo_title());
                            intent.putExtra("video_price", moreVideo.getVideo_price());
                            intent.putExtra("video_id", moreVideo.getVideo_id());
                            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            int a = 0;
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                if (ds.child("video_id").getValue().toString().equals(moreVideo.getVideo_id())) {
                                    a = 1;
                                    Intent intent = new Intent(v.getContext(), VideoPlayer.class);
                                    intent.putExtra("video_title", moreVideo.getVideo_title());
                                    intent.putExtra("video_id", moreVideo.getVideo_id());
                                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                }
                            }
                            if (a == 0) {
                                Intent intent = new Intent(v.getContext(), GooglePayTestActivity.class);
                                intent.putExtra("video_title", moreVideo.getVideo_title());
                                intent.putExtra("video_price", moreVideo.getVideo_price());
                                intent.putExtra("video_id", moreVideo.getVideo_id());
                                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return MainImageUploadInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, price, price_, teacher, upload_time;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.more_video_title);
            price = itemView.findViewById(R.id.more_video_price);
            image = itemView.findViewById(R.id.more_video_thumb_recycle);
            teacher = itemView.findViewById(R.id.more_video_teacher);
            price_ = itemView.findViewById(R.id.more_video_price_);
            upload_time = itemView.findViewById(R.id.more_video_upload_time);
        }
    }
}

class MoreVideo {
    private String video_title, video_description, video_price, video_teacher, video_duration;
    private String video_id, video_upload_time, strike_price;

    public MoreVideo() {
        //empty constructor needed
    }

    public MoreVideo(String video_title, String video_description, String video_upload_time,
                     String video_teacher, String video_price,
                     String video_id, String video_duration, String strike_price) {
        this.video_title = video_title;
        this.video_teacher = video_teacher;
        this.video_price = video_price;
        this.video_id = video_id;
        this.video_description = video_description;
        this.video_upload_time = video_upload_time;
        this.video_duration = video_duration;
        this.strike_price = strike_price;
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

    public String getStrike_price() {
        return strike_price;
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

class PdfRecycler extends RecyclerView.Adapter<PdfRecycler.ViewHolder> {

    View view;
    Context context;
    List<Pdfs> MainImageUploadInfoList;

    public PdfRecycler(Context context, List<Pdfs> TempList) {
        this.MainImageUploadInfoList = TempList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_pdf_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Pdfs pdfs = MainImageUploadInfoList.get(position);
        holder.title.setText(pdfs.getPdf_title());
        holder.details.setText(String.format("by %s (%s)", pdfs.getPdf_teacher(), pdfs.getPdf_upload_time()));

        holder.itemView.setOnClickListener(v -> {
            Dialog myDialog = new Dialog(context);
            myDialog.setContentView(R.layout.show_popup);
            TextView title = myDialog.findViewById(R.id.notification_title_popup);
            TextView content = myDialog.findViewById(R.id.notification_content_popup);

            title.setText(pdfs.getPdf_title());
            content.setText(pdfs.getPdf_description());

            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
        });

        holder.download_pdf.setOnClickListener(view -> {
            StorageReference reference = FirebaseStorage.getInstance().getReference("store/pdfs");
            reference.child(pdfs.getPdf_id() + ".pdf").getDownloadUrl().addOnSuccessListener(uri -> {
                Toast.makeText(context, "Pdf will be downloaded shortly...", Toast.LENGTH_SHORT).show();
                downloadFile(context, pdfs.getPdf_title(), ".pdf",
                        String.valueOf(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)),
                        uri.toString());
            });
        });
    }

    @Override
    public int getItemCount() {
        return MainImageUploadInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, details;
        ImageView download_pdf;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.pdf_title);
            details = itemView.findViewById(R.id.pdf_details);
            download_pdf = itemView.findViewById(R.id.download_pdf);
        }
    }

    public long downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        return downloadmanager.enqueue(request);
    }
}

class Pdfs {
    private String pdf_title, pdf_description, pdf_teacher;
    private String pdf_id, pdf_upload_time;

    public Pdfs() {
        //empty constructor needed
    }

    public Pdfs(String pdf_title, String pdf_description, String pdf_teacher,
                String pdf_id, String pdf_upload_time) {
        this.pdf_title = pdf_title;
        this.pdf_description = pdf_description;
        this.pdf_teacher = pdf_teacher;
        this.pdf_id = pdf_id;
        this.pdf_upload_time = pdf_upload_time;
    }

    public String getPdf_description() {
        return pdf_description;
    }

    public String getPdf_id() {
        return pdf_id;
    }

    public String getPdf_teacher() {
        return pdf_teacher;
    }

    public String getPdf_title() {
        return pdf_title;
    }

    public String getPdf_upload_time() {
        return pdf_upload_time;
    }
}

class FsmRecycler extends RecyclerView.Adapter<FsmRecycler.ViewHolder> {

    View view;
    Context context;
    List<Fsm> MainImageUploadInfoList;

    public FsmRecycler(Context context, List<Fsm> TempList) {
        this.MainImageUploadInfoList = TempList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_fsm_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Fsm fsm = MainImageUploadInfoList.get(position);
        holder.title.setText(fsm.getFsm_title());
        holder.details.setText(String.format("by %s (%s)", fsm.getFsm_teacher(), fsm.getFsm_upload_time()));

        holder.itemView.setOnClickListener(v -> {
            Dialog myDialog = new Dialog(context);
            myDialog.setContentView(R.layout.show_popup);
            TextView title = myDialog.findViewById(R.id.notification_title_popup);
            TextView content = myDialog.findViewById(R.id.notification_content_popup);

            title.setText(fsm.getFsm_title());
            content.setText(fsm.getFsm_content());

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
            title = itemView.findViewById(R.id.fsm_title);
            details = itemView.findViewById(R.id.fsm_details);
        }
    }
}

class Fsm {
    private String fsm_title, fsm_content, fsm_teacher;
    private String fsm_id, fsm_upload_time;

    public Fsm() {
        //empty constructor needed
    }

    public Fsm(String fsm_title, String fsm_content, String fsm_teacher,
               String fsm_id, String fsm_upload_time) {
        this.fsm_title = fsm_title;
        this.fsm_content = fsm_content;
        this.fsm_teacher = fsm_teacher;
        this.fsm_id = fsm_id;
        this.fsm_upload_time = fsm_upload_time;
    }

    public String getFsm_content() {
        return fsm_content;
    }

    public String getFsm_id() {
        return fsm_id;
    }

    public String getFsm_teacher() {
        return fsm_teacher;
    }

    public String getFsm_title() {
        return fsm_title;
    }

    public String getFsm_upload_time() {
        return fsm_upload_time;
    }
}
