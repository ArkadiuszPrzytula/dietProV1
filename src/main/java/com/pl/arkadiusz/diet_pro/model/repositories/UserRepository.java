package com.pl.arkadiusz.diet_pro.model.repositories;

import com.pl.arkadiusz.diet_pro.model.entities.User;
import org.h2.pagestore.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserById(Long userId);

    Optional<User> findUserByEmail(String email);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    List<User> getAllUserByActive(boolean Active);

    Optional<User> findUserByUsernameAndActive(String username, boolean active);


    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"user.roles"})
    Optional<User> getUSerWithAllRollesByUsername(String username);
}
