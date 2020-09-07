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
import com.pl.arkadiusz.diet_pro.services.EmailService;
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

    private TokenRepository tokenRepository;

    private EmailService emailService;

    @Autowired
    public UserServiceDefault(UserRepository userRepository,
                              ModelMapper modelMapper,
                              LoggedUserService loggedUserService, TokenRepository tokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.loggedUserService = loggedUserService;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
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
    public UserPlainDto getUserPlainDto(Long id) {
        User user = userRepository.getUserById(id).orElseThrow(UserNotFoudException::new);
        modelMapper.map(user, UserPlainDto.class);
        return modelMapper.map(user, UserPlainDto.class);

    }



    @Override
    public String createVerificationToken(UserPlainDto user) {
        String token = UUID.randomUUID().toString();
        User u = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new UserNotFoudException(String.valueOf(user.getId())));
        VerificationToken verificationToken = new VerificationToken(u, token);
        Optional<VerificationToken> tokenByUser = tokenRepository.getByUser(u);
        tokenByUser.ifPresent(value -> tokenRepository.delete(value));
        VerificationToken save = tokenRepository.save(verificationToken);
        return save.getToken();
    }


    @Override
    public VerificationTokenDTO getVerificationToken(String token) throws InvalidTokenException {
        System.out.println(token);
        VerificationToken byToken = tokenRepository.getByToken(token).orElseThrow(InvalidTokenException::new);
        VerificationTokenDTO map = modelMapper.map(byToken, VerificationTokenDTO.class);
        map.setUserId(byToken.getUser().getId());
        return map;
    }

    @Override
    public VerificationTokenDTO getVerificationToken(Long userId) throws InvalidTokenException {
        User user = userRepository.getUserById(userId).orElseThrow(UserNotFoudException::new);
        Optional<VerificationToken> byUser = tokenRepository.getByUser(user);
        if (byUser.isEmpty()) {
            return null;
        }
        VerificationToken verificationToken = byUser.get();

        VerificationTokenDTO map = modelMapper.map(verificationToken, VerificationTokenDTO.class);
        map.setUserId(verificationToken.getUser().getId());
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
