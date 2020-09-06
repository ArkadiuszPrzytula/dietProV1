package com.pl.arkadiusz.diet_pro.controllers;

import com.pl.arkadiusz.diet_pro.Listeners.OnRegistrationCompleteEvent;
import com.pl.arkadiusz.diet_pro.dto.UserPlainDto;
import com.pl.arkadiusz.diet_pro.dto.UserRegisterDTO;
import com.pl.arkadiusz.diet_pro.dto.VerificationTokenDTO;
import com.pl.arkadiusz.diet_pro.errors.InvalidTokenException;
import com.pl.arkadiusz.diet_pro.errors.TokenExpiredException;
import com.pl.arkadiusz.diet_pro.services.RegistrationService;
import com.pl.arkadiusz.diet_pro.services.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;


@Controller
@RequestMapping(value = "/registration")
public class RegistrationController {
    private final RegistrationService registrationService;

    private final UserService userService;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public RegistrationController(RegistrationService registrationService, UserService userService, ApplicationEventPublisher applicationEventPublisher) {
        this.registrationService = registrationService;
        this.userService = userService;
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @PostMapping
    public ResponseEntity<Void> registerUser(@RequestBody @Valid UserRegisterDTO userRegisterDTO, HttpServletRequest httpServletRequest) {
        URI uri;
        String contextPath = httpServletRequest.getContextPath();
        UserPlainDto savedUser = registrationService.register(userRegisterDTO);
        applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(savedUser, httpServletRequest.getLocale(), contextPath));
        uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/user").path("/{id}").buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }


    @GetMapping("/confirm.html")
    public ResponseEntity<Void> confirmRegistration(WebRequest webRequest, @RequestParam("token") String token) throws InvalidTokenException, TokenExpiredException {
        URI uri;
        Long verifyUserId =-1L;
//        Locale locale = webRequest.getLocale();
        VerificationTokenDTO verificationToken = userService.getVerificationToken(token);
        System.out.println(verificationToken);
        userService.checkTokenExpireTime(verificationToken);
        verifyUserId = userService.verifyUser(verificationToken.getUserId());


        URI uri1 = ServletUriComponentsBuilder.fromCurrentContextPath().replacePath("/user").path("/{id}").buildAndExpand(verificationToken.getUserId()).toUri();
//        uri = ServletUriComponentsBuilder.fromCurrentContextPath().removePathExtension("/user/{id}");
        return ResponseEntity.created(uri1).build();
    }

}
