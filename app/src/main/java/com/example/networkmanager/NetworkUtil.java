package com.example.networkmanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
    public static  boolean  INTERNET_CONNECTED = false;

//    set   a   functionality that allows bluetooth connection and  hotspot connection
//    for  sharing files  just  like  in file_commander transfer
    public  static String getConnectivityStatus(Context context){
        String status = null;
        ConnectivityManager cm =  (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork !=null){
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                status = "Wifi is connected   successfully";
                INTERNET_CONNECTED = true;

            }else if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                status = "Mobile data  is connected successfully";
                INTERNET_CONNECTED = true;
            }else if(activeNetwork.getType() == ConnectivityManager.TYPE_BLUETOOTH){
            status = "Bluetooth  is connected successfully";
        }
            else{
                INTERNET_CONNECTED = false;
                status = "No internet  connection is available";
            }
        }

        return  status;
    }
}
