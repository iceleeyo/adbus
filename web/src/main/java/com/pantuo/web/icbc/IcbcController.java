package com.pantuo.web.icbc;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Map;
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
import com.pantuo.util.BusinessException;
import com.pantuo.util.HttpTookit;
import com.pantuo.util.JsonTools;
import com.pantuo.web.view.CardView;

@Controller
public class IcbcController {
	private static Logger log = LoggerFactory.getLogger(IcbcController.class);
	static AtomicInteger s = new AtomicInteger();

	@Autowired
	IcbcServiceImpl icbcService;

	@RequestMapping(value = "/js/icbcCallBack")
	@ResponseBody
	public String jsicbcCallBack(String paytype, Model model, HttpServletRequest request) {
		return sd(request);
	}

	@RequestMapping(value = "/icbcCallBack")
	@ResponseBody
	public String icbcCallBack(String paytype, Model model, HttpServletRequest request) {
		return sd(request);
	}

	private String sd(HttpServletRequest request) {
		printReq(request);
		String src = icbcService.getCallSign(request);
		log.info("src=" + src);
		log.info("paytype=" + request.getParameter("p"));
		icbcService.checkCallBack(src, request);
		return "200";
	}

	private void printReq(HttpServletRequest request) {
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
	}

	@RequestMapping(value = "/icbc_demo", method = RequestMethod.GET)
	public String config(Model model) {
		long _seriam = 20160321155523L;
		CardView c = new CardView(null, null, 1d, 1);
		icbcService.sufficeIcbcSubmit(model, _seriam, c, "k");
		return "/icbc/testpay";
	}

	@RequestMapping(value = "/icbck", method = RequestMethod.GET)
	public String icbck(Model model, HttpServletRequest request) {
		printReq(request);
		long _seriam = 20160321155523L;
		CardView c = new CardView(null, null, 1d, 1);
		icbcService.sufficeIcbcSubmit(model, _seriam, c, "k");
		
		getUCode(request.getParameter("code"));
		return "/icbc/testpay";
	}

	public void getUCode(String code) {

		String s;
		try {
			s = HttpTookit.doGet("https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx4239d89b9295d235&secret=3abd859948177cd8e028cdc9e3aad6cc&code=" + code
					+ "&grant_type=authorization_code", null);
			if (s != null) {
				Map map = (Map) JsonTools.readValue(s, Map.class);
				if (map != null) {
					System.out.println(map.get("openid"));
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}

	}
}
