package com.pl.arkadiusz.diet_pro.services.impl;

import com.pl.arkadiusz.diet_pro.dto.userDto.UserPlainDto;
import com.pl.arkadiusz.diet_pro.errors.UserNotFoundException;
import com.pl.arkadiusz.diet_pro.model.entities.Privilege;
import com.pl.arkadiusz.diet_pro.model.entities.Role;
import com.pl.arkadiusz.diet_pro.model.entities.User;
import com.pl.arkadiusz.diet_pro.model.repositories.UserRepository;
import com.pl.arkadiusz.diet_pro.services.LoggedUserService;
import com.pl.arkadiusz.diet_pro.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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
            log.debug("{}-register: User before save: {}",this.getClass().getSimpleName());
            allUser = userRepository.findAll();
        } else {
            allUser = userRepository.getAllUserByActive(true);
        }

        return allUser.stream().map(p -> {
            UserPlainDto map = modelMapper.map(p, UserPlainDto.class);
            List<String> collect = p.getRoles().stream().map(Role::getName).collect(Collectors.toList());
            map.setGraduatesName(collect);
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public UserPlainDto getUserPlainDto(Long id) {
        User user = getRawUserById(id);
        modelMapper.map(user, UserPlainDto.class);
        return modelMapper.map(user, UserPlainDto.class);
    }

    @Override
    public UserPlainDto getUserPlainDto(String email) throws UserNotFoundException {
        User user = getRawUserByEmail(email);
        modelMapper.map(user, UserPlainDto.class);
        return modelMapper.map(user, UserPlainDto.class);
    }


    private User getRawUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(String.valueOf("username:" + username)));
    }

    private User getRawUserById(Long userId) {
        return userRepository.findUserById(userId).orElseThrow(() -> new UserNotFoundException("id:" + userId));
    }

    private User getRawUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("email:" + email));
    }

}
