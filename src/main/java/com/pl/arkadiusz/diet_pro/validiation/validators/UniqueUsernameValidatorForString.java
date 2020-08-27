package com.pl.arkadiusz.diet_pro.validiation.validators;

import com.pl.arkadiusz.diet_pro.services.ValidationService;
import com.pl.arkadiusz.diet_pro.validiation.constrains.UniqueUsername;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@Scope("prototype")
@Slf4j
public class UniqueUsernameValidatorForString implements ConstraintValidator<UniqueUsername, String> {

    private ValidationService validationService;

    @Autowired
    public UniqueUsernameValidatorForString(ValidationService validationService) {
        this.validationService = validationService;
    }


    public void initialize(UniqueUsername constraint) {
    }

    public boolean isValid(String username, ConstraintValidatorContext context) {
        log.debug("Validating unique username: {}", username);
        boolean unique = validationService.isUniqueUsername(username);
        log.debug("Is username '{}' unique? {}", username, unique);
        return unique;
    }
}

