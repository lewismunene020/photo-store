package com.example.databasehandler;



public class UserModel {
    private  int  id;
    private  String  name;
    private   String pass;

    //    setting the constructor   now
    public UserModel(int  id , String name  , String pass){
        this.id = id;
        this.name =  name;
        this.pass = pass;


    }

    //    non  parameterized   constructor  for  safety  purposes
    public UserModel() {
    }

//    toString  method   for buddling  all  data  in  one string



    //    getters   and   setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


}
