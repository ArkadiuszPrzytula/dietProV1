package com.pl.arkadiusz.diet_pro.validiation.constrains;


import com.pl.arkadiusz.diet_pro.validiation.validators.UniqueEmailValidatorForString;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueEmailValidatorForString.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "This e-mail is not available";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
