package com.pantuo.util;

import java.util.List;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

/**
 * 
 * <b><code>hidname</code></b>
 * <p>
 * ${hidname(JJJJJJJJJ)}
 * </p>
 * <b>Creation Time:</b> 2015年5月21日 上午11:39:50
 * @author impanxh@gmail.com
 * @since pantuotech 1.0-SNAPSHOT
 */
public class HiddleUserNameEx implements TemplateMethodModelEx {
	//   @Override
	public Object exec(List args) throws TemplateModelException {
		String s = "";
		try {
			TemplateScalarModel tsm = (TemplateScalarModel) args.get(0);
			s = tsm.getAsString();
		} catch (ClassCastException cce) {
			String mess = "Error: Expecting numerical argument here";
			throw new TemplateModelException(mess);
		}

		s = s.length() > 4 ? "****" + s.substring(s.length() - 4, s.length()) : "****"
				+ s.substring(s.length() - 1, s.length());
		return new SimpleScalar(s);
	}

}
