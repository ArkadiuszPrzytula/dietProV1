package com.pl.arkadiusz.diet_pro.services;

import javax.mail.MessagingException;

public interface EmailService {
    //    @SendMail
    void sendMail(String to,
                  String subject,
                  String text,
                  boolean isHtmlContent) throws MessagingException;
}
