package com.pl.arkadiusz.diet_pro.services.impl;

import com.pl.arkadiusz.diet_pro.dto.userDto.PasswordResetRequest;
import com.pl.arkadiusz.diet_pro.dto.userDto.TokenDTO;
import com.pl.arkadiusz.diet_pro.errors.InvalidTokenException;
import com.pl.arkadiusz.diet_pro.errors.TokenExpiredException;
import com.pl.arkadiusz.diet_pro.errors.UserNotFoudException;
import com.pl.arkadiusz.diet_pro.model.entities.User;
import com.pl.arkadiusz.diet_pro.model.entities.Token;
import com.pl.arkadiusz.diet_pro.model.repositories.TokenRepository;
import com.pl.arkadiusz.diet_pro.model.repositories.UserRepository;
import com.pl.arkadiusz.diet_pro.services.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
public class UserAccountServiceDefault implements UserAccountService {

    private final TokenRepository tokenRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountServiceDefault(TokenRepository tokenRepository, ModelMapper modelMapper, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public TokenDTO getToken(String token) throws InvalidTokenException {
        Token fullToken = tokenRepository.getByToken(token).orElseThrow(() -> new InvalidTokenException());
        log.debug("{}-getToken: receive token: {}", this.getClass().getSimpleName(), fullToken);
        TokenDTO tokenDTO = modelMapper.map(fullToken, TokenDTO.class);
        tokenDTO.setUserId(fullToken.getUser().getId());
        log.debug("{}-getToken: return tokenDTO: {}", this.getClass().getSimpleName(), tokenDTO);
        return tokenDTO;
    }

    @Override
    public boolean checkTokenExpireTime(TokenDTO token) throws TokenExpiredException {
        long tokenExpireTime = token.getExpiryDate().getTime();
        Calendar calendar = Calendar.getInstance();
        if (tokenExpireTime - calendar.getTime().getTime() <= 0) {
            log.debug("{}-checkTokenTime: ERROR {}", this.getClass().getSimpleName(), TokenExpiredException.class.getSimpleName());
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
        log.debug("{}-verifyUser: user verified!{}", this.getClass().getSimpleName(), user);
        return verifyUser.getId();
    }

    @Override
    public String createTokenUsername(String username) {
        User user = getRawUserByUsername(username);
        Token token = prepareToken(user);
        log.debug("{}-createTokenNyUsername: token created: {} for user {}", this.getClass().getSimpleName(), token, user);
        return token.getToken();
    }

    @Override
    public String createTokenEmail(String email) {
        User user = getRawUserByEmail(email);
        Token token = prepareToken(user);
        log.debug("{}-createTokenByEmail : token created: {} for user {}", this.getClass().getSimpleName(), token, user);
        return token.getToken();
    }

    private Token prepareToken(User user) {
        String token = UUID.randomUUID().toString();
        Token passwordRestartToken = new Token(user, token);
        Optional<Token> tokenByUser = tokenRepository.getByUser(user);
        tokenByUser.ifPresent(tokenRepository::delete);
        return tokenRepository.save(passwordRestartToken);
    }

    @Override
    public Long editUser(Long userId, PasswordResetRequest passwordResetRequest) {
        User user = getRawUserById(userId);
        user.setPassword(passwordEncoder.encode(passwordResetRequest.getPassword()));
        User savedUser = userRepository.save(user);
        log.debug("{}-editUser : password changed!", this.getClass().getSimpleName());
        return savedUser.getId();
    }

    @Override
    public void clearToken(TokenDTO token) throws InvalidTokenException {
        Token rawToken = tokenRepository.getByToken(token.getToken()).orElseThrow(
                InvalidTokenException::new);
        tokenRepository.delete(rawToken);
    }

    private User getRawUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoudException("username:" + username));
        log.debug("{}-getRawUserByUsername: receive user: {}", this.getClass().getSimpleName(), user);
        return user;
    }

    private User getRawUserById(Long userId) {
        User user = userRepository.findUserById(userId).orElseThrow(
                () -> new UserNotFoudException("id:" + userId));
        log.debug("{}-getRawUserById: receive user: {}", this.getClass().getSimpleName(), user);
        return user;
    }

    private User getRawUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoudException("email:" + email));
        log.debug("{}-getRawUserByEmail: receive user: {}", this.getClass().getSimpleName(), user);
        return user;
    }
}
