package com.pantuo.web;

import com.pantuo.dao.pojo.*;
import com.pantuo.mybatis.domain.*;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.BusLineCheckService;
import com.pantuo.service.BusService;
import com.pantuo.service.TimeslotService;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.web.view.BusInfoView;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	@RequestMapping(value = "/busOnline_history/{busid}")
	public String busOnline_history(Model model,@PathVariable("busid") int busid,HttpServletResponse response) {
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		model.addAttribute("busid", busid);
		return "busOnline_history";
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
