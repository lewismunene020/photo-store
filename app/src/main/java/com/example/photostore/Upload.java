package com.example.photostore;

public class Upload {
    private  String   image_name;
    private  String  image_url;

    public   Upload(){
//        empty   constructor
    }
    public    Upload(String   name   , String   imageUrl){
        if(name.trim().equals("")){
            name  =  "No  Name";
        }
        image_name =  name;
        image_url =  imageUrl;
    }

//    getters   and setters

    public   String   getImage_name(){
        return  image_name;
    }
    public   void setImage_name(String n){
        image_name = n;
    }

    public  String   getImage_url(){
        return  image_url;
    }
    public   void  setImage_url(String url){
        image_url = url;
    }


}
