package com.pantuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.pantuo.service.DataInitializationService;
import com.pantuo.simulate.LineCarsCount;

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

    @Bean
    public boolean initialize() throws Exception {
    	lineCarsCount.countCars();
        initService.intialize();
        return true;
    }
}
