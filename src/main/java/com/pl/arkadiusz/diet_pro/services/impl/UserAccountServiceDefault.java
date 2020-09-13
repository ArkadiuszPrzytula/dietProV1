package com.pl.arkadiusz.diet_pro.services.impl;

import com.pl.arkadiusz.diet_pro.dto.userDto.PasswordResetRequest;
import com.pl.arkadiusz.diet_pro.dto.userDto.TokenDTO;
import com.pl.arkadiusz.diet_pro.errors.InvalidTokenException;
import com.pl.arkadiusz.diet_pro.errors.TokenExpiredException;
import com.pl.arkadiusz.diet_pro.errors.UserNotFoudException;
import com.pl.arkadiusz.diet_pro.model.entities.PasswordRestartToken;
import com.pl.arkadiusz.diet_pro.model.entities.User;
import com.pl.arkadiusz.diet_pro.model.entities.VerificationToken;
import com.pl.arkadiusz.diet_pro.model.repositories.RePasswordTokenRepository;
import com.pl.arkadiusz.diet_pro.model.repositories.VerificationTokenRepository;
import com.pl.arkadiusz.diet_pro.model.repositories.UserRepository;
import com.pl.arkadiusz.diet_pro.services.UserAccountService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserAccountServiceDefault implements UserAccountService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RePasswordTokenRepository rePasswordTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountServiceDefault(VerificationTokenRepository verificationTokenRepository, ModelMapper modelMapper, UserRepository userRepository, RePasswordTokenRepository rePasswordTokenRepository, PasswordEncoder passwordEncoder) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.rePasswordTokenRepository = rePasswordTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public TokenDTO getVerificationToken(String token) throws InvalidTokenException {
        VerificationToken byToken = verificationTokenRepository.getByToken(token).orElseThrow(InvalidTokenException::new);
        System.out.println("by token " + byToken);
        TokenDTO map = modelMapper.map(byToken, TokenDTO.class);
        map.setUserId(byToken.getUser().getId());
        return map;
    }

    @Override
    public TokenDTO getVerificationToken(Long userId) throws InvalidTokenException {
        User user = getRawUserById(userId);
        VerificationToken token = verificationTokenRepository.getByUser(user).orElseThrow(() -> new InvalidTokenException());
        System.out.println("by token " + token);
        TokenDTO map = modelMapper.map(token, TokenDTO.class);
        map.setUserId(token.getUser().getId());
        return map;
    }


    @Override
    public TokenDTO getRestartToken(String token) throws InvalidTokenException {
        PasswordRestartToken byToken = rePasswordTokenRepository.getByToken(token).orElseThrow(() -> new InvalidTokenException());
        System.out.println("by token: " + byToken);
        TokenDTO tokenDTO = modelMapper.map(byToken, TokenDTO.class);
        tokenDTO.setUserId(byToken.getUser().getId());
        return tokenDTO;
    }

    @Override
    public boolean checkTokenExpireTime(TokenDTO token) throws TokenExpiredException {
        long tokenExpireTime = token.getExpiryDate().getTime();
        Calendar calendar = Calendar.getInstance();
        if (tokenExpireTime - calendar.getTime().getTime() <= 0) {
            throw new TokenExpiredException();
        } else {
            return true;
        }
    }

    @Override
    public Long verifyUser(Long userId) {
        User user = getRawUserById(userId);
        user.setEnable(true);
        User verifyUser = userRepository.save(user);
        return verifyUser.getId();
    }

    @Override
    public String createPasswordRestartToken(String email) {
        User user = getRawUserByEmail(email);
        PasswordRestartToken save = prepareToken(user);
        return save.getToken();
    }

    @Override
    public String createVerificationToken(String username) {
        User user = getRawUserByUsername(username);
        VerificationToken token = prepareToken(user);
        return save.getToken();
    }

    private PasswordRestartToken prepareToken(User user) {
        String token = UUID.randomUUID().toString();
        PasswordRestartToken passwordRestartToken = new PasswordRestartToken(user, token);
        Optional<PasswordRestartToken> tokenByUser = rePasswordTokenRepository.getByUser(user);
        tokenByUser.ifPresent(rePasswordTokenRepository::delete);
        return rePasswordTokenRepository.save(passwordRestartToken);
    }

    @Override
    public Long editUser(Long userId, PasswordResetRequest passwordResetRequest) {
        User user = getRawUserById(userId);
        user.setPassword(passwordEncoder.encode(passwordResetRequest.getPassword()));
        User save = userRepository.save(user);
        return save.getId();
    }

    @Override
    public void clearToken(TokenDTO token) {

    }


    private User getRawUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoudException(String.valueOf("username:" + username)));
    }

    private User getRawUserById(Long userId) {
        return userRepository.findUserById(userId).orElseThrow(() -> new UserNotFoudException("id:" + userId));
    }

    private User getRawUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoudException(String.valueOf("email:" + email)));
    }
}
