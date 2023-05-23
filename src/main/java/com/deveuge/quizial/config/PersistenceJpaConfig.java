package com.deveuge.quizial.config;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.deveuge.quizial.model.entity.User;
import com.deveuge.quizial.util.aspect.AuditorAwareImpl;

@Configuration
@EnableJpaRepositories(basePackages = {"com.deveuge.quizial.model"})
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
public class PersistenceJpaConfig {
	
	private Properties properties;
	
	@PostConstruct
    public void init() throws IOException {
		properties = PropertiesLoaderUtils.loadAllProperties("init.properties");
    }
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setPackagesToScan("com.deveuge.quizial.model");
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setJpaProperties(additionalProperties());
		return em;
	}
	
	@Bean
    AuditorAware<User> auditorProvider() {
        return new AuditorAwareImpl();
    }

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(properties.getProperty("database.driver"));
        dataSource.setUrl(properties.getProperty("database.url"));
        dataSource.setUsername(properties.getProperty("database.username"));
        dataSource.setPassword(properties.getProperty("database.password"));
        return dataSource;
    }
    
	private Properties additionalProperties() {
		Properties additionalProperties = new Properties();
		additionalProperties.setProperty("hibernate.hbm2ddl.auto", properties.getProperty("database.ddl"));
		additionalProperties.setProperty("hibernate.hbm2ddl.schema_filter_provider", properties.getProperty("database.ddl.schema_filter"));
		additionalProperties.setProperty("hibernate.show_sql", properties.getProperty("database.log"));
		return additionalProperties;
	}
	
}