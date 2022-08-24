package com.example.photostore;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;


public class photoEditorFragment extends Fragment {

    private static final  int  PICK_IMAGE_REQUEST = 1;
    private ImageView chooseImage;
    private ImageView showUploads;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public photoEditorFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static photoEditorFragment newInstance(String param1, String param2) {
        photoEditorFragment fragment = new photoEditorFragment();
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
       View view = inflater.inflate(R.layout.fragment_photo_editor, container, false);

       chooseImage = view.findViewById(R.id.image_picker);
       showUploads = view.findViewById(R.id.show_uploads);

       chooseImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               openFileChooser();
           }
       });
        showUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUploadsActivity();
            }
        });
        return view;
    }

    private void  openFileChooser(){
        Intent intent  =  new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent  ,  PICK_IMAGE_REQUEST); //  Deprecated  method should  be  handled  later
    }


    public   void showUploadsActivity(){
        Intent i   =    new  Intent(getActivity() , ImagesActivity.class);
        startActivity(i);

    }
}