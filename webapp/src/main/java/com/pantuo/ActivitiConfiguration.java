package com.pantuo;

import java.io.IOException;

import org.activiti.engine.*;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringExpressionManager;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
@ComponentScan(basePackages = { "com.pantuo.activiti" })
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
    
    @Value("${activiti.smtp.mailServerUsername}")
    private String mailServerUsername;
    
    @Value("${activiti.smtp.mailServerPassword}")
    private String mailServerPassword;

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration(ApplicationContext context) throws IOException {
        SpringProcessEngineConfiguration conf = new SpringProcessEngineConfiguration();
        conf.setDataSource(dataSource);
        conf.setTransactionManager(transactionManager);
        conf.setDatabaseSchemaUpdate(schemaUpdate);
        conf.setMailServerHost(smtpHost);
        conf.setMailServerPort(smtpPort);
        conf.setMailServerUsername(mailServerUsername);
        conf.setMailServerPassword(mailServerPassword);
//        conf.setJpaPersistenceUnitName("adbus-pu");
        conf.setJpaHandleTransaction(true);
        conf.setJpaCloseEntityManager(true);
        
        conf.setJobExecutorActivate(true);
        conf.setAsyncExecutorActivate(true);
        conf.setLabelFontName("宋体");
        conf.setActivityFontName("宋体");

        //expose spring beans to activiti
        conf.setExpressionManager(new SpringExpressionManager(context, null));
        conf.setApplicationContext(context);

        PathMatchingResourcePatternResolver r = new PathMatchingResourcePatternResolver();
        conf.setDeploymentResources(r.getResources(autoDeployPath));
        conf.buildProcessEngine();
        return conf;
    }
    
    @Bean
    ProcessEngineFactoryBean processEngineFactoryBean(SpringProcessEngineConfiguration conf, ApplicationContext context) {
    	ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
    	factoryBean.setProcessEngineConfiguration(conf);
        factoryBean.setApplicationContext(context);
    	return factoryBean;
    }
    /*
    @Bean
    ProcessEngine processEngine(ProcessEngineFactoryBean factoryBean) throws Exception {
        return factoryBean.getObject();
    }*/

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
