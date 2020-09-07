package com.pl.arkadiusz.diet_pro.services.impl;

import com.pl.arkadiusz.diet_pro.dto.UserPlainDto;
import com.pl.arkadiusz.diet_pro.services.SendMailToUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;



@Service
public class SendMailToUserServiceDefault implements SendMailToUserService {

    private final MailServiceDefault emailService;

    @Autowired
    public SendMailToUserServiceDefault(MailServiceDefault emailService) {
        this.emailService = emailService;
    }


    @Override
    public void sendVerificationTokenToUser(String appUrl, UserPlainDto user, String verificationToken) {
        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = appUrl + "/account/confirm.html?token=" + verificationToken;

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
    }
}
