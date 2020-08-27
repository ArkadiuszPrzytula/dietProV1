package com.pl.arkadiusz.diet_pro.services.impl;

import com.pl.arkadiusz.diet_pro.dto.UserRegisterDTO;
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
    public Long register(UserRegisterDTO userRegisterDTO) {
        User user = createNewUserToRegister(userRegisterDTO);
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    private User createNewUserToRegister(UserRegisterDTO userRegisterDTO) {
        log.debug("RegistrationService-register: to create user {}", userRegisterDTO);
        User user = modelMapper.map(userRegisterDTO, User.class);
        log.debug("RegistrationService-register: User after mapping from UserRegister: {}", user);
        user.setEnabled(true);
        user.setActive(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setPersonalData(null);
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setRoles(Arrays.asList(roleRepository.findByName(Role.RoleValue.ROLE_USER.roleString).get()));
        log.debug("RegistrationService-register: User before save: {}", user);
        return user;
    }

}
