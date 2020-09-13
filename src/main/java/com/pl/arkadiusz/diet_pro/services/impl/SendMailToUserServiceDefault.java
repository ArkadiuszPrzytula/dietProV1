package com.pl.arkadiusz.diet_pro.services.impl;

import com.pl.arkadiusz.diet_pro.dto.Email;
import com.pl.arkadiusz.diet_pro.dto.MailFiles;
import com.pl.arkadiusz.diet_pro.dto.userDto.UserPlainDto;
import com.pl.arkadiusz.diet_pro.model.entities.enums.TokenType;
import com.pl.arkadiusz.diet_pro.services.SendMailToUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;


@Service
public class SendMailToUserServiceDefault implements SendMailToUserService {

    private final String APP_NAME = "DietPRO";

    @Value("classpath:/static/image/bocolies/brocoli_himan.png")
    private Resource logoSource;

    @Value("classpath:/templates/mail/VerificationRegistrationTokenTemplate.html")
    private Resource verificationTokenTemplate;

    @Value("classpath:/templates/mail/RePasswordTokenTemplate.html")
    private Resource rePasswordTokenTemplate;
    private final String logoName = "logo";
    private final String logoContext = "image/png";

    private final TemplateEngine templateEngine;

    private final MailServiceDefault emailService;

    @Autowired
    public SendMailToUserServiceDefault(TemplateEngine templateEngine, MailServiceDefault emailService) {
        this.templateEngine = templateEngine;
        this.emailService = emailService;
    }


    @Override
    public void sendTokenToUserFactory(String appUrl, UserPlainDto user, String token, TokenType tokenType) throws MessagingException, IOException {
        switch (tokenType) {
            case RE_PASSWORD:
                sendTokenToUser(appUrl, user, token, APP_NAME + " change password", rePasswordTokenTemplate);
                break;
            case REGISTRATION_VERIFY:
                sendTokenToUser(appUrl, user, token, APP_NAME + "user registration verification", verificationTokenTemplate);
                break;
            default:
                //todo

        }

    }

    private void sendTokenToUser(String appUrl, UserPlainDto user, String token, String subject, Resource template) throws IOException, MessagingException {
        List<MailFiles> inLinesFiles = new LinkedList<>();
        MailFiles logoImg = new MailFiles(logoName, logoSource, logoContext);
        // Logo image add to  inn lines files
        inLinesFiles.add(logoImg);
//        List<MailFiles> attachments = new LinkedList<>();
        // Create template content based on  app url, username, token and, allias to app logo image
        Context context = getThContextToSendToken(appUrl, user.getUsername(), token, logoImg.getName());
        String process = templateEngine.process(template.getURI().toString(), context);
        String recipientAddress = user.getEmail();
        Email build = Email.builder()
                .subject(subject)
                .to(recipientAddress)
                .inLinesFiles(Optional.of(inLinesFiles))
                .attachments(Optional.empty())
                .messageText(process).build();
        emailService.sendHttpMail(build);
    }


    private  void sendPasswordRestartToken(String appUrl, UserPlainDto user, String token) {
        List<MailFiles> inLinesFiles = new LinkedList<>();
        MailFiles logoImg = new MailFiles(logoName, logoSource, logoContext);
        inLinesFiles.add(logoImg);

        getThContextToSendToken(appUrl, user.getUsername(), token, logoImg.getName());

    }


    private Context getThContextToSendToken(String appUrl, String user, String verificationToken, String logoImg) {
        Context context = new Context();
        context.setVariable("appUrl", "http://localhost:8082" +appUrl);
        context.setVariable("username", user);
        context.setVariable("token", verificationToken);
        context.setVariable(logoName, logoImg);
        return context;
    }


}
