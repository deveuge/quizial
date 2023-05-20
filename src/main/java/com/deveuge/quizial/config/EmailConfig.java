package com.deveuge.quizial.config;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {
	
	private Properties properties;
	
	@PostConstruct
    public void init() throws IOException {
		properties = PropertiesLoaderUtils.loadAllProperties("init.properties");
    }
	
	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost(getProperty("host"));
	    mailSender.setPort(Integer.valueOf(getProperty("port")));
	    
	    mailSender.setUsername(getProperty("username"));
	    mailSender.setPassword(getProperty("password"));
	    
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", getProperty("protocol"));
	    props.put("mail.smtp.ssl.trust", getProperty("ssl.trust"));
	    props.put("mail.smtp.auth", getProperty("auth"));
	    props.put("mail.smtp.starttls.enable", getProperty("tls"));
	    props.put("mail.debug", getProperty("debug"));
	    
	    return mailSender;
	}
	
	private String getProperty(String property) {
		return properties.getProperty("email." + property);
	}
}
