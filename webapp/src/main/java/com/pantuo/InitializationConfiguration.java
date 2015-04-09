package com.pantuo;

import com.pantuo.service.DataInitializationService;
import org.activiti.engine.*;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Initialization configuration
 *
 * @author tliu
 */

@Configuration
@ImportResource("classpath:/properties.xml")
public class InitializationConfiguration {
    @Autowired
    DataInitializationService initService;

    @Bean
    public boolean initialize() throws Exception {
        initService.intialize();
        return true;
    }
}
