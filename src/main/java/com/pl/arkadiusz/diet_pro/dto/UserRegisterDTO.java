package com.pl.arkadiusz.diet_pro.dto;

import com.pl.arkadiusz.diet_pro.validiation.constrains.SamePassword;
import com.pl.arkadiusz.diet_pro.validiation.constrains.UniqueEmail;
import com.pl.arkadiusz.diet_pro.validiation.constrains.UniqueUsername;
import lombok.Data;

import javax.validation.constraints.*;

@Data @SamePassword
public class UserRegisterDTO {

    @NotBlank
    @Size(min = 3, max = 50) @UniqueUsername
    private String username;
    @NotBlank @Email
    @UniqueEmail
    private String email;
    @NotBlank @Size(min = 4, max = 12)
    private String password;
     @NotBlank @Size(min = 4, max = 12)
    private String rePassword;

}
