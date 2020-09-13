package com.pl.arkadiusz.diet_pro.controllers;

import com.pl.arkadiusz.diet_pro.Listeners.OnRegistrationCompleteEvent;
import com.pl.arkadiusz.diet_pro.dto.userDto.UserPlainDto;
import com.pl.arkadiusz.diet_pro.dto.userDto.UserRegisterDTO;
import com.pl.arkadiusz.diet_pro.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;


@RestController
@RequestMapping(value = "/registration")
public class RegistrationController {
    private final RegistrationService registrationService;

    private final PasswordEncoder passwordEncoder;


    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public RegistrationController(RegistrationService registrationService, PasswordEncoder passwordEncoder, ApplicationEventPublisher applicationEventPublisher) {
        this.registrationService = registrationService;
        this.passwordEncoder = passwordEncoder;
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




}
