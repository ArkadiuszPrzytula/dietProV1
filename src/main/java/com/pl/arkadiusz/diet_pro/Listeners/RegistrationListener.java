package com.pl.arkadiusz.diet_pro.Listeners;

import com.pl.arkadiusz.diet_pro.controllers.OnRegistrationCompleteEvent;
import com.pl.arkadiusz.diet_pro.dto.UserPlainDto;

import com.pl.arkadiusz.diet_pro.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Qualifier("dev")
    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService service;


    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent onRegistrationCompleteEvent) {
        this.confirmRegistration(onRegistrationCompleteEvent);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent onRegistrationCompleteEvent) {
        UserPlainDto user = onRegistrationCompleteEvent.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = onRegistrationCompleteEvent.getAppUrl() + "/registration/confirm.html?token=" + token;
        String message = messages.getMessage("message.regSucc", null, onRegistrationCompleteEvent.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        log.debug(message + "\r\n" + "http://localhost:8082" + confirmationUrl);
        email.setText(message + "\r\n" + "http://localhost:8082" + confirmationUrl);

        mailSender.send(email);

    }
}
