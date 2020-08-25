package com.pl.arkadiusz.diet_pro.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class User {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name ="passwords", nullable = false)
    private String password;

    @Column (name = "emails", nullable = false)
    private String email;


    // Relations part //

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_data_id", referencedColumnName = "id")
    private PersonalData personalData;



}
