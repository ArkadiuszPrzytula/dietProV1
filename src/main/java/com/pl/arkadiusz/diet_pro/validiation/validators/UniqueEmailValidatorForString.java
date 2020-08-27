package com.pl.arkadiusz.diet_pro.validiation.validators;

import com.pl.arkadiusz.diet_pro.services.ValidationService;
import com.pl.arkadiusz.diet_pro.validiation.constrains.UniqueEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@Scope("prototype")
@Slf4j
public class UniqueEmailValidatorForString implements ConstraintValidator<UniqueEmail, String> {

    private ValidationService validationService;

    @Autowired
    public UniqueEmailValidatorForString(ValidationService validationService) {
        this.validationService = validationService;
    }

    public void initialize(UniqueEmail constraint) {
    }

    public boolean isValid(String email, ConstraintValidatorContext context) {
        log.debug("Validating unique email: {}", email);
        boolean unique = validationService.isUniqueEmail(email);
        log.debug("Is email '{}' unique? {}", email, unique);
        return unique;
    }
}
