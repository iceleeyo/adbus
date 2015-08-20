package com.pantuo.util.cglib;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InterfaceMaker;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.codehaus.jackson.map.ObjectMapper;
import org.objectweb.asm.Type;

public class ProxyVoForPageOrJson {
	public final static String extractField = "get_";
	public final static String FORMATKEY = extractField + "%s";

	/**
	 * 写此方法的目的
	 * 避免在通过mybatis jpa hibernate 等dao工具查得pojo时,再数据转义成相应的对象到页面展示
	 * 
	 * 对象动态增加属性,可以在 对象转成json时 输出这些额外的属性,或是模版语言在取值时取到这些增加的字段
	 * @param daoObj
	 * @param extratFieldPair
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public static Object andFieldAndGetJavaBean(final Object daoObj, final Map<String, Object> extratFieldPair) {
		//Create a dynamice interface
		InterfaceMaker im = new InterfaceMaker();
		//Define a getter method for field, i.e., get_typeName.
		Type[] parameters = new Type[] {};
		for (Map.Entry<String, Object> entry : extratFieldPair.entrySet()) {
			if (entry.getValue() == null)
				throw new RuntimeException("if field value is null,please throw it");
			Signature signature = new Signature(entry.getKey(), Type.getType(entry.getValue().getClass()), parameters);
			im.add(signature, parameters);
		}
		//Finish creating the interface
		Class myInterface = im.create();

		Enhancer e = new Enhancer();
		e.setSuperclass(daoObj.getClass());
		e.setInterfaces(new Class[] { myInterface });
		e.setCallback(new MethodInterceptor() {
			//Interceptor Method
			public Object intercept(Object obj, Method m, Object[] args, MethodProxy proxy) throws Throwable {
				//check if begin _ ;add by impanxh
				return m.getName().startsWith(extractField) ? extratFieldPair.get(m.getName()) : proxy.invoke(daoObj,
						args);
			}
		});
		return e.create();
	}

	public static void main(String[] args) throws Exception {
		//模拟查的数据
		DataFromDb datafromDb = new DataFromDb();
		//增加一个typeName字段
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put(String.format(extractField + "%s", "typeName"), "视频");

		DataFromDb proxyObject = (DataFromDb) andFieldAndGetJavaBean(datafromDb, map);
		//输出json看字段是否输出
		ObjectMapper jsonTool = new ObjectMapper();
		System.out.println(jsonTool.writeValueAsString(proxyObject));
	}
}
