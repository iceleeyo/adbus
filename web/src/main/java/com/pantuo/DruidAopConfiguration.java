package com.pantuo;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration

@ImportResource("classpath:/druid_spring.xml")
public class DruidAopConfiguration {
 
}
