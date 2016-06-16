package com.pantuo;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by tliu on 9/3/14.
 */
@Configuration
@ImportResource("classpath:/properties.xml")
@ComponentScan(basePackages = {"com.pantuo.service", "com.pantuo.aspect"})
public class TestServiceConfiguration {
}
