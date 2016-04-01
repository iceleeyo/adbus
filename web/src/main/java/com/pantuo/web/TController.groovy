package com.pantuo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pantuo.dynamic.service.GroovySimpleInterface;
import com.pantuo.dynamic.service.GroovyFactory;

/**
 * Index controller
 *
 */
@Controller

public class TController {
	@RequestMapping(value = "/intro-w1")
	public String test() {
		GroovySimpleInterface foo2 = (GroovySimpleInterface) GroovyFactory.context.getBean("Foo");
		GroovySimpleInterface foo = GroovyFactory.context.getBean(GroovySimpleInterface.class);
		   foo.execute();
		return "intro/about-me";
	}
	@Autowired
	@Lazy
	GroovySimpleInterface foo;
}
