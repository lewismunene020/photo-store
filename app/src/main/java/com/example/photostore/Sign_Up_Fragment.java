package com.example.photostore;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
    private String  user_storage_path = "users";



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
        email_edit_text = (EditText) view.findViewById(R.id.email_edit_text);
        password_edit_text = (EditText) view.findViewById(R.id.password_edit_text);
        confirm_password_edit_text = (EditText) view.findViewById(R.id.confirm_password_edit_text);
        sign_up_btn = (Button) view.findViewById(R.id.sign_up_btn);
        google_sign_in_btn = (Button) view.findViewById(R.id.google_sign_in_btn);
        upload_redirect = (Button) view.findViewById(R.id.upload_image_redirect) ;

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
        String  email  =  email_edit_text.getText().toString();
        String  password = password_edit_text.getText().toString();
        String  confirm_password =   confirm_password_edit_text.getText().toString();

        if(email_is_valid(email)){
            if(validate_password(password , confirm_password)){
                userUpload =  new UserUpload(email , password);
                userFirebaseAccess = new UserFirebaseAccess();
                boolean   my_bool = userFirebaseAccess.insertUserData("users" , userUpload);
                if(my_bool){
                    Toast.makeText(getActivity() , "Sign up  successful" ,Toast.LENGTH_LONG ).show();
                }else{
                    Toast.makeText(getActivity() , my_bool+"\nSomething went  wrong !!" ,Toast.LENGTH_LONG ).show();

                }
            }
        }
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