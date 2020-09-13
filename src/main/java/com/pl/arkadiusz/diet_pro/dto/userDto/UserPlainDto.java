package com.pl.arkadiusz.diet_pro.dto.userDto;

import com.pl.arkadiusz.diet_pro.model.entities.Role;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class UserPlainDto {
    private Long id;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    private boolean active;

    private String username;

    private boolean enable;

    private String email;

  private Collection<String> name;


}
