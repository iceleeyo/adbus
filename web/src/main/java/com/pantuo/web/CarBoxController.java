package com.pantuo.web;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
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
import com.pantuo.mybatis.domain.BodyOrderLog;
import com.pantuo.mybatis.domain.CardboxHelper;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.CardService;
import com.pantuo.service.security.Request;
import com.pantuo.util.MD5Util;
import com.pantuo.util.OpenApiV1;
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
	@Lazy
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
	public Pair<Boolean, String> putIncar(@PathVariable("type") String type,HttpServletRequest request,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			@RequestParam(value = "proid", required = true) int proid,
			@RequestParam(value = "needCount", required = false,defaultValue = "1") int needCount,
			@RequestParam(value = "startdate1", required = false) String startdate1,
			@RequestParam(value = "days", required = false) int days) {
		return cardService.putIncar(proid, needCount, days,principal, city,startdate1,type,request);
	}
	@RequestMapping(value = "/buy/{type}")
	@ResponseBody
	public Pair<Boolean, String> buy(@PathVariable("type") String type,HttpServletRequest request,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			@RequestParam(value = "proid", required = true) int proid,
			@RequestParam(value = "needCount", defaultValue = "1") int needCount,
			@RequestParam(value = "startdate1", required = false) String startdate1,
			@RequestParam(value = "days", required = false) int days) {
		return cardService.buy(proid, needCount, days,principal, city,startdate1, type,request);
	}
	@RequestMapping(value = "/payment")
	@ResponseBody
	public Pair<Boolean, Object> payment(
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			@RequestParam(value = "paytype", required = false) String paytype,
			@RequestParam(value = "divid", required = false) String divid,
			@RequestParam(value = "meids", required = false) String meids,
			@RequestParam(value = "boids", required = false) String boids,
			@RequestParam(value = "isdiv", required = false) int isdiv,
			@RequestParam(value = "startdate1", required = false) String startdate1,
			@RequestParam(value = "payment", required = false) String payment,
			@RequestParam(value = "seriaNum", required = false) long seriaNum,
			@RequestParam(value = "runningNum", required = false) long runningNum,
			HttpServletRequest request
			) {
		
		
		 Pair<Boolean, Object> r=cardService.payment(request,startdate1,paytype,isdiv,divid, seriaNum, principal, city,  meids,boids,runningNum);
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
	
	@RequestMapping(value = "/paySuccess/{metype}")
	public String paysuccess(Model model,@PathVariable("metype") String metype) {
		model.addAttribute("metype", metype);
		return "paySuccess";
	}

	@RequestMapping("sift_body")
	@ResponseBody
	public DataTablePage<JpaBusOrderDetailV2> searchPro(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal) {
		Page<JpaBusOrderDetailV2> page = cardService.searchProducts(city, principal, req);
		return new DataTablePage(page, req.getDraw());
	}
	
	@Value("${body.onlineUrl}")
	String bodyOnlineUrl;
	@RequestMapping(value = "/carTask")
	public String list(Model model,@RequestParam(value = "id", defaultValue = "0") int id) {
		model.addAttribute("bodyOnlineUrl", bodyOnlineUrl);
		model.addAttribute("Md5", MD5Util.getMd5(id));
		return "myCardTask";
	}
	
	@RequestMapping("ajax-myCards")
	@ResponseBody
	public DataTablePage<CardBoxHelperView> myCards(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,HttpServletResponse response) {
		Page<CardBoxHelperView> page = cardService.myCards(city, principal, req);
		 response.setHeader("Access-Control-Allow-Origin", "*");
		return new DataTablePage(page, req.getDraw());
	}
	@RequestMapping("ajax-bodyOrderLog")
	@ResponseBody
	public List<BodyOrderLog> getBodyOrderLog(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		return cardService.getBodyOrderLog(principal, req);
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
	public DataTablePage<JpaCardBoxBody> queryCarBoxBody(HttpServletResponse response,TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId) {
		Page<JpaCardBoxBody> jpabuspage = cardService.queryCarBoxBody(cityId, req, req.getPage(), req.getLength(),
				req.getSort("totalprice"));
		 response.setHeader("Access-Control-Allow-Origin", "*");
		return new DataTablePage(jpabuspage, req.getDraw());
	}
	@RequestMapping("/queryCarHelperyByid/{id}")
	@ResponseBody
	public JpaCardBoxHelper queryCarHelperyByid(@PathVariable("id") int id,HttpServletResponse response) {
		 response.setHeader("Access-Control-Allow-Origin", "*");
		return cardService.queryCarHelperyByid(id);
	}
	@RequestMapping("/editCarHelper")
	@ResponseBody
	@OpenApiV1
	public Pair<Boolean, String> editCarHelper(CardboxHelper helper, Principal principal,
			@RequestParam("stat") String stas,
			@RequestParam(value="remarks",required=false) String remarks,
			@RequestParam(value="creater",required=false) String creater,HttpServletResponse response) {
		 response.setHeader("Access-Control-Allow-Origin", "*");
		 String userId=principal==null?(creater==null?"":creater):Request.getUserId(principal);
		return cardService.editCarHelper(helper,stas,userId,remarks);
	}
	@RequestMapping("ajax-queryCarBoxMedia")
	@ResponseBody
	public DataTablePage<JpaCardBoxMedia> queryCarBoxMedia(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId) {
		Page<JpaCardBoxMedia> jpabuspage = cardService.queryCarBoxMedia(cityId, req, req.getPage(), req.getLength(),
				req.getSort("totalprice"));
		return new DataTablePage(jpabuspage, req.getDraw());
	}
	
	@RequestMapping(value = "/ajax-checkPayStats")
	@ResponseBody
	public boolean saveCardBoxMedia(
			@RequestParam(value = "runningNum", defaultValue = "0") long runningNum,
			@RequestParam(value = "orderId", defaultValue = "-1") int orderId
			) {
		return cardService.checkPayed(runningNum,orderId);
	}
}
