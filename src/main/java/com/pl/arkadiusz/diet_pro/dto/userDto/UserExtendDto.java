package com.pl.arkadiusz.diet_pro.dto.userDto;

import com.pl.arkadiusz.diet_pro.model.entities.Role;
import com.pl.arkadiusz.diet_pro.model.entities.enums.Sex;
import lombok.Data;
import java.time.LocalDate;
import java.util.Collection;

@Data
public class UserExtendDto {
    private Long id;

    private boolean active;

    private String username;

    private boolean enabled;

    private PersonalDataDTO personalDataDTO;

    private String email;

    private Collection<Role> roles;

    @Data
    public class PersonalDataDTO {
        private String name;

        private String lastName;

        private Sex sex;

        private LocalDate dayOfBirth;

        private Long weightInKilo;

        private float highInCm;
    }
}
