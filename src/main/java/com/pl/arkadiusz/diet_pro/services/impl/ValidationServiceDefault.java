package com.pl.arkadiusz.diet_pro.services.impl;

import com.pl.arkadiusz.diet_pro.model.repositories.UserRepository;
import com.pl.arkadiusz.diet_pro.services.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ValidationServiceDefault implements ValidationService {
    private UserRepository userRepository;

    @Autowired
    public ValidationServiceDefault(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isUniqueEmail(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Override
    public boolean isUniqueUsername(String username) {
        return !userRepository.existsByUsername(username);
    }
    @Override
    public boolean PasswordAndRePasswordAreTheSame(String password, String rePassword) {
        return password.equals(rePassword);
    }
}
