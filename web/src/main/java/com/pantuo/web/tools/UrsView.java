package com.pantuo.web.tools;

public class UrsView {
	public String ftlName;
	public String controllerName;
	public String methodName;
	public String requestType;
	public String requestUrl;
	public Class<?>[] methodParmaTypes;

	public UrsView(String requestUrl, String requestType, String controllerName, String requestMethodName,
			String ftlName,

			Class<?>[] methodParmaTypes) {
		this.requestUrl = requestUrl;
		this.requestType = requestType;
		this.controllerName = controllerName;
		this.methodName = requestMethodName;
		this.methodParmaTypes = methodParmaTypes;
		this.ftlName = ftlName;

	}

	public String getControllerName() {
		return controllerName;
	}

	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public Class<?>[] getMethodParmaTypes() {
		return methodParmaTypes;
	}

	public void setMethodParmaTypes(Class<?>[] methodParmaTypes) {
		this.methodParmaTypes = methodParmaTypes;
	}

	public String getFtlName() {
		return ftlName;
	}

	public void setFtlName(String ftlName) {
		this.ftlName = ftlName;
	}
	
}
