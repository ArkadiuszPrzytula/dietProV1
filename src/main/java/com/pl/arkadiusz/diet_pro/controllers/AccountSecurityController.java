package com.pl.arkadiusz.diet_pro.controllers;

import com.pl.arkadiusz.diet_pro.dto.userDto.PasswordResetRequest;
import com.pl.arkadiusz.diet_pro.dto.userDto.UserPlainDto;
import com.pl.arkadiusz.diet_pro.dto.userDto.TokenDTO;
import com.pl.arkadiusz.diet_pro.errors.InvalidTokenException;
import com.pl.arkadiusz.diet_pro.errors.TokenExpiredException;
import com.pl.arkadiusz.diet_pro.model.entities.enums.TokenType;
import com.pl.arkadiusz.diet_pro.services.SendMailToUserService;
import com.pl.arkadiusz.diet_pro.services.UserAccountService;
import com.pl.arkadiusz.diet_pro.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping(value = "/account")
public class AccountSecurityController {

    private final UserAccountService userAccountService;

    private final UserService userService;

    private final SendMailToUserService sendMailToUserService;

    @Autowired
    public AccountSecurityController(UserAccountService userService, UserService userService1, SendMailToUserService sendMailToUserService) {
        this.userAccountService = userService;
        this.userService = userService1;

        this.sendMailToUserService = sendMailToUserService;
    }

    @PostMapping("/resend-verify-token/{id}")
    public ResponseEntity<Void> resentVerifyToken(@PathVariable("id") Long id) throws MessagingException, IOException {
        UserPlainDto user = userService.getUserPlainDto(id);
        if (checkUserIsEnabled(user)) {
            return ResponseEntity.notFound().build();
        }

        String token = userAccountService.createTokenUsername(user.getUsername());
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();
        sendMailToUserService.sendTokenToUserFactory(uri.getPath(), user, token, TokenType.REGISTRATION_VERIFY);
        return ResponseEntity.created(uri).build();
    }

    private boolean checkUserIsEnabled(UserPlainDto user) {
        return !user.isEnable();
    }


    @GetMapping("/forgot-password.html/{email}")
    public ResponseEntity<Void> PrepareRePasswordToken(@Email @PathVariable("email") String email) throws MessagingException, IOException {
        UserPlainDto user = userService.getUserPlainDto(email);
        String token = userAccountService.createTokenEmail(user.getEmail());
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();
        sendMailToUserService.sendTokenToUserFactory(uri.getPath(), user, token, TokenType.RE_PASSWORD);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/confirm.html")
    public ResponseEntity<Void> confirmRegistration(WebRequest webRequest,
                                                    @RequestParam("token") String token) throws InvalidTokenException, TokenExpiredException {

        TokenDTO verificationToken = userAccountService.getToken(token);
        userAccountService.checkTokenExpireTime(verificationToken);
        userAccountService.verifyUser(verificationToken.getUserId());
        userAccountService.clearToken(verificationToken);

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().replacePath("/user").path("/{id}")
                .buildAndExpand(verificationToken.getUserId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/password-reset.html")
    public ResponseEntity<Void> restartUserPassword(@RequestParam("token") String token,
                                                    @RequestBody @Valid PasswordResetRequest passwordResetRequest) throws TokenExpiredException, InvalidTokenException {


        TokenDTO restartToken = userAccountService.getToken(token);
        userAccountService.checkTokenExpireTime(restartToken);
        userAccountService.changeUserPassword(restartToken.getUserId(), passwordResetRequest);
        userAccountService.clearToken(restartToken);

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().replacePath("/user").path("/{id}")
                .buildAndExpand(restartToken.getUserId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
