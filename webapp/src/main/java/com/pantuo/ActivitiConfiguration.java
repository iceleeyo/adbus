package com.pantuo;

import org.activiti.engine.*;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import scala.reflect.Ident;

import javax.sql.DataSource;

/**
 * Activiti BPM configuration
 *
 * @author tliu
 */

@Configuration
public class ActivitiConfiguration {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration() {
        SpringProcessEngineConfiguration conf = new SpringProcessEngineConfiguration();
        conf.setDataSource(dataSource);
        conf.setTransactionManager(transactionManager);
        conf.setDatabaseSchemaUpdate("true");
//        conf.setMailServerHost("localhost");
//        conf.setMailServerPort(25);
        conf.setJpaHandleTransaction(true);
        conf.setJpaCloseEntityManager(true);
        conf.setJobExecutorActivate(false);

//        ClassPathResource autoDeploy = new ClassPathResource("/com/pantuo/activiti/autodeployment/autodeploy.*.bpmn20.xml");
//        conf.setDeploymentResources(new Resource[] {autoDeploy});

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
