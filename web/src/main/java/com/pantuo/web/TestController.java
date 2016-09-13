package com.pantuo.web;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.service.NewScheduleService;
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
     @Autowired
     NewScheduleService newScheduleService;
	@RequestMapping("/a/public_test")
	@ResponseBody
	public void getAllContracts(ScheduleRequest req, @DateTimeFormat(pattern = "yyyy-MM-dd") Date c2) {

		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(req.getCreateTime()));

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