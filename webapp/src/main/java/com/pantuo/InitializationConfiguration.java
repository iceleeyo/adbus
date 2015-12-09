package com.pantuo;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Lazy;

import com.pantuo.service.ContractInitService;
import com.pantuo.service.DataInitializationService;
import com.pantuo.simulate.CountMonth;
import com.pantuo.simulate.LineCarsCount;
import com.pantuo.simulate.LineOnlineCount;
import com.pantuo.simulate.QueryBusInfo;

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
	@Autowired
	LineCarsCount lineCarsCount;
	/**
	 * 初始化线路对来今与未来1，2，3相应今天的上刊量
	 */
	@Autowired
	LineOnlineCount lineOnlineCount;
	@Autowired
	QueryBusInfo qb;
	@Autowired
	CountMonth countMonth;

	@Bean
	public boolean initialize() throws Exception {
		//contractInitService.intialize();
//		lineCarsCount.countCars();
//		lineOnlineCount.countCars();
		qb.countCars();
		qb.countCars2();
		initService.intialize();
		countMonth.act(new Date());
		
		return true;
	}
}
