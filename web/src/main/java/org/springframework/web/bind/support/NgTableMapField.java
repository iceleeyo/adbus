package org.springframework.web.bind.support;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
/**
 * 
 * <b><code>NgTableMapField</code></b>
 * <p>
 * 该注解用于标注 解析ngtable中的map对象（从json字符串解析到Map）
 * </p>
 * <b>Creation Time:</b> 2016年1月15日 下午7:28:30
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */

@Target({METHOD, FIELD})
@Retention(RUNTIME)
@Documented
public @interface NgTableMapField {
}