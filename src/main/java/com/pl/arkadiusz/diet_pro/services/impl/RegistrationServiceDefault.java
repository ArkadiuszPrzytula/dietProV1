package com.pl.arkadiusz.diet_pro.services.impl;

import com.pl.arkadiusz.diet_pro.dto.userDto.UserPlainDto;
import com.pl.arkadiusz.diet_pro.dto.userDto.UserRegisterDTO;
import com.pl.arkadiusz.diet_pro.model.entities.Role;
import com.pl.arkadiusz.diet_pro.model.entities.User;
import com.pl.arkadiusz.diet_pro.model.repositories.RoleRepository;
import com.pl.arkadiusz.diet_pro.model.repositories.UserRepository;
import com.pl.arkadiusz.diet_pro.services.RegistrationService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;

@Setter
@Transactional
@Slf4j
@Validated
@Service
public class RegistrationServiceDefault implements RegistrationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public RegistrationServiceDefault(PasswordEncoder passwordEncoder,
                                      UserRepository userRepository,
                                      RoleRepository roleRepository,
                                      ModelMapper modelMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    @Validated
    public UserPlainDto register(UserRegisterDTO userRegisterDTO) {
        User user = createNewUserToRegister(userRegisterDTO);
        User savedUser = userRepository.save(user);
        log.debug("{}-register: User before save: {}",this.getClass().getSimpleName(), user);
        return modelMapper.map(savedUser, UserPlainDto.class);
    }

    private User createNewUserToRegister(UserRegisterDTO userRegisterDTO) {
        log.debug("{}-create new user: to create user {}", this.getClass().getSimpleName(), userRegisterDTO);
        User user = modelMapper.map(userRegisterDTO, User.class);
        log.debug("{}-create new user: User after mapping from UserRegister: {}", this.getClass().getSimpleName(), user);
        user.setActive(true);
        user.setPersonalData(null);
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setRoles(Arrays.asList(roleRepository.findByName(Role.RoleValue.ROLE_USER.roleString).get()));
        log.debug("{}-create new user: User before save: {}",this.getClass().getSimpleName(), user);
        return user;
    }

}
