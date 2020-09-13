package com.pl.arkadiusz.diet_pro.dto.userDto;

import com.pl.arkadiusz.diet_pro.validiation.constrains.SamePassword;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data @SamePassword
public class PasswordResetRequest {

    @NotBlank
    @Size(min = 4, max = 12)
    private String password;

    @NotBlank @Size(min = 4, max = 12)
    private String rePassword;
}
