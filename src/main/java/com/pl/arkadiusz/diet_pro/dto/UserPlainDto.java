package com.pl.arkadiusz.diet_pro.dto;

import com.pl.arkadiusz.diet_pro.model.entities.Role;
import lombok.Data;

import java.util.Collection;

@Data
public class UserPlainDto {
    private Long id;

    private boolean active;

    private String username;

    private boolean enabled;

    private String email;

    private Collection<Role> roles;

}