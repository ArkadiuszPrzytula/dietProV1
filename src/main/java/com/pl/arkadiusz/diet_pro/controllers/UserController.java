package com.pl.arkadiusz.diet_pro.controllers;

import com.pl.arkadiusz.diet_pro.dto.UserPlainDto;
import com.pl.arkadiusz.diet_pro.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserPlainDto>> getAllUsers() {
        List<UserPlainDto> allUser = new ArrayList<>();
        try {
            allUser = userService.getAllUser();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        System.out.println(allUser);
        return new ResponseEntity<>(allUser, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<UserPlainDto>> getUser() {

        return null;
    }
}
