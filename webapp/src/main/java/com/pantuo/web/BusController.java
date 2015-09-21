package com.pantuo.web;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

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

import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaBusModel;
import com.pantuo.dao.pojo.JpaBusOnline;
import com.pantuo.dao.pojo.JpaBusUpLog;
import com.pantuo.dao.pojo.JpaBusinessCompany;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.JpaPublishLine;
import com.pantuo.mybatis.domain.Bus;
import com.pantuo.mybatis.domain.BusOnline;
import com.pantuo.mybatis.domain.CountableBusLine;
import com.pantuo.mybatis.domain.CountableBusModel;
import com.pantuo.mybatis.domain.CountableBusinessCompany;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.BusLineCheckService;
import com.pantuo.service.BusService;
import com.pantuo.util.Pair;
import com.pantuo.web.view.BusInfoView;
import com.pantuo.web.view.ContractLineDayInfo;

/**
 * @author tliu
 */
@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/bus")
public class BusController {
	private static Logger log = LoggerFactory.getLogger(BusController.class);

	@Autowired
	private BusService busService;

	@Autowired
	BusLineCheckService busLineCheckService;
	@RequestMapping("ajax-list")
	@ResponseBody
	public DataTablePage<BusInfoView> getAllBuses(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city) {
		if (city == null || city.getMediaType() != JpaCity.MediaType.body)
			return new DataTablePage(Collections.emptyList());
		Page<JpaBus> jpabuspage = busService.getAllBuses(cityId, req, req.getPage(), req.getLength(),
				req.getSort("id"), false);
		return new DataTablePage(busService.queryBusinfoView(req, jpabuspage), req.getDraw());
	}
	@RequestMapping("ajax-findBusByLineid")
	@ResponseBody
	public DataTablePage<BusInfoView> findBusByLineid(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city) {
		if (city == null || city.getMediaType() != JpaCity.MediaType.body)
			return new DataTablePage(Collections.emptyList());
		Page<JpaBus> jpabuspage = busService.getAllBuses(cityId, req, req.getPage(), req.getLength(),
				req.getSort("id"), false);
		return new DataTablePage(busService.queryBusinfoView(req, jpabuspage), req.getDraw());
	}
	/**
	 * 
	 * 合同 线路当天合计
	 *
	 * @param req
	 * @param cityId
	 * @param city
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping("ajax-daysNumber")
	@ResponseBody
	public ContractLineDayInfo daysNumber(int publish_line_id,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city) {
		return  busService.getContractBusLineTodayInfo(publish_line_id);
	}
	
	/**
	 * 
	 * 线路可上刊检查
	 *
	 * @param city
	 * @param principal
	 * @param stday
	 * @param plid
	 * @param days
	 * @return
	 * @throws ParseException
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "/ajax-checkFree")
	@ResponseBody
	public Pair<Boolean, String> checkFree(
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			@RequestParam(value = "stday", required = true) String stday,
			@RequestParam(value = "plid", required = true) int plid,
			@RequestParam(value = "days", required = true) int  days) throws ParseException {
		return busService.checkFree( stday, days,city,plid);
	}
	
	
	@RequestMapping("ajax-mistake_handle")
	@ResponseBody
	public DataTablePage<BusInfoView> mistake_handle(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city) {
		if (city == null || city.getMediaType() != JpaCity.MediaType.body)
			return new DataTablePage(Collections.emptyList());
		Page<JpaBus> jpabuspage = busService.getAllBuses(cityId, req, req.getPage(), req.getLength(),
				req.getSort("id"), false);
		return new DataTablePage(busService.queryBusinfoView(req, jpabuspage), req.getDraw());
	}
	
	@RequestMapping("offlineContract/{id}/{publishLineId}")
	@ResponseBody
	public BusOnline updateOffline(TableRequest req,@PathVariable int id,@PathVariable int publishLineId,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city, Principal principal) {
		return busService.offlineBusContract(cityId, id,publishLineId,principal);
	}
	@RequestMapping("saveBus")
	@ResponseBody
	public Pair<Boolean, String> saveBus(Bus bus,@CookieValue(value = "city", defaultValue = "-1") int cityId, Principal principal) throws JsonGenerationException, JsonMappingException, IOException {
		return busService.saveBus(bus, cityId,principal);
	}
	
	@RequestMapping("ajax-busOnline_history")
	@ResponseBody
	public DataTablePage<JpaBusOnline> busOnlinehistory(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city) {
		if (city == null || city.getMediaType() != JpaCity.MediaType.body)
			return new DataTablePage(Collections.emptyList());
		Page<JpaBusOnline> jpabuspage = busService.getbusOnlinehistory(cityId, req, req.getPage(), req.getLength(),
				req.getSort("id"));
		return new DataTablePage(jpabuspage, req.getDraw());
	}
	
	@RequestMapping("ajax-busUpdate_history")
	@ResponseBody
	public DataTablePage<BusInfoView> busUpdatehistory(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city)throws JsonParseException, JsonMappingException, IOException {
		if (city == null || city.getMediaType() != JpaCity.MediaType.body)
			return new DataTablePage(Collections.emptyList());
		Page<JpaBusUpLog> jpabuspage = busService.getbusUphistory(cityId, req, req.getPage(), req.getLength(),
				null);
		return new DataTablePage(busService.queryBusinfoView2(req, jpabuspage), req.getDraw());
	}
	@RequestMapping("ajax-busUpdate_query")
	@ResponseBody
	public DataTablePage<BusInfoView> busUpdatequery(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city)throws JsonParseException, JsonMappingException, IOException {
		if (city == null || city.getMediaType() != JpaCity.MediaType.body)
			return new DataTablePage(Collections.emptyList());
		Page<JpaBusUpLog> jpabuspage = busService.getbusUphistory(cityId, req, req.getPage(), req.getLength(),
				null);
		return new DataTablePage(busService.queryBusinfoView2(req, jpabuspage), req.getDraw());
	}

	@RequestMapping("ajax-all-lines")
	@ResponseBody
	public DataTablePage<JpaBusline> getAllLines(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city) {
		if (city == null || city.getMediaType() != JpaCity.MediaType.body)
			return new DataTablePage(Collections.emptyList());

		String levelStr = req.getFilter("level");
		JpaBusline.Level level = null;
		if (!StringUtils.isBlank(levelStr)) {
			level = JpaBusline.Level.fromNameStr(levelStr);
			try {
				level = JpaBusline.Level.valueOf(levelStr);
			} catch (Exception e) {
			}
		}
		return new DataTablePage(busService.getAllBuslines(cityId, level, req.getFilter("name"), req.getPage(),
				req.getLength(), req.getSort("id")), req.getDraw());
	}

	@RequestMapping(value = "/ajaxdetail/{id}")
	@ResponseBody
	public JpaBus ajaxdetail(@PathVariable int id, Model model, HttpServletRequest request) {
		return busService.findById(id);
	}

	@RequestMapping("ajax-all-models")
	@ResponseBody
	public DataTablePage<JpaBusModel> getAllModels(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city) {
		if (city == null || city.getMediaType() != JpaCity.MediaType.body)
			return new DataTablePage(Collections.emptyList());

		return new DataTablePage(busService.getAllBusModels(cityId, req.getFilter("name"),
				req.getFilter("manufacturer"), req.getPage(), req.getLength(), req.getSort("id")), req.getDraw());
	}

	@RequestMapping("ajax-all-companies")
	@ResponseBody
	public DataTablePage<JpaBusinessCompany> getAllCompanies(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city) {
		if (city == null || city.getMediaType() != JpaCity.MediaType.body)
			return new DataTablePage(Collections.emptyList());

		return new DataTablePage(busService.getAllBusinessCompanies(cityId, req.getFilter("name"),
				req.getFilter("contact"), req.getPage(), req.getLength(), req.getSort("id")), req.getDraw());
	}

	@RequestMapping(value = "/list")
	public String list() {
		return "bus_list";
	}
	@RequestMapping(value = "/busUpdate_query")
	public String querybusUp() {
		return "busUpdate_query";
	}
	@RequestMapping(value = "/busOnline_history/{busid}")
	public String busOnline_history(Model model,@PathVariable("busid") int busid,HttpServletResponse response) {
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		model.addAttribute("busid", busid);
		return "busOnline_history";
	}
	@RequestMapping(value = "/busUpdate_history/{busid}")
	public String busUpdate_history(Model model,@PathVariable("busid") int busid,HttpServletResponse response) {
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		model.addAttribute("busid", busid);
		return "busUpdate_history";
	}

	@RequestMapping(value = "/findBusByLineid/{publishlineid}")
	public String findBusByLineid(Model model, @PathVariable("publishlineid") int publishlineid,
			@CookieValue(value = "city", defaultValue = "-1") int cityId) {
		JpaPublishLine jpaPublishLine = busLineCheckService.queryPublishLineByid(publishlineid);
		model.addAttribute("jpaPublishLine", jpaPublishLine);
		model.addAttribute("companys", busService.getAllCompany(cityId));
		model.addAttribute("plid", publishlineid);
		model.addAttribute("seriaNum", jpaPublishLine != null ? jpaPublishLine.getSeriaNum() : StringUtils.EMPTY);

		return "busOfline_list";
	}
	@RequestMapping(value = "/mistake_handle")
	public String mistake_handle(Model model,@CookieValue(value = "city", defaultValue = "-1") int cityId) {
		model.addAttribute("companys",busService. getAllCompany(cityId));
		return "mistake_handle";
	}
	@RequestMapping(value = "findAllCompany")
	@ResponseBody
	public List<JpaBusinessCompany> findAllCompany(Model model,@CookieValue(value = "city", defaultValue = "-1") int cityId) {
		return busService. getAllCompany(cityId);
	}

	@RequestMapping(value = "/lines")
	public String lines() {
		return "bus_lines";
	}

	@RequestMapping(value = "/models")
	public String models() {
		return "bus_models";
	}

	@RequestMapping(value = "/companies")
	public String companies() {
		return "bus_companies";
	}

	@RequestMapping("ajax-bus-lines")
	@ResponseBody
	public Iterable<CountableBusLine> getBuslines(@CookieValue(value = "city", defaultValue = "-1") int city,
			@RequestParam("level") JpaBusline.Level level,
			@RequestParam(value = "category", required = false) JpaBus.Category category,
			@RequestParam(value = "lineId", required = false) Integer lineId,
			@RequestParam(value = "modelId", required = false) Integer modelId,
			@RequestParam(value = "companyId", required = false) Integer companyId) {
		return busService.getBuslines(city, level, category, lineId, modelId, companyId);
	}

	@RequestMapping("ajax-bus-models")
	@ResponseBody
	public Iterable<CountableBusModel> getBusModels(@CookieValue(value = "city", defaultValue = "-1") int city,
			@RequestParam("level") JpaBusline.Level level,
			@RequestParam(value = "category", required = false) JpaBus.Category category,
			@RequestParam(value = "lineId", required = false) Integer lineId,
			@RequestParam(value = "modelId", required = false) Integer modelId,
			@RequestParam(value = "companyId", required = false) Integer companyId) {
		return busService.getBusModels(city, level, category, lineId, modelId, companyId);
	}

	@RequestMapping("ajax-bus-companies")
	@ResponseBody
	public Iterable<CountableBusinessCompany> getBusinessCompanies(
			@CookieValue(value = "city", defaultValue = "-1") int city, @RequestParam("level") JpaBusline.Level level,
			@RequestParam(value = "category", required = false) JpaBus.Category category,
			@RequestParam(value = "lineId", required = false) Integer lineId,
			@RequestParam(value = "modelId", required = false) Integer modelId,
			@RequestParam(value = "companyId", required = false) Integer companyId) {
		return busService.getBusinessCompanies(city, level, category, lineId, modelId, companyId);
	}
	@RequestMapping(value = "/batchOnline")
	@ResponseBody
	public Pair<Boolean, String> batchOnline(
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			@RequestParam(value = "stday", required = true) String stday,
			@RequestParam(value = "ids", required = true) String ids,
			@RequestParam(value = "contractid", required = true) int contractid,
			@RequestParam(value = "plid", required = true) int plid,
			@RequestParam(value = "days", required = true) int  days) throws ParseException {
		return busService.batchOnline(ids,stday, days, contractid,principal,city,plid);
	}
}
