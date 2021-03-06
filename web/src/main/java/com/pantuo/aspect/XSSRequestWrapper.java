package com.pantuo.aspect;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.pantuo.util.XSSReplace;

import freemarker.core._RegexBuiltins.replace_reBI;
import scala.remote;

public class XSSRequestWrapper extends HttpServletRequestWrapper {

	public XSSRequestWrapper(HttpServletRequest servletRequest) {
		super(servletRequest);
	}

	@Override
	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);

		if (values == null) {
			return null;
		}

		int count = values.length;
		String[] encodedValues = new String[count];
		for (int i = 0; i < count; i++) {
			encodedValues[i] = XSSReplace.stripXSS(values[i]);
		}

		return encodedValues;
	}

	@Override
	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		return XSSReplace.stripXSS(value);
	}

	@Override
	public String getHeader(String name) {
		return  super.getHeader(name);
		//return stripXSS(value);
	}

}