package com.example.photostore;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class UserFirebaseAccess {
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference() ;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageTask storageTask;
    protected boolean successful_insert = false;

    public UserFirebaseAccess(){
//        empty   constructor
    }


    public StorageTask uploadFile(String   file_name , Uri mImageUri){
        StorageReference  fileReference  = storageReference.child(file_name);

        return  storageTask =  fileReference.putFile(mImageUri);
    }

    public  String  getDownloadUrl(String  file_path){
        return  "";
    }

    public   boolean insertFileData(String folder_name, Upload upload) {


        String  uploadId = databaseReference.push().getKey();
        databaseReference.child(folder_name).child(uploadId).setValue(upload).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                successful_insert = true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                successful_insert = false;

            }
        });

        return  successful_insert;
//        remember to   set   an  oncompletion  listener   that changes  successful insert to true

    }

    private void insertSuccesful(boolean b) {
        successful_insert = b;
    }

    //    inserts  userdata   to  the  database  during  signup
    public   boolean insertUserData(String folder_name, UserUpload upload) {

//        final boolean[] successful_insert = new boolean[1];
        String  uploadId = databaseReference.push().getKey();
         databaseReference.child(folder_name).child(uploadId).setValue(upload, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                successful_insert = true;
            }
        });
        return successful_insert;
    }

}
