package com.example.photostore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity implements ImagesAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private ImagesAdapter imagesAdapter;
    private List<Upload> uploads;
    private DatabaseReference databaseReference;
    private String image_storage_path = "image_uploads/";
    private String link = "";
    private String images_database_storage = "image_uploads_data";
    private ProgressBar progressCircle;

    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        recyclerView = findViewById(R.id.show_images_recycler_view);
        progressCircle = findViewById(R.id.images_activity_progress_bar);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        uploads = new ArrayList<>();
        imagesAdapter = new ImagesAdapter(ImagesActivity.this, uploads);
        recyclerView.setAdapter(imagesAdapter);
        imagesAdapter.setOnItemClickListener(ImagesActivity.this);

        firebaseStorage = FirebaseStorage.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference(images_database_storage);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //CLEARING UPLOADS  LIST BEFORE REFRESHING
                uploads.clear();

                //foreach  loop   for iterating through the snapshot and getting each entry as a post snapshot
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload current_upload = postSnapshot.getValue(Upload.class);
                    current_upload.setUploadKey(postSnapshot.getKey());
                    uploads.add(current_upload);
                }

                imagesAdapter.notifyDataSetChanged();
                progressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ImagesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressCircle.setVisibility(View.INVISIBLE);
            }

        });

    }

    @Override
    public void OnItemClick(int position) {
        Toast.makeText(ImagesActivity.this, "Normal clicks at position " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnItemEdit(int position) {
        Toast.makeText(ImagesActivity.this, "Edit clicks at position " + position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void OnItemDelete(int position) {
        Upload selected_item = uploads.get(position);
        String uploadKey = selected_item.getUploadKey();

        //TODO : SET  A  LOADER WHILE  THE IMAGE IS BEING DELETED
        StorageReference image_reference = firebaseStorage.getReferenceFromUrl(selected_item.getImage_url());

        //SETTING UP AN ALERT DIALOG
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.sure_to_delete));
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO if the user clicks yes then delete the images
                //TODO : SHOW A CONFIRM MESSAGE TO ASK WHETHER TO DELETE OR NOT
                image_reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        databaseReference.child(uploadKey).removeValue();
                        Toast.makeText(ImagesActivity.this, "Record deleted successfully !!! ", Toast.LENGTH_SHORT).show();
                        //TODO  : SHOW AN ALERT IF DELETION IS SUCCESSFUL AND IF SOMETHING WRONG HAPPENS
                    }
                });
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


    }

    @Override
    public void OnItemShare(int position) {
        //TODO REDIRECT TO SHARE SCREEN AND SHARE THE IMAGE  WITH A MESSAGE THAT :
        //THIS WAS SHARED FROM  PHOTO_STORE
        Upload selected_item = uploads.get(position);
        String uploadKey = selected_item.getUploadKey();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Shared from Photo_Store");
        intent.putExtra(Intent.EXTRA_TEXT, selected_item.getImage_url());
        startActivity(Intent.createChooser(intent, "Share Using :"));
//        Toast.makeText(ImagesActivity.this ,"Share clicks at position "+position , Toast.LENGTH_SHORT).show();
    }
}