package com.example.databasehandler;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SqlLiteDB extends SQLiteOpenHelper {
    public static final String USERS_TABLE = "USER_TABLE";
    public static final String COLUMN_USER_EMAIL = "USER_EMAIL";
    private static final String COLUMN_USER_PASS = "USER_PASS";
   
    public static final String COLUMN_ID = "ID";

    public SqlLiteDB(@Nullable Context context) {
        super(context, "customer.db", null, 1);
    }

    //    creates the  table  if  its not  created
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE  TABLE  " + USERS_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USER_EMAIL + " TEXT , " + COLUMN_USER_PASS + " TEXT)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public  boolean addOne(UserModel userModel){
        SQLiteDatabase db  = this.getWritableDatabase();
        ContentValues cv   =  new ContentValues();

        cv.put(COLUMN_USER_EMAIL , userModel.getName());
        cv.put(COLUMN_USER_PASS , userModel.getPass());

        long insert = db.insert(USERS_TABLE, null, cv);
        return  insert == -1 ? false : true  ;
//        if(insert == -1){
//            return  false;
//        }else{
//            return  true;
//        }
    }

    public List<UserModel> getEveryone(){
        List<UserModel> returnList  =  new ArrayList<>();
        String  queryString  = "SELECT *  FROM  "+USERS_TABLE;

        SQLiteDatabase db  =  this.getReadableDatabase();
//        raw  query enables  us to  do prepared statements

        Cursor cursor = db.rawQuery(queryString , null , null);

        if(cursor.moveToFirst()){
//            loop through  the   result  set  ,  create  a  customer  model  and  the  append  to the customer model   list
            do{
                int  userId  =  cursor.getInt(0);
                String  userEmail  = cursor.getString(1);
                String   userPass =  cursor.getString(2);

                UserModel userModel =  new UserModel(userId , userEmail ,userPass);
                returnList.add(userModel);

            }while(cursor.moveToNext());
        }else{
//                failure   .... do  not  add   anything  to  the  list   return  an  empty list  
        }
        cursor.close();
        db.close();
        return  returnList;
    }


    //    deleting functionality
    public  boolean deleteOne(UserModel userModel){
        SQLiteDatabase db  =  this.getWritableDatabase();
        String queryString  = "DELETE FROM "+USERS_TABLE+" WHERE "+COLUMN_ID +" = "+userModel.getId();
        Cursor cursor = db.rawQuery(queryString, null, null);

//        set a   functionality for setting the  table  autoincrement  to  normal incase  of  delete  of   an  entity
        if(cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }
    }
}
