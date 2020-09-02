package com.pl.arkadiusz.diet_pro.setup;

import com.pl.arkadiusz.diet_pro.model.entities.Privilege;
import com.pl.arkadiusz.diet_pro.model.entities.Role;
import com.pl.arkadiusz.diet_pro.model.entities.User;
import com.pl.arkadiusz.diet_pro.model.repositories.PrivilegeRepository;
import com.pl.arkadiusz.diet_pro.model.repositories.RoleRepository;
import com.pl.arkadiusz.diet_pro.model.repositories.UserRepository;
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

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    private final PasswordEncoder passwordEncoder;

    public SetupDataLoader(UserRepository userRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * This method create all privileges and roles in database. Additionally create basic admin account and normal user
     * On the end method sets
     *
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
        createRoleIfNotFound(Role.RoleValue.ROLE_MODERATOR.roleString, normalUserPrivileges);

        createInitUser(Role.RoleValue.ROLE_ADMIN.roleString, "admin", "admin@admin.com", true);

        createInitUser(Role.RoleValue.ROLE_USER.roleString, "user", "user@user.com", true);
        createInitUser(Role.RoleValue.ROLE_USER.roleString, "inactiveUser", "inactiveUser@user.com", false);
        createInitUser(Role.RoleValue.ROLE_MODERATOR.roleString, "inactiveModerator", "inactiveModerator@user.com", false);

        alreadySetup = true;
    }

    private void createInitUser(String role, String username, String email, boolean active) {
        Role adminRole = roleRepository.findByName(role).get();
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(username));
        user.setEmail(email);

        user.setRoles(Arrays.asList(adminRole));
        user.setEnabled(true);
        user.setActive(active);
        userRepository.save(user);

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
