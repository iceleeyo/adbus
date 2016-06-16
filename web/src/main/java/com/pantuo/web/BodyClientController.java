package com.pantuo.web;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.pantuo.service.CardService;
import com.pantuo.util.Only1ServieUniqLong;
import com.pantuo.web.view.BodyProView;
import com.pantuo.web.view.Offlinecontract;

@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/bodyClient")
@SessionAttributes("offlinecontract")
public class BodyClientController {
	private static Logger log = LoggerFactory.getLogger(BodyClientController.class);
	
	@Value("${body.onlineUrl}")
	String bodyOnlineUrl;
	@Autowired
	@Lazy
	CardService cardService;
	@RequestMapping("be_createOnlineContract")
	public String be_createOnlineContract(Model model, Principal principal,
			@RequestParam(value="jsonStr",required = false) String jsonStr,
			@RequestParam(value = "_seriaNum", required = false, defaultValue = "-1") long _seriaNum,
			@RequestParam(value = "helpid", required = false, defaultValue = "-1") long helpid) {
		model.addAttribute("_seriaNum", _seriaNum > 0 ? _seriaNum : Only1ServieUniqLong.getUniqLongNumber());
		model.addAttribute("helpid", helpid);
		model.addAttribute("offlinecontract", cardService.getContractfromJsonStr(jsonStr));
		return "redirect:/bodyClient/createOnlineContract";
	}
	@RequestMapping("createOnlineContract")
	public String createOnlineContract(Model model, Principal principal,
			@RequestParam(value = "_seriaNum", required = false, defaultValue = "-1") long _seriaNum,
			@ModelAttribute("offlinecontract") Offlinecontract offlinecontract,
			@RequestParam(value = "helpid", required = false, defaultValue = "-1") long helpid) {
		model.addAttribute("seriaNum", _seriaNum > 0 ? _seriaNum : Only1ServieUniqLong.getUniqLongNumber());
		model.addAttribute("fromOrder", _seriaNum > 0);
		model.addAttribute("helpid", helpid);
		model.addAttribute("bodyOnlineUrl", bodyOnlineUrl);
		model.addAttribute("offlinecontract", offlinecontract);
		return "onlineContract_enter";
	}

}
