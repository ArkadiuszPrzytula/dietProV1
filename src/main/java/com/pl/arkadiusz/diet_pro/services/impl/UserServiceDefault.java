package com.pl.arkadiusz.diet_pro.services.impl;

import com.pl.arkadiusz.diet_pro.dto.UserPlainDto;
import com.pl.arkadiusz.diet_pro.model.entities.Privilege;
import com.pl.arkadiusz.diet_pro.model.entities.Role;
import com.pl.arkadiusz.diet_pro.model.entities.User;
import com.pl.arkadiusz.diet_pro.model.repositories.UserRepository;
import com.pl.arkadiusz.diet_pro.services.LoggedUserService;
import com.pl.arkadiusz.diet_pro.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserServiceDefault implements UserService {
    private final UserRepository userRepository;


    private final ModelMapper modelMapper;

    private final LoggedUserService loggedUserService;

    @Autowired
    public UserServiceDefault(UserRepository userRepository,
                              ModelMapper modelMapper,
                              LoggedUserService loggedUserService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.loggedUserService = loggedUserService;
    }

    @Override
    public List<UserPlainDto> getAllUser() {
        List<User> allUser;
        if (loggedUserService.checkLoggedUserAuthorities(Privilege.PrivilegeValue.READ_ALL.stringValue)) {
            allUser = userRepository.findAll();
            System.out.println("checked");
        } else {
            allUser = userRepository.getAllUserByActive(true);
        }

        return allUser.stream().map(p -> {
            UserPlainDto map = modelMapper.map(p, UserPlainDto.class);
            List<String> collect = p.getRoles().stream().map(Role::getName).collect(Collectors.toList());
            map.setName(collect);
            return map;
        }).collect(Collectors.toList());
    }

}
