package com.pl.arkadiusz.diet_pro.setup;

import com.pl.arkadiusz.diet_pro.model.entities.Privilege;
import com.pl.arkadiusz.diet_pro.model.entities.Role;
import com.pl.arkadiusz.diet_pro.model.entities.User;
import com.pl.arkadiusz.diet_pro.model.repositories.PrivilegeRepository;
import com.pl.arkadiusz.diet_pro.model.repositories.RoleRepository;
import com.pl.arkadiusz.diet_pro.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * This method create all privileges and roles in database. Additionally create basic admin account and normal user
     * On the end method sets
     * @param contextRefreshedEvent
     */
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (alreadySetup)
            return;

        Privilege banUser = createPrivilegeIfNotFound(Privilege.PrivilegeValue.BAN_USER.stringValue);
        Privilege modifyAll = createPrivilegeIfNotFound(Privilege.PrivilegeValue.MODIFY_ALL.stringValue);
        Privilege readAll = createPrivilegeIfNotFound(Privilege.PrivilegeValue.READ_ALL.stringValue);
        Privilege readCasualElements = createPrivilegeIfNotFound(Privilege.PrivilegeValue.READ_ALL.stringValue);
        Privilege userDelete = createPrivilegeIfNotFound(Privilege.PrivilegeValue.USER_DELETE.stringValue);

        List<Privilege> adminPrivileges = Arrays.asList(banUser, modifyAll, readAll, userDelete);
        List<Privilege> normalUserPrivileges = Arrays.asList(readCasualElements);
        createRoleIfNotFound(Role.RoleValue.ROLE_ADMIN.roleString, adminPrivileges);
        createRoleIfNotFound(Role.RoleValue.ROLE_USER.roleString, normalUserPrivileges);

        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setEmail("admin@admin.com");
        admin.setRoles(Arrays.asList(adminRole));
        admin.setEnabled(true);
        userRepository.save(admin);

        Role userRole = roleRepository.findByName("ROLE_USER").get();
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user"));
        user.setEmail("user@user.com");
        user.setRoles(Arrays.asList(userRole));
        user.setEnabled(true);
        userRepository.save(user);

        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Optional<Privilege> gotPrivilege = privilegeRepository.findByName(name);
        Privilege privilege;
        if (gotPrivilege.isEmpty()) {
            privilege = privilegeRepository.save(new Privilege(name));
        } else {
            privilege = gotPrivilege.get();
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(
            String name, List<Privilege> privileges) {
        Optional<Role> gotRole = roleRepository.findByName(name);
        Role role;
        if (gotRole.isEmpty()) {
            role = roleRepository.save(new Role(name, privileges));
        } else {
            role = gotRole.get();
        }
        return role;
    }
}
