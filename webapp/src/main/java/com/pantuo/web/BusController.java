
package com.pantuo.web;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
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
import org.springframework.validation.BindingResult;
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
import com.pantuo.dao.pojo.JpaBusline.Level;
import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.JpaLineUpLog;
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
import com.pantuo.vo.CountView;
import com.pantuo.vo.ModelCountView;
import com.pantuo.web.view.AdjustLogView;
import com.pantuo.web.view.BusInfoView;
import com.pantuo.web.view.BusModelGroupView;
import com.pantuo.web.view.ContractLineDayInfo;
import com.pantuo.web.view.PulishLineView;

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
	private BusRepository busRepository;

	@Autowired
	BusLineCheckService busLineCheckService;
	@RequestMapping("ajax-list")
	@ResponseBody
	public DataTablePage<BusInfoView> getAllBuses(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city) {
		if (city == null || city.getMediaType() != JpaCity.MediaType.body)
			return new DataTablePage(Collections.emptyList());
		/*Page<JpaBus> jpabuspage = busService.getAllBuses(cityId, req, req.getPage(), req.getLength(),
				req.getSort("id"), false);
		return new DataTablePage(busService.queryBusinfoView(req, jpabuspage), req.getDraw());*/
		return busService.getAllBusesForContract(cityId, req, req.getPage(), req.getLength(),
				req.getSort("id"), false);
	}

	@RequestMapping("ajax-countbus_list")
	@ResponseBody
	public Collection<BusModelGroupView> countbus_list(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city) {
		if (city == null || city.getMediaType() != JpaCity.MediaType.body)
			return new ArrayList<BusModelGroupView>(0);
		/*Page<JpaBus> jpabuspage = busService.getAllBuses(cityId, req, req.getPage(), Integer.MAX_VALUE,
				req.getSort("id"), true);
		return busService.queryModelGroup(req, jpabuspage);*/
		
		return busService.queryModelGroup4Contract(cityId, req, true);
	}
	@RequestMapping("ajax-ModelCount_list")
	@ResponseBody
	public CountView ModelCount_list(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city) {
		Page<JpaPublishLine> jpabuspage=busLineCheckService.queryAllPublish(cityId, req, req.getPage(), req.getLength(),
				req.getSort("id"));
		return busService.ModelCountlist(req, jpabuspage);
	}
	@RequestMapping("ajax-list.xls")
	public void exportExcel(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city,HttpServletResponse resp) {
		Page<JpaBus> jpabuspage = busService.getAllBuses(cityId, req, req.getPage(), 1024 * 100, req.getSort("id"),
				false);
		 busService.exportBusExcel(req, jpabuspage,resp);
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

		return busService.getMybatisAllBuses(cityId, req, req.getPage(), req.getLength(), req.getSort("id"), false);

		/*Page<JpaBus> jpabuspage = busService.getAllBuses(cityId, req, req.getPage(), req.getLength(),
				req.getSort("id"), false);
		return new DataTablePage(busService.queryBusinfoView(req, jpabuspage), req.getDraw());*/
	}
	
 	@RequestMapping("offlineContract/{id}/{publishLineId}")
	@ResponseBody
	public BusOnline updateOffline(TableRequest req,@PathVariable int id,@PathVariable int publishLineId,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city, Principal principal) {
		return busService.offlineBusContract(cityId, id,publishLineId,principal);
	}
 	
 	
	@RequestMapping("ajax-adJustLog")
	@ResponseBody
	public DataTablePage<AdjustLogView> adJustLog(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city) {
		if (city == null || city.getMediaType() != JpaCity.MediaType.body)
			return new DataTablePage(Collections.emptyList());
		return busService.getAdJustLog(cityId, req, req.getPage(), req.getLength(), req.getSort("id"));
	}

	@RequestMapping("saveBus")
	@ResponseBody
	public Pair<Boolean, String> saveBus(Bus bus, @CookieValue(value = "city", defaultValue = "-1") int cityId,
			Principal principal, HttpServletRequest request,@RequestParam(value="uodated1") String updated1) throws JsonGenerationException, JsonMappingException,
			IOException, ParseException{
		return busService.saveBus(bus, updated1,cityId, principal,request);
	}
	
	@RequestMapping("changeLine")
	@ResponseBody
	public Pair<Boolean, String> changeLine(String ids, int newLineId,@CookieValue(value = "city", defaultValue = "-1") int cityId,
			Principal principal, HttpServletRequest request) throws JsonGenerationException, JsonMappingException,
			IOException {
		return busService.changeLine(ids,newLineId, cityId, principal,request);
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
	@RequestMapping("ajax-bus_offShelf")
	@ResponseBody
	public DataTablePage<JpaBusOnline> findBusonline(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city) throws ParseException {
		if (city == null || city.getMediaType() != JpaCity.MediaType.body)
			return new DataTablePage(Collections.emptyList());
		Page<JpaBusOnline> jpabuspage = busService.getbusOnlineList(cityId, req, req.getPage(), req.getLength(),
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
	@RequestMapping("ajax-busUpLog_list")
	@ResponseBody
	public DataTablePage<BusInfoView> busUpdateLog(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city)throws JsonParseException, JsonMappingException, IOException {
		if (city == null || city.getMediaType() != JpaCity.MediaType.body)
			return new DataTablePage(Collections.emptyList());
		Page<JpaBusUpLog> jpabuspage = busService.getbusUphistory(cityId, req, req.getPage(), req.getLength(),
				req.getSort("id"));
		return new DataTablePage(busService.queryBusinfoView2(req, jpabuspage), req.getDraw());
	}
	@RequestMapping("ajax-lineUpLog_list")
	@ResponseBody
	public DataTablePage<BusInfoView> lineUpdateLog(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city)throws JsonParseException, JsonMappingException, IOException {
		if (city == null || city.getMediaType() != JpaCity.MediaType.body)
			return new DataTablePage(Collections.emptyList());
		Page<JpaLineUpLog> jpabuspage = busService.getlineUphistory(cityId, req, req.getPage(), req.getLength(),
				req.getSort("id"));
		return new DataTablePage(busService.queryBusinfoView3(req, jpabuspage), req.getDraw());
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

		return new DataTablePage(busService.getAllBusModels(cityId,req, req.getFilter("name"),
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
	
	@RequestMapping("ajax-bus_orders")
	@ResponseBody
	public DataTablePage<PulishLineView> bus_orders(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city)throws JsonParseException, JsonMappingException, IOException {
		if (city == null || city.getMediaType() != JpaCity.MediaType.body)
			return new DataTablePage(Collections.emptyList());
		return busService.queryOrders(cityId, req, req.getPage(), req.getLength(),
				req.getSort("id"));
	}


	@RequestMapping(value = "/list")
	public String list() {
		return "bus_list";
	}
	@RequestMapping(value = "/list_sales")
	public String list_sales() {
		return "lines_sales";
	}
	
	@RequestMapping(value = "/list_changeDate")
	public String list_changeDate() {
		return "lines_changeDate";
	}
	
	@RequestMapping(value = "/busAndOrderSearch")
	public String busAndOrderSearch() {
		return "busAndOrderSearch";
	}
	
	@RequestMapping(value = "/adJustLog")
	public String adJustLog(Model model,@CookieValue(value = "city", defaultValue = "-1") int cityId) {
		model.addAttribute("companys",busService. getAllCompany(cityId));
		return "bus_adJustLog";
	}
	
	
	@RequestMapping(value = "/mlist")
	public String mlist() {
		return "bus_mlist";
	}
	@RequestMapping(value = "/busUpdate_query")
	public String querybusUp() {
		return "busUpdate_query";
	}
	@RequestMapping(value = "/busUpLog_list")
	public String busUpLog_list() {
		return "busUpLog_list";
	}
	@RequestMapping(value = "/lineUpLog_list")
	public String lineUpLog_list() {
		return "lineUpLog_list";
	}
	@RequestMapping(value = "/busOnline_history/{busid}")
	public String busOnline_history(Model model,@PathVariable("busid") int busid,HttpServletResponse response) {
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		model.addAttribute("busid", busid);
		return "busOnline_history";
	}
	@RequestMapping(value = "/querybusOnline/{publishLineid}")
	public String querybusOnline(Model model,@PathVariable("publishLineid") int publishLineid,HttpServletResponse response) {
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		model.addAttribute("publishLineid", publishLineid);
		return "publishLine_query";
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
	
	@RequestMapping(value = "/contractSearch")
	public String contractSearch(Model model,@CookieValue(value = "city", defaultValue = "-1") int cityId) {
		model.addAttribute("companys",busService. getAllCompany(cityId));
		return "contractSearch";
	}
	@RequestMapping(value = "findAllCompany")
	@ResponseBody
	public List<JpaBusinessCompany> findAllCompany(Model model,@CookieValue(value = "city", defaultValue = "-1") int cityId) {
		return busService. getAllCompany(cityId);
	}
	@RequestMapping(value = "findLevelMap")
	@ResponseBody
	public Map<String, String> findLevelMap() {
		Map<String, String> nameStrMap = new HashMap<String, String>();
            for (Level l : Level.values()) {
                nameStrMap.put(l.name(), l.getNameStr());
            }
                return nameStrMap;
	}

	
	@RequestMapping(value = "/orders")
	public String orders(Model model,@CookieValue(value = "city", defaultValue = "-1") int cityId) {
		model.addAttribute("companys",busService. getAllCompany(cityId));
		return "bus_orders";
	}
	@RequestMapping(value = "/lines")
	public String lines() {
		return "bus_lines";
	}

	@RequestMapping(value = "/models")
	public String models() {
		return "bus_models";
	}
	@RequestMapping(value = "/bus_offShelf")
	public String bus_offShelf() {
		return "bus_offShelf";
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
	
	//批量上刊
	@RequestMapping(value = "/batchOnline")
	@ResponseBody
	public Pair<Boolean, String> batchOnline(
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			@RequestParam(value = "stday", required = true) String stday,
			@RequestParam(value = "ids", required = true) String ids,
			@RequestParam(value = "contractid", required = true) int contractid,
			@RequestParam(value = "plid", required = true) int plid,
			@RequestParam(value = "days", required = false) int  days,
			@RequestParam(value = "fday", required = false) int  fday,
			@RequestParam(value = "adtype", required = false) String  adtype,
			@RequestParam(value = "print", required = false) String  print,
			@RequestParam(value = "sktype", required = false) String  sktype
			) throws ParseException {
		return busService.batchOnline(ids,stday, days, contractid,principal,city,plid,fday,adtype,print,sktype);
	}
	
	//批量下刊
	@RequestMapping(value = "/batchOffline")
	@ResponseBody
	public Pair<Boolean, String> batchOffline(
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			@RequestParam(value = "offday", required = true) String offday,
			@RequestParam(value = "ids", required = true) String ids
			) throws ParseException{
		return busService.batchOffline(ids,offday,principal,city);
	}
	
	//调刊补刊
	@RequestMapping(value = "/changeDate")
	@ResponseBody
	public Pair<Boolean, String> changeDate(
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			@RequestParam(value = "sday", required = true) String sday,
			@RequestParam(value = "days", required = false) int days,
			@RequestParam(value = "eday", required = false) String eday,
			@RequestParam(value = "ids", required = true) String ids
			) throws ParseException{
		return busService.changeDate(ids,sday,days,eday,principal,city);
	}
}
