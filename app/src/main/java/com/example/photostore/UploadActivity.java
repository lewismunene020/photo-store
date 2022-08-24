package com.example.photostore;

import android.app.PendingIntent;
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

import com.example.utilities.BytesConvertor;
import com.example.utilities.NotificationCreator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class UploadActivity extends AppCompatActivity {
    public static final int BYTES_TO_KILO_BYTES = 1024;
    public static final int BYTES_TO_MEGA_BYTES = 1024 * BYTES_TO_KILO_BYTES;
    public static final int BYTES_TO_GIGA_BYTES = 1024 * BYTES_TO_MEGA_BYTES;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button mButtonChooseImage;
    private Button mButtonUploadImage;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFilename;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private final String image_storage_path = "image_uploads/";
    private final String images_database_storage = "image_uploads_data";

    private StorageTask storageTask;
    private BytesConvertor bytesConvertor = new BytesConvertor();
    private String link = "";
    private NotificationCreator notificationCreator;

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
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(images_database_storage);


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
    private  String   gettingDownloadUrl(String  name){
        return storageReference.child(name).getDownloadUrl().toString();
    }

    private   void   uploadFile(){
        if(mImageUri != null){
            String   image_name  = image_storage_path +System.currentTimeMillis() +"."+getFileExtension(mImageUri);
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
                                //TODO  : SET UPLOAD COMPLETE MESSAGE IN THE STATUS BAR
                                //TODO  : SHOW A  BOTTOM VIEW THAT UPLOAD IS COMPLETE
                                //TODO  : INTRODUCE A BUTTON THAT REDIRECTS TO SHOW  "SHOW_UPLOADS ACTIVITY" IF USER EXITS A CERTAIN ACTIVITY
                            } , 3000);


                            Toast.makeText(UploadActivity.this , "Upload successful " , Toast.LENGTH_SHORT).show();

                            storageReference.child(image_name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                @Override
                                public void onSuccess(Uri uri) {
//                                    if  the   download  url  is     found  lets  upload to   firebase
                                    link  =  uri.toString();
//                                    Toast.makeText(UploadActivity.this ,  uri.toString(), Toast.LENGTH_SHORT).show();
                                    Upload  upload =  new  Upload(mEditTextFilename.getText().toString().trim() ,link);
                                    insertIntoDB(link  , upload);



                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    link = "null";

                                }
                            });


                            Toast.makeText(UploadActivity.this ,  image_name, Toast.LENGTH_SHORT).show();





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
                            int progress = (int) (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            String progressString = bytesConvertor.convertToProgressString(snapshot.getBytesTransferred(), snapshot.getTotalByteCount());
                            //TODO :SHOW THE RATE OF UPLOAD AND THE SIZE OF UPLOAD MADE AT A PARTICULAR INSTANCE
                            mProgressBar.setProgress(progress);
                            //TODO  : SET THE PROGRESSBAR TO APPEAR IN STATUS BAR
                            //TODO  : SHOW A BOTTOM VIEW THAT SAYS "UPLOADING...." AND DISAPPEARS AFTER A WHILE

                            String notificationString = "Photo store Upload";
                            int PHOTO_STORE_CHANNEL_ID = 20002;

                            Intent intent = new Intent(UploadActivity.this, ShowUploads.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pendingIntent = PendingIntent.getActivity(UploadActivity.this,
                                    0, intent, PendingIntent.FLAG_IMMUTABLE);

                            notificationCreator = new NotificationCreator(UploadActivity.this, "photo store upload", 10000);
                            notificationCreator.createUploadNotification(notificationString, progress, progressString);

                            if (progress == 100) {
                                notificationCreator.createAlertNotification(notificationString, "upload complete !! click to  view ", pendingIntent);
                            }

                            /* IF  BUILD VERSION IS GREATER THAN  OREO */
//                            String uploadingNotificationId ="Photo store upload";
//                            int CHANNEL_ID = 10000;
//                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//                                NotificationChannel notificationChannel = new NotificationChannel( uploadingNotificationId,
//                                        "Photo store upload" , IMPORTANCE_DEFAULT);
//                                NotificationManager manager = getSystemService(NotificationManager.class);
//                                manager.createNotificationChannel(notificationChannel);
//                            }
//                            NotificationCompat.Builder builder = new NotificationCompat.Builder(UploadActivity.this,
//                                    uploadingNotificationId).
//                                    setSmallIcon(R.mipmap.ic_launcher).
//                                    setContentTitle("Photo store upload").
//                                    setContentText(progressString).
//                                    setProgress(100 , (Integer)progress , false).
//                                    setPriority(NotificationCompat.PRIORITY_DEFAULT).
////                                    setContentIntent(pendingIntent).
//                                    setAutoCancel(true);
//                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(UploadActivity.this);
//                            notificationManagerCompat.notify(CHANNEL_ID, builder.build());

                            //If progress  is 100  cancel the notification  and set  the upload complete  notification
//                            if(progress == 100){
//                                notificationManagerCompat.cancel(CHANNEL_ID);
//                                NotificationCompat.Builder builder2 = new NotificationCompat.Builder(UploadActivity.this,
//                                        uploadingNotificationId).
//                                        setSmallIcon(R.mipmap.ic_launcher).
//                                        setContentTitle("Photo store upload").
//                                        setContentText("upload complete").
//                                        setPriority(NotificationCompat.PRIORITY_DEFAULT).
//                                    setContentIntent(pendingIntent).
//                                  setAutoCancel(true);
//                                NotificationManagerCompat notificationManagerCompat2 = NotificationManagerCompat.from(UploadActivity.this);
//                                notificationManagerCompat2.notify(CHANNEL_ID, builder2.build());
//
//                            }


                        }
                    });

        }else{
            Toast.makeText(this , "Please  select  an  image  file  !!!" , Toast.LENGTH_SHORT).show();
        }
    }


    private void insertIntoDB(String link, Upload upload) {
        String  uploadId = databaseReference.push().getKey();
        databaseReference.child(uploadId).setValue(upload, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(UploadActivity.this, "database  insert  successful", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public   void showUploadsActivity(View v){
        Intent   i   =    new  Intent(this , ImagesActivity.class);
        startActivity(i);

    }
}