package com.pantuo.dynamic.service;

/**
 * 
 * <b><code>Application</code></b>
 * <p>
 * spring boot 注册方式
 * </p>
 * <b>Creation Time:</b> 2015年12月6日 下午3:29:11
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
///@SpringBootApplication
public class Application {

	{/*
		   public static void main(String[] args) {
		       SpringApplication app = new SpringApplication(Application)
		       // Add GroovyScriptFactory after Application is prepared...
		       app.addListeners(new ApplicationListener<ApplicationPreparedEvent>() {
		           void onApplicationEvent(ApplicationPreparedEvent event) {
		               def registry = (BeanDefinitionRegistry) event.applicationContext.autowireCapableBeanFactory
		               def bd = BeanDefinitionBuilder.genericBeanDefinition(GroovyScriptFactory)
		                       .addConstructorArgValue("file:/C:/someDir/src/main/static/FoobarService.groovy")
		                       .getBeanDefinition()
		               bd.setAttribute(ScriptFactoryPostProcessor.REFRESH_CHECK_DELAY_ATTRIBUTE, 1000)
		               registry.registerBeanDefinition('foobar0', bd)
		           }
		       })
		       app.run(args)
		   }
		   @Bean
		   ScriptFactoryPostProcessor scriptFactory() {
		       new ScriptFactoryPostProcessor();
		   }
		   
		   @Bean
		   GroovyScriptFactory scriptFactory() {
		   	GenericGroovyApplicationContext
		       new GroovyScriptFactory();
		   }
		*/

	}

}