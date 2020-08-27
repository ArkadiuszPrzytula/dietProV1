package com.pl.arkadiusz.diet_pro.services;

import com.pl.arkadiusz.diet_pro.dto.UserRegisterDTO;

public interface RegistrationService {
    Long register(UserRegisterDTO userRegisterDTO);
}
