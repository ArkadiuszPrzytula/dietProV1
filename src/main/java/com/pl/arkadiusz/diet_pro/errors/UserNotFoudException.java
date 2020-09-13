package com.pl.arkadiusz.diet_pro.errors;



public class UserNotFoudException extends RuntimeException  {
    public UserNotFoudException(String message) {
        super("No found user with " + message);
    }


    public UserNotFoudException() {
        super("No found user");
    }


}
