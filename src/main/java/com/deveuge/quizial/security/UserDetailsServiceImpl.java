package com.deveuge.quizial.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.deveuge.quizial.model.entity.User;
import com.deveuge.quizial.model.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
 
    @Autowired
    private UserService userService;
 
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userService.findByUsernameOrEmail(username, username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserPrincipalImpl(user);
    }
}