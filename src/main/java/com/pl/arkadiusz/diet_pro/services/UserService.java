package com.pl.arkadiusz.diet_pro.services;

import com.pl.arkadiusz.diet_pro.dto.UserPlainDto;
import com.pl.arkadiusz.diet_pro.dto.VerificationTokenDTO;
import com.pl.arkadiusz.diet_pro.errors.InvalidTokenException;
import com.pl.arkadiusz.diet_pro.errors.TokenExpiredException;


import java.util.List;

public interface UserService {
    List<UserPlainDto> getAllUser() throws NoSuchFieldException;

    VerificationTokenDTO createVerificationToken(UserPlainDto user, String token);

    VerificationTokenDTO getVerificationToken(String token) throws InvalidTokenException;

    boolean checkTokenExpireTime(VerificationTokenDTO verificationToken) throws TokenExpiredException;

    Long verifyUser(Long userId);
}
