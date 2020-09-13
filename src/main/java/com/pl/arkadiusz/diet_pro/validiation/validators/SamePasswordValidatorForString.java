package com.pl.arkadiusz.diet_pro.validiation.validators;

import com.pl.arkadiusz.diet_pro.dto.userDto.PasswordResetRequest;
import com.pl.arkadiusz.diet_pro.dto.userDto.UserRegisterDTO;
import com.pl.arkadiusz.diet_pro.services.ValidationService;
import com.pl.arkadiusz.diet_pro.validiation.constrains.SamePassword;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@Scope("prototype")
@Slf4j
public class SamePasswordValidatorForString implements ConstraintValidator<SamePassword, Object> {
    private ValidationService validationService;
    private String passwordToValid;
    private String rePasswordToValid;

    @Autowired
    public SamePasswordValidatorForString(ValidationService validationService) {
        this.validationService = validationService;
    }

    @Override
    public void initialize(SamePassword constraintAnnotation) {

    }

    @SneakyThrows
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        setPasswordsToValid(value);
        if (passwordToValid == null || rePasswordToValid == null) {
            return true;
        } else {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Password and RePassword are not the same!")
                    .addPropertyNode("rePassword").addConstraintViolation();
            return validationService.PasswordAndRePasswordAreTheSame(passwordToValid, rePasswordToValid);
        }
    }

    private void setPasswordsToValid(Object value) {
        if (value instanceof UserRegisterDTO) {
            passwordToValid = ((UserRegisterDTO) value).getPassword();
            rePasswordToValid = ((UserRegisterDTO) value).getRePassword();
        } else if (value instanceof PasswordResetRequest) {
            passwordToValid = ((PasswordResetRequest) value).getPassword();
            rePasswordToValid = ((PasswordResetRequest) value).getRePassword();
        }
    }
}

