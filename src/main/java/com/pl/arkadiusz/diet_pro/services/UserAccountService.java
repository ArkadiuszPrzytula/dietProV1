package com.pl.arkadiusz.diet_pro.services;

import com.pl.arkadiusz.diet_pro.dto.userDto.PasswordResetRequest;
import com.pl.arkadiusz.diet_pro.dto.userDto.TokenDTO;
import com.pl.arkadiusz.diet_pro.errors.InvalidTokenException;
import com.pl.arkadiusz.diet_pro.errors.TokenExpiredException;

public interface UserAccountService {
    TokenDTO getToken(String token) throws InvalidTokenException;

    String createTokenUsername(String user);

    boolean checkTokenExpireTime(TokenDTO verificationToken) throws TokenExpiredException;

    Long verifyUser(Long userId);

    String createTokenEmail(String email);

    Long changeUserPassword(Long userId, PasswordResetRequest passwordResetRequest);

    void clearToken(TokenDTO token) throws InvalidTokenException;

}
