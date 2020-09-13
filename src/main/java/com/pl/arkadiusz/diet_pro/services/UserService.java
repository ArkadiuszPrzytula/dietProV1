package com.pl.arkadiusz.diet_pro.services;

import com.pl.arkadiusz.diet_pro.dto.userDto.PasswordResetRequest;
import com.pl.arkadiusz.diet_pro.dto.userDto.UserPlainDto;


import java.util.List;

public interface UserService {

    List<UserPlainDto> getAllUser() throws NoSuchFieldException;

    UserPlainDto getUserPlainDto(Long id);


    UserPlainDto getUserPlainDto(String email);


}
