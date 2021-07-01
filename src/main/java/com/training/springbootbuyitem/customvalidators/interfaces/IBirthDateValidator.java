package com.training.springbootbuyitem.customvalidators.interfaces;

import com.training.springbootbuyitem.customvalidators.BirthDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target( { ElementType.METHOD, ElementType.FIELD })
@Constraint(validatedBy = BirthDateValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface IBirthDateValidator {
    String message() default "Default Message";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}