package com.pl.arkadiusz.diet_pro.model.repositories;

import com.pl.arkadiusz.diet_pro.model.entities.PasswordRestartToken;
import com.pl.arkadiusz.diet_pro.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RePasswordTokenRepository  extends JpaRepository<PasswordRestartToken, Long> {
    Optional<PasswordRestartToken> getByUser(User user);
    Optional<PasswordRestartToken> getByToken(String token);

}
