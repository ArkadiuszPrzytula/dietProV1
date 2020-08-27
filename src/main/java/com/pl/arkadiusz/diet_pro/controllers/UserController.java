package com.pl.arkadiusz.diet_pro.controllers;

import com.pl.arkadiusz.diet_pro.services.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;


}
