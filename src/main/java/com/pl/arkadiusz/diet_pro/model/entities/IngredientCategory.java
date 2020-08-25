package com.pl.arkadiusz.diet_pro.model.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ingredient_categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class IngredientCategory {

    @Column(name = "ingredient_category_names", unique = true, nullable = false)
    private String ingredientName;

    // Relation parts //

    @ManyToMany(mappedBy = "ingredientCategories")
    private List<Ingredient> ingredients = new ArrayList<>();

}
