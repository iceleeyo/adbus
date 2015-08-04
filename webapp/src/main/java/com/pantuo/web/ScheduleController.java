package com.pantuo.web;

import com.pantuo.dao.pojo.*;
import com.pantuo.mybatis.domain.BlackAd;
import com.pantuo.mybatis.domain.Box;
import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.FlatScheduleListItem;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.*;
import com.pantuo.util.DateUtil;
import com.pantuo.util.ExcelUtil;
import com.pantuo.util.GlobalMethods;
import com.pantuo.util.OrderIdSeq;
import com.pantuo.util.Pair;
import com.pantuo.web.view.OrderView;
import com.pantuo.web.view.SuppliesView;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.swing.table.TableModel;

import java.io.*;
import java.security.Principal;
import java.text.ParseException;
import java.util.*;

/**
 * @author tliu
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
    private TimeslotService timeslotService;

    @Autowired
    private CityService cityService;
    @Autowired
    private ActivitiService activitiService;

    @Autowired
    private BusScheduleService busScheduleService;
    @Autowired
	private SuppliesService suppliesService;
    @Autowired
    private ContractService contractService;
    public static final List<BlackAd> ls = new ArrayList<BlackAd>();
    /**
     * 排期表表单
     */
    @RequestMapping("{orderId}")
    public String getOrderSchedule (
            Model model,
            @PathVariable("orderId") int orderId,Principal principal) {
    	
        JpaOrders order = orderService.getJpaOrder(orderId);

        if (order != null && order.getStartTime().before(order.getEndTime())) {
            Calendar cal = DateUtil.newCalendar();
            cal.setTime(order.getStartTime());

            List<String> dates = new ArrayList<String> ();
            while (cal.getTime().before(order.getEndTime())) {
                dates.add(DateUtil.longDf.get().format(cal.getTime()));
                cal.add(Calendar.DATE, 1);
            }
            model.addAttribute("dates", dates);
            OrderView orderView = activitiService.findOrderViewByOrder(orderId, principal); //new OrderView();
            orderView.setProduct(order.getProduct());
            orderView.setOrder(order);
            JpaCity city = cityService.fromId(order.getCity());
            SuppliesView suppliesView = suppliesService.getSuppliesDetail(order.getSuppliesId(), null);
            model.addAttribute("orderview", orderView);
            model.addAttribute("orderIdSeq", OrderIdSeq.getLongOrderId(order));
            model.addAttribute("mediaType", city.getMediaType());
            model.addAttribute("suppliesView", suppliesView);
            model.addAttribute("ischedule", "Y");
        }
        return "order_schedule";
    }
    @RequestMapping("querySchedule/{taskId}")
    public String querySchedule (Model model,@PathVariable("taskId") String taskId,Principal principal) {
    	OrderView orderView=activitiService.findOrderViewByTaskId(taskId, principal);
    	JpaOrders order = orderView.getOrder();
    	if (order != null && order.getStartTime().before(order.getEndTime())) {
    		Calendar cal = DateUtil.newCalendar();
    		cal.setTime(order.getStartTime());
    		List<String> dates = new ArrayList<String> ();
    		while (cal.getTime().before(order.getEndTime())) {
    			dates.add(DateUtil.longDf.get().format(cal.getTime()));
    			cal.add(Calendar.DATE, 1);
    		}
    		JpaCity city = cityService.fromId(order.getCity());
    		SuppliesView suppliesView = suppliesService.getSuppliesDetail(order.getSuppliesId(), null);
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
    public List<Report> getScheduleListForOrder(
            @RequestParam(value = "orderId", required = true) int orderId,
            @CookieValue(value="city", defaultValue = "-1") int cityId) {

        try {
            JpaOrders order = orderService.getJpaOrder(orderId);
            if (order.getType() != JpaProduct.Type.video) {
                //TODO: image/info排期
                return Collections.EMPTY_LIST;
            }

            JpaCity city = cityService.fromId(order.getCity());
            if (city.getId() != cityId || city.getMediaType() != JpaCity.MediaType.screen)
                return Collections.EMPTY_LIST;

            List<Report> reports = new LinkedList<Report> ();
            Page<JpaTimeslot> slots = timeslotService.getAllTimeslots(cityId, null, 0, 999, null, false);
            Iterable<JpaGoods> goods = service.getGoodsForOrder(orderId);

            Map<Integer, Report> reportMap = new HashMap<Integer, Report> ();
            for (JpaTimeslot slot : slots) {
                Report r = new Report(slot);
                reports.add(r);
                reportMap.put(slot.getId(), r);
            }

            for (JpaGoods g : goods) {
                if (g.getBox() == null) {
                    log.warn ("No boxId for goods {}", g.getId());
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
     * 排期表
     */
    @RequestMapping("order-body-ajax-list")
    @ResponseBody
    public DataTablePage<JpaBusSchedule> getBodyScheduleListForOrder(
            @RequestParam(value = "orderId", required = true) int orderId,
            @CookieValue(value="city", defaultValue = "-1") int cityId,
            TableRequest req) {

        try {
            JpaOrders order = orderService.getJpaOrder(orderId);
            if (order.getType() != JpaProduct.Type.body) {
                return new DataTablePage(Collections.EMPTY_LIST);
            }

            JpaCity city = cityService.fromId(order.getCity());
            if (city.getId() != cityId || city.getMediaType() != JpaCity.MediaType.body)
                return new DataTablePage(Collections.EMPTY_LIST);

            Page<JpaBusSchedule> busSchedules = busScheduleService.getByOrder(cityId, orderId,
                    req.getPage(), req.getLength(), req.getSort("id"));

            return new DataTablePage<>(busSchedules, req.getDraw());
        } catch (Exception e) {
            log.error("Fail to get schedule for order {}", orderId, e);
            return new DataTablePage(Collections.EMPTY_LIST);
        }
    }

    /**
     * 剩余时段表表单
     * @return
     */
    @RequestMapping("report")
    public String getScheduleReport(Model model,
            @RequestParam(value = "from", required = false) String fromStr,
            @RequestParam(value = "days", required = false, defaultValue = "7") int days,
            @RequestParam(value = "type", required = false, defaultValue = "video") JpaProduct.Type type) {
        Date from = null;

        if (StringUtils.isNotBlank(fromStr)) {
            try {
                from = DateUtil.longDf.get().parse(fromStr);
            } catch (Exception e) {}
        }
        if (from == null) {
            from = new Date();
        }

        Date d = from;
        List<String> dates = new ArrayList<String> ();
        dates.add(DateUtil.longDf.get().format(d));
        for (int i = 0 ; i< days - 1; i++) {
            d = DateUtils.addDays(d, 1);
            dates.add(DateUtil.longDf.get().format(d));
        }
        model.addAttribute("from", DateUtil.longDf.get().format(from));
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
    public List<Report> getScheduleReportList (TableRequest req,
                                               @CookieValue(value="city", defaultValue = "-1") int city) {
        String name = req.getFilter("name");
        String fromStr = req.getFilter("from");
        int days = req.getFilterInt("days", 7);
        JpaProduct.Type type = req.getFilter("type", JpaProduct.Type.class, JpaProduct.Type.video);

        if (type != JpaProduct.Type.video) {
            //TODO:image/info 排期单
            return Collections.EMPTY_LIST;
        }

        try {
            Page<JpaTimeslot> slots = timeslotService.getAllTimeslots(city, name, 0, 999, null, false);
            Date from = DateUtil.longDf.get().parse(fromStr);
            List<Box> boxes = service.getBoxes(from, days);

            //total row
            long totalDuration = 0;
            Map<String/*date*/, UiBox> totalBoxes = new HashMap<String, UiBox> ();
            Date d = from;
            for (int i=0; i<days; i++) {
                UiBox t = new UiBox();
                t.setDay(d);
                totalBoxes.put(DateUtil.longDf.get().format(d), t);
                d = DateUtils.addDays(d, 1);
            }

            List<Report> reports = new LinkedList<Report> ();
            Map<Integer, Report> reportMap = new HashMap<Integer, Report> ();
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

            for (Box b : boxes) {
                Report r = reportMap.get(b.getSlotId());
                if (r != null) {
                    String key = r.addBox(b);
                    Box t = totalBoxes.get(key);
                    if (t != null) {
                        t.setRemain(t.getRemain()- (b.getSize() - b.getRemain()));
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
    public String getScheduleList(Model model,
                                    @RequestParam(value = "day", required = false) String dayStr,
                                    @RequestParam(value = "type", required = false, defaultValue = "video") JpaProduct.Type type) {
        Date day = null;

        if (StringUtils.isNotBlank(dayStr)) {
            try {
                day = DateUtil.longDf.get().parse(dayStr);
            } catch (Exception e) {}
        }
        if (day == null) {
            day = new Date();
        }

        model.addAttribute("day", DateUtil.longDf.get().format(day));
        model.addAttribute("type", type);

        return "schedule_list";
    }
	@PreAuthorize(" hasRole('ShibaOrderManager')" + " or hasRole('ShibaFinancialManager')"
			+ "or hasRole('BeiguangMaterialManager')" + "or hasRole('BeiguangScheduleManager')"
			+ "or hasRole('ShibaSuppliesManager')  ")
    /**
     * 排条单
     */
    @RequestMapping("box-detail-ajax-list")
    @ResponseBody
    public List<Report> getScheduleDetailList (TableRequest req,
                                               @RequestParam(value="filler", defaultValue = "true") boolean filler,
                                               @CookieValue(value="city", defaultValue = "-1") int cityId,
                                               @ModelAttribute("city") JpaCity city) {
        if (city == null || city.getMediaType() != JpaCity.MediaType.screen)
            return Collections.emptyList();

        String name = req.getFilter("name");
        String dayStr = req.getFilter("day");
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

            Iterable<JpaBox> boxes = service.getBoxesAndGoods(day, 1);

            //total row
            long totalDuration = 0;
            Map<String/*date*/, UiBox> totalBoxes = new HashMap<String, UiBox> ();
            Date d = day;
            String dStr = DateUtil.longDf.get().format(d);
            for (int i=0; i<1; i++) {
                UiBox t = new UiBox();
                t.setDay(d);
                totalBoxes.put(DateUtil.longDf.get().format(d), t);
                d = DateUtils.addDays(d, 1);
            }

            List<Report> reports = new LinkedList<Report> ();
            Map<Integer, Report> reportMap = new HashMap<Integer, Report> ();
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
                    fillBoxWithFiller(ran, b, fillerSupplies);
                }
                Report r = reportMap.get(b.getSlotId());
                if (r != null) {
                    String key = r.addBox(b);
                    Box t = totalBoxes.get(key);
                    if (t != null) {
                        t.setRemain(t.getRemain()- (b.getSize() - b.getRemain()));
                    }
                }

            }

            for (Report r : reports) {
                if (filler && r.getBoxes().isEmpty()) {
                    fillReportWithFiller(ran, cityId, day, r, fillerSupplies);
                }
            }

            //add total row
/*            Report totalReport = new Report(new JpaTimeslot("汇总", null, totalDuration, false));
            totalReport.setBoxes(totalBoxes);
            reports.add(totalReport);*/

            return flatDetailForGoods(dStr, reports);
        } catch (Exception e) {
            log.error("invalid day {}, should be in yyyy-MM-dd", dayStr, e);
            return Collections.EMPTY_LIST;
        }
    }

    private void fillReportWithFiller(Random ran, int city, Date day, Report report, LinkedHashMap<Long, List<Supplies>> suppliesMap) {
        JpaBox box = new JpaBox(city, day, report.getSlot());
        fillBoxWithFiller(ran, box, suppliesMap);
    }

    private void fillBoxWithFiller(Random ran, JpaBox box, LinkedHashMap<Long, List<Supplies>> suppliesMap) {
        List<BoxRemain.Remain> remains = box.getRemains().getRemains();
        for (BoxRemain.Remain remain : remains) {
            long start = remain.getStart();
            long size = remain.getSize();
            for (long rem=size; rem > 0;) {
                List<Supplies> supplies = suppliesMap.get(rem);
                if (supplies == null) {
                    Iterator<Map.Entry<Long,List<Supplies>>> iter = suppliesMap.entrySet().iterator();
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
                            JpaProduct.Type.values()[s.getSuppliesType()],
                            s.getIndustryId(),
                            s.getUserId(), s.getDuration(), s.getFilePath(), s.getInfoContext(),
                            /*JpaSupplies.Status.values()[s.getStats()],*/null,
                            s.getOperFristuser(), s.getOperFristcomment(),
                            s.getOperFinaluser(),s.getOperFinalcomment(),
                            s.getSeqNumber(), s.getCarNumber(), s.getResponseCid());
                    JpaProduct p = new JpaProduct(
                            Integer.MAX_VALUE, JpaProduct.Type.video,
                            "filler", s.getDuration(),
                            0, 0, 0, 0,
                            JpaBusline.Level.A,
                            0, 0, 0, 0,
                            true, true, false, null, null);
                    JpaOrders o = new JpaOrders(Integer.MAX_VALUE, "", js, p, null, 0, null, null, null,
                            JpaProduct.Type.video, JpaOrders.PayType.remit, JpaOrders.Status.completed,
                            null, null, 0, null, null, null, null, null, null);
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
    class Key_slot{
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
    public List<FlatScheduleListItem> getTotalRecord(List list,String monthDay){
		 List<FlatScheduleListItem> scheduleList = new ArrayList<FlatScheduleListItem>();
		 Map<Key_slot, List<FlatScheduleListItem>> map= new LinkedHashMap<Key_slot, List<FlatScheduleListItem>>();
		 for (Object object : list) {
			 FlatScheduleListItem obj =(FlatScheduleListItem)object;
			 Key_slot key=new Key_slot(obj.getSlot(),obj.getSlotSize(),obj.getSlotDesc());
			 if(!map.containsKey(key)){
				 map.put(key, new ArrayList<FlatScheduleListItem>());
			 } 
			 map.get(key).add(obj);
		}
		for (Map.Entry<Key_slot, List<FlatScheduleListItem>> object : map.entrySet()) {
			List<FlatScheduleListItem> w = object.getValue();
			while (ismore(object)) {
				BlackAd blackAd=getFristEmelemt(ls);
				if(null!=blackAd){
				FlatScheduleListItem sw = new FlatScheduleListItem();
				sw.setMaterialName(blackAd.getSeqNumber()+"-"+blackAd.getAdName());
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
		if(sortTree.isEmpty()){
			 List<BlackAd> bList=contractService.queryAllBlackAd();
			 if(bList.size()>0){
				for (BlackAd blackAd : bList) {
					ls.add(blackAd);
				}
			 }else{
				 return null; 
			 }
		}
		BlackAd frist = sortTree.get(0);
	    sortTree.remove(frist);
	    sortTree.add(sortTree.size(), frist);
	    return frist;
	}
	public Boolean ismore(Map.Entry<Key_slot, List<FlatScheduleListItem>> object){
		List<FlatScheduleListItem> w = object.getValue();
		int c = 0;
		for (FlatScheduleListItem b : w) {
			if (b.getMaterialSize() != null)
				c += b.getMaterialSize();
		}
		if(c==0  ){
			w.clear();
		}
		if (object.getKey().slotSize >= c) {
		   return true;
		}
		 else{
			return false;
		}
	}

    /**
     * 排条单
     */
    @RequestMapping("list.xls")
    public void exportScheduleDetailList (TableRequest req,
                                               @CookieValue(value="city", defaultValue = "-1") int cityId,
                                              @ModelAttribute("city") JpaCity city,
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
                dayNumber ++;
                cal0.add(Calendar.DATE, 1);
            }

            dayStr2 = DateUtil.longDf3.get().format(day);
            monthDay = DateUtil.longDf4.get().format(day);
        } catch(Exception e) {}

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
        beans.put("number", "编号:BJ-公交（日）-"+year+"-"+dayNumber);
        XLSTransformer transformer = new XLSTransformer();
        try {
            resp.setHeader("Content-Type", "application/x-xls");
            resp.setHeader("Content-Disposition", "attachment; filename=\"schedule-[" + dayStr + "].xls\"");
            InputStream is = new BufferedInputStream(ScheduleController.class.getResourceAsStream(templateFileName));
            org.apache.poi.ss.usermodel.Workbook workbook = transformer.transformXLS(is, beans);

            ExcelUtil.dynamicMergeCells((HSSFSheet)workbook.getSheetAt(0), 1, 0, 1, 2);

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

    private List<Report> flatDetailForGoods(String day, List<Report> reports) {
        List<Report> list = new LinkedList<Report> ();
        for (Report r : reports) {
            UiBox box = r.getBox(day);
            if (box == null || box.getGoods().isEmpty()) {
                //fill in blank box
                list.add(r);
            } else {
                //create report records for each goods
                List<JpaGoods> goods = box.fetchSortedGoods();
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


    public static class Report implements Serializable {
        private Map<String/*date*/, UiBox> boxes;
        private JpaTimeslot slot;

        public Report(JpaTimeslot slot) {
            this.slot = slot;
            this.boxes = new HashMap<String, UiBox> ();
        }

        public String addBox(Box box) {
            String key = DateUtil.longDf.get().format(box.getDay());
            if (!(box instanceof UiBox))
                box = new UiBox(box);
            boxes.put(key, (UiBox)box);
            return key;
        }

        public String addBox(JpaBox box) {
            List<JpaGoods> goods = box.getGoods();

            String key = DateUtil.longDf.get().format(box.getDay());
            UiBox b = boxes.get(key);
            if (b == null) {
                b = new UiBox(box);
                boxes.put(key, b);
            }
            if (goods != null) {
                for (JpaGoods g : goods) {
                    b.addGood(g);
                }
            }
            return key;
        }

        public String addBox(JpaBox box, JpaGoods good) {
            String key = DateUtil.longDf.get().format(box.getDay());
            UiBox b = boxes.get(key);
            if (b != null) {
                b.addGood(good);
            } else {
                b = new UiBox(box);
                b.addGood(good);
                boxes.put(key, b);
            }

            return key;
        }

        public Map<String/*date*/, UiBox> getBoxes() {
            return boxes;
        }

        private void setBoxes(Map<String, UiBox> boxes) {
            this.boxes = boxes;
        }

        private UiBox getBox(String dayStr) {
            return boxes.get(dayStr);
        }

        public JpaTimeslot getSlot() {
            return slot;
        }
    }

    public static class UiBox extends Box {
        private List<JpaGoods> goods;
        public UiBox () {
            goods = new ArrayList<JpaGoods>();
        }
        public UiBox (Box box) {
            BeanUtils.copyProperties(box, this);
            if (goods == null) {
                goods = new ArrayList<JpaGoods>();
            }
        }

        public UiBox (JpaBox box) {
            BeanUtils.copyProperties(box, this);
            goods = new ArrayList<JpaGoods>();
        }

        public void addGood(JpaGoods good) {
            //cut connection from JpaGoods to Box to avoid loop in serialization
            good.setBox(null);
            goods.add(good);
        }

        public List<JpaGoods> getGoods() {
            return goods;
        }

        public void setGoods(List<JpaGoods> goods) {
            this.goods = goods;
        }

        public List<JpaGoods> fetchSortedGoods() {
            List<JpaGoods> list = null;
            if (goods != null)
                list = new ArrayList<JpaGoods>(goods);
            else
                list = new ArrayList<JpaGoods>();

            Collections.sort(list, new Comparator<JpaGoods>() {
              //  @Override
                public int compare(JpaGoods o1, JpaGoods o2) {
                    return (int) (o1.getInboxPosition() - o2.getInboxPosition());
                }
            });
            return list;
        }

        public String getRemainStr () {
            return DateUtil.toShortStr(this.getRemain());
        }
    }
}
