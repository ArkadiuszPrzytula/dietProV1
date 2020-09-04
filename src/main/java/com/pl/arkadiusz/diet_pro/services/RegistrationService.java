package com.pl.arkadiusz.diet_pro.services;

import com.pl.arkadiusz.diet_pro.dto.UserPlainDto;
import com.pl.arkadiusz.diet_pro.dto.UserRegisterDTO;

public interface RegistrationService {
    UserPlainDto register(UserRegisterDTO userRegisterDTO);
}
