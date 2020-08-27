package com.pl.arkadiusz.diet_pro.validiation.constrains;


import com.pl.arkadiusz.diet_pro.validiation.validators.SamePasswordValidatorForString;
import lombok.ToString;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SamePasswordValidatorForString.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SamePassword {
    String message() default "Password and RePassword are not the same!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
