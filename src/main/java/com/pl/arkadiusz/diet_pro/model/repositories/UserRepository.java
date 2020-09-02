package com.pl.arkadiusz.diet_pro.model.repositories;

import com.pl.arkadiusz.diet_pro.model.entities.User;
import org.h2.pagestore.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);


    List<User> getAllUserByActive(boolean Active);

    Optional<User> findUserByUsernameAndActive(String username, boolean active);



    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"user.roles"})
    Optional<User> getSpecialByUsername(String username);

}
