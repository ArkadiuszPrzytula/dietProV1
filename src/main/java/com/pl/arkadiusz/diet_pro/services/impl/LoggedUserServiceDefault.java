package com.pl.arkadiusz.diet_pro.services.impl;

import com.pl.arkadiusz.diet_pro.model.entities.Role;
import com.pl.arkadiusz.diet_pro.model.repositories.RoleRepository;
import com.pl.arkadiusz.diet_pro.model.repositories.UserRepository;
import com.pl.arkadiusz.diet_pro.services.LoggedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class LoggedUserServiceDefault implements LoggedUserService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public LoggedUserServiceDefault(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String getLoggedUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getLoggedUserGrantedAuthority() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

    @Override
    public boolean checkLoggedUserAuthorities(String privilege) {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        Stream<String> stringStream = authorities.stream().map(Object::toString);
        return stringStream.anyMatch(p -> p.equalsIgnoreCase(privilege));
    }

    @Override
    public Set<Role> getRoles() {
        return roleRepository
                .findByUsers(userRepository.findByUsername(getLoggedUserName())
                        .orElse(null)).orElse(null);

    }

}
