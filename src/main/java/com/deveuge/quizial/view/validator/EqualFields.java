package com.deveuge.quizial.view.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.deveuge.quizial.view.validator.impl.EqualFieldsValidator;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EqualFieldsValidator.class})
public @interface EqualFields {
 
    String message() default "{NotMatch.user.password}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
 
    String baseField();
    String matchField();
    
}