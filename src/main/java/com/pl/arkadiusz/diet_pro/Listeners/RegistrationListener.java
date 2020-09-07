package com.pl.arkadiusz.diet_pro.Listeners;

import com.pl.arkadiusz.diet_pro.dto.UserPlainDto;

import com.pl.arkadiusz.diet_pro.dto.VerificationTokenDTO;
import com.pl.arkadiusz.diet_pro.services.EmailService;
import com.pl.arkadiusz.diet_pro.services.SendMailToUserService;
import com.pl.arkadiusz.diet_pro.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.UUID;

@Slf4j
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService service;


    @Autowired
    private SendMailToUserService sendMailToUserService;


    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent onRegistrationCompleteEvent) {
        this.confirmRegistration(onRegistrationCompleteEvent);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent onRegistrationCompleteEvent) {
        UserPlainDto user = onRegistrationCompleteEvent.getUser();

        String verificationToken = service.createVerificationToken(user);


        String appUrl = onRegistrationCompleteEvent.getAppUrl();
        sendMailToUserService.sendVerificationTokenToUser(appUrl, user, verificationToken);

//        mailSender.send(email);

    }


}
