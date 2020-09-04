package com.pl.arkadiusz.diet_pro.services.impl;

import com.pl.arkadiusz.diet_pro.dto.UserPlainDto;
import com.pl.arkadiusz.diet_pro.dto.VerificationTokenDTO;
import com.pl.arkadiusz.diet_pro.errors.InvalidTokenException;
import com.pl.arkadiusz.diet_pro.errors.UserNotFoudException;
import com.pl.arkadiusz.diet_pro.errors.TokenExpiredException;
import com.pl.arkadiusz.diet_pro.model.entities.Privilege;
import com.pl.arkadiusz.diet_pro.model.entities.Role;
import com.pl.arkadiusz.diet_pro.model.entities.User;
import com.pl.arkadiusz.diet_pro.model.entities.VerificationToken;
import com.pl.arkadiusz.diet_pro.model.repositories.TokenRepository;
import com.pl.arkadiusz.diet_pro.model.repositories.UserRepository;
import com.pl.arkadiusz.diet_pro.services.LoggedUserService;
import com.pl.arkadiusz.diet_pro.services.UserService;
import lombok.SneakyThrows;
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

    private TokenRepository tokenRepository;

    @Autowired
    public UserServiceDefault(UserRepository userRepository,
                              ModelMapper modelMapper,
                              LoggedUserService loggedUserService, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.loggedUserService = loggedUserService;
        this.tokenRepository = tokenRepository;
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


    @Override
    public VerificationTokenDTO createVerificationToken(UserPlainDto user, String token) {
        System.out.println("token :" +token);
        System.out.println("user :" +user);
        User u = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new UserNotFoudException(String.valueOf(user.getId())));
        System.out.println("Data base user : " +u);
        VerificationToken verificationToken = new VerificationToken(u, token);
        System.out.println("token: " +verificationToken);
        VerificationToken save = tokenRepository.save(verificationToken);
        System.out.println("saved token: " +save);
        return modelMapper.map(save, VerificationTokenDTO.class);
    }

    @SneakyThrows
    @Override
    public VerificationTokenDTO getVerificationToken(String token) {
        VerificationToken byToken = tokenRepository.getByToken(token).orElseThrow(InvalidTokenException::new);
        System.out.println("getVerificationToken: " + byToken);
        VerificationTokenDTO map = modelMapper.map(byToken, VerificationTokenDTO.class);
        System.out.println("getVerificationToken after map: " + map);
        map.setUserId(byToken.getUser().getId());
        System.out.println("finalll " + map);
        return map;
    }

    @Override
    public boolean checkTokenExpireTime(VerificationTokenDTO verificationToken) throws TokenExpiredException {
        long tokenExpireTime = verificationToken.getExpiryDate().getTime();
        Calendar calendar = Calendar.getInstance();
        if (tokenExpireTime - calendar.getTime().getTime() <= 0) {
            throw new TokenExpiredException();
        } else {
            return true;
        }
    }

    @Override
    public Long verifyUser(Long userId) {
        System.out.println(userId);
        User user = userRepository.getUserById(userId).orElseThrow(() -> new UserNotFoudException(String.valueOf(userId)));
        user.setEnable(true);
        User verifyUser = userRepository.save(user);
        return verifyUser.getId();
    }

}
