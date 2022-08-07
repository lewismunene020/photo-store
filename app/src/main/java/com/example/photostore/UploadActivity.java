package com.example.photostore;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class UploadActivity extends AppCompatActivity {
    private static final  int  PICK_IMAGE_REQUEST = 1;
    private Button mButtonChooseImage ;
    private  Button mButtonUploadImage;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFilename;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    private Uri mImageUri;

    private StorageReference storageReference ;
    private DatabaseReference databaseReference;

    private StorageTask storageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


        mButtonChooseImage =  findViewById(R.id.button_choose_image);
        mButtonUploadImage =  findViewById(R.id.button_upload);
        mTextViewShowUploads =  findViewById(R.id.text_view_show_uploads);
        mImageView  =  findViewById(R.id.image_view);
        mProgressBar =  findViewById(R.id.progress_bar);
        mEditTextFilename =  findViewById(R.id.edit_text_file_name);

//        firebase   variable  initialization
        storageReference = FirebaseStorage.getInstance().getReference("imageuploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("imageuploadsdata");


        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        mButtonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(storageTask != null  && storageTask.isInProgress() ){
                    Toast.makeText(UploadActivity.this , "File  currently  uploading please wait ..." , Toast.LENGTH_LONG).show();
                }else{
                    uploadFile();
                }
            }
        });

        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUploadsActivity(view);
            }
        });
    }


    private void  openFileChooser(){
        Intent intent  =  new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent  ,  PICK_IMAGE_REQUEST); //  Deprecated  method should  be  handled  later
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==  PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            mImageUri   =  data.getData();
//            try  and use  picasso  dependency
            mImageView.setImageURI(mImageUri);
        }
    }

    //    extension extractor
    private  String  getFileExtension(Uri  uri){
        ContentResolver cr  =   getContentResolver();
        MimeTypeMap mime  = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(cr.getType(uri));
    }
    private   void   uploadFile(){
        if(mImageUri != null){
            String   image_name  = System.currentTimeMillis() +"."+getFileExtension(mImageUri);
            StorageReference  fileReference  = storageReference.child(image_name);

            storageTask =  fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler  = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            } , 3000);


                            Toast.makeText(UploadActivity.this , "Upload successful " , Toast.LENGTH_SHORT).show();
                            Upload  upload =  new  Upload(mEditTextFilename.getText().toString().trim() ,
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
//                            String  link = (taskSnapshot.getStorage().getMetadata()).toString();

                            String  link   = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                            Toast.makeText(UploadActivity.this ,  link, Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadActivity.this , e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            int  progress  =  (int)(100.0 * snapshot.getBytesTransferred() /snapshot.getTotalByteCount());
                            mProgressBar.setProgress(progress);
                        }
                    });

        }else{
            Toast.makeText(this , "Please  select  an  image  file  !!!" , Toast.LENGTH_SHORT).show();
        }
    }


    public   void showUploadsActivity(View v){
        Intent   i   =    new  Intent(this , ShowUploads.class);
        startActivity(i);

    }
}