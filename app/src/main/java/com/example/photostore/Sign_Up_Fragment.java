package com.example.photostore;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Sign_Up_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Sign_Up_Fragment extends Fragment {

    //  Sign up elements   are  found   here
    private EditText email_edit_text;
    private EditText password_edit_text;
    private EditText confirm_password_edit_text;
    private final EditTextValidator validator = new EditTextValidator();
    private UserUpload userUpload;
    private UserFirebaseAccess userFirebaseAccess;
    private String users_storage_path = "photo_store_android_users";
    private DatabaseReference databaseReference;
    private MyReceiver myReceiver = null;
    public static boolean EMAIL_EXISTS = false;
    private List<UserUpload> storedUserUpload;
    private static String user_exists;

    private LinearLayout progress_circle_layout;
    private ConstraintLayout signupConstraintLayout;

    private static List<String> users_found = new ArrayList<>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Sign_Up_Fragment() {
        // Required empty public constructor
    }


    public static Sign_Up_Fragment newInstance(String param1, String param2) {
        Sign_Up_Fragment fragment = new Sign_Up_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign__up_, container, false);
        email_edit_text = view.findViewById(R.id.email_edit_text);
        password_edit_text = view.findViewById(R.id.password_edit_text);
        confirm_password_edit_text = view.findViewById(R.id.confirm_password_edit_text);

        progress_circle_layout = view.findViewById(R.id.signup_progress_circle_layout);
        signupConstraintLayout = view.findViewById(R.id.signup_constraint_layout);

        Button sign_up_btn = view.findViewById(R.id.sign_up_btn);
        Button google_sign_in_btn = view.findViewById(R.id.google_sign_in_btn);

        Button upload_redirect = view.findViewById(R.id.upload_image_redirect);
        userFirebaseAccess = new UserFirebaseAccess();
        myReceiver = new MyReceiver();


//        initializing  stored  users
        storedUserUpload = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        upload_redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  =  new Intent(getActivity() , UploadActivity.class);
                startActivity(i);
            }
        });
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpNewUser();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void insertUserToDB() {
        String  email  =  email_edit_text.getText().toString().trim();
        String  password = password_edit_text.getText().toString().trim();
        String  confirm_password =   confirm_password_edit_text.getText().toString().trim();

        if(email_is_valid(email)){
            if(validate_password(password , confirm_password)){
                    if(internet_is_connected()) {
                        //TODO :SHOWING THE LOADER
                        progress_circle_layout.setVisibility(View.VISIBLE);

                        String uploadId = databaseReference.push().getKey();
                        userUpload = new UserUpload(email, password, uploadId);

                        //                    checking the internet connection is active

                        databaseReference.child(users_storage_path).child(uploadId).setValue(userUpload).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                redirectToHomePage();
                                Toast.makeText(getActivity(), "Sign up  successful", Toast.LENGTH_LONG).show();
                                //TODO  REDIRECTING TO HOME PAGE

                                //TODO :HIDING THE  LOADER

                                progress_circle_layout.setVisibility(View.GONE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        /* IF THE  INTERNET  IS  NOT CONNECTED   THEN   DISPLAY  ERROR  VIEW  AND  TOAST FOR  NO INTERNET  */


                    } else {
//                        Toast.makeText(getActivity() , "NO  INTERNET  CONNECTION !!!" ,Toast.LENGTH_LONG ).show();
                    }
            }
        }
    }

    private void redirectToHomePage() {

        Intent i = new Intent(getActivity(), HomeActivity.class);
        startActivity(i);
    }

    private boolean internet_is_connected() {
        myReceiver.onReceive(getActivity(), new Intent());
        return myReceiver.CONNECTED;
    }

    private void signUpNewUser() {
        final String email = email_edit_text.getText().toString().trim();
        final String password = password_edit_text.getText().toString().trim();
        final String confirm_password = confirm_password_edit_text.getText().toString().trim();

        if(internet_is_connected()) {
            Task<DataSnapshot> dataSnapshotTask  = FirebaseDatabase.getInstance().getReference(users_storage_path).get();
            dataSnapshotTask.onSuccessTask(new SuccessContinuation<DataSnapshot, Object>() {
                @NonNull
                @Override
                public Task<Object> then(DataSnapshot dataSnapshot) throws Exception {
                    for(DataSnapshot userSnapshot  : dataSnapshot.getChildren()){
                        UserUpload currentUserUpload = userSnapshot.getValue(UserUpload.class); // getting the  user  details  in the current  snapshot
                        if(currentUserUpload.getEmail().equals(email)){
                            users_found.add(currentUserUpload.getEmail());
                            user_exists = "true";
                            break;
                        }else{

                        }

                    }
                    if (users_found.contains(email)) {
                        //TODO :SHOW AN ALERT TO SHOW USER EXISTS
                        Toast.makeText(getActivity(), "user exists in arraylist ", Toast.LENGTH_LONG).show();
                        user_exists = "true";
                        progress_circle_layout.setVisibility(View.GONE);

                    } else {
                        //TODO :INSERT DATA TO  PHONE'S SQLITE DATABASE AND IF THEIR  IS  A  PROFILE IMAGE  DOWNLOAD IT TO THE PHONES STORAGE AND STORE ITS PATH INT  THE SQLITEDB
                        //TODO :REDIRECT TO THE  HOME PAGE
                        insertUserToDB();
                        user_exists = "false";
                    }
                    return  null;
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //TODO :SHOW "SOMETHING WENT WRONG  ACTIVITY"
//                Toast.makeText(getActivity() , e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else{
            // TODO :IF NO INTERNET   LET'S REDIRECT TO  THE NETWORK ERROR  PAGE OR SHOW   AN ALERT TO SHOW THAT THERE IS  NO NETWORK
        }

    }

    private boolean email_is_valid(String email) {
        boolean correct  =  false;
        if(!email .equals("")){
            if(validator.validate_email(email)){
                correct =  true;
//                Toast.makeText(getActivity() , "Correct  !!!!" ,Toast.LENGTH_LONG ).show();
            }else{
                Toast.makeText(getActivity() , "Invalid  email address" ,Toast.LENGTH_LONG ).show();
            }
        }else{
            Toast.makeText(getActivity() , "Email  cannot be  blank" ,Toast.LENGTH_LONG ).show();
        }
        return  correct;
    }

    private boolean validate_password(String password, String confirm_password) {
        boolean correct  =  false;
        if(password.length() < 8 || confirm_password.length() < 8){
            Toast.makeText(getActivity() , "Password  should  have   a  minimum  of   8 characters !!!" , Toast.LENGTH_LONG).show();
        }else{
            if(!password.equals(confirm_password)){
                Toast.makeText(getActivity() , "Passwords   do  not  match " , Toast.LENGTH_LONG).show();
            }else{
                correct =   true;
            }
        }

        return correct;
    }


}