package com.pantuo.util;

import java.io.IOException;
import java.io.Writer;
import org.apache.log4j.Logger;
import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
/**
 * 
 * <b><code>FreemarkerExceptionHandler</code></b>
 * <p>
 * 处理freemarker 异常可以直接返回一个提示说是 信息数据加载异常
 * </p>
 * <b>Creation Time:</b> 2015年7月6日 上午10:16:54
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
public class FreemarkerExceptionHandler implements TemplateExceptionHandler {
	private static final Logger log = Logger.getLogger(FreemarkerExceptionHandler.class);

	public void handleTemplateException(TemplateException te, Environment env, Writer out) throws TemplateException {
		try {
			out.write("[Error: " + te.getMessage() + "]");
			log.warn("[Freemarker Error: " + te.getMessage() + "]");
		} catch (IOException e) {
			log.warn(e.getMessage());
			throw new TemplateException("Failed to print error message. Cause: " + e, env);
		}
	}
}