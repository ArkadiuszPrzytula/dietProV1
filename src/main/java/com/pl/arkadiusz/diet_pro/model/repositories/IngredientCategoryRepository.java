package com.pl.arkadiusz.diet_pro.model.repositories;

import com.pl.arkadiusz.diet_pro.model.entities.IngredientCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory, Long> {
//    Optional<IngredientCategory> findByActiveAndId(Boolean active, Long id);
    Optional<IngredientCategory> findById( Long id);

}
