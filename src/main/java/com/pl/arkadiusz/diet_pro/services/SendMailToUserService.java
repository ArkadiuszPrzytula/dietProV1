package com.pl.arkadiusz.diet_pro.services;

import com.pl.arkadiusz.diet_pro.dto.UserPlainDto;

public interface SendMailToUserService {
    void sendVerificationTokenToUser(String appUrl, UserPlainDto user, String verificationToken);
}
