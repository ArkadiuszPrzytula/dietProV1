package com.pl.arkadiusz.diet_pro.services.impl;

import com.pl.arkadiusz.diet_pro.model.entities.Privilege;
import com.pl.arkadiusz.diet_pro.model.entities.Role;
import com.pl.arkadiusz.diet_pro.model.entities.User;
import com.pl.arkadiusz.diet_pro.model.repositories.RoleRepository;
import com.pl.arkadiusz.diet_pro.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;


    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * Method constitute user authentication system. It take username from login attempt and check if it exists in the database.
     * If user exist, build security.core.userdetails.User on its basis.
     *
     * @param userName
     * @return security.core.userdetails.User
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(userName);
        if (user.isEmpty()) {

            throw new NoSuchFieldError();
        }
        User u = user.get();
        return new org.springframework.security.core.userdetails
                .User(u.getUsername(), u.getPassword(), u.isEnable(),
                true, true, u.isActive(), getAuthorities(u.getRoles()));
    }


    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }


    private List<String> getPrivileges(Collection<Role> roles) {
        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();

        for (Role role : roles) {
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
