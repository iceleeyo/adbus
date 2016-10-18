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
 * 增加 ${changeMoney(price)} 字符串截取功能
 * </p>
 * <b>Creation Time:</b> 2015年5月21日 上午11:39:50
 * @author impanxh@gmail.com
 * @since pantuotech 1.0-SNAPSHOT
 */
public class ChangeMoney implements TemplateMethodModelEx {

	public Object exec(List args) throws TemplateModelException {
		double s = 0;
		try {
			TemplateNumberModel tnm = (TemplateNumberModel) args.get(0);
			s = tnm.getAsNumber().doubleValue();
		} catch (ClassCastException cce) {
			String mess = "Error: Expecting numerical argument here";
			throw new TemplateModelException(mess);
		}

		return new SimpleScalar(change(s));
	}


	 public  String change(double v) {  
    	 final String UNIT = "万千佰拾亿千佰拾万千佰拾元角分";  
    	 final String DIGIT = "零壹贰叁肆伍陆柒捌玖";  
    	  final double MAX_VALUE = 9999999999999.99D;  
      if (v < 0 || v > MAX_VALUE){  
          return "参数非法!";  
      }  
      long l = Math.round(v * 100);  
      if (l == 0){  
          return "零元整";  
      }  
      String strValue = l + "";  
      // i用来控制数  
      int i = 0;  
      // j用来控制单位  
      int j = UNIT.length() - strValue.length();  
      String rs = "";  
      boolean isZero = false;  
      for (; i < strValue.length(); i++, j++) {  
       char ch = strValue.charAt(i);  
       if (ch == '0') {  
        isZero = true;  
        if (UNIT.charAt(j) == '亿' || UNIT.charAt(j) == '万' || UNIT.charAt(j) == '元') {  
         rs = rs + UNIT.charAt(j);  
         isZero = false;  
        }  
       } else {  
        if (isZero) {  
         rs = rs + "零";  
         isZero = false;  
        }  
        rs = rs + DIGIT.charAt(ch - '0') + UNIT.charAt(j);  
       }  
      }  
      if (!rs.endsWith("分")) {  
       rs = rs + "整";  
      }  
      rs = rs.replaceAll("亿万", "亿");  
      return rs;  
     }  
}
