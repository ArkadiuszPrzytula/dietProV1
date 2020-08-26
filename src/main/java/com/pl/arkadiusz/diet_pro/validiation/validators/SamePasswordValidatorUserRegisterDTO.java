package com.pl.arkadiusz.diet_pro.validiation.validators;

import com.pl.arkadiusz.diet_pro.dto.UserRegisterDTO;
import com.pl.arkadiusz.diet_pro.validiation.constrains.SamePassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SamePasswordValidatorUserRegisterDTO implements ConstraintValidator<SamePassword, UserRegisterDTO> {

    @Override
    public void initialize(SamePassword constraintAnnotation) {

    }

    @Override
    public boolean isValid(UserRegisterDTO userRegisterDTO, ConstraintValidatorContext constraintValidatorContext) {
        if (userRegisterDTO.getPassword() == null || userRegisterDTO.getRePassword() == null) {
            return true;
        } else {
            boolean valid = userRegisterDTO.getPassword().equals(userRegisterDTO.getRePassword());
            if (!valid) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                ;
                constraintValidatorContext.buildConstraintViolationWithTemplate("SamePassword.userRegisterDTO.rePassword")
                        .addPropertyNode("rePassword").addConstraintViolation();
            }
            return valid;
        }

    }
}
