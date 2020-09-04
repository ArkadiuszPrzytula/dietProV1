package com.pl.arkadiusz.diet_pro.errors;

public class InvalidTokenException extends Exception {
    public InvalidTokenException() {
        super("invalid token");
    }
}
