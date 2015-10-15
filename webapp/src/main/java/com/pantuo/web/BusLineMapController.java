package com.pantuo.web;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.BusMapService;
import com.pantuo.service.BusService;
import com.pantuo.util.Pair;
import com.pantuo.web.view.MapLocationSession;

/**
 * 
 * <b><code>BusLineMapController</code></b>
 * <p>
 * 位置查附近的线路
 * </p>
 * <b>Creation Time:</b> 2015年8月22日 上午9:58:28
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
@Controller
@RequestMapping("/api")
@SessionAttributes("_mapLocationKey")
public class BusLineMapController {

	private static Logger log = LoggerFactory.getLogger(BusController.class);
	@Autowired
	private BusService busService;

	@Autowired
	private BusMapService busMapService;
	@RequestMapping(value = "/public_sortTable")
	public String sortTable() {
		return "sortTable";
	}
	
	@RequestMapping(value = "/wantbuy")
	public String wantbuy() {
		return "bus_wantbuy";
	}
	@RequestMapping(value = "/lines")
	public String lines() {
		return "map_lines";
	}
	@RequestMapping(value = "/linesManage")
	public String linesManage() {
		return "lines_list";
	}
	
	@RequestMapping(value = "/linesCheck")
	public String linesCheck() {
		return "lines_check";
	}

	@RequestMapping(value = "/landmarkM_lines")
	public String LandmarkM_lines() {
		return "landmarkM_lines";
	}

	@RequestMapping(value = "/trackM_lines")
	public String trackM_lines() {
		return "trackM_lines";
	}

	Pair<Double, Double> BEIBA_COMPANY = new Pair<Double, Double>(116.31718990229, 39.939290559991);
	private final String BEIBA_COMPANY_NAME = "北巴传媒广告分公司";
	private final String BEIBA_COMPANY_ADDRESS = "北京市海淀区紫竹院路32号";

	@RequestMapping(value = "/public_lineMap")
	public String public_lineMap(Model model, HttpServletResponse response, String lineName,
			@ModelAttribute("_mapLocationKey") MapLocationSession user) {
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		model.addAttribute("lineName", lineName);
		return "map_site";
	}

	@RequestMapping(value = "/lineMap")
	public String lineMap(Model model, HttpServletResponse response, String lineName,
			@ModelAttribute("_mapLocationKey") MapLocationSession user) {
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		model.addAttribute("lineName", lineName);
		return "map_site";
	}

	@RequestMapping(value = "/public_simple")
	public String public_simple(Model model, HttpServletResponse response, String address) {
		return getGps(model, response, address);
	}

	@RequestMapping(value = "/simple")
	public String map_simple(Model model, HttpServletResponse response, String address) {
		return getGps(model, response, address);
	}

	private String getGps(Model model, HttpServletResponse response, String address) {
		response.setHeader("X-Frame-Options", "SAMEORIGIN");

		Pair<Double, Double> r = busMapService.getLocationFromAddress(model, address);
		if (r != null) {
			model.addAttribute("locationPair", r);
			model.addAttribute("address", address);
		} else {
			model.addAttribute("locationPair", BEIBA_COMPANY);
			model.addAttribute("address", BEIBA_COMPANY_ADDRESS);
			model.addAttribute("BEIBA_COMPANY_NAME", BEIBA_COMPANY_NAME);
		}
		return "map_location";
	}

	@RequestMapping("public-lines")
	@ResponseBody
	public DataTablePage<JpaBusline> getAllLines(Model model, TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city,
			SessionStatus status) {
		DataTablePage<JpaBusline> r = getLines(model, req, cityId, city);
		if (!r.getContent().isEmpty()) {
			List<JpaBusline> f = r.getContent();
			for (JpaBusline jpaBusline : f) {
				jpaBusline.set_month1day(0);
				jpaBusline.set_month2day(0);
				jpaBusline.set_today(0);
				jpaBusline.set_month3day(0);
			}
		}
		return r;
	}
	@RequestMapping("ajax-all-lines")
	@ResponseBody
	public DataTablePage<JpaBusline> lines(Model model, TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city,
			SessionStatus status) {
		DataTablePage<JpaBusline> r = getLines(model, req, cityId, city);
		return r;
	}
	private DataTablePage<JpaBusline> getLines(Model model, TableRequest req, int cityId, JpaCity city) {
		if (city == null || city.getMediaType() != JpaCity.MediaType.body)
			return new DataTablePage(Collections.emptyList());

		String levelStr = req.getFilter("level");
		String searchAdress = req.getFilter("address");
		String siteLine = req.getFilter("siteLine");

		if (StringUtils.isNoneBlank(siteLine)) {
			Page<JpaBusline> w = busMapService.querySiteLineSearch(model, cityId, siteLine, req.getPage(),
					req.getLength(), req.getSort("id"));
			return new DataTablePage(w, req.getDraw());
		}

		if (StringUtils.isNoneBlank(searchAdress)) {
			Page<JpaBusline> w = busMapService.getAllBuslines(model, cityId, searchAdress, req.getPage(),
					req.getLength(), req.getSort("id"));
			return new DataTablePage(w, req.getDraw());
		}
		model.addAttribute("_mapLocationKey", MapLocationSession.EMPTY);
		//status.setComplete();
		JpaBusline.Level level = null;
		if (!StringUtils.isBlank(levelStr)) {
			level = JpaBusline.Level.fromNameStr(levelStr);
			try {
				level = JpaBusline.Level.valueOf(levelStr);
			} catch (Exception e) {
			}
		}
		Page<JpaBusline> w = busService.getAllBuslines(cityId, level, req.getFilter("name"), req.getPage(),
				req.getLength(), req.getSort("id"));
		//busMapService.putLineCarToPageView(req, w)
		return new DataTablePage(w, req.getDraw());
	}
	@RequestMapping(value = "/ajaxdetail/{id}")
	@ResponseBody
	public JpaBusline ajaxdetail(@PathVariable int id, Model model, HttpServletRequest request) {
		return busMapService.findLineById(id);
	}

}
