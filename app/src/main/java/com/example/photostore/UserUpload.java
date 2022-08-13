package com.example.photostore;

public class UserUpload {
    

        private  String   email;
        private  String  password;
        private  String  unique_id;

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



    public UserUpload(String email_add, String pass, String uploadId) {
        if(email_add.trim().equals("")){
            email_add  =  "No  email_add";
        }
        email =  email_add;
        password =  pass;
        unique_id = uploadId;
    }

//    getters   and setters
        public String getUnique_id() {
            return unique_id;
        }
        public void setUnique_id(String unique_id) {
            this.unique_id = unique_id;
        }
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


