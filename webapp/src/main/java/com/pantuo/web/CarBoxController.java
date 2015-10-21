package com.pantuo.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.dao.pojo.JpaBusOrderDetailV2;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.CardService;
import com.pantuo.util.Pair;

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
	public Pair<Double, Integer> saveCardBoxMedia(@PathVariable("type") String type,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			@RequestParam(value = "proid", required = true) int proid,
			@RequestParam(value = "uprice", required = false) int uprice,
			@RequestParam(value = "needCount", required = false) int needCount) {
		return cardService.saveCard(proid, uprice, needCount, principal, city, type);
	}
	@RequestMapping(value = "/payment")
	@ResponseBody
	public Pair<Boolean, String> payment(
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			@RequestParam(value = "paytype", required = false) String paytype,
			@RequestParam(value = "divid", required = false) String divid,
			@RequestParam(value = "meids", required = false) String meids,
			@RequestParam(value = "boids", required = false) String boids,
			@RequestParam(value = "seriaNum", required = false) long seriaNum) {
		 Pair<Boolean, String> r=cardService.payment(paytype, divid, seriaNum, principal, city);
		 if(r.getLeft()){
			 cardService.updateCardboxUser(seriaNum,principal);
			 cardService.confirmByids(principal,meids,boids);
		 }
		return r;
	}

	@RequestMapping(value = "/delOneCarBox/{type}/{id}")
	@ResponseBody
	public Pair<Boolean, String> delOneCarBox(Model model, @PathVariable("id") int id, Principal principal,
			@PathVariable("type") String type,HttpServletRequest request) {
		return cardService.delOneCarBox(type,id);
	}

	@RequestMapping(value = "/carboxMoney")
	@ResponseBody
	public double carboxMoney(Model model, Principal principal, HttpServletRequest request) {
		return cardService.getBoxPrice(principal);
	}

	@RequestMapping(value = "/test")
	@ResponseBody
	public void test(Model model, Principal principal, HttpServletRequest request) {
		cardService.test();
	}

	@RequestMapping("sift_body")
	@ResponseBody
	public DataTablePage<JpaBusOrderDetailV2> searchPro(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal) {
		Page<JpaBusOrderDetailV2> page = cardService.searchProducts(city, principal, req);
		return new DataTablePage(page, req.getDraw());
	}
}
