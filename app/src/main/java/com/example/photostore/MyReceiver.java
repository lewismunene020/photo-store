package com.example.photostore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.networkmanager.NetworkUtil;

public class MyReceiver extends BroadcastReceiver{
    public   static  boolean CONNECTED  =  false;
    @Override
    public void onReceive(Context context, @Nullable Intent intent) {
        String status  = NetworkUtil.getConnectivityStatus(context);
        if(status == null){
            status = "No internet  connection";


        }
        Toast.makeText(context , status , Toast.LENGTH_LONG).show();
        CONNECTED = NetworkUtil.INTERNET_CONNECTED;
    }

    public   boolean  connected(){
        return  CONNECTED;
    }
}
