package org.springframework.web.bind.support;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * <b><code>NgTable</code></b>
 * <p>
 * 该注解用于绑定请求参数（JSON字符串）
 * </p>
 * <b>Creation Time:</b> 2016年1月15日 下午7:28:42
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NgTable {

	/**
	 * 用于绑定的请求参数名字
	 */
	String value() default "";
	
	/**
	 * 是否必须，默认是
	 */
	boolean required() default true;

}