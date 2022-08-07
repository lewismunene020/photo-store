package com.example.photostore;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent i   =  new Intent(SplashScreenActivity.this,MainActivity.class );
//                startActivity(i);
//                finish();
//            }
//        },3000);


    }
}