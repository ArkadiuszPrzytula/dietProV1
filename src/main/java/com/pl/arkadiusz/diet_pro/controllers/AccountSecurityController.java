package com.pl.arkadiusz.diet_pro.controllers;

import com.pl.arkadiusz.diet_pro.dto.UserPlainDto;
import com.pl.arkadiusz.diet_pro.dto.VerificationTokenDTO;
import com.pl.arkadiusz.diet_pro.errors.InvalidTokenException;
import com.pl.arkadiusz.diet_pro.errors.TokenExpiredException;
import com.pl.arkadiusz.diet_pro.services.SendMailToUserService;
import com.pl.arkadiusz.diet_pro.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/account")
public class AccountSecurityController {
    private final UserService userService;

    private final SendMailToUserService sendMailToUserService;

    @Autowired
    public AccountSecurityController(UserService userService, SendMailToUserService sendMailToUserService) {
        this.userService = userService;
        this.sendMailToUserService = sendMailToUserService;
    }

    @PostMapping("/resend-verify-token/{id}")
    public ResponseEntity<Void> resentVerifyToken(@PathVariable("id") Long id) throws InvalidTokenException {
        System.out.println(id);
        VerificationTokenDTO verificationToken = userService.getVerificationToken(id);
        UserPlainDto user = userService.getUserPlainDto(id);
        System.out.println(user);
        if (verificationToken != null && !user.isEnable()) {
            System.out.println(user.isEnable());
            String token = userService.createVerificationToken(user);
            URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();
            sendMailToUserService.sendVerificationTokenToUser(uri.getPath(),user, token);
            return ResponseEntity.created(uri).build();
        }
        return ResponseEntity.notFound().build();


    }


    @GetMapping("/confirm.html")
    public ResponseEntity<Void> confirmRegistration(WebRequest webRequest, @RequestParam("token") String token) throws InvalidTokenException, TokenExpiredException {
        URI uri;
        Long verifyUserId = -1L;
        VerificationTokenDTO verificationToken = userService.getVerificationToken(token);
        userService.checkTokenExpireTime(verificationToken);
        userService.verifyUser(verificationToken.getUserId());


        URI uri1 = ServletUriComponentsBuilder.fromCurrentContextPath().replacePath("/user").path("/{id}")
                .buildAndExpand(verificationToken.getUserId()).toUri();

        return ResponseEntity.created(uri1).build();
    }
}
