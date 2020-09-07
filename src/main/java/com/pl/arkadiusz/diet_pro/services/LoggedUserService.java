package com.pl.arkadiusz.diet_pro.services;


import com.pl.arkadiusz.diet_pro.model.entities.Role;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

public interface LoggedUserService {

     String getLoggedUserName();

    Collection<? extends GrantedAuthority> getLoggedUserGrantedAuthority();

    boolean checkLoggedUserAuthorities(String privilege);

    Set<Role> getRoles();
}