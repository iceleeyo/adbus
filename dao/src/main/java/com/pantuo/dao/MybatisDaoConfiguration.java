package com.pantuo.dao;



import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@ImportResource("classpath:/properties.xml")
@MapperScan("com.pantuo.mybatis.persistence")
public class MybatisDaoConfiguration {

/*	  private @Value("${jdbc.database}") String database;
	    private @Value("${jdbc.driverClassName}") String driverClassName;
	    private @Value("${jdbc.url}") String url;
	    private @Value("${jdbc.username}") String username;
	    private @Value("${jdbc.password}") String password;

	    @Bean
	    public DataSource dataSource() {
	        BasicDataSource ds = new org.apache.commons.dbcp.BasicDataSource();
	        ds.setDriverClassName(driverClassName);
	        ds.setUrl(url);
	        ds.setUsername(username);
	        ds.setPassword(password);
	        System.out.println("#####init-myibatis");
	        return ds;
	    }*/

	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean
	public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setTypeAliasesPackage("com.pantuo.mybatis.domain");
		return sessionFactory;
	}
}
