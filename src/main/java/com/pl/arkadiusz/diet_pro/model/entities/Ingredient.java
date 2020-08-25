package com.pl.arkadiusz.diet_pro.model.entities;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ingredients")
@Setter
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor

public class Ingredient extends EntityBase {

    @Column(name = "ingredient_name", nullable = false)
    private String ingredientName;

    @Column(name = "calories_per_100g")
    private Long caloriesPer100g;

    // Relations part //

    @ManyToMany
    @JoinTable(name = "ingredient_category_ingredient",
    joinColumns = @JoinColumn(name = "ingredient_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_category_id"))
    private List<IngredientCategory> ingredientCategories = new ArrayList<>();



}
