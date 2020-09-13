package com.pl.arkadiusz.diet_pro.services;

import com.pl.arkadiusz.diet_pro.dto.userDto.UserPlainDto;
import com.pl.arkadiusz.diet_pro.dto.userDto.UserRegisterDTO;

public interface RegistrationService {
    UserPlainDto register(UserRegisterDTO userRegisterDTO);
}
