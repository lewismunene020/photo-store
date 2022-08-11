package com.example.photostore;

import java.util.regex.Pattern;
public class EditTextValidator {
        private static  String email_regex_pattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        public boolean validate_email(String emailAddress) {
            return Pattern.compile(email_regex_pattern)
                    .matcher(emailAddress)
                    .matches();
        }
    private String validate_password(String password, String confirm_password) {
        String correct  =  "";
        if(password.length() < 8 || confirm_password.length() < 8){
            correct = "password  requires   a  minimum  of   8 characters ";
        }else{
            if(!password.equals(confirm_password)){
                correct = "Passwords   do  not  match " ;
            }else{
                correct =   "success";
            }
        }

        return correct;
    }
}
