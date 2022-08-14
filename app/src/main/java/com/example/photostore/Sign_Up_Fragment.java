package com.example.photostore;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private Button sign_up_btn;
    private Button google_sign_in_btn;
    private  EditTextValidator validator =  new EditTextValidator();
    private  Button upload_redirect;
    private  UserUpload userUpload;
    private UserFirebaseAccess  userFirebaseAccess;
    private String  users_storage_path = "photo_store_android_users";
    private DatabaseReference databaseReference;
    private MyReceiver myReceiver = null;
    public  static   boolean  EMAIL_EXISTS = false;
    private List<UserUpload> storedUserUpload;



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
        View  view  = inflater.inflate(R.layout.fragment_sign__up_, container, false);
        email_edit_text = view.findViewById(R.id.email_edit_text);
        password_edit_text = view.findViewById(R.id.password_edit_text);
        confirm_password_edit_text = view.findViewById(R.id.confirm_password_edit_text);
        sign_up_btn = view.findViewById(R.id.sign_up_btn);
        google_sign_in_btn = view.findViewById(R.id.google_sign_in_btn);
        upload_redirect = view.findViewById(R.id.upload_image_redirect);
        userFirebaseAccess = new UserFirebaseAccess();
        myReceiver =  new MyReceiver();

//        initializing  stored  users
        storedUserUpload  = new ArrayList<>();

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
                signUpUser();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void signUpUser() {
        String  email  =  email_edit_text.getText().toString().trim();
        String  password = password_edit_text.getText().toString().trim();
        String  confirm_password =   confirm_password_edit_text.getText().toString().trim();

        if(email_is_valid(email)){
            if(validate_password(password , confirm_password)){
//                lets   check  if   the  email exists
                if(email_exists(email)){
                    Toast.makeText(getActivity() ,"email  already   exists   please  try   another  email" ,Toast.LENGTH_LONG).show();
                }
                else if(internet_is_connected()){
                    String  uploadId = databaseReference.push().getKey();
                    userUpload =  new UserUpload(email , password ,uploadId );
//                checking the internet connection is active

                    databaseReference.child(users_storage_path).child(uploadId).setValue(userUpload).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity() , "Sign up  successful" ,Toast.LENGTH_LONG ).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity() , e.getMessage() ,Toast.LENGTH_LONG ).show();

                        }
                    });
                    /* IF THE  INTERNET  IS  NOT CONNECTED   THEN   DISPLAY  ERROR  VIEW  AND  TOAST FOR  NO INTERNET  */
                }else{
                    Toast.makeText(getActivity() , "NO  INTERNET  CONNECTION !!!" ,Toast.LENGTH_LONG ).show();
                }

            }
        }
    }

    private boolean internet_is_connected() {
        Intent i;
        myReceiver.onReceive(getActivity() ,  i = new  Intent());
        return myReceiver.CONNECTED;
    }

    private boolean email_exists(String email) {
        boolean user_exists = false;
        DatabaseReference db  = FirebaseDatabase.getInstance().getReference(users_storage_path);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot userSnapshot  : snapshot.getChildren()){
                    UserUpload currentUserUpload = userSnapshot.getValue(UserUpload.class); // getting the  user  details  in the current  snapshot
                    storedUserUpload.add(currentUserUpload);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                storedUserUpload = null;
            }
        });

        //LETS ITERATE THROUGH THE USERS AND FIND A USER WITH THE SUBMITTED EMAIL
        for(UserUpload upload : storedUserUpload){
            if( upload.getEmail().equals(email)){
//                Toast.makeText(getActivity() , upload.getEmail() , Toast.LENGTH_LONG).show();
                user_exists =  true;
                break;
            }
        }
            return user_exists;
    }

    private boolean email_is_valid(String email) {
        boolean correct  =  false;
        if(email != ""){
            if(validator.validate_email(email.trim())){
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