package com.pantuo.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.service.NewScheduleService;
import com.pantuo.util.HttpTookit;
import com.pantuo.util.JsonTools;
import com.pantuo.web.schedule.OrderRequest;
import com.pantuo.web.schedule.SchedUltResult;
import com.pantuo.web.view.ScheduleRequest;

/**
 * Index controller
 *
 * @author tliu
 */
@Controller
public class TestController {

	private static Logger log = LoggerFactory.getLogger(TestController.class);
	@Autowired
	NewScheduleService newScheduleService;

	@RequestMapping("/a/public_test")
	@ResponseBody
	public void getAllContracts(ScheduleRequest req, @DateTimeFormat(pattern = "yyyy-MM-dd") Date c2) {

		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(req.getCreateTime()));

	}

	@RequestMapping("/public_url/{key}")
	@ResponseBody
	public void public_url(@PathVariable("key") String key, HttpServletResponse response) {
		try {

			String json = HttpTookit.doGet("http://60.205.168.48:9009/url/get/" + key, StringUtils.EMPTY);
			if (StringUtils.isNoneBlank(json)) {
				log.info(json);
				Map<String, Object> map = (Map<String, Object>) JsonTools.readValue(json, Map.class);
				if (map.containsKey("sourceUrl_s")) {
					response.sendRedirect((String) map.get("sourceUrl_s"));
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@RequestMapping("/putTocar")
	@ResponseBody
	public void putTocar(OrderRequest req) {
		System.out.println(req.toString());

	}

	@RequestMapping("/checkTimeSole")
	@ResponseBody
	public SchedUltResult checkTimeSole(OrderRequest req) {
		System.out.println(req.toString());
		return newScheduleService.checkTimeSole(req);

	}

}
