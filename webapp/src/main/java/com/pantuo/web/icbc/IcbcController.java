package com.pantuo.web.icbc;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.service.impl.IcbcServiceImpl;

@Controller
public class IcbcController {
	private static Logger log = LoggerFactory.getLogger(IcbcController.class);
	static AtomicInteger s = new AtomicInteger();

	@Autowired
	IcbcServiceImpl icbcService;

	@RequestMapping(value = "/icbcCallBack")
	@ResponseBody
	public String icbcCallBack(Model model, HttpServletRequest request) {
		try {
			request.setCharacterEncoding("GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Enumeration<String> pNames = request.getParameterNames();
		while (pNames.hasMoreElements()) {
			String name = (String) pNames.nextElement();
			String value = request.getParameter(name);
			log.info(name + "=" + value);
		}
		String src = icbcService.getCallSign(request);
		log.info("src=" + src);
		icbcService.checkCallBack(src, request);
		return "200";
	}

	@RequestMapping(value = "/icbc_demo", method = RequestMethod.GET)
	public String config(Model model) {
		long _seriam = 20160321155523L;
		icbcService.sufficeIcbcSubmit(model, _seriam);
		return "/icbc/testpay";
	}

}
