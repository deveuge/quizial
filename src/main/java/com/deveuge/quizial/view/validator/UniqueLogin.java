package com.deveuge.quizial.view.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.deveuge.quizial.view.validator.impl.UniqueLoginValidator;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueLoginValidator.class)
public @interface UniqueLogin {
	
    String message() default "{Repeated.user.username}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
}