
package com.pantuo.web;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.dao.BusRepository;
import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaBusModel;
import com.pantuo.dao.pojo.JpaBusOnline;
import com.pantuo.dao.pojo.JpaBusUpLog;
import com.pantuo.dao.pojo.JpaBusinessCompany;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.JpaLineUpLog;
import com.pantuo.dao.pojo.JpaPublishLine;
import com.pantuo.dao.pojo.JpaBusline.Level;
import com.pantuo.mybatis.domain.Bus;
import com.pantuo.mybatis.domain.BusOnline;
import com.pantuo.mybatis.domain.CountableBusLine;
import com.pantuo.mybatis.domain.CountableBusModel;
import com.pantuo.mybatis.domain.CountableBusinessCompany;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.BusLineCheckService;
import com.pantuo.service.BusService;
import com.pantuo.service.CardService;
import com.pantuo.util.Pair;
import com.pantuo.web.view.AdjustLogView;
import com.pantuo.web.view.BusInfoView;
import com.pantuo.web.view.ContractLineDayInfo;

/**
 * @author tliu
 */
@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/carbox")
public class CarBoxController {
	private static Logger log = LoggerFactory.getLogger(CarBoxController.class);


	@Autowired
	CardService cardService;
	
	/**
	 * 
	 * 加入购物车
	 *
	 * @param city
	 * @param principal
	 * @param proid
	 * @param needCount
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "/saveCard/{type}")
	@ResponseBody
	public Pair<Double, Integer> saveCardBoxMedia(
			@PathVariable("type") String type,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			@RequestParam(value = "proid", required = true) int  proid,
			@RequestParam(value = "uprice", required = true) int  uprice,
			@RequestParam(value = "needCount", required = false) int  needCount
			) {
		return cardService.saveCard(proid,uprice,needCount,principal,city,type);
	}
	@RequestMapping(value = "/delOneCarBox/{id}")
	@ResponseBody
	public Pair<Boolean, String> delOneCarBox(Model model,
			@PathVariable("id") int id, Principal principal,
				HttpServletRequest request) {
		return cardService.delOneCarBox(id);
	}

	
}
