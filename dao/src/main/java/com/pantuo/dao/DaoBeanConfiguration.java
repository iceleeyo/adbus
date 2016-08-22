package com.pantuo.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallFilter;

import javax.sql.DataSource;

import java.sql.SQLException;

/**
 * @author tliu
 */

@Configuration
@EnableJpaRepositories
//@PropertySource("classpath:jdbc.properties")
@ImportResource("classpath:/properties.xml")
@ComponentScan(basePackages = {"com.pantuo.dao"})
public class DaoBeanConfiguration {
	
	
	private static final Logger log = LoggerFactory.getLogger(DaoBeanConfiguration.class);
	
	private @Value("${jdbc.database}") String database;
	private @Value("${jdbc.driverClassName}") String driverClassName;
	private @Value("${jdbc.url}") String url;
	private @Value("${jdbc.username}") String username;
	private @Value("${jdbc.password}") String password;

	private @Value("${jdbc.maxActive}") Integer maxActive;
	private @Value("${jdbc.initialSize}") Integer initialSize;
	private @Value("${jdbc.maxWait}") Integer maxWait;
	private @Value("${jdbc.minIdle}") Integer minIdle;
	private @Value("${jdbc.timeBetweenEvictionRunsMillis}") Integer timeBetweenEvictionRunsMillis;
	private @Value("${jdbc.minEvictableIdleTimeMillis}") Long minEvictableIdleTimeMillis;
	private @Value("${jdbc.filters}") String filters;
	private @Value("${jdbc.multiStatementAllow}") String multiStatementAllow;

	@Bean
	public DataSource druidDataSource() {
		DruidDataSource druidDataSource = new DruidDataSource();
		druidDataSource.setDriverClassName(driverClassName);
		druidDataSource.setUrl(url);
		druidDataSource.setUsername(username);
		druidDataSource.setPassword(password);
		//--
		druidDataSource.setMaxActive(maxActive);
		druidDataSource.setInitialSize(initialSize);
		druidDataSource.setMaxWait(maxWait);
		druidDataSource.setMinIdle(minIdle);
		druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);

		if (StringUtils.isNoneBlank(filters)) {
			try {
				druidDataSource.setFilters(filters);//
				for (Filter f : druidDataSource.getProxyFilters()) {
					if (f instanceof WallFilter) {
						WallFilter wall = (WallFilter) f;
						com.alibaba.druid.wall.WallConfig config = new com.alibaba.druid.wall.WallConfig();
						config.setMultiStatementAllow(BooleanUtils.toBoolean(multiStatementAllow));
						wall.setConfig(config);
					}
				}

			} catch (SQLException e) {
				log.error("druid set filter error!", e);
			}
		}
		
		return druidDataSource;
	}

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setDataSource(dataSource);
        lef.setJpaVendorAdapter(jpaVendorAdapter);
        lef.getJpaPropertyMap().put("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");
//        lef.setPersistenceUnitName("adbus-pu");
        lef.setPackagesToScan("com.pantuo.dao");
        return lef;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(false);
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        hibernateJpaVendorAdapter.setDatabase(Database.valueOf(database));
        return hibernateJpaVendorAdapter;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

}