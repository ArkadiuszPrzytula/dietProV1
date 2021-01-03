package com.pl.arkadiusz.diet_pro.services.impl;

import com.pl.arkadiusz.diet_pro.dto.Email;
import com.pl.arkadiusz.diet_pro.dto.MailFiles;
import com.pl.arkadiusz.diet_pro.services.EmailService;


import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.util.List;
import java.util.function.Consumer;

@Transactional
@Service
public class MailServiceDefault implements EmailService {

    private final JavaMailSender javaMailSender;

    public MailServiceDefault(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @Override
    public void sendHttpMail(final Email email) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new
                MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setSubject(email.getSubject());
        mimeMessageHelper.setTo(email.getTo());
        mimeMessageHelper.setText(email.getMessageText(), true);
        email.getAttachments().ifPresent(addAttachments(mimeMessageHelper));


        email.getInLinesFiles().ifPresent(addInLines(mimeMessageHelper));
        javaMailSender.send(mimeMessage);
    }

    private Consumer<List<MailFiles>> addAttachments(MimeMessageHelper mimeMessageHelper) {
        return s -> s.forEach(p -> {
            try {
                mimeMessageHelper.addAttachment(p.getName(), p.getResource(), p.getContext());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
    }

    private Consumer<List<MailFiles>> addInLines(MimeMessageHelper mimeMessageHelper) {
        return s -> s.forEach(p -> {
            try {
                mimeMessageHelper.addInline(p.getName(), p.getResource(), p.getContext());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
    }


}