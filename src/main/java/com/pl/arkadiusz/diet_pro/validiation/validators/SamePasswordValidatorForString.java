package com.pl.arkadiusz.diet_pro.validiation.validators;

import com.pl.arkadiusz.diet_pro.dto.UserRegisterDTO;
import com.pl.arkadiusz.diet_pro.services.ValidationService;
import com.pl.arkadiusz.diet_pro.validiation.constrains.SamePassword;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Component
@Scope("prototype")
@Slf4j
public class SamePasswordValidatorForString implements ConstraintValidator<SamePassword, UserRegisterDTO> {

    private ValidationService validationService;

    @Autowired
    public SamePasswordValidatorForString(ValidationService validationService) {
        this.validationService = validationService;
    }

    @Override
    public void initialize(SamePassword constraintAnnotation) {

    }

    @SneakyThrows
    @Override
    public boolean isValid(UserRegisterDTO userRegisterDTO, ConstraintValidatorContext constraintValidatorContext) {
        if (userRegisterDTO.getPassword() == null || userRegisterDTO.getRePassword() == null) {
            return true;
        } else {

            constraintValidatorContext.buildConstraintViolationWithTemplate("Password and RePassword are not the same!")
                    .addPropertyNode("rePassword").addConstraintViolation();
            return validationService.PasswordAndRePasswordAreTheSame(userRegisterDTO.getPassword(), userRegisterDTO.getRePassword());
            }

        }

    }

