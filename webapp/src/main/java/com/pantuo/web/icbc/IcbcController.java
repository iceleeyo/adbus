package com.pantuo.web.icbc;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
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
		String src = getCallSign(request);
		log.info("src=" + src);
		check(src, request);
		return "200";
	}

	public void check(String src, HttpServletRequest request) {
		try {
			String notifySign = request.getParameter("NotifySign");
			log.info("notifySign=" + notifySign);
			byte[] sign = ReturnValue.base64dec(notifySign.getBytes());
			String p = this.getClass().getResource("/icbc").getPath();
			FileInputStream in1 = new FileInputStream(p + "/ebb2cpublic.crt");
			byte[] bcert = new byte[in1.available()];
			in1.read(bcert);
			in1.close();

			//int r = ReturnValue.verifySign(src.getBytes(), src.length(), sign, bcert);
			//log.info("r=" + r);
			log.info("srcLength=" + src.length());
			//byte[] EncCert = ReturnValue.base64enc(bcert);//.getBytes("UTF-8")
			//--
			byte[] s2 = src.getBytes("gbk");
			log.info("r_gbk=" + ReturnValue.verifySign(s2, s2.length, bcert, sign));
			//log.info("r_src=" + ReturnValue.verifySign(src.getBytes(), src.length(), bcert, sign));
		} catch (Exception e) {

			log.error("check=", e);
		}
	}

	public String getCallSign(HttpServletRequest request) {

		StringBuilder sb = new StringBuilder();
		sb.append("APIName=");
		sb.append(StringUtils.trimToEmpty(request.getParameter("APIName")));
		sb.append("&");
		sb.append("APIVersion=");
		sb.append(StringUtils.trimToEmpty(request.getParameter("APIVersion")));
		sb.append("&");
		sb.append("Shop_code=");
		sb.append(StringUtils.trimToEmpty(request.getParameter("Shop_code")));
		sb.append("&");
		sb.append("MerchantURL=");
		sb.append(StringUtils.trimToEmpty(request.getParameter("MerchantURL")));
		sb.append("&");
		sb.append("Serial_no=");
		sb.append(StringUtils.trimToEmpty(request.getParameter("Serial_no")));
		sb.append("&");
		sb.append("PayStatusZHCN=");
		sb.append(StringUtils.trimToEmpty(request.getParameter("PayStatusZHCN")));
		sb.append("&");
		sb.append("TranErrorCode=");
		sb.append(StringUtils.trimToEmpty(request.getParameter("TranErrorCode")));
		sb.append("&");
		sb.append("TranErrorMsg=");
		sb.append(StringUtils.trimToEmpty(request.getParameter("TranErrorMsg")));
		sb.append("&");
		sb.append("ContractNo=");
		sb.append(StringUtils.trimToEmpty(request.getParameter("ContractNo")));
		sb.append("&");
		sb.append("ContractAmt=");
		sb.append(StringUtils.trimToEmpty(request.getParameter("ContractAmt")));
		sb.append("&");

		sb.append("Account_cur=");
		sb.append(StringUtils.trimToEmpty(request.getParameter("Account_cur")));
		sb.append("&");
		sb.append("JoinFlag=");
		sb.append(StringUtils.trimToEmpty(request.getParameter("JoinFlag")));
		sb.append("&");
		sb.append("ShopJoinFlag=");
		sb.append(StringUtils.trimToEmpty(request.getParameter("ShopJoinFlag")));
		sb.append("&");
		sb.append("CustJoinFlag=");
		sb.append(StringUtils.trimToEmpty(request.getParameter("CustJoinFlag")));
		sb.append("&");
		sb.append("CustJoinNumber=");
		sb.append(StringUtils.trimToEmpty(request.getParameter("CustJoinNumber")));
		sb.append("&");
		sb.append("SendType=");
		sb.append(StringUtils.trimToEmpty(request.getParameter("SendType")));
		sb.append("&");
		sb.append("TranTime=");
		sb.append(StringUtils.trimToEmpty(request.getParameter("TranTime")));
		sb.append("&");
		sb.append("NotifyTime=");
		sb.append(StringUtils.trimToEmpty(request.getParameter("NotifyTime")));
		sb.append("&");
		sb.append("Shop_acc_num=");
		sb.append(StringUtils.trimToEmpty(request.getParameter("Shop_acc_num")));
		sb.append("&");
		sb.append("PayeeAcct=");
		sb.append(StringUtils.trimToEmpty(request.getParameter("PayeeAcct")));
		sb.append("&");
		sb.append("PayeeName=");
		if(StringUtils.isNoneBlank(request.getParameter("PayeeName"))){
		try {
			sb.append(new String(request.getParameter("PayeeName").getBytes("ISO-8859-1"),"GBK"));
		} catch (UnsupportedEncodingException e) {
		}
		}
		//sb.append(StringUtils.trimToEmpty(request.getParameter("PayeeName")));

		return sb.toString();
	}

	@RequestMapping(value = "/icbc_demo", method = RequestMethod.GET)
	public String config(Model model, HttpSession session) {
		String TranTime = "20160321155523";
		String contractNo = String.valueOf((20151211102039L) + s.incrementAndGet());
		String callback = "http://busme.cn/icbcCallBack";
		model.addAttribute("TranTime", TranTime);
		model.addAttribute("callback", callback);
		StringBuilder dBuilder = new StringBuilder();
		dBuilder.append("APIName=B2B&APIVersion=001.001.001.001&Shop_code=0200EC14729207").append("&MerchantURL=");
		dBuilder.append(callback);
		dBuilder.append("&ContractNo=");
		dBuilder.append(contractNo);
		dBuilder.append("&ContractAmt=10");
		dBuilder.append("&Account_cur=001&JoinFlag=2&SendType=0&TranTime=" + TranTime + "&");
		dBuilder.append("Shop_acc_num=0200004519000100173&PayeeAcct=0200004519000100173");
		//0200004519000100173

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
