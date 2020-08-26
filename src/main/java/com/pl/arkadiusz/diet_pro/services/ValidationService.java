package com.pl.arkadiusz.diet_pro.services;

public interface ValidationService {

    boolean isUniqueEmail(String email);

    boolean isUniqueUsername(String username);

}
