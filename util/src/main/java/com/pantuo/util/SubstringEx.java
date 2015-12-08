package com.pantuo.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;
/**
 * 
 * <b><code>SubstringEx</code></b>
 * <p>
 * 增加 ${substring(description, 0, 80)} 字符串截取功能
 * </p>
 * <b>Creation Time:</b> 2015年5月21日 上午11:39:50
 * @author impanxh@gmail.com
 * @since pantuotech 1.0-SNAPSHOT
 */
public class SubstringEx implements TemplateMethodModelEx {

	//   @Override
	public Object exec(List args) throws TemplateModelException {
		int argCount = args.size(), left = 0, right = 0;
		String s = "";
		if (argCount != 3) {
			throw new TemplateModelException("Error: Expecting 1 string and 2 numerical arguments here");
		}
		try {
			TemplateScalarModel tsm = (TemplateScalarModel) args.get(0);
			s = tsm.getAsString();
		} catch (ClassCastException cce) {
			String mess = "Error: Expecting numerical argument here";
			throw new TemplateModelException(mess);
		}

		try {
			TemplateNumberModel tnm = (TemplateNumberModel) args.get(1);
			left = tnm.getAsNumber().intValue();

			tnm = (TemplateNumberModel) args.get(2);
			right = tnm.getAsNumber().intValue();

		} catch (ClassCastException cce) {
			String mess = "Error: Expecting numerical argument here";
			throw new TemplateModelException(mess);
		}
		return new SimpleScalar(subStr(s, left, right));
	}

	private String subStr(String s, int start, int end) {
		return end < StringUtils.length(s) ? StringUtils.substring(s, start, end) + "..." : StringUtils.substring(s,
				start, end);
	}

	private String getSubstring(String s, int start, int end) {
		int[] codePoints = new int[end - start];
		int length = s.length();
		int i = 0;
		for (int offset = 0; offset < length && i < codePoints.length;) {
			int codepoint = s.codePointAt(offset);
			if (offset >= start) {
				codePoints[i] = codepoint;
				i++;
			}
			offset += Character.charCount(codepoint);
		}
		return new String(codePoints, 0, i);
	}
}
