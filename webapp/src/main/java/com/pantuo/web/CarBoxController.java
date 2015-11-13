package com.pantuo.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.pantuo.dao.pojo.JpaCardBoxBody;
import com.pantuo.dao.pojo.JpaCardBoxHelper;
import com.pantuo.dao.pojo.JpaCardBoxMedia;
import com.pantuo.mybatis.domain.CardboxHelper;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.CardService;
import com.pantuo.util.Pair;
import com.pantuo.web.view.CardBoxHelperView;
import com.pantuo.web.view.CardTotalView;

/**
 * @author tliu
 */
@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/carbox")
public class CarBoxController {
	private static Logger log = LoggerFactory.getLogger(CarBoxController.class);

	@Autowired
	CardService cardService;
	@Autowired
	ActivitiService activitiService;
	/**
	 * 
	 * 加入购物车
	 *
	 * @param cityjpa
	 * @param principal
	 * @param proid
	 * @param needCount
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "/saveCard/{type}")
	@ResponseBody
	public CardTotalView saveCardBoxMedia(@PathVariable("type") String type,
			@CookieValue(value = "city", defaultValue = "-1") int cityjpa, Principal principal,
			@RequestParam(value = "proid", required = true) int proid,
			@RequestParam(value = "needCount", required = false) int needCount,@RequestParam(value = "IsDesign", required = false) int IsDesign) {
		return cardService.saveCard(proid, needCount, principal, cityjpa, type,IsDesign);
	}
	@RequestMapping(value = "/putIncar/{type}")
	@ResponseBody
	public Pair<Boolean, String> putIncar(@PathVariable("type") String type,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			@RequestParam(value = "proid", required = true) int proid,
			@RequestParam(value = "needCount", required = false) int needCount,
			@RequestParam(value = "days", required = false) int days) {
		return cardService.putIncar(proid, needCount, days,principal, city, type);
	}
	@RequestMapping(value = "/buy/{type}")
	@ResponseBody
	public Pair<Boolean, String> buy(@PathVariable("type") String type,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			@RequestParam(value = "proid", required = true) int proid,
			@RequestParam(value = "needCount", required = false) int needCount,
			@RequestParam(value = "days", required = false) int days) {
		return cardService.buy(proid, needCount, days,principal, city, type);
	}
	@RequestMapping(value = "/payment")
	@ResponseBody
	public Pair<Boolean, String> payment(
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			@RequestParam(value = "paytype", required = false) String paytype,
			@RequestParam(value = "divid", required = false) String divid,
			@RequestParam(value = "meids", required = false) String meids,
			@RequestParam(value = "boids", required = false) String boids,
			@RequestParam(value = "startdate1", required = false) String startdate1,
			@RequestParam(value = "payment", required = false) String payment,
			@RequestParam(value = "seriaNum", required = false) long seriaNum) {
		
		
		 Pair<Boolean, String> r=cardService.payment(startdate1,paytype, divid, seriaNum, principal, city,  meids,boids);
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
	
	@RequestMapping(value = "/totalView")
	@ResponseBody
	public CardTotalView totalView(Model model, Principal principal, HttpServletRequest request) {
		return cardService.getCarSumInfo(principal);
	}

	@RequestMapping(value = "/test")
	@ResponseBody
	public void test(Model model, Principal principal, HttpServletRequest request) {
		cardService.test();
	}
	
	@RequestMapping(value = "/paySuccess")
	public String paysuccess() {
		return "paySuccess";
	}

	@RequestMapping("sift_body")
	@ResponseBody
	public DataTablePage<JpaBusOrderDetailV2> searchPro(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal) {
		Page<JpaBusOrderDetailV2> page = cardService.searchProducts(city, principal, req);
		return new DataTablePage(page, req.getDraw());
	}
	
	
	@RequestMapping(value = "/carTask")
	public String list() {
		return "myCardTask";
	}
	
	@RequestMapping("ajax-myCards")
	@ResponseBody
	public DataTablePage<CardBoxHelperView> myCards(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal) {
		Page<CardBoxHelperView> page = cardService.myCards(city, principal, req);
		return new DataTablePage(page, req.getDraw());
	}
	@RequestMapping(value = "/queryCarBoxBody/{helpid}")
	public String queryCarBoxBody(Model model,@PathVariable("helpid") int helpid,HttpServletResponse response) {
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		model.addAttribute("helpid", helpid);
		return "carBoxBodyDetail";
	}
	@RequestMapping(value = "/queryCarBoxMedia/{helpid}")
	public String queryCarBoxMedia(Model model,@PathVariable("helpid") int helpid,HttpServletResponse response) {
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		model.addAttribute("helpid", helpid);
		return "carBoxMediaDetail";
	}
	@RequestMapping("ajax-queryCarBoxBody")
	@ResponseBody
	public DataTablePage<JpaCardBoxBody> queryCarBoxBody(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId) {
		Page<JpaCardBoxBody> jpabuspage = cardService.queryCarBoxBody(cityId, req, req.getPage(), req.getLength(),
				req.getSort("totalprice"));
		return new DataTablePage(jpabuspage, req.getDraw());
	}
	@RequestMapping("/queryCarHelperyByid/{id}")
	@ResponseBody
	public JpaCardBoxHelper queryCarHelperyByid(@PathVariable("id") int id) {
		return cardService.queryCarHelperyByid(id);
	}
	@RequestMapping("/editCarHelper")
	@ResponseBody
	public Pair<Boolean, String> editCarHelper(CardboxHelper helper,@RequestParam("stat") String stas) {
		return cardService.editCarHelper(helper,stas);
	}
	@RequestMapping("ajax-queryCarBoxMedia")
	@ResponseBody
	public DataTablePage<JpaCardBoxMedia> queryCarBoxMedia(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId) {
		Page<JpaCardBoxMedia> jpabuspage = cardService.queryCarBoxMedia(cityId, req, req.getPage(), req.getLength(),
				req.getSort("totalprice"));
		return new DataTablePage(jpabuspage, req.getDraw());
	}
}
