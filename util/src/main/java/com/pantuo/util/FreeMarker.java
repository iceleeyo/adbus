package com.pantuo.util;

import java.io.File;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateModel;

/** 
* @author  
*/
public class FreeMarker {
	private Configuration cfg; //模版配置对象 

	public FreeMarker init(String templatesPath) throws Exception {
		cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(new File(templatesPath));
		cfg.setEncoding(Locale.CHINA, "UTF-8");
		return this;
	}
	
	public FreeMarker setSharedVariable(String name, TemplateModel tm) throws Exception {
		cfg.setSharedVariable(name, tm);
		return this;
	}

	public void process(Map map, String ftl, Writer out) throws Exception {
		//创建模版对象 
		Template t = cfg.getTemplate(ftl);
		//在模版上执行插值操作，并输出到制定的输出流中 
		t.process(map, out);
	}

}