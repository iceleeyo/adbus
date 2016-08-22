package com.pantuo.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.dao.GoodsBlackRepository;
import com.pantuo.dao.TimeslotRepository;
import com.pantuo.dao.pojo.BoxRemain;
import com.pantuo.dao.pojo.JpaBox;
import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.JpaGoods;
import com.pantuo.dao.pojo.JpaGoodsBlack;
import com.pantuo.dao.pojo.JpaInfoImgSchedule;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaSupplies;
import com.pantuo.dao.pojo.JpaTimeslot;
import com.pantuo.mybatis.domain.BlackAd;
import com.pantuo.mybatis.domain.Box;
import com.pantuo.mybatis.domain.GoodsBlackExample;
import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.mybatis.persistence.GoodsBlackMapper;
import com.pantuo.mybatis.persistence.UserAutoCompleteMapper;
import com.pantuo.pojo.FlatScheduleListItem;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.CityService;
import com.pantuo.service.ContractService;
import com.pantuo.service.OrderService;
import com.pantuo.service.ProductService;
import com.pantuo.service.ReportService;
import com.pantuo.service.ScheduleService;
import com.pantuo.service.SuppliesService;
import com.pantuo.service.TimeslotService;
import com.pantuo.util.DateUtil;
import com.pantuo.util.ExcelUtil;
import com.pantuo.util.OrderIdSeq;
import com.pantuo.util.Pair;
import com.pantuo.vo.MediaInventory;
import com.pantuo.vo.ScheduleView;
import com.pantuo.web.schedule.SchedUltResult;
import com.pantuo.web.schedule.ScheduleInfo;
import com.pantuo.web.view.OrderView;
import com.pantuo.web.view.SolitSortView;
import com.pantuo.web.view.SuppliesView;
import com.pantuo.web.view.report.BlackAdGrouop;
import com.pantuo.web.view.report.FreeBox;
import com.pantuo.web.view.report.Report;
import com.pantuo.web.view.report.UiBox;

import net.sf.jxls.transformer.XLSTransformer;

/**
 * @author tliu
 * @author impanxh重构
 *
 * 排期表controller
 */
@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/schedule")
public class ScheduleController {
	private static Logger log = LoggerFactory.getLogger(ScheduleController.class);

	@Autowired
	private ScheduleService service;

	@Autowired
	private OrderService orderService;
	@Autowired
	@Lazy
	ProductService productService;

	@Autowired
	private TimeslotService timeslotService;
	@Autowired
	private TimeslotRepository timeslotRepository;
	@Autowired
	private UserAutoCompleteMapper userAutoCompleteMapper;

	@Autowired
	private CityService cityService;
	@Autowired
	private ActivitiService activitiService;

	@Autowired
	private SuppliesService suppliesService;
	@Autowired
	private ContractService contractService;

	@Autowired
	private GoodsBlackRepository goodsBlackRepository;
	@Autowired
	GoodsBlackMapper goodsBlackMapper;

	@Autowired
	ReportService reportService;

	public static final List<BlackAd> ls = new ArrayList<BlackAd>();

	/**
	 * 
	 * 
	 * 初始化库存信息
	 *
	 * @param days
	 * @param start
	 * @param city
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "/initBaseBox", method = RequestMethod.GET)
	@ResponseBody
	public String initBaseBox(int days, String start,
			@RequestParam(value = "city", required = false, defaultValue = "1") int city) {
		Date startDate;
		String r = null;
		try {
			startDate = DateUtil.longDf.get().parse(start);
			scheduleService.initAllBoxMemory();
			JpaOrders order = new JpaOrders();
			order.setStartTime(startDate);
			JpaProduct product = new JpaProduct();
			product.setDays(days);
			order.setProduct(product);
			order.setCity(city);
			scheduleService.checkDbBoxState(order, true, null);

		} catch (ParseException e) {
			log.error("params-error :{}", e);
		}
		return r;
	}

	/** 
	 * process 获取进度 
	 * @param request 
	 * @param response 
	 * @return 
	 * @throws Exception 
	 */
	@RequestMapping(value = "/session/{_key}", method = RequestMethod.GET)
	@ResponseBody
	public ScheduleInfo jecprocess2(@PathVariable("_key") String _key, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return (ScheduleInfo) request.getSession().getAttribute(_key);
	}

	/** 
	 * process 获取进度 
	 * @param request 
	 * @param response 
	 * @return 
	 * @throws Exception 
	 */
	@RequestMapping(value = "/process", method = RequestMethod.GET)
	@ResponseBody
	public ScheduleInfo jecprocess(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return (ScheduleInfo) request.getSession().getAttribute("schInfo");
	}

	@RequestMapping(value = "/testsch/{id}/{ischeck}")
	@ResponseBody
	public SchedUltResult testsch(Model model, HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") int id, @PathVariable("ischeck") boolean ischeck,
			@RequestParam(value = "startdate1", required = false) String startdate1,
			@RequestParam(value = "taskid", required = false) String taskid,
			@RequestParam(value = "bm", required = false, defaultValue = "0") int bm,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal) {
		//		long t=System.currentTimeMillis();
		//add by xiaoli
		JpaOrders order = orderService.getJpaOrder(id);
		if (order.getType() == JpaProduct.Type.info || order.getType() == JpaProduct.Type.image) {
			return scheduleService.scheduleInfoImg(order, taskid, startdate1);
		} else {
			SchedUltResult r = scheduleService.checkInventory(id, taskid, startdate1, ischeck, bm == 1, request,
					principal);
			return r;
		}
		//		log.info("SchedUltResult time:{}",System.currentTimeMillis()-t);

	}

	@RequestMapping(value = "/queryFeature/{id}")
	@ResponseBody
	public SchedUltResult queryFeature(Model model, HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") int id, @RequestParam(value = "startdate1", required = false) String startdate1,
			Principal principal) {
		return scheduleService.checkInventory(id, startdate1, request, principal);
	}

	@RequestMapping(value = "/writeExcel/{orderid}")
	public void writeExcel(Model model, HttpServletRequest request, HttpServletResponse response,
			@PathVariable("orderid") int orderid) {
		scheduleService.writeExcel("order-schedule.xls", orderid, response);
	}

	@Autowired
	private ScheduleService scheduleService;

	/**
	 * 排期表表单
	 */
	@RequestMapping("{orderId}")
	public String getOrderSchedule(Model model, @PathVariable("orderId") int orderId, Principal principal) {

		JpaOrders order = orderService.getJpaOrder(orderId);

		if (order != null && order.getStartTime().before(order.getEndTime())) {
			Calendar cal = DateUtil.newCalendar();
			cal.setTime(order.getStartTime());

			List<String> dates = new ArrayList<String>();
			while (cal.getTime().before(order.getEndTime())) {
				dates.add(DateUtil.longDf.get().format(cal.getTime()));
				cal.add(Calendar.DATE, 1);
			}
			model.addAttribute("dates", dates);
			OrderView orderView = activitiService.findOrderViewByOrder(orderId, principal); //new OrderView();
			orderView.setProduct(order.getProduct());
			orderView.setOrder(order);
			JpaCity city = cityService.fromId(order.getCity());
			SuppliesView suppliesView = suppliesService.getSuppliesDetail(order, principal);
			model.addAttribute("orderview", orderView);
			model.addAttribute("orderIdSeq", OrderIdSeq.getLongOrderId(order));
			model.addAttribute("mediaType", city.getMediaType());
			model.addAttribute("suppliesView", suppliesView);
			model.addAttribute("ischedule", "Y");
		}
		return "order_schedule2";
	}

	@RequestMapping("ajax-schedule")
	@ResponseBody
	public Collection<ScheduleView> ajaxSchedule(@RequestParam(value = "orderId", required = true) int orderId) {
		Map<Integer, ScheduleView> map = new LinkedHashMap<Integer, ScheduleView>();

		List<JpaTimeslot> lotlList = timeslotRepository.findAll();
		for (JpaTimeslot jpaTimeslot : lotlList) {
			ScheduleView view = new ScheduleView();
			map.put(jpaTimeslot.getId(), view);
			view.setTimeslot(jpaTimeslot);
		}

		JpaOrders order = orderService.getJpaOrder(orderId);
		if (order != null) {
			String dString = DateUtil.longDf.get().format(order.getStartTime());
			List<MediaInventory> list = userAutoCompleteMapper.getScheduleViewByDateStr(orderId, dString);
			for (MediaInventory mediaInventory : list) {
				ScheduleView scheduleView = map.get(mediaInventory.getSotid());
				String d = DateUtil.longDf.get().format(mediaInventory.getDay());
				scheduleView.getMap().put(d, mediaInventory.getNum());

			}
			return map.values();
		}
		return Collections.EMPTY_LIST;
	}

	@RequestMapping("querySchedule/{taskId}")
	public String querySchedule(Model model, @PathVariable("taskId") String taskId, Principal principal) {
		OrderView orderView = activitiService.findOrderViewByTaskId(taskId, principal);
		JpaOrders order = orderView.getOrder();
		if (order != null && order.getStartTime().before(order.getEndTime())) {
			Calendar cal = DateUtil.newCalendar();
			cal.setTime(order.getStartTime());
			List<String> dates = new ArrayList<String>();
			while (cal.getTime().before(order.getEndTime())) {
				dates.add(DateUtil.longDf.get().format(cal.getTime()));
				cal.add(Calendar.DATE, 1);
			}
			JpaCity city = cityService.fromId(order.getCity());
			SuppliesView suppliesView = suppliesService.getSuppliesDetail(order, principal);
			model.addAttribute("dates", dates);
			model.addAttribute("orderview", orderView);
			model.addAttribute("orderIdSeq", OrderIdSeq.getLongOrderId(order));
			model.addAttribute("mediaType", city.getMediaType());
			model.addAttribute("suppliesView", suppliesView);
			model.addAttribute("ischedule", "Y");
			model.addAttribute("orderId", orderView.getOrder().getId());
		}
		return "order_schedule";
	}

	/**
	 * 排期表
	 */
	@RequestMapping("order-ajax-list")
	@ResponseBody
	public List<Report> getScheduleListForOrder(@RequestParam(value = "orderId", required = true) int orderId,
			@CookieValue(value = "city", defaultValue = "-1") int cityId) {

		try {
			JpaOrders order = orderService.getJpaOrder(orderId);
			if (order.getType() != JpaProduct.Type.video) {
				//TODO: image/info排期
				return Collections.EMPTY_LIST;
			}

			JpaCity city = cityService.fromId(order.getCity());
			if (city.getId() != cityId || city.getMediaType() != JpaCity.MediaType.screen)
				return Collections.EMPTY_LIST;

			List<Report> reports = new LinkedList<Report>();
			Page<JpaTimeslot> slots = timeslotService.getAllTimeslots(cityId, null, 0, 999, null, false);
			Iterable<JpaGoods> goods = service.getGoodsForOrder(orderId);

			Map<Integer, Report> reportMap = new HashMap<Integer, Report>();
			for (JpaTimeslot slot : slots) {
				Report r = new Report(slot);
				reports.add(r);
				reportMap.put(slot.getId(), r);
			}

			for (JpaGoods g : goods) {
				if (g.getBox() == null) {
					log.warn("No boxId for goods {}", g.getId());
				}
				JpaBox b = g.getBox();
				Report r = reportMap.get(b.getSlotId());
				if (r != null) {
					r.addBox(b, g);
				}
			}

			//remove empty rows
			Iterator<Report> iter = reports.iterator();
			while (iter.hasNext()) {
				Report r = iter.next();
				if (r.getBoxes().isEmpty())
					iter.remove();
			}
			return reports;
		} catch (Exception e) {
			log.error("Fail to get schedule for order {}", orderId, e);
			return Collections.EMPTY_LIST;
		}
	}

	/**
	 * 剩余时段表表单
	 * @return
	 */
	@RequestMapping("report")
	public String getScheduleReport(Model model, @RequestParam(value = "from", required = false) String fromStr,
			@RequestParam(value = "end", required = false) String endStr,
			@RequestParam(value = "days", required = false, defaultValue = "7") int days,
			@RequestParam(value = "type", required = false, defaultValue = "video") JpaProduct.Type type) {
		Date from = null, end = null;

		if (StringUtils.isNotBlank(fromStr)) {
			try {
				from = DateUtil.longDf.get().parse(fromStr);
			} catch (Exception e) {
			}
		}
		if (StringUtils.isNotBlank(endStr)) {
			try {
				end = DateUtil.longDf.get().parse(endStr);
			} catch (Exception e) {
			}
		}
		if (from == null) {
			from = new Date();
		}
		if (end == null) {
			end = DateUtils.addDays(from, 10);
		}

		Date d = from;
		List<String> dates = new ArrayList<String>();
		dates.add(DateUtil.longDf.get().format(d));

		while (d.before(end)) {
			d = DateUtils.addDays(d, 1);
			dates.add(DateUtil.longDf.get().format(d));
		}
		model.addAttribute("from", DateUtil.longDf.get().format(from));
		model.addAttribute("end", DateUtil.longDf.get().format(end));
		model.addAttribute("days", days);
		model.addAttribute("dates", dates);
		model.addAttribute("type", type);

		return "schedule_report";
	}

	/*@PreAuthorize(" !hasRole('advertiser')  ")*/
	@PreAuthorize(" hasRole('ShibaOrderManager')" + " or hasRole('ShibaFinancialManager')"
			+ "or hasRole('BeiguangMaterialManager')" + "or hasRole('BeiguangScheduleManager')"
			+ "or hasRole('ShibaSuppliesManager')  ")
	/**
	 * 剩余时段表
	 */
	@RequestMapping("box-ajax-list")
	@ResponseBody
	public List<Report> getScheduleReportList(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		return getBoxResult(req, city);
	}

	/**
	 * 
	 * 剩余时段表导出excel
	 *
	 * @param req
	 * @param city
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 * @author impanxh@gmail.com
	 */
	@RequestMapping("ajax-reportBoxExcel")
	@ResponseBody
	public void reportBoxExcel(TableRequest req,	Principal principal, @CookieValue(value = "city", defaultValue = "-1") int city) {
		req.setPushLet(new com.pantuo.web.push.PushLet("/reportBoxExcel/", principal));
		req.getPushLet().pushMsgToClient("开始查询要导出的数据!");
		List<Report> result = getBoxResult(req, city);
		
		reportService.exportEexcel(req, city, result);
	}

	private List<Report> getBoxResult(TableRequest req, int city) {
		String name = req.getFilter("name");
		String fromStr = req.getFilter("from");
		String endStr = req.getFilter("end");
		int days = req.getFilterInt("days", 7);
		JpaProduct.Type type = req.getFilter("type", JpaProduct.Type.class, JpaProduct.Type.video);

		if (type != JpaProduct.Type.video) {
			//TODO:image/info 排期单
			return Collections.EMPTY_LIST;
		}

		try {
			Page<JpaTimeslot> slots = timeslotService.getAllTimeslots(city, name, 0, 999, null, false);
			Date from = DateUtil.longDf.get().parse(fromStr);
			Date end = DateUtil.longDf.get().parse(endStr);
			List<Box> boxes = service.getBoxes(from, days, end);

			//total row
			long totalDuration = 0;
			Map<String/*date*/, UiBox> totalBoxes = new HashMap<String, UiBox>();
			Date d = from;
			/*for (int i = 0; i < days; i++) {
				UiBox t = new UiBox();
				t.setDay(d);
				totalBoxes.put(DateUtil.longDf.get().format(d), t);
				d = DateUtils.addDays(d, 1);
			}*/
			while (!d.after(end)) {
				UiBox t = new UiBox();
				t.setDay(d);
				totalBoxes.put(DateUtil.longDf.get().format(d), t);
				d = DateUtils.addDays(d, 1);
			}

			List<Report> reports = new LinkedList<Report>();
			Map<Integer, Report> reportMap = new HashMap<Integer, Report>();
			for (JpaTimeslot slot : slots) {
				totalDuration += slot.getDuration();
				Report r = new Report(slot);
				reports.add(r);
				reportMap.put(slot.getId(), r);
			}

			for (Box t : totalBoxes.values()) {
				t.setSize(totalDuration);
				t.setRemain(0L);
			}

			for (Box b : boxes) {
				Report r = reportMap.get(b.getSlotId());
				if (r != null) {
					String key = r.addBox(b);
					Box t = totalBoxes.get(key);
					if (t != null) {
						//System.out.println(key+"  " +t.getRemain() +" -- "+ );
						//	t.setRemain(t.getRemain() - (b.getRemain()-30 + b.getFremain() ));
						t.setRemain(t.getRemain() + b.getRemain() - 30 + b.getFremain());
					}
				}

			}

			//add total row
			Report totalReport = new Report(new JpaTimeslot(city, "汇总", null, totalDuration, false));
			totalReport.setBoxes(totalBoxes);
			reports.add(totalReport);

			return reports;
		} catch (Exception e) {
			log.error("invalid from {}, should be in yyyy-MM-dd", fromStr, e);
			return Collections.EMPTY_LIST;
		}
	}

	/* @PreAuthorize(" !hasRole('advertiser')  ")*/
	@PreAuthorize(" hasRole('ShibaOrderManager')" + " or hasRole('ShibaFinancialManager')"
			+ "or hasRole('BeiguangMaterialManager')" + "or hasRole('BeiguangScheduleManager')"
			+ "or hasRole('ShibaSuppliesManager')  ")
	/**
	 * 排条单表单
	 * @return
	 */
	@RequestMapping("list")
	public String getScheduleList(Model model, @RequestParam(value = "day", required = false) String dayStr,
			@RequestParam(value = "type", required = false, defaultValue = "video") JpaProduct.Type type) {
		Date day = null;

		if (StringUtils.isNotBlank(dayStr)) {
			try {
				day = DateUtil.longDf.get().parse(dayStr);
			} catch (Exception e) {
			}
		}
		if (day == null) {
			day = new Date();
		}

		model.addAttribute("day", DateUtil.longDf.get().format(day));
		model.addAttribute("type", type);

		return "schedule_list";
	}

	@RequestMapping("/mediaInventory")
	public String getmediaInventory(Model model) {
		Date day = new Date();
		model.addAttribute("day", DateUtil.longDf.get().format(day));
		return "mediaInventory";
	}

	@RequestMapping("ajax-mediaInventory")
	@ResponseBody
	public List<MediaInventory> getajaxmediaInventory(TableRequest req) {
		String dayStr = req.getFilter("day");
		if (StringUtils.isNotBlank(dayStr)) {
			return userAutoCompleteMapper.getMediaInventory(dayStr);
		}
		return Collections.emptyList();
	}

	@PreAuthorize(" hasRole('ShibaOrderManager')" + " or hasRole('ShibaFinancialManager')"
			+ "or hasRole('BeiguangMaterialManager')" + "or hasRole('BeiguangScheduleManager')"
			+ "or hasRole('ShibaSuppliesManager')  ")
	@RequestMapping("ajax-sortSolt")
	@ResponseBody
	public List<SolitSortView> sortSolt(TableRequest req,
			@RequestParam(value = "filler", defaultValue = "true") boolean filler,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city) {
		if (city == null || city.getMediaType() != JpaCity.MediaType.screen)
			return Collections.emptyList();

		String dayStr = req.getFilter("day");
		String soltid = req.getFilter("soltid");
		Date day;
		try {
			day = DateUtil.longDf.get().parse(dayStr);
			return service.querySortView(day, NumberUtils.toInt(soltid, Integer.MAX_VALUE), 1);
		} catch (ParseException e) {
			return Collections.EMPTY_LIST;
		}
	}

	@RequestMapping("sortSolit")
	@ResponseBody
	public Pair<Boolean, String> sortSolit(String sortString) {
		return service.sortSolit(sortString);
	}

	/**
	 * 排条单
	 */
	@RequestMapping("box-detail-ajax-list")
	@ResponseBody
	public List<Report> getScheduleDetailList(TableRequest req,
			@RequestParam(value = "filler", defaultValue = "true") boolean filler,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city) {
		if (city == null || city.getMediaType() != JpaCity.MediaType.screen)
			return Collections.emptyList();

		String name = req.getFilter("name");
		String dayStr = req.getFilter("day");
		boolean _loadBlack = BooleanUtils.toBoolean(req.getFilter("_loadBlack"));
		JpaProduct.Type type = req.getFilter("type", JpaProduct.Type.class, JpaProduct.Type.video);

		if (type != JpaProduct.Type.video) {
			//TODO:image/info 排条单
			return Collections.EMPTY_LIST;
		}

		try {
			Page<JpaTimeslot> slots = timeslotService.getAllTimeslots(cityId, name, 0, 999, null, false);
			Date day = DateUtil.longDf.get().parse(dayStr);
			Random ran = new Random(day.getTime());
			LinkedHashMap<Long, List<Supplies>> fillerSupplies = suppliesService.queryFillerSupplies(cityId);

			List<Supplies> blackSupplies = suppliesService.queryAllBlackSupplies(cityId);

			Map<Integer, Supplies> blackSuppliesMap = buildSuppliesMap(blackSupplies);

			Iterable<JpaBox> boxes = service.getBoxesAndGoods(day, 1);
			//查底版 impanxh
			Iterable<JpaGoodsBlack> blacks = service.getFreeGoods(day, 1);

			Map<Integer, List<JpaGoodsBlack>> timeslotMap = chageList2Map(blacks);

			//total row
			long totalDuration = 0;
			Map<String/*date*/, UiBox> totalBoxes = new HashMap<String, UiBox>();
			Date d = day;
			String dStr = DateUtil.longDf.get().format(d);
			for (int i = 0; i < 1; i++) {
				UiBox t = new UiBox();
				t.setDay(d);
				totalBoxes.put(DateUtil.longDf.get().format(d), t);
				d = DateUtils.addDays(d, 1);
			}

			List<Report> reports = new LinkedList<Report>();
			Map<Integer, Report> reportMap = new HashMap<Integer, Report>();
			for (JpaTimeslot slot : slots) {
				totalDuration += slot.getDuration();
				Report r = new Report(slot);
				reports.add(r);
				reportMap.put(slot.getId(), r);
			}

			for (Box t : totalBoxes.values()) {
				t.setSize(totalDuration);
				t.setRemain(totalDuration);
			}

			for (JpaBox b : boxes) {
				if (filler) {
					///    fillBoxWithFiller(ran, b, fillerSupplies);
				}
				Report r = reportMap.get(b.getSlotId());
				if (r != null) {
					String key = r.addBox(b);
					Box t = totalBoxes.get(key);
					if (t != null) {
						t.setRemain(t.getRemain() - (b.getSize() - b.getRemain()));
					}
				}

			}
			if (_loadBlack) {
				AtomicInteger jicount = new AtomicInteger(0);
				for (Report r : reports) {
					findFreeGoodsInBoxes(jicount, r.getBoxes(), blackSupplies);
					if (filler && r.getBoxes().isEmpty()) {
						//   fillReportWithFiller(ran, cityId, day, r, fillerSupplies);
					}
				}
			}

			//add total row
			/*            Report totalReport = new Report(new JpaTimeslot("汇总", null, totalDuration, false));
			            totalReport.setBoxes(totalBoxes);
			            reports.add(totalReport);*/

			return flatDetailForGoods(dStr, reports, new BlackAdGrouop(timeslotMap, blackSuppliesMap));
		} catch (Exception e) {
			log.error("invalid day {}, should be in yyyy-MM-dd", dayStr, e);
			return Collections.EMPTY_LIST;
		}
	}

	public Map<Integer, Supplies> buildSuppliesMap(Iterable<Supplies> iterable) {
		Map<Integer, Supplies> _temp = new HashMap<Integer, Supplies>();
		for (Supplies v : iterable) {
			_temp.put(v.getId(), v);
		}
		return _temp;
	}

	public Map<Integer, List<JpaGoodsBlack>> chageList2Map(Iterable<JpaGoodsBlack> iterable) {

		Map<Integer, List<JpaGoodsBlack>> _temp = new HashMap<Integer, List<JpaGoodsBlack>>();
		for (JpaGoodsBlack jpaGoodsBlack : iterable) {
			if (!_temp.containsKey(jpaGoodsBlack.getSlotId())) {
				_temp.put(jpaGoodsBlack.getSlotId(), new ArrayList<JpaGoodsBlack>());
			}
			_temp.get(jpaGoodsBlack.getSlotId()).add(jpaGoodsBlack);
		}
		return _temp;
	}

	public void deleteBlack(int solotid, Date day) {
		GoodsBlackExample example = new GoodsBlackExample();
		GoodsBlackExample.Criteria c = example.createCriteria();
		c.andSlotIdEqualTo(solotid);
		c.andDayEqualTo(day);
		goodsBlackMapper.deleteByExample(example);
	}

	/**
	 * 
	 * 找单个boxes 里空的goods并存储到goods_black表
	 *
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public void findFreeGoodsInBoxes(AtomicInteger jicount, Map<String, UiBox> boxes, List<Supplies> blackSupplies) {
		int s = blackSupplies.size();
		for (Map.Entry<String, UiBox> entry : boxes.entrySet()) {
			UiBox uiBox = entry.getValue();
			List<FreeBox> frees = uiBox.fetchFreeGoods();
			deleteBlack(uiBox.getSlotId(), uiBox.getDay());
			if (!blackSupplies.isEmpty()) {
				List<JpaGoodsBlack> saveList = new ArrayList<JpaGoodsBlack>();
				for (FreeBox freeBox : frees) {
					long _c = freeBox.begin;

					while (_c < freeBox.end) {

						if (jicount.get() >= s) {
							jicount.set(0);
							;
						}
						Supplies _indexSupplie = blackSupplies.get(jicount.get());
						if (_c + _indexSupplie.getDuration() <= freeBox.end) {
							JpaGoodsBlack e = new JpaGoodsBlack();
							e.setSlotId(uiBox.getSlotId());
							e.setDay(uiBox.getDay());
							e.setSuppliesId(_indexSupplie.getId());
							e.setInboxPosition(_c);
							e.setSort_index((int) _c);
							e.setCity(uiBox.getCity());
							e.setSize(_indexSupplie.getDuration());

							saveList.add(e);
							_c += _indexSupplie.getDuration();

						}
						jicount.incrementAndGet();
					}
				}
				goodsBlackRepository.save(saveList);
			}

			//System.out.println(uiBox.getSlotId());
		}

	}

	private void fillReportWithFiller(Random ran, int city, Date day, Report report,
			LinkedHashMap<Long, List<Supplies>> suppliesMap) {
		JpaBox box = new JpaBox(city, day, report.getSlot());
		fillBoxWithFiller(ran, box, suppliesMap);
	}

	private void fillBoxWithFiller(Random ran, JpaBox box, LinkedHashMap<Long, List<Supplies>> suppliesMap) {
		List<BoxRemain.Remain> remains = box.getRemains().getRemains();
		for (BoxRemain.Remain remain : remains) {
			long start = remain.getStart();
			long size = remain.getSize();
			for (long rem = size; rem > 0;) {
				List<Supplies> supplies = suppliesMap.get(rem);
				if (supplies == null) {
					Iterator<Map.Entry<Long, List<Supplies>>> iter = suppliesMap.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry<Long, List<Supplies>> e = iter.next();
						if (e.getKey() <= rem) {
							supplies = e.getValue();
							break;
						}
					}
				}
				if (supplies != null) {
					Supplies s = supplies.get(ran.nextInt(supplies.size()));
					JpaSupplies js = new JpaSupplies(box.getCity(), s.getName(),
							JpaProduct.Type.values()[s.getSuppliesType()], s.getIndustryId(), s.getUserId(),
							s.getDuration(), s.getFilePath(), s.getInfoContext(),
							/*JpaSupplies.Status.values()[s.getStats()],*/null, s.getOperFristuser(),
							s.getOperFristcomment(), s.getOperFinaluser(), s.getOperFinalcomment(), s.getSeqNumber(),
							s.getCarNumber(), s.getResponseCid());
					JpaProduct p = new JpaProduct(Integer.MAX_VALUE, JpaProduct.Type.video, "filler", s.getDuration(),
							0, 0, 0, 0, 0, 0, 0, 0, true, true, false, null, null);
					JpaOrders o = new JpaOrders(Integer.MAX_VALUE, "", js, p, null, 0, null, null, null,
							JpaProduct.Type.video, JpaOrders.PayType.remit, JpaOrders.Status.completed, null, null, 0,
							null, null, null, null, null);
					JpaGoods g = new JpaGoods(box.getCity(), 0, s.getDuration(), false, false, 0);
					g.setOrder(o);
					box.put(g, start);
					start += s.getDuration();
					rem -= s.getDuration();
				} else {
					log.warn("No suitable supplies to fillBoxWithFiller, rem={}", rem);
					break;
				}
			}
		}
	}

	class Key_slot {
		String slot;
		int slotSize;
		private String slotDesc;

		public String getSlot() {
			return slot;
		}

		public void setSlot(String slot) {
			this.slot = slot;
		}

		public int getSlotSize() {
			return slotSize;
		}

		public void setSlotSize(int slotSize) {
			this.slotSize = slotSize;
		}

		public String getSlotDesc() {
			return slotDesc;
		}

		public void setSlotDesc(String slotDesc) {
			this.slotDesc = slotDesc;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((slot == null) ? 0 : slot.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key_slot other = (Key_slot) obj;
			if (slot == null) {
				if (other.slot != null)
					return false;
			} else if (!slot.equals(other.slot))
				return false;
			return true;
		}

		public Key_slot(String slot, int slotSize, String slotDesc) {
			super();
			this.slot = slot;
			this.slotSize = slotSize;
			this.slotDesc = slotDesc;
		}
	}

	public List<FlatScheduleListItem> getTotalRecord(List list, String monthDay) {
		List<FlatScheduleListItem> scheduleList = new ArrayList<FlatScheduleListItem>();
		Map<Key_slot, List<FlatScheduleListItem>> map = new LinkedHashMap<Key_slot, List<FlatScheduleListItem>>();
		for (Object object : list) {
			FlatScheduleListItem obj = (FlatScheduleListItem) object;
			Key_slot key = new Key_slot(obj.getSlot(), obj.getSlotSize(), obj.getSlotDesc());
			if (!map.containsKey(key)) {
				map.put(key, new ArrayList<FlatScheduleListItem>());
			}
			map.get(key).add(obj);
		}
		for (Map.Entry<Key_slot, List<FlatScheduleListItem>> object : map.entrySet()) {
			List<FlatScheduleListItem> w = object.getValue();
			while (ismore(object)) {
				BlackAd blackAd = getFristEmelemt(ls);
				if (null != blackAd) {
					FlatScheduleListItem sw = new FlatScheduleListItem();
					sw.setMaterialName(blackAd.getSeqNumber() + "-" + blackAd.getAdName());
					sw.setMaterialSize(blackAd.getDuration().intValue());
					sw.setSlotDesc(object.getKey().slotDesc);
					sw.setMonthDay(monthDay);
					sw.setSlot(object.getKey().slot);
					sw.setSlotSize(object.getKey().slotSize);
					w.add(sw);
				}
			}
		}
		Collection<List<FlatScheduleListItem>> mapList = map.values();
		for (List<FlatScheduleListItem> list2 : mapList) {
			scheduleList.addAll(list2);
		}
		return scheduleList;
	}

	private BlackAd getFristEmelemt(List<BlackAd> sortTree) {
		if (sortTree.isEmpty()) {
			List<BlackAd> bList = contractService.queryAllBlackAd();
			if (bList.size() > 0) {
				for (BlackAd blackAd : bList) {
					ls.add(blackAd);
				}
			} else {
				return null;
			}
		}
		BlackAd frist = sortTree.get(0);
		sortTree.remove(frist);
		sortTree.add(sortTree.size(), frist);
		return frist;
	}

	public Boolean ismore(Map.Entry<Key_slot, List<FlatScheduleListItem>> object) {
		List<FlatScheduleListItem> w = object.getValue();
		int c = 0;
		for (FlatScheduleListItem b : w) {
			if (b.getMaterialSize() != null)
				c += b.getMaterialSize();
		}
		if (c == 0) {
			w.clear();
		}
		if (object.getKey().slotSize >= c) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 排条单
	 */
	@RequestMapping("exportList")
	public void exportScheduleDetailList(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city,
			HttpServletResponse resp) {
		String dayStr = req.getFilter("day");

		String dayStr2 = dayStr;
		String monthDay = dayStr;
		int dayNumber = 0;
		int year = 0;
		try {
			Date day = DateUtil.longDf.get().parse(dayStr);
			Calendar cal = DateUtil.newCalendar();
			cal.setTime(day);
			year = cal.get(Calendar.YEAR);
			Calendar cal0 = DateUtil.newCalendar();
			cal0.set(year, Calendar.JANUARY, 1);
			cal0.set(Calendar.HOUR, 0);
			cal0.set(Calendar.MINUTE, 0);
			cal0.set(Calendar.SECOND, 0);
			cal0.set(Calendar.MILLISECOND, 0);
			while (cal0.before(cal)) {
				dayNumber++;
				cal0.add(Calendar.DATE, 1);
			}

			dayStr2 = DateUtil.longDf3.get().format(day);
			monthDay = DateUtil.longDf4.get().format(day);
		} catch (Exception e) {
		}

		String templateFileName = "/jxls/schedule_list.xls";
		List scheduleList = new ArrayList();
		List<Report> list = getScheduleDetailList(req, true, cityId, city);
		for (Report r : list) {
			scheduleList.add(new FlatScheduleListItem(monthDay, r));
		}
		//        for (Object r : scheduleList) {
		//        	FlatScheduleListItem w=	 (FlatScheduleListItem) (r);
		//        	System.err.println(w.toString());
		//        }
		//        scheduleList= getTotalRecord(scheduleList,monthDay);
		Map beans = new HashMap();
		beans.put("report", scheduleList);
		beans.put("title", dayStr2 + "全天档_二频道_标准版广告排条单");
		beans.put("number", "编号:BJ-公交（日）-" + year + "-" + dayNumber);
		XLSTransformer transformer = new XLSTransformer();
		try {
			resp.setHeader("Content-Type", "application/x-xls");
			resp.setHeader("Content-Disposition", "attachment; filename=\"schedule-[" + dayStr + "].xls\"");
			InputStream is = new BufferedInputStream(ScheduleController.class.getResourceAsStream(templateFileName));
			org.apache.poi.ss.usermodel.Workbook workbook = transformer.transformXLS(is, beans);

			ExcelUtil.dynamicMergeCells((HSSFSheet) workbook.getSheetAt(0), 1, 0, 1, 2);

			OutputStream os = new BufferedOutputStream(resp.getOutputStream());
			workbook.write(os);
			is.close();
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error("Fail to export excel for city {}, req {}", cityId, req);
			throw new RuntimeException("Fail to export excel", e);
		}
	}

	@RequestMapping("info-list")
	public String getinfoScheduleList(Model model, @RequestParam(value = "day", required = false) String dayStr,
			@RequestParam(value = "type", required = false, defaultValue = "video") JpaProduct.Type type) {
		Date day = null;

		if (StringUtils.isNotBlank(dayStr)) {
			try {
				day = DateUtil.longDf.get().parse(dayStr);
			} catch (Exception e) {
			}
		}
		if (day == null) {
			day = new Date();
		}

		model.addAttribute("day", DateUtil.longDf.get().format(day));
		model.addAttribute("type", type);

		return "InfoSchedule_list";
	}

	@RequestMapping("img-list")
	public String getimgScheduleList(Model model, @RequestParam(value = "day", required = false) String dayStr,
			@RequestParam(value = "type", required = false, defaultValue = "video") JpaProduct.Type type) {
		Date day = null;

		if (StringUtils.isNotBlank(dayStr)) {
			try {
				day = DateUtil.longDf.get().parse(dayStr);
			} catch (Exception e) {
			}
		}
		if (day == null) {
			day = new Date();
		}

		model.addAttribute("day", DateUtil.longDf.get().format(day));
		model.addAttribute("type", type);

		return "ImgSchedule_list";
	}

	@RequestMapping("info-ajax-list/{mtype}")
	@ResponseBody
	public List<JpaInfoImgSchedule> getJpaInfoSchedule(TableRequest req, @PathVariable("mtype") String mtype,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal) throws ParseException {
		return timeslotService.getInfoSchedule(city, req, principal, mtype);
	}

	/**
		 * 图片info排条单
		 * @throws ParseException 
		 */
	@RequestMapping("exportInfoImglist/{mtype}")
	public void exportInfoScheduleDetailList(TableRequest req, @PathVariable("mtype") String mtype,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city,
			HttpServletResponse resp) throws ParseException {
		String dayStr = req.getFilter("day");
		String dayStr2 = dayStr;
		try {
			Date day = DateUtil.longDf.get().parse(dayStr);
			dayStr2 = DateUtil.longDf3.get().format(day);
		} catch (Exception e) {
		}
		List<JpaInfoImgSchedule> repoList = timeslotService.getInfoSchedule(cityId, req, null, mtype);
		String templateFileName = "";
		Map beans = new HashMap();
		beans.put("report", repoList);
		if (StringUtils.equals("info", mtype)) {
			templateFileName = "/jxls/InfoSchedule_list.xls";
			beans.put("title", dayStr2 + "_INFO字幕服务信息日编单");
			beans.put("total", repoList.size());
		} else {
			templateFileName = "/jxls/ImgSchedule_list.xls";
			beans.put("title", dayStr2 + "_INFO图片服务信息日编单");
			beans.put("attachsize", repoList.size());
			beans.put("supplisize", supplisize(repoList));

		}
		XLSTransformer transformer = new XLSTransformer();
		try {
			resp.setHeader("Content-Type", "application/x-xls");
			resp.setHeader("Content-Disposition", "attachment; filename=\"" + mtype + "schedule-[" + dayStr + "].xls\"");
			InputStream is = new BufferedInputStream(ScheduleController.class.getResourceAsStream(templateFileName));
			org.apache.poi.ss.usermodel.Workbook workbook = transformer.transformXLS(is, beans);
			if (!StringUtils.equals("info", mtype)) {
				ExcelUtil.dynamicMergeCells((HSSFSheet) workbook.getSheetAt(0), 1, 0, 1, 2, 3);
			}
			OutputStream os = new BufferedOutputStream(resp.getOutputStream());
			workbook.write(os);
			is.close();
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error("Fail to export excel for city {}, req {}", cityId, req);
			throw new RuntimeException("Fail to export excel", e);
		}
	}

	private int supplisize(List<JpaInfoImgSchedule> list) {
		Set set = new HashSet();
		for (JpaInfoImgSchedule jpaInfoImgSchedule : list) {
			set.add(jpaInfoImgSchedule.getOrder().getSuppliesId());
		}
		return set.size();
	}

	private List<Report> flatDetailForGoods(String day, List<Report> reports, BlackAdGrouop blackGroup) {
		List<Report> list = new LinkedList<Report>();
		for (Report r : reports) {
			UiBox box = r.getBox(day);
			/*if (box == null || box.getGoods().isEmpty()) {
				//fill in blank box
				list.add(r);//by liuchao
			} else {
				//create report records for each goods
				List<JpaGoods> goods = box.fetchSortedGoods(true, blackGroup);
				//clear goods to make it ready for later copy
				box.setGoods(null);
				for (JpaGoods g : goods) {
					UiBox b = new UiBox(box);
					b.addGood(g);
					Report newReport = new Report(r.getSlot());
					newReport.addBox(b);
					list.add(newReport);
				}
			}*/

			if (box != null) {
				//create report records for each goods
				List<JpaGoods> goods = box.fetchSortedGoods(true, blackGroup);
				//clear goods to make it ready for later copy
				box.setGoods(null);
				for (JpaGoods g : goods) {
					UiBox b = new UiBox(box);
					b.addGood(g);
					Report newReport = new Report(r.getSlot());
					newReport.addBox(b);
					list.add(newReport);
				}
			}
		}
		return list;
	}

}
