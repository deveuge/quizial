package com.deveuge.quizial.view.validator.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.deveuge.quizial.model.service.UserService;
import com.deveuge.quizial.view.validator.UniqueEmail;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
 
    private UserService userService;
 
    public UniqueEmailValidator(UserService userService) {
        this.userService = userService;
    }
 
    @Override
	public void initialize(UniqueEmail constraint) {
    }
 
    @Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && userService.findByEmail(email) == null;
    }
 
}