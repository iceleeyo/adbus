package com.pantuo.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * <b><code>OpenApiV1</code></b>
 * <p>
 *-开放api权限控制 
 * </p>
 * <b>Creation Time:</b> 2016年3月10日 下午3:35:09
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpenApiV1 {
}