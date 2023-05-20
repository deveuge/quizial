package com.deveuge.quizial.config.initializer;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
@PropertySource("classpath:quizial.properties")
public class WebInitializer implements WebApplicationInitializer {

	private Properties properties;

    @Override
    public void onStartup(ServletContext container) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation("com.deveuge.quizial.config");
        container.addListener(new ContextLoaderListener(context));
        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

		try {
			properties = PropertiesLoaderUtils.loadAllProperties("init.properties");
	        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
	        		properties.getProperty("upload.tmp.folder"), getUploadMaxSize(), getUploadMaxSize() * 2, getUploadMaxSize() / 2);
	        dispatcher.setMultipartConfig(multipartConfigElement);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
	
	private int getUploadMaxSize() {
		return Integer.valueOf(properties.getProperty("upload.max.megabytes")) * 1024 * 1024;
	}
}