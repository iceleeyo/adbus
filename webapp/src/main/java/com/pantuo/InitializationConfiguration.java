package com.pantuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.pantuo.service.DataInitializationService;
import com.pantuo.simulate.LineCarsCount;
import com.pantuo.simulate.LineOnlineCount;

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
	/**
	 * 初始化线路对应的车辆数量
	 */
	@Autowired
	LineCarsCount lineCarsCount;
	/**
	 * 初始化线路对来今与未来1，2，3相应今天的上刊量
	 */
	@Autowired
	LineOnlineCount lineOnlineCount;

	@Bean
	public boolean initialize() throws Exception {
		lineCarsCount.countCars();
		lineOnlineCount.countCars();
		initService.intialize();
		return true;
	}
}
