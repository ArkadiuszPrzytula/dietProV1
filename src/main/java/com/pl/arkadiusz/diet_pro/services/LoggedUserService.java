package com.pl.arkadiusz.diet_pro.services;


import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface LoggedUserService {

     String getLoggedUserName();

    Collection<? extends GrantedAuthority> getLoggedUserGrantedAuthority();

    boolean checkLoggedUserAuthorities(String privilege);
}