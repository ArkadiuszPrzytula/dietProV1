package com.pl.arkadiusz.diet_pro.services.impl;

import com.pl.arkadiusz.diet_pro.model.entities.Privilege;
import com.pl.arkadiusz.diet_pro.model.entities.User;
import com.pl.arkadiusz.diet_pro.model.repositories.PrivilegeRepository;
import com.pl.arkadiusz.diet_pro.model.repositories.RoleRepository;
import com.pl.arkadiusz.diet_pro.model.repositories.UserRepository;
import com.pl.arkadiusz.diet_pro.services.LoggedUserService;
import com.pl.arkadiusz.diet_pro.services.UserPrivilegesVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;



@Service
public class UserPrivilegesVerificationServiceDefault implements UserPrivilegesVerificationService {

    UserRepository userRepository;

    RoleRepository roleRepository;

    PrivilegeRepository privilegeRepository;



    @Autowired
    public UserPrivilegesVerificationServiceDefault(UserRepository userRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
    }


    @Override
    public Set<Privilege> getAllUserPrivileges() {
//        User loggedUser = userRepository.getSpecialByUsername(LoggedUserService.getLoggedUserName()).orElseThrow(InternalError::new);
//        Set<Privilege> privileges = new HashSet<>();
//        loggedUser.getRoles().stream().map(p -> privilegeRepository
//                .findByRoles(p).stream().filter(Optional::isPresent)
//                .map(Optional::get).collect(Collectors.toSet()))
//                .forEach(privileges::addAll);
        return null;
    }

    @Override
    public boolean verify(String privilege) {
        return getAllUserPrivileges().contains(privilegeRepository.findByName(privilege).get());
    }

}
