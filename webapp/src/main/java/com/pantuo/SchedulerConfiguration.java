package com.pantuo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Spring Scheduler configuration
 *
 * @author impanxh
 */
@Configuration
@EnableScheduling
@ComponentScan(basePackages = { "com.pantuo.simulate" })
public class SchedulerConfiguration implements SchedulingConfigurer {

	private static ScheduledTaskRegistrar taskRegistrar;

	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskScheduler());
		setTaskRegistrar(taskRegistrar);
	}

	@Bean(destroyMethod = "shutdown")
	public Executor taskScheduler() {
		return Executors.newScheduledThreadPool(2);
	}

	@Bean
	public ScheduledTaskRegistrar getRegistrat() {
		return getTaskRegistrar();
	}

	public static ScheduledTaskRegistrar getTaskRegistrar() {
		return taskRegistrar;
	}

	public static void setTaskRegistrar(ScheduledTaskRegistrar taskRegistrar) {
		SchedulerConfiguration.taskRegistrar = taskRegistrar;
	}
}
