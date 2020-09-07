package com.pl.arkadiusz.diet_pro.services;

import com.pl.arkadiusz.diet_pro.dto.UserPlainDto;
import com.pl.arkadiusz.diet_pro.dto.VerificationTokenDTO;
import com.pl.arkadiusz.diet_pro.errors.InvalidTokenException;
import com.pl.arkadiusz.diet_pro.errors.TokenExpiredException;


import java.util.List;

public interface UserService {
    List<UserPlainDto> getAllUser() throws NoSuchFieldException;

    UserPlainDto getUserPlainDto(Long id);

    String createVerificationToken(UserPlainDto user);

    VerificationTokenDTO getVerificationToken(String token) throws InvalidTokenException;

    VerificationTokenDTO getVerificationToken(Long userId) throws InvalidTokenException;

    boolean checkTokenExpireTime(VerificationTokenDTO verificationToken) throws TokenExpiredException;

    Long verifyUser(Long userId);
}
