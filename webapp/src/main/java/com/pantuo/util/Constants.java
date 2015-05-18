package com.pantuo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;




public class Constants {
	
	public static String SESSION_U_KEY="SESSION_U_KEY";
	
	public static String FILE_UPLOAD_DIR="/WEB-INF/upload_temp/";
	private static Properties normalProperties = null;

	public static void init(String contextPath) {

		try {
			normalProperties = loadProperties("classpath:dev.webapp.properties");
		} catch (IOException e) {

		}
	}
	private static Properties loadProperties(String contextPath) throws IOException {
		Properties props = new Properties();
		FileInputStream fis = new FileInputStream(contextPath);
		props.load(fis);
		fis.close();
		return props;
	}
	public static String getValue(String name) {
		return normalProperties.getProperty(name);
	}
}
