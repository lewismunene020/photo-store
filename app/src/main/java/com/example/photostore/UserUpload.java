package com.example.photostore;

public class UserUpload {
    

        private  String   email;
        private  String  password;

        public   UserUpload(){
//        empty   constructor
        }
        public    UserUpload(String   email_add   , String   pass){
            if(email_add.trim().equals("")){
                email_add  =  "No  email_add";
            }
            email =  email_add;
            password =  pass;
        }

//    getters   and setters

        public   String   getEmail(){
            return  email;
        }
        public   void setEmail(String n){
            email = n;
        }

        public  String   getPassword(){
            return  password;
        }
        public   void  setPassword(String url){
            password = url;
        }


}


