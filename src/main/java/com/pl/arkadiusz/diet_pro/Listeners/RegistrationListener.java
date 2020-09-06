package com.pl.arkadiusz.diet_pro.Listeners;

import com.pl.arkadiusz.diet_pro.dto.UserPlainDto;

import com.pl.arkadiusz.diet_pro.services.EmailService;
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
    private EmailService emailService;


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

        String previousText =
                "\r Helo " + user.getUsername() + "! \r\n Click link below to  confirm your registration in dietPro app." +
                        "\r\n " +
                        "\r\n If you din't registered in our app, please skip this mail. ";
        String txt = previousText + " \r\n http://localhost:8082" + confirmationUrl;


        try {
            emailService.sendMail(recipientAddress, subject, txt, false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

//        mailSender.send(email);

    }
}
