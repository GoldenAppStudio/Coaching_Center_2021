package com.goldenappstudio.coachinginstitutes2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class FreeStudyMaterial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_study_material);

        DatabaseReference databaseReference;
        List<FreeStudyMaterialClass> list = new ArrayList<>();
        RecyclerView recyclerView;
        final FreeStudyMaterialRecycler[] adapter = new FreeStudyMaterialRecycler[1];

        recyclerView = findViewById(R.id.free_study_material_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SkeletonScreen skeletonScreen = Skeleton.bind(recyclerView)
                .adapter(adapter[0])
                .load(R.layout.free_study_material_recycler)
                .show();

        databaseReference = FirebaseDatabase.getInstance().getReference("fsm/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FreeStudyMaterialClass freeStudyMaterialClass = dataSnapshot.getValue(FreeStudyMaterialClass.class);
                    list.add(freeStudyMaterialClass);
                }

                adapter[0] = new FreeStudyMaterialRecycler(FreeStudyMaterial.this, list);
                recyclerView.setAdapter(adapter[0]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }
}

// Free Study Material Recycler Class ...
class FreeStudyMaterialRecycler extends RecyclerView.Adapter<FreeStudyMaterialRecycler.ViewHolder> {
    View view;
    Context context;
    List<FreeStudyMaterialClass> MainImageUploadInfoList;

    public FreeStudyMaterialRecycler(Context context, List<FreeStudyMaterialClass> TempList) {
        this.MainImageUploadInfoList = TempList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.free_study_material_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final FreeStudyMaterialClass freeStudyMaterialClass = MainImageUploadInfoList.get(position);
        holder.title.setText(freeStudyMaterialClass.getTitle());
        holder.details.setText(String.format("by %s (%s)", freeStudyMaterialClass.getAuthor(), freeStudyMaterialClass.getDate()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), StudyMaterial.class);
            intent.putExtra("fsm_uid", freeStudyMaterialClass.getUid());
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
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
            title = itemView.findViewById(R.id.free_study_material_title);
            details = itemView.findViewById(R.id.free_study_material_details);
        }
    }
}


// Free Study Material Class ...
class FreeStudyMaterialClass {
    String title, author, date, uid;

    public FreeStudyMaterialClass() {
    }      // needs an empty constructor ...

    public FreeStudyMaterialClass(String title, String author, String date, String uid) {
        this.title = title;
        this.date = date;
        this.author = author;
        this.uid = uid;
    }

    // Getters ...
    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getUid() {
        return uid;
    }
}