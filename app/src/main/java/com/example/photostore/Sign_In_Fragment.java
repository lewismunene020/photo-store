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
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Sign_In_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Sign_In_Fragment extends Fragment {
    private static List<String> users_found = new ArrayList<>();
    private MyReceiver myReceiver;
    private Button login_btn;
    private EditText email_edit_text;
    private EditText password_edit_text;
    private EditTextValidator validator;
    private String users_storage_path = "photo_store_android_users";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout progress_circle_layout;

    public Sign_In_Fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Sign_In_Fragment newInstance(String param1, String param2) {
        Sign_In_Fragment fragment = new Sign_In_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_sign__in_, container, false);
        myReceiver = new MyReceiver();
        login_btn = view.findViewById(R.id.sign_in_btn);
        email_edit_text = view.findViewById(R.id.email_edit_text);
        password_edit_text = view.findViewById(R.id.password_edit_text);
        validator = new EditTextValidator();

        progress_circle_layout = view.findViewById(R.id.signin_progress_circle_layout);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO CHECKING USER FROM THE DATABASE
                loginUser();
            }
        });
        return view;
    }

    private void loginUser() {
        final String email = email_edit_text.getText().toString().trim();
        final String password = password_edit_text.getText().toString().trim();
        if (email_is_valid(email)) {
            if (internet_is_connected()) {
                progress_circle_layout.setVisibility(View.VISIBLE);
                Task<DataSnapshot> dataSnapshotTask = FirebaseDatabase.getInstance().getReference(users_storage_path).get();
                dataSnapshotTask.onSuccessTask(new SuccessContinuation<DataSnapshot, Object>() {
                    @NonNull
                    @Override
                    public Task<Object> then(DataSnapshot dataSnapshot) throws Exception {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            UserUpload currentUserUpload = userSnapshot.getValue(UserUpload.class); // getting the  user  details  in the current  snapshot
                            if (currentUserUpload.getEmail().equals(email)) {
                                users_found.add(currentUserUpload.getEmail());
                                users_found.add(currentUserUpload.getPassword());
                                break;
                            }
                        }
                        if (users_found.contains(email)) {
                            if (users_found.contains(password)) {
                                redirectToHomePage();
                                Toast.makeText(getActivity(), "Login successful ", Toast.LENGTH_SHORT).show();
                            } else {
                                //TODO :INSERT DATA TO  PHONE'S SQLITE DATABASE AND IF THEIR  IS  A  PROFILE IMAGE  DOWNLOAD IT TO THE PHONES STORAGE AND STORE ITS PATH INT  THE SQLITEDB
                                //TODO :REDIRECT TO THE  HOME PAGE
                                Toast.makeText(getActivity(), "invalid email  or  password", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            //TODO :SHOW AN ALERT TO SHOW USER DOES  NOT EXISTS
                            Toast.makeText(getActivity(), "invalid email or password", Toast.LENGTH_SHORT).show();
                        }
                        progress_circle_layout.setVisibility(View.GONE);
                        return null;
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //TODO :SHOW "SOMETHING WENT WRONG  ACTIVITY"
//                Toast.makeText(getActivity() , e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                progress_circle_layout.setVisibility(View.VISIBLE);
            }
        }
    }


    private void redirectToHomePage() {

        Intent i = new Intent(getActivity(), HomeActivity.class);
        startActivity(i);
    }

    //INTERNET  CONNECTION CHECKER
    private boolean internet_is_connected() {
        myReceiver.onReceive(getActivity(), new Intent());
        return myReceiver.CONNECTED;
    }

    private boolean email_is_valid(String email) {
        boolean correct = false;
        if (!email.equals("")) {
            if (validator.validate_email(email)) {
                correct = true;
//                Toast.makeText(getActivity() , "Correct  !!!!" ,Toast.LENGTH_LONG ).show();
            } else {
                Toast.makeText(getActivity(), "Invalid  email address", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "Email  cannot be  blank", Toast.LENGTH_LONG).show();
        }
        return correct;
    }
}