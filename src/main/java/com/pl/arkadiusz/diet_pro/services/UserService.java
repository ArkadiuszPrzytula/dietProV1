package com.pl.arkadiusz.diet_pro.services;

import com.pl.arkadiusz.diet_pro.dto.UserPlainDto;

import java.util.Collection;
import java.util.List;

public interface UserService {
    List<UserPlainDto> getAllUser() throws NoSuchFieldException;
}
