package com.pl.arkadiusz.diet_pro.model.repositories;

import com.pl.arkadiusz.diet_pro.model.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> getByToken(String token);
}
