package com.pl.arkadiusz.diet_pro.services;

import com.pl.arkadiusz.diet_pro.model.entities.Privilege;

import java.util.Set;

public interface UserPrivilegesVerificationService {
    Set<Privilege> getAllUserPrivileges();

    boolean verify(String privilege);
}
