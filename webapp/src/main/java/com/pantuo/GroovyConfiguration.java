package com.pantuo;

import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericGroovyApplicationContext;
import org.springframework.scripting.groovy.GroovyScriptFactory;
import org.springframework.scripting.support.ScriptFactoryPostProcessor;

import com.pantuo.dynamic.service.GroovyFactory;
/**
 * 
 * <b><code>GroovyConfiguration</code></b>
 * <p>
 * 初始化groovy
 * </p>
 * <b>Creation Time:</b> 2015年12月6日 下午4:43:51
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
@Configuration
public class GroovyConfiguration {

	@Deprecated
	public boolean initialize() throws Exception {
		GenericGroovyApplicationContext ctx = new GenericGroovyApplicationContext();
		ctx.load("classpath:com/pantuo/dynamic/service/Test.groovy");
		AnnotatedBeanDefinitionReader configReader = new AnnotatedBeanDefinitionReader(ctx);
		configReader.register(GroovyConfiguration.class);
		ctx.refresh();
		return true;

	}

	//@Bean
	public GroovyScriptFactory groovyFactory2() {
		// GroovyScriptFactory factory= new GroovyScriptFactory();
		// factory.
		return null;
	}

	@Bean
	public GroovyFactory groovyFactory() {
		GroovyFactory factory = new GroovyFactory();
		factory.setDirectory("groovy", "/com/pantuo/service/impl");
		return factory;
	}

	@Bean
	public ScriptFactoryPostProcessor processor() {
		return new ScriptFactoryPostProcessor();
	}

}
