package com.pl.arkadiusz.diet_pro.Listeners;

import com.pl.arkadiusz.diet_pro.dto.userDto.UserPlainDto;

import com.pl.arkadiusz.diet_pro.model.entities.enums.TokenType;
import com.pl.arkadiusz.diet_pro.services.SendMailToUserService;
import com.pl.arkadiusz.diet_pro.services.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ApplicationListener;

import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;

@Slf4j
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    @Autowired
    private UserAccountService userAccountService;


    @Autowired
    private SendMailToUserService sendMailToUserService;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent onRegistrationCompleteEvent) {
        this.confirmRegistration(onRegistrationCompleteEvent);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent onRegistrationCompleteEvent) {
        UserPlainDto user = onRegistrationCompleteEvent.getUser();

        String verificationToken = userAccountService.createTokenUsername(user.getUsername());

        String appUrl = onRegistrationCompleteEvent.getAppUrl();

        try {
            sendMailToUserService.sendTokenToUserFactory(appUrl, user, verificationToken, TokenType.REGISTRATION_VERIFY);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }


    }


}
