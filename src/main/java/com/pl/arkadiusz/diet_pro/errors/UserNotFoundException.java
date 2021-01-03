package com.pl.arkadiusz.diet_pro.errors;



public class UserNotFoundException extends RuntimeException  {
    public UserNotFoundException(String message) {
        super("No found user with " + message);
    }


    public UserNotFoundException() {
        super("No found user");
    }


}
