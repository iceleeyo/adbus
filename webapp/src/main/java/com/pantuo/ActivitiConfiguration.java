package com.pantuo;

import java.io.IOException;

import org.activiti.engine.*;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.PathMatcher;

import scala.reflect.Ident;

import javax.sql.DataSource;

/**
 * Activiti BPM configuration
 *
 * @author tliu
 */

@Configuration
@ImportResource("classpath:/properties.xml")
public class ActivitiConfiguration {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Value("${activiti.db.schema.update}")
    private String schemaUpdate;
    
    @Value("${activiti.auto.deploy.path}")
    private String autoDeployPath;
    
    @Value("${activiti.smtp.host}")
    private String smtpHost;
    
    @Value("${activiti.smtp.port}")
    private int smtpPort;

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration() throws IOException {
        SpringProcessEngineConfiguration conf = new SpringProcessEngineConfiguration();
        conf.setDataSource(dataSource);
        conf.setTransactionManager(transactionManager);
        conf.setDatabaseSchemaUpdate(schemaUpdate);
        conf.setMailServerHost(smtpHost);
        conf.setMailServerPort(smtpPort);
//        conf.setJpaPersistenceUnitName("adbus-pu");
        conf.setJpaHandleTransaction(true);
        conf.setJpaCloseEntityManager(true);
        conf.setJobExecutorActivate(false);

        PathMatchingResourcePatternResolver r = new PathMatchingResourcePatternResolver();
        conf.setDeploymentResources(r.getResources(autoDeployPath));

        return conf;
    }

    @Bean
    ProcessEngine processEngine(SpringProcessEngineConfiguration conf) throws Exception {
        ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
        factoryBean.setProcessEngineConfiguration(conf);
        return factoryBean.getObject();
    }

    @Bean
    RepositoryService repositoryService (ProcessEngine engine) {
        return engine.getRepositoryService();
    }

    @Bean
    RuntimeService runtimeService(ProcessEngine engine) {
        return engine.getRuntimeService();
    }

    @Bean
    TaskService taskService(ProcessEngine engine) {
        return engine.getTaskService();
    }

    @Bean
    HistoryService historyService(ProcessEngine engine) {
        return engine.getHistoryService();
    }

    @Bean
    ManagementService managementService(ProcessEngine engine) {
        return engine.getManagementService();
    }

    @Bean
    IdentityService identityService(ProcessEngine engine) {
        return engine.getIdentityService();
    }
}
