package com.deveuge.quizial.view.validator.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.deveuge.quizial.model.service.UserService;
import com.deveuge.quizial.util.Constants;
import com.deveuge.quizial.view.validator.AccountNumber;

public class AccountNumberValidator implements ConstraintValidator<AccountNumber, String> {
 
    private UserService userService;
 
    public AccountNumberValidator(UserService userService) {
        this.userService = userService;
    }
 
    @Override
	public void initialize(AccountNumber constraint) {
    }
 
    @Override
	public boolean isValid(String ip, ConstraintValidatorContext context) {
        return ip != null && userService.countByIp(ip) < Constants.MAX_ACCOUNTS_BY_IP;
    }
 
}