package com.deveuge.quizial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.deveuge.quizial.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.sessionManagement()
    		.invalidSessionUrl("/")
    		.maximumSessions(2)
    		.expiredUrl("/");
    	
        http.authorizeRequests()
    		.antMatchers("/admin").hasRole("ADMIN")
        	.antMatchers("/legal/*", "/q/*").permitAll()
        	.anyRequest().authenticated()
        	.and().formLogin().loginPage("/").permitAll().usernameParameter("li-username").passwordParameter("li-password")
        	.and().logout().permitAll().invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessUrl("/")
        	.and().rememberMe().key("remember-me-token").rememberMeParameter("remember-me")
        	.and().httpBasic();
    }
    
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/uploads/**");
	}
	
	@Override
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
