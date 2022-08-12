package com.example.photostore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.networkmanager.NetworkUtil;

public class MyReceiver extends BroadcastReceiver{
    public   static  boolean CONNECTED  =  false;
    private   NetworkUtil networkUtil ;
    @Override
    public void onReceive(Context context, @Nullable Intent intent) {
        networkUtil = new NetworkUtil();
        String status  = networkUtil.getConnectivityStatus(context);
        if(status == null){
            status = "No internet  connection";


        }
        Toast.makeText(context , status , Toast.LENGTH_LONG).show();
        CONNECTED = networkUtil.INTERNET_CONNECTED;
    }

    public   boolean  connected(){
        return  CONNECTED;
    }
}
