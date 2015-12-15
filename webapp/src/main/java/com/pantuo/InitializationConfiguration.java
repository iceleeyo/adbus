package com.pantuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Lazy;

import com.pantuo.service.ContractInitService;
import com.pantuo.service.DataInitializationService;

/**
 * Initialization configuration
 *
 * @author tliu
 */

@Configuration
@ImportResource("classpath:/properties.xml")
public class InitializationConfiguration {
	@Autowired
	@Lazy
	DataInitializationService initService;
	
	
	@Autowired
	ContractInitService contractInitService;
	
	/**
	 * 初始化线路对应的车辆数量
	 */
	/**
	 * 初始化线路对来今与未来1，2，3相应今天的上刊量
	 */

	@Bean
	public boolean initialize() throws Exception {
		//contractInitService.intialize();
//		lineCarsCount.countCars();
//		lineOnlineCount.countCars();
		initService.intialize();
		
		return true;
	}
}
