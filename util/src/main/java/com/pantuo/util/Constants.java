package com.pantuo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Constants {
	
	public static final String BUSSINESS_ERROR = "意外情况:可能是订单属主不对!";

	public static final String TASK_NOT_EXIT = "要处理的任务不存在!";
	public static final String ORDER_NOT_EXIT = "要处理的订单不存在!";
	public static final String CREATEID_WRONG = "订单创建者信息不匹配!";

	public static final String NOT_SUPPORT = "%s 订单已支付暂不支付关闭!";
	public static final String CLOSEORDER_SUCCESS = "%s 订单已关闭,您可以到[已完成订单]查询该订单!";

	public static final String FINAL_STATE = "已完成";
	public static final String CLOSED_STATE = "已关闭";

	public static String SESSION_U_KEY = "SESSION_U_KEY";

	public static String FILE_UPLOAD_DIR = "/WEB-INF/upload_temp/";
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
