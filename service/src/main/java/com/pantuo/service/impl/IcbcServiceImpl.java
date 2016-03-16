package com.pantuo.service.impl;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import cn.com.infosec.icbc.ReturnValue;

@Service
public class IcbcServiceImpl {
	private static Logger log = LoggerFactory.getLogger(IcbcServiceImpl.class);

	public void sufficeIcbcSubmit(Model model, long _seriam) {
		String TranTime = "20160321155523";
		String contractNo = String.valueOf(_seriam);
		String callback = "http://busme.cn/icbcCallBack";
		StringBuilder dBuilder = new StringBuilder();
		dBuilder.append("APIName=B2B&APIVersion=001.001.001.001&Shop_code=0200EC14729207").append("&MerchantURL=");
		dBuilder.append(callback);
		dBuilder.append("&ContractNo=");
		dBuilder.append(contractNo);
		dBuilder.append("&ContractAmt=10");
		dBuilder.append("&Account_cur=001&JoinFlag=2&SendType=0&TranTime=" + TranTime + "&");
		dBuilder.append("Shop_acc_num=0200004519000100173&PayeeAcct=0200004519000100173");

		model.addAttribute("a1", jiami(dBuilder.toString()));
		model.addAttribute("a2", jiami2(dBuilder.toString()));
		model.addAttribute("contractNo", contractNo);
		model.addAttribute("TranTime", TranTime);
		model.addAttribute("callback", callback);
	}

	public int checkCallBack(String src, HttpServletRequest request) {
		int r = -1;
		try {
			String notifySign = request.getParameter("NotifySign");
			log.info("notifySign=" + notifySign);
			byte[] sign = ReturnValue.base64dec(notifySign.getBytes());
			String p = this.getClass().getResource("/icbc").getPath();
			FileInputStream in1 = new FileInputStream(p + "/ebb2cpublic.crt");
			byte[] bcert = new byte[in1.available()];
			in1.read(bcert);
			in1.close();
			log.info("srcLength=" + src.length());
			byte[] s2 = src.getBytes("gbk");
			r = ReturnValue.verifySign(s2, s2.length, bcert, sign);
			log.info("r_gbk=" + r);
			//log.info("r_src=" + ReturnValue.verifySign(src.getBytes(), src.length(), bcert, sign));
		} catch (Exception e) {
			log.error("check=", e);
		}
		return r;
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
		if (StringUtils.isNoneBlank(request.getParameter("PayeeName"))) {
			try {
				sb.append(new String(request.getParameter("PayeeName").getBytes("ISO-8859-1"), "GBK"));
			} catch (UnsupportedEncodingException e) {
			}
		}
		//sb.append(StringUtils.trimToEmpty(request.getParameter("PayeeName")));

		return sb.toString();
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
