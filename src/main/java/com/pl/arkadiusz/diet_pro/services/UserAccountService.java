package com.pl.arkadiusz.diet_pro.services;

import com.pl.arkadiusz.diet_pro.dto.userDto.PasswordResetRequest;
import com.pl.arkadiusz.diet_pro.dto.userDto.TokenDTO;
import com.pl.arkadiusz.diet_pro.errors.InvalidTokenException;
import com.pl.arkadiusz.diet_pro.errors.TokenExpiredException;

public interface UserAccountService {
    TokenDTO getRestartToken(String token) throws InvalidTokenException;

    String createVerificationToken(String user);

    TokenDTO getVerificationToken(String token) throws InvalidTokenException;

    TokenDTO getVerificationToken(Long userId) throws InvalidTokenException;

    boolean checkTokenExpireTime(TokenDTO verificationToken) throws TokenExpiredException;

    Long verifyUser(Long userId);

    String createPasswordRestartToken(String email);

    Long editUser(Long userId, PasswordResetRequest passwordResetRequest);

    void clearToken(TokenDTO token);

}
