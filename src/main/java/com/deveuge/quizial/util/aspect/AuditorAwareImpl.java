package com.deveuge.quizial.util.aspect;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.deveuge.quizial.model.entity.User;
import com.deveuge.quizial.model.service.UserService;
import com.deveuge.quizial.util.SecurityUtils;

@Component
public class AuditorAwareImpl implements AuditorAware<User> {
	
	@Autowired
	private UserService userService;
	 
    @Override
    public Optional<User> getCurrentAuditor() {
        return Optional.of(userService.findByUsername(SecurityUtils.getUsername()));
    }

}