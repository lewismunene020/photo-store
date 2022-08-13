package com.example.photostore;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private  ImagesAdapter imagesAdapter;
    private List<Upload> uploads;
    private DatabaseReference databaseReference;
    private   String  image_storage_path = "image_uploads/";
    private   String  link ="";
    private  String  images_database_storage = "image_uploads_data";
    private ProgressBar progressCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        recyclerView = findViewById(R.id.show_images_recycler_view);
        progressCircle = findViewById(R.id.images_activity_progress_bar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        uploads =  new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference(images_database_storage);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //foreach  loop   for iterating through the snapshot and getting each entry as a post snapshot
                    for(DataSnapshot postSnapshot : snapshot.getChildren()){
                        Upload  current_upload  =postSnapshot.getValue(Upload.class);
                        uploads.add(current_upload);
                    }
                    imagesAdapter = new ImagesAdapter(ImagesActivity.this , uploads);
                    recyclerView.setAdapter(imagesAdapter);
                    progressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ImagesActivity.this ,error.getMessage() , Toast.LENGTH_SHORT ).show();
                    progressCircle.setVisibility(View.INVISIBLE);
            }

        });

    }
}