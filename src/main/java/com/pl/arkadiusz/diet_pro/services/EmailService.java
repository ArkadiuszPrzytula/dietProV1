package com.pl.arkadiusz.diet_pro.services;

import com.pl.arkadiusz.diet_pro.dto.Email;

import javax.mail.MessagingException;

public interface EmailService {

    void sendHttpMail(Email email) throws MessagingException;
}
