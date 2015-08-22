package com.pantuo.web;

import java.util.Collections;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.BusMapService;
import com.pantuo.service.BusService;
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
public class BusLineMapController {

	private static Logger log = LoggerFactory.getLogger(BusController.class);
	@Autowired
	private BusService busService;

	@Autowired
	private BusMapService busMapService;

	@RequestMapping(value = "/lines")
	public String lines() {
		return "map_lines";
	}

	@RequestMapping(value = "/lineMap")
	public String lineMap(Model model, HttpServletResponse response, String lineName) {
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		model.addAttribute("lineName", lineName);
		return "map_site";
	}

	@RequestMapping(value = "/simple")
	public String map_simple(Model model, HttpServletResponse response, String address) {
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		model.addAttribute("locationPair", busMapService.getLocationFromAddress(model, address));
		model.addAttribute("address", address);
		return "map_location";
	}

	@RequestMapping("ajax-all-lines")
	@ResponseBody
	public DataTablePage<JpaBusline> getAllLines(Model model, TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city) {
		if (city == null || city.getMediaType() != JpaCity.MediaType.body)
			return new DataTablePage(Collections.emptyList());

		String levelStr = req.getFilter("level");
		String searchAdress = req.getFilter("address");
		System.out.println(searchAdress);

		if (StringUtils.isNoneBlank(searchAdress)) {
			Page<JpaBusline> w = busMapService.getAllBuslines(model, cityId, searchAdress, req.getPage(),
					req.getLength(), req.getSort("id"));
			return new DataTablePage(w, req.getDraw());
		}
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
}
