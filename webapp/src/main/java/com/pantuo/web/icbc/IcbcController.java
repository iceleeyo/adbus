package com.pantuo.web.icbc;

import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.infosec.icbc.ReturnValue;

@Controller
public class IcbcController {
	private static Logger log = LoggerFactory.getLogger(IcbcController.class);
	static AtomicInteger s = new AtomicInteger();

	@RequestMapping(value = "/icbcCallBack")
	@ResponseBody
	public String icbcCallBack(Model model, HttpServletRequest request) {
		Enumeration<String> pNames = request.getParameterNames();
		while (pNames.hasMoreElements()) {
			String name = (String) pNames.nextElement();
			String value = request.getParameter(name);
			log.info(name + "=" + value);
		}

		return "200";
	}

	@RequestMapping(value = "/icbc_demo", method = RequestMethod.GET)
	public String config(Model model, HttpSession session) {
		String TranTime = "20151226195923";
		String contractNo = String.valueOf((20151211101098L) + s.incrementAndGet());
		String callback = "http://busme.cn/icbcCallBack";
		model.addAttribute("TranTime", TranTime);
		model.addAttribute("callback", callback);
		StringBuilder dBuilder = new StringBuilder();
		dBuilder.append("APIName=B2B&APIVersion=001.001.001.001&Shop_code=0200EC13762216").append("&MerchantURL=");
		dBuilder.append(callback);
		dBuilder.append("&ContractNo=");
		dBuilder.append(contractNo);
		dBuilder.append("&ContractAmt=10");
		dBuilder.append("&Account_cur=001&JoinFlag=2&SendType=0&TranTime=" + TranTime + "&");
		dBuilder.append("Shop_acc_num=0200004519000100324&PayeeAcct=0200004519000100324");

		model.addAttribute("a1", jiami(dBuilder.toString()));
		model.addAttribute("a2", jiami2(dBuilder.toString()));
		model.addAttribute("contractNo", contractNo);
		return "/icbc/testpay";
	}

	public String jiami(String tranData) {
		System.out.println("tranData" + tranData);

		String password = "12345678";
		String SignMsgBase64 = "";
		String p = this.getClass().getResource("/icbc").getPath();
		try {
			byte[] byteSrc = tranData.getBytes();
			char[] keyPass = password.toCharArray();
			FileInputStream in1 = new FileInputStream(p + "/user.crt");
			byte[] bcert = new byte[in1.available()];
			in1.read(bcert);
			in1.close();
			FileInputStream in2 = new FileInputStream(p + "/user.key");
			byte[] bkey = new byte[in2.available()];
			in2.read(bkey);
			in2.close();
			byte[] sign = ReturnValue.sign(byteSrc, byteSrc.length, bkey, keyPass);
			if (sign == null) {
				log.info("sign fail!");
			} else {
				log.info("sign success!");
				byte[] EncSign = ReturnValue.base64enc(sign);
				SignMsgBase64 = new String(EncSign).toString();
				return SignMsgBase64;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return SignMsgBase64;
	}

	public String jiami2(String tranData) {
		String password = "12345678";
		String CertBase64 = "";
		try {
			String p = this.getClass().getResource("/icbc").getPath();
			byte[] byteSrc = tranData.getBytes();
			char[] keyPass = password.toCharArray();

			FileInputStream in1 = new FileInputStream(p + "/user.crt");
			byte[] bcert = new byte[in1.available()];
			in1.read(bcert);
			in1.close();
			FileInputStream in2 = new FileInputStream(p + "/user.key");
			byte[] bkey = new byte[in2.available()];
			in2.read(bkey);
			in2.close();
			byte[] sign = ReturnValue.sign(byteSrc, byteSrc.length, bkey, keyPass);
			if (sign == null) {
				log.info("sign fail!");
			} else {
				byte[] EncCert = ReturnValue.base64enc(bcert);
				CertBase64 = new String(EncCert).toString();
				return CertBase64;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return CertBase64;
	}
}
