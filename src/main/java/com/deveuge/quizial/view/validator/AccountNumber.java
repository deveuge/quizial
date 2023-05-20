package com.deveuge.quizial.view.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.deveuge.quizial.view.validator.impl.AccountNumberValidator;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AccountNumberValidator.class)
public @interface AccountNumber {
	
    String message() default "{Ip.user.max.accounts}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
}