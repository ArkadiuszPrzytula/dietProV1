package com.pl.arkadiusz.diet_pro.model.entities;

import com.pl.arkadiusz.diet_pro.model.entities.enums.Sex;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "personal_data")
@Setter
@Getter
@ToString(callSuper = true)
public class PersonalData extends EntityBase{

    @Column(name = "names")
    private String name;

    @Column(name = "lastNames")
    private String lastName;

    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(name = "day_of_birth")
    private LocalDate dayOfBirth;

    @Column(name = "weight_in_kilo")
    private Long weightInKilo;

    @Column(name = "highs_in_cm")
    private float highInCm;

}
