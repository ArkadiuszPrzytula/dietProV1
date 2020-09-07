package com.pl.arkadiusz.diet_pro.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AuthUserRequest {

    @NotNull @NotBlank @Size(min = 4, max = 12)
    private String username;

    @NotNull @NotBlank @Size(min = 4, max = 12)
    private String password;

}
