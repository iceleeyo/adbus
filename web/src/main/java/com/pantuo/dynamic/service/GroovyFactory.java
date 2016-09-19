package com.pantuo.dynamic.service;

import java.io.File;
import java.io.FileFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class GroovyFactory implements ApplicationContextAware {
	private static Logger logger = LoggerFactory.getLogger(GroovyFactory.class);
	private String[] directory;

	public void setDirectory(String... directory) {
		this.directory = directory;
	}

	@Deprecated
	public void setApplicationContext_(ApplicationContext context) throws BeansException {
		// 只有这个对象才能注册b ean到spring容器  
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();

		// 因为spring会自动将xml解析成BeanDefinition对象然后进行实例化，这里我们没有用xml，所以自己定义BeanDefinition  
		// 这些信息跟spring配置文件的方式差不多，只不过有些东西lang:groovy标签帮我们完成了  
		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setBeanClassName("org.springframework.scripting.groovy.GroovyScriptFactory");
		final String refreshCheckDelay = "org.springframework.scripting.support.ScriptFactoryPostProcessor.refreshCheckDelay";
		final String language = "org.springframework.scripting.support.ScriptFactoryPostProcessor.language";
		// 刷新时间  
		bd.setAttribute(refreshCheckDelay, 500);
		// 语言脚本  
		bd.setAttribute(language, "groovy");
		// 文件目录  
		bd.getConstructorArgumentValues().addIndexedArgumentValue(0, "classpath:groovy/FooImpl.groovy");
		// 注册到spring容器  
		beanFactory.registerBeanDefinition("Foo", bd);
		//  beanFactory.registerBeanDefinition(Foo.class.getName(), bd);  
	}

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		// 只有这个对象才能注册bean到spring容器
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();

		final String refreshCheckDelay = "org.springframework.scripting.support.ScriptFactoryPostProcessor.refreshCheckDelay";
		final String language = "org.springframework.scripting.support.ScriptFactoryPostProcessor.language";
		File root = new File(Thread.currentThread().getContextClassLoader().getResource(".").getFile());

		for (String groovyPath : directory) {
			String realDirectory = root + File.separator + groovyPath;
			logger.info("groovy path:{} ",realDirectory);
			File[] files = new File(realDirectory).listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.getName().endsWith(".groovy") ? true : false;
				}
			});
			for (File file : files) {
				GenericBeanDefinition bd = new GenericBeanDefinition();
				bd.setBeanClassName("org.springframework.scripting.groovy.GroovyScriptFactory");
				// 刷新时间
				bd.setAttribute(refreshCheckDelay, 500);
				// 语言脚本
				bd.setAttribute(language, "groovy");
				// 文件目录
				String groovyFile = "classpath:" + groovyPath + File.separator + file.getName(); //file.getPath().replace(root.getPath(), "");
				String beanName = file.getName().replace(".groovy", "");
				bd.getConstructorArgumentValues().addIndexedArgumentValue(0, groovyFile);
				// 注册到spring容器
				beanFactory.registerBeanDefinition(beanName, bd);
				logger.info("register bean :{} file:{}", beanName, groovyFile);
			}
		}

	}

}
