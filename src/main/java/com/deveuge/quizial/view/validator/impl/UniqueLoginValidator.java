package com.deveuge.quizial.view.validator.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.deveuge.quizial.model.service.UserService;
import com.deveuge.quizial.view.validator.UniqueLogin;

public class UniqueLoginValidator implements ConstraintValidator<UniqueLogin, String> {
 
    private UserService userService;
 
    public UniqueLoginValidator(UserService userService) {
        this.userService = userService;
    }
 
    @Override
	public void initialize(UniqueLogin constraint) {
    }
 
    @Override
	public boolean isValid(String login, ConstraintValidatorContext context) {
        return login != null && userService.findByUsername(login) == null;
    }
 
}