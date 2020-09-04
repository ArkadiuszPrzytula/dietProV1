package com.pl.arkadiusz.diet_pro.errors;

public class TokenExpiredException extends Exception {
    public TokenExpiredException() {
        super("Sorry! Your token has expired");
    }
}
