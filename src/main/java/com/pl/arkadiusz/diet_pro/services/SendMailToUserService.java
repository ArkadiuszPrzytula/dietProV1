package com.pl.arkadiusz.diet_pro.services;

import com.pl.arkadiusz.diet_pro.dto.userDto.UserPlainDto;
import com.pl.arkadiusz.diet_pro.model.entities.enums.TokenType;


import javax.mail.MessagingException;
import java.io.IOException;

public interface SendMailToUserService {

    void sendTokenToUserFactory(String appUrl, UserPlainDto user, String token, TokenType tokenType) throws MessagingException, IOException;

}
