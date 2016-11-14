package com.pantuo.web;

import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.JpaIndustry;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.mybatis.domain.TimeslotReport;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.pojo.highchart.*;
import com.pantuo.service.*;
import com.pantuo.util.DateUtil;
import com.pantuo.web.view.CountMonthView;
import com.pantuo.web.view.OrderView;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.text.ParseException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tliu
 *
 * 报表
 */
@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/report")
public class ReportController {
    private static Logger log = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService service;

    @Autowired
    private IndustryService industryService;
    
    
    
    @RequestMapping(value = "/publishCountM")
	public String sift() {
		return "publishCountM";
	}
    @RequestMapping(value = "/addQR")
    public String addQR() {
    	return "report/addQR";
    }
    @RequestMapping(value = "/QRList")
    public String QRList() {
    	return "report/QRList";
    }
    @RequestMapping(value = "/QRdetail/{para}")
    public String QRdetail(@PathVariable("para") String para,Model model) {
    	model.addAttribute("para", para);
    	return "report/QRdetail";
    }
    
	@PreAuthorize(" hasRole('ShibaOrderManager')" + " or hasRole('ShibaFinancialManager')"
			+ "or hasRole('BeiguangMaterialManager')" + "or hasRole('BeiguangScheduleManager')"
			+ "or hasRole('ShibaSuppliesManager')  ")
    @RequestMapping("day")
     public String remainTimeslots(Model model,
                                       @RequestParam(value="day", required = false) String dayStr,
                                       @RequestParam(value="baseY", required = false) Long baseY,
                                       @RequestParam(value="span", required = false, defaultValue = "30") int span,
                                       @CookieValue(value="city", defaultValue = "-1") int city) {
        Date to = null;
        if (StringUtils.isBlank(dayStr)) {
            to = new Date();
        } else {
            try {
                to = DateUtil.longDf.get().parse(dayStr);
            } catch (Exception e) {
                to = new Date();
            }
        }
        dayStr = DateUtil.longDf.get().format(to);

        HighChartBuilder<Date> b = new HighChartBuilder<Date>("剩余时段趋势图", XType.DATE);

        DayList days = DayList.range(to, span);

        DatetimeSeries s = null;
        List<TimeslotReport> slots = null;

        s = Series.newDatetimeSeries(SeriesType.TIMESLOT, days);
        s.addName("remain", "剩余总时长").addName("ordered", "售出总时长");
        slots = service.getRemainTimeslots(city, days.getEarlyestDay(), days.getLastDay(), null);
        for (TimeslotReport data : slots) {
            s.put(data.getDay(), data);
        }
        b.addSeries("TIMESLOT", s);

        //peak time
        s = Series.newDatetimeSeries(SeriesType.TIMESLOT, days);
        s.addName("remain", "高峰剩余时长").addName("ordered", "高峰售出时长");
        slots = service.getRemainTimeslots(city, days.getEarlyestDay(), days.getLastDay(), true);
        for (TimeslotReport data : slots) {
            s.put(data.getDay(), data);
        }
        b.addSeries("TIMESLOT_PEAK", s);

        model.addAttribute("remainTimeSlots", b.build());
        model.addAttribute("day", dayStr);
        model.addAttribute("baseY", baseY);
        return "report_remainTimeslots";
    }

		@PreAuthorize(" hasRole('ShibaOrderManager')" + " or hasRole('ShibaFinancialManager')"
				+ "or hasRole('BeiguangMaterialManager')" + "or hasRole('BeiguangScheduleManager')"
				+ "or hasRole('ShibaSuppliesManager')  ")
    @RequestMapping("dayp")
    public String remainTimeslotsPercent(Model model,
                                  @RequestParam(value="day", required = false) String dayStr,
                                  @RequestParam(value="baseY", required = false) Long baseY,
                                  @RequestParam(value="span", required = false, defaultValue = "30") int span,
                                  @CookieValue(value="city", defaultValue = "-1") int city) {
        Date to = null;
        if (StringUtils.isBlank(dayStr)) {
            to = new Date();
        } else {
            try {
                to = DateUtil.longDf.get().parse(dayStr);
            } catch (Exception e) {
                to = new Date();
            }
        }
        dayStr = DateUtil.longDf.get().format(to);

        HighChartBuilder<Date> b = new HighChartBuilder<Date>("剩余时段趋势图", XType.DATE);

        DayList days = DayList.range(to, span);

        DatetimeSeries s;
        List<TimeslotReport> slots = null;

        s = Series.newDatetimeSeries(SeriesType.TIMESLOT, days);
        slots = service.getRemainTimeslots(city, days.getEarlyestDay(), days.getLastDay(), false);
        s.addName("remain", "平峰剩余时长").addName("ordered", "平峰售出时长");
        for (TimeslotReport data : slots) {
            s.put(data.getDay(), data);
        }
        b.addSeries("TIMESLOT1", s);
        b.addSeries("TIMESLOT2", s);

        //peak time
        s = Series.newDatetimeSeries(SeriesType.TIMESLOT, days);
        s.addName("remain", "高峰剩余时长").addName("ordered", "高峰售出时长");
        slots = service.getRemainTimeslots(city, days.getEarlyestDay(), days.getLastDay(), true);
        for (TimeslotReport data : slots) {
            s.put(data.getDay(), data);
        }
        b.addSeries("TIMESLOT_PEAK1", s);
        b.addSeries("TIMESLOT_PEAK2", s);

        b.setStacked(true);

        model.addAttribute("remainTimeSlots", b.build());
        model.addAttribute("day", dayStr);
        model.addAttribute("baseY", baseY);
        return "report_remainTimeslotsPercent";
    }

		@PreAuthorize(" hasRole('ShibaOrderManager')" + " or hasRole('ShibaFinancialManager')"
				+ "or hasRole('BeiguangMaterialManager')" + "or hasRole('BeiguangScheduleManager')"
				+ "or hasRole('ShibaSuppliesManager')  ")
    @RequestMapping("wow")
    //周同比
    public String remainTimeslotsWeekOnWeek(Model model,
                                  @RequestParam(value="day", required = false) String dayStr,
                                  @RequestParam(value="span", required = false, defaultValue = "14") int span,
                                  @RequestParam(value="baseY", required = false) Long baseY,
                                  @CookieValue(value="city", defaultValue = "-1") int city) {
        Date to = null;
        if (StringUtils.isBlank(dayStr)) {
            to = new Date();
        } else {
            try {
                to = DateUtil.longDf.get().parse(dayStr);
            } catch (Exception e) {
                to = new Date();
            }
        }
        dayStr = DateUtil.longDf.get().format(to);

        DayList thisWeek = DayList.range(to, span - 1);
        DayList prevWeek = DayList.range(DateUtils.addDays(thisWeek.getEarlyestDay(), -1), span - 1);
        List<Integer> xAxis = new ArrayList<Integer> ();
        Map<Date, Integer> thisWeekIndex = new HashMap<Date, Integer> ();
        Map<Date, Integer> prevWeekIndex = new HashMap<Date, Integer> ();
        List<Date> thisList = thisWeek.toDayList();
        List<Date> prevList = prevWeek.toDayList();
        for (int i = 1; i <= span; i++) {
            xAxis.add(i);
            thisWeekIndex.put(thisList.get(i - 1), i);
            prevWeekIndex.put(prevList.get(i - 1), i);
        }

        //fetch this week data
        HighChartBuilder<Integer> b = new HighChartBuilder<Integer>("剩余时段同比趋势图", XType.WEEK);
        Series<Integer, TimeslotReport> s = (Series<Integer, TimeslotReport>)Series.newCategorySeries(SeriesType.TIMESLOT, xAxis);
        s.addName("remain", "剩余时长（最近）");
        List<TimeslotReport> slots = service.getRemainTimeslots(city, thisWeek.getEarlyestDay(), thisWeek.getLastDay(), null);
        for (TimeslotReport data : slots) {
            if (thisWeekIndex.containsKey(data.getDay())) {
                s.put(thisWeekIndex.get(data.getDay()), data);
            }
        }
        b.addSeries("THIS_WEEK", s);

        //fetch prev week data
        s = (Series<Integer, TimeslotReport>)Series.newCategorySeries(SeriesType.TIMESLOT, xAxis);
        s.addName("remain", "剩余时长（历史）");
        slots = service.getRemainTimeslots(city, prevWeek.getEarlyestDay(), prevWeek.getLastDay(), null);
        for (TimeslotReport data : slots) {
            if (prevWeekIndex.containsKey(data.getDay())) {
                s.put(prevWeekIndex.get(data.getDay()), data);
            }
        }
        b.addSeries("PREV_WEEK", s);

        b.setxAxis(xAxis);

        model.addAttribute("remainTimeSlots", b.build());
        model.addAttribute("day", dayStr);
        model.addAttribute("span", span);
        model.addAttribute("baseY", baseY);
        return "report_wowTimeslots";
    }


	@PreAuthorize(" hasRole('ShibaOrderManager')" + " or hasRole('ShibaFinancialManager')"
				+ "or hasRole('BeiguangMaterialManager')" + "or hasRole('BeiguangScheduleManager')"
				+ "or hasRole('ShibaSuppliesManager')  ")
	@RequestMapping("mom")
    //月同比
    public String remainTimeslotsMonthOnMonth(Model model,
                                            @RequestParam(value="year", required = false) Integer year,
                                            @RequestParam(value="baseY", required = false) Long baseY,
                                            @CookieValue(value="city", defaultValue = "-1") int city) {

        int thisYear = DateUtil.getYearAndMonthAndHour(new Date())[0];
        int yearOne = year == null ? thisYear : year;
        int yearTwo = yearOne - 1;

        List<Integer> xAxis = new ArrayList<Integer> ();
        for (int i = 1; i <= 12; i++) {
            xAxis.add(i);
        }

        //fetch this year's data
        HighChartBuilder<Integer> b = new HighChartBuilder<Integer>("剩余时段同比趋势图", XType.MONTH);
        Series<Integer, TimeslotReport> s = (Series<Integer, TimeslotReport>)Series.newCategorySeries(SeriesType.LONG_TIMESLOT, xAxis);
        s.addName("remain", "剩余时长（" + yearOne + "年）");
        List<TimeslotReport> slots = service.getMonthlyRemainTimeslots(city, yearOne, null);
        for (TimeslotReport data : slots) {
            s.put(data.getMonth(), data);
        }
        b.addSeries("THIS_YEAR", s);

        //fetch prev year's data
        s = (Series<Integer, TimeslotReport>)Series.newCategorySeries(SeriesType.LONG_TIMESLOT, xAxis);
        s.addName("remain", "剩余时长（" + yearTwo + "年）");
        slots = service.getMonthlyRemainTimeslots(city, yearTwo, null);
        for (TimeslotReport data : slots) {
            s.put(data.getMonth(), data);
        }
        b.addSeries("PREV_YEAR", s);

        b.setxAxis(xAxis);

        model.addAttribute("remainTimeSlots", b.build());
        model.addAttribute("year", yearOne);
        model.addAttribute("thisYear", thisYear);
        model.addAttribute("baseY", baseY);
        return "report_momTimeslots";
    }


    /*@PreAuthorize(" !hasRole('advertiser')  ")*/
   	@PreAuthorize(" hasRole('ShibaOrderManager')" + " or hasRole('ShibaFinancialManager')"
   			+ "or hasRole('BeiguangMaterialManager')" + "or hasRole('BeiguangScheduleManager')"
   			+ "or hasRole('ShibaSuppliesManager')  ")
   	@RequestMapping("monthp")
    //月对比
    public String remainTimeslotsMonthPercent(Model model,
                                              @RequestParam(value="year", required = false) Integer year,
                                              @RequestParam(value="baseY", required = false) Long baseY,
                                              @CookieValue(value="city", defaultValue = "-1") int city) {

        int thisYear = DateUtil.getYearAndMonthAndHour(new Date())[0];
        int yearOne = year == null ? thisYear : year;

        List<Integer> xAxis = new ArrayList<Integer> ();
        for (int i = 1; i <= 12; i++) {
            xAxis.add(i);
        }

        //fetch normal data
        HighChartBuilder<Integer> b = new HighChartBuilder<Integer>("剩余时段同比趋势图", XType.MONTH);
        Series<Integer, TimeslotReport> s;
        s = (Series<Integer, TimeslotReport>)Series.newCategorySeries(SeriesType.LONG_TIMESLOT, xAxis);
        s.addName("remain", "平峰剩余时长").addName("ordered", "平峰售出时长");
        List<TimeslotReport> slots = service.getMonthlyRemainTimeslots(city, yearOne, false);
        for (TimeslotReport data : slots) {
            s.put(data.getMonth(), data);
        }
        b.addSeries("MONTH1", s);
        b.addSeries("MONTH2", s);

        //fetch peak data
        s = (Series<Integer, TimeslotReport>)Series.newCategorySeries(SeriesType.LONG_TIMESLOT, xAxis);
        s.addName("remain", "高峰剩余时长").addName("ordered", "高峰售出时长");
        slots = service.getMonthlyRemainTimeslots(city, yearOne, true);
        for (TimeslotReport data : slots) {
            s.put(data.getMonth(), data);
        }
        b.addSeries("MONTH_PEAK1", s);
        b.addSeries("MONTH_PEAK2", s);

        b.setxAxis(xAxis);
        b.setStacked(true);

        model.addAttribute("remainTimeSlots", b.build());
        model.addAttribute("year", yearOne);
        model.addAttribute("thisYear", thisYear);
        model.addAttribute("baseY", baseY);
        return "report_momTimeslotsPercent";
    }


   	
    /*@PreAuthorize(" !hasRole('advertiser')  ")*/
   	@PreAuthorize(" hasRole('ShibaOrderManager')" + " or hasRole('ShibaFinancialManager')"
   			+ "or hasRole('BeiguangMaterialManager')" + "or hasRole('BeiguangScheduleManager')"
   			+ "or hasRole('ShibaSuppliesManager')  ")
   	@RequestMapping("hour")
    public String remainHourlyTimeslots(Model model,
                                  @RequestParam(value="day", required = false) String dayStr,
                                  @RequestParam(value="baseY", required = false) Long baseY,
                                  @CookieValue(value="city", defaultValue = "-1") int city) {
        Date to = null;
        if (StringUtils.isBlank(dayStr)) {
            to = DateUtil.trimDate(new Date());
        } else {
            try {
                to = DateUtil.longDf.get().parse(dayStr);
            } catch (Exception e) {
                to = DateUtil.trimDate(new Date());
            }
        }
        dayStr = DateUtil.longDf.get().format(to);

        int[] dayDiff = new int[] {-1, 0, 1};

        HighChartBuilder<Integer> b = new HighChartBuilder<Integer>("三天剩余时段趋势图", XType.HOUR);
        List<Integer> xAxis = new ArrayList<Integer> ();
        for (int i = 0; i < 24; i++) {
            xAxis.add(i);
        }
        Series s = null;
        List<TimeslotReport> slots = null;

        Date theDay = null;
        for (int diff : dayDiff) {
            if (diff == 0) {
                theDay = to;
            } else {
                Calendar cal = DateUtil.newCalendar();
                cal.setTime(to);
                cal.add(Calendar.DATE, diff);
                theDay = cal.getTime();
            }
            s = Series.newCategorySeries(SeriesType.TIMESLOT, xAxis);
            s.addName("remain", DateUtil.longDf2.get().format(theDay) + "剩余时长").addName("ordered", "售出总时长（" + DateUtil.longDf2.get().format(theDay) + "）");
            slots = service.getHourlyTimeslots(city, theDay, null);
            for (TimeslotReport data : slots) {
                s.put(data.getHour(), data);
            }
            b.addSeries("TIMESLOT" + (diff + 1), s);
        }

        b.setxAxis(xAxis);

        model.addAttribute("remainTimeSlots", b.build());
        model.addAttribute("day", dayStr);
        model.addAttribute("baseY", baseY);
        return "report_hourTimeslots";
    }


   	@PreAuthorize(" hasRole('ShibaOrderManager')" + " or hasRole('ShibaFinancialManager')"
   			+ "or hasRole('BeiguangMaterialManager')" + "or hasRole('BeiguangScheduleManager')"
   			+ "or hasRole('ShibaSuppliesManager')  ")
   	
   	@RequestMapping("hourp")
    public String remainHourlyTimeslotsPercent(Model model,
                                        @RequestParam(value="day", required = false) String dayStr,
                                        @RequestParam(value="baseY", required = false) Long baseY,
                                        @CookieValue(value="city", defaultValue = "-1") int city) {
        Date to = null;
        if (StringUtils.isBlank(dayStr)) {
            to = DateUtil.trimDate(new Date());
        } else {
            try {
                to = DateUtil.longDf.get().parse(dayStr);
            } catch (Exception e) {
                to = DateUtil.trimDate(new Date());
            }
        }
        dayStr = DateUtil.longDf.get().format(to);

        HighChartBuilder<Integer> b = new HighChartBuilder<Integer>("当天剩余时段对比", XType.HOUR);
        List<Integer> xAxis = new ArrayList<Integer> ();
        for (int i = 0; i < 24; i++) {
            xAxis.add(i);
        }
        Series s = null;
        List<TimeslotReport> slots = null;

        Date theDay = null;
        theDay = to;
        s = Series.newCategorySeries(SeriesType.TIMESLOT, xAxis);
        s.addName("remain", "剩余总时长").addName("ordered", "售出总时长（" + DateUtil.longDf2.get().format(theDay) + "）");
        slots = service.getHourlyTimeslots(city, theDay, null);
        for (TimeslotReport data : slots) {
            s.put(data.getHour(), data);
        }
        b.addSeries("TIMESLOT1", s);
        b.addSeries("TIMESLOT2", s);

        b.setxAxis(xAxis);
        b.setStacked(true);

        model.addAttribute("remainTimeSlots", b.build());
        model.addAttribute("day", dayStr);
        model.addAttribute("baseY", baseY);
        return "report_hourTimeslotsPercent";
    }


    /*@PreAuthorize(" !hasRole('advertiser')  ")*/
   	@PreAuthorize(" hasRole('ShibaOrderManager')" + " or hasRole('ShibaFinancialManager')"
   			+ "or hasRole('BeiguangMaterialManager')" + "or hasRole('BeiguangScheduleManager')"
   			+ "or hasRole('ShibaSuppliesManager')  ")
   	@RequestMapping("dayorderp")
    public String orderPercent(Model model,
                                         @RequestParam(value="day", required = false) String dayStr,
                                         @RequestParam(value="baseY", required = false) Long baseY,
                                         @RequestParam(value="span", required = false, defaultValue = "90") int span,
                                         @CookieValue(value="city", defaultValue = "-1") int city) {
        Date to = null;
        if (StringUtils.isBlank(dayStr)) {
            to = new Date();
        } else {
            try {
                to = DateUtil.longDf.get().parse(dayStr);
            } catch (Exception e) {
                to = new Date();
            }
        }
        dayStr = DateUtil.longDf.get().format(to);

        HighChartBuilder<Date> b = new HighChartBuilder<Date>("售出情况趋势图", XType.DATE);

        DayList days = DayList.range(to, -span);

        DatetimeSeries s;
        List<TimeslotReport> slots = null;

        s = Series.newDatetimeSeries(SeriesType.TIMESLOT, days);
        slots = service.getOrderTimeslots(city, days.getEarlyestDay(), days.getLastDay(), null);
        s.setPointer(false, 1).addName("remain", "剩余时长").addName("paid", "已售出时长").addName("notPaid", "预售时长");
        for (TimeslotReport data : slots) {
            s.put(data.getDay(), data);
        }
        b.addSeries("TIMESLOT1", s);
        b.addSeries("TIMESLOT2", s);
        b.addSeries("TIMESLOT3", s);

        b.setStacked(true);
        b.setType(ChartType.area);

        model.addAttribute("remainTimeSlots", b.build());
        model.addAttribute("day", dayStr);
        model.addAttribute("baseY", baseY);
        return "report_dailyOrderPercent";
    }


    /*@PreAuthorize(" !hasRole('advertiser')  ")*/
   	@PreAuthorize(" hasRole('ShibaOrderManager')" + " or hasRole('ShibaFinancialManager')"
   			+ "or hasRole('BeiguangMaterialManager')" + "or hasRole('BeiguangScheduleManager')"
   			+ "or hasRole('ShibaSuppliesManager')  ")
    @RequestMapping("dayindustryp")
    public String orderIndustryPercent(Model model,
                               @RequestParam(value="day", required = false) String dayStr,
                               @RequestParam(value="baseY", required = false) Long baseY,
                               @RequestParam(value="industry", required = false, defaultValue = "0,1") String industryIdStr,
                               @RequestParam(value="span", required = false, defaultValue = "90") int span,
                               @CookieValue(value="city", defaultValue = "-1") int city) {
        Date to = null;
        if (StringUtils.isBlank(dayStr)) {
            to = new Date();
        } else {
            try {
                to = DateUtil.longDf.get().parse(dayStr);
            } catch (Exception e) {
                to = new Date();
            }
        }
        dayStr = DateUtil.longDf.get().format(to);

        List<Integer> industryIds = new ArrayList<Integer> ();
        try {
            if (industryIdStr != null) {
                for (String id : industryIdStr.split(",")) {
                    industryIds.add(Integer.valueOf(id));
                }
            }
        } catch (Exception e) {
            log.warn("Can not parse industry ids {}", industryIdStr, e);
            industryIds.add(0);
            industryIds.add(1);
        }

        //load industries
        List<JpaIndustry> industries = industryService.getIndustries(industryIds);

        HighChartBuilder<Date> b = new HighChartBuilder<Date>("行业售出情况趋势图", XType.DATE);

        DayList days = DayList.range(to, -span);

        DatetimeSeries s = null;
        Map<Integer, List<TimeslotReport>> slots =  service.getOrderTimeslotsByIndustries(city, days.getEarlyestDay(), days.getLastDay(), industryIds, null);
        Iterator<Map.Entry<Integer,List<TimeslotReport>>> iter = slots.entrySet().iterator();

        List<String> seriesNames = new ArrayList<String> ();
        Map<String, String> yNames = new HashMap<String, String> ();

        while (iter.hasNext()) {
            Map.Entry<Integer, List<TimeslotReport>> e = iter.next();

            s = Series.newDatetimeSeries(SeriesType.TIMESLOT, days);
            s.setPointer(false, 1).addName("remain", "剩余时长").addName("paid", "已售出时长").addName("notPaid", "预售时长");
            for (TimeslotReport data : e.getValue()) {
                s.put(data.getDay(), data);
            }

            if (e.getKey() == -1) {
                b.addSeries("OTHER_PAID", s);
                b.addSeries("OTHER_NOTPAID", s);
                seriesNames.add(0, "OTHER_PAID");
                seriesNames.add(0, "OTHER_NOTPAID");
                yNames.put("OTHER_PAID", "paid");
                yNames.put("OTHER_NOTPAID", "notPaid");
                s.setPointer(false, 1).addName("remain", "剩余时长").addName("paid", "其他行业已售").addName("notPaid", "其他行业预售");
            } else {
                for (JpaIndustry industry : industries) {
                    if (industry.getId() == e.getKey()) {
                        b.addSeries(industry.getId() + "_PAID", s);
                        b.addSeries(industry.getId() + "_NOTPAID", s);
                        seriesNames.add(industry.getId() + "_PAID");
                        seriesNames.add(industry.getId() + "_NOTPAID");
                        yNames.put(industry.getId() + "_PAID", "paid");
                        yNames.put(industry.getId() + "_NOTPAID", "notPaid");
                        s.setPointer(false, 1).addName("remain", "剩余时长")
                                .addName("paid", industry.getName() + "已售").addName("notPaid", industry.getName() +"预售");
                    }
                }
            }
        }

        if (s != null) {
            b.addSeries("REMAIN", s);
            seriesNames.add(0, "REMAIN");
            yNames.put("REMAIN", "remain");
        } 
        b.setStacked(true);
        b.setType(ChartType.area);

        model.addAttribute("remainTimeSlots", b.build());
        model.addAttribute("day", dayStr);
        model.addAttribute("seriesNames", seriesNames);
        model.addAttribute("yNames", yNames);
        model.addAttribute("baseY", baseY);
        return "report_dailyIndustryPercent";
    }

    @PreAuthorize(" hasRole('ShibaOrderManager')" + " or hasRole('ShibaFinancialManager')"
            + "or hasRole('BeiguangMaterialManager')" + "or hasRole('BeiguangScheduleManager')"
            + "or hasRole('ShibaSuppliesManager')  ")
    @RequestMapping("daysalesp")
    public String salesPercent(Model model,
                               @RequestParam(value="day", required = false) String dayStr,
                               @RequestParam(value="baseY", required = false) Long baseY,
                               @RequestParam(value="span", required = false, defaultValue = "90") int span,
                               @ModelAttribute("city") JpaCity city) {
        Date to = null;
        if (StringUtils.isBlank(dayStr)) {
            to = new Date();
        } else {
            try {
                to = DateUtil.longDf.get().parse(dayStr);
            } catch (Exception e) {
                to = new Date();
            }
        }
        dayStr = DateUtil.longDf.get().format(to);

        HighChartBuilder<Date> b = new HighChartBuilder<Date>("财务收入情况趋势图", XType.DATE);

        DayList days = DayList.range(to, span);

        DatetimeSeries s;
        Map<JpaProduct.Type, List<TimeslotReport>> slots = null;
        slots = service.getSalesDailyTimeslots(city.getId(), city.getMediaType(), days.getEarlyestDay(), days.getLastDay());

        List<String> seriesNames = new ArrayList<String> ();
        Map<String, String> yNames = new HashMap<String, String> ();
        for (JpaProduct.Type type : JpaProduct.Type.values()) {
            if (!slots.containsKey(type))
                continue;

            s = Series.newDatetimeSeries(SeriesType.SALES_INCOME, days);
            s.setPointer(false, 1).addName("income", type.getTypeName() + "财务收入");
            for (TimeslotReport data : slots.get(type)) {
                s.put(data.getDay(), data);
            }

            String name = "TIMESLOT_" + type.name();
            b.addSeries(name, s);
            seriesNames.add(name);
            yNames.put(name, "income");
        }

        b.setStacked(true);
        b.setType(ChartType.area);

        model.addAttribute("remainTimeSlots", b.build());
        model.addAttribute("day", dayStr);
        model.addAttribute("baseY", baseY);
        model.addAttribute("seriesNames", seriesNames);
        model.addAttribute("yNames", yNames);
        return "report_dailySalesPercent";
    }
    /*@PreAuthorize(" !hasRole('advertiser')  ")*/
    @PreAuthorize(" hasRole('ShibaOrderManager')" + " or hasRole('ShibaFinancialManager')"
            + "or hasRole('BeiguangMaterialManager')" + "or hasRole('BeiguangScheduleManager')"
            + "or hasRole('ShibaSuppliesManager')  ")
    @RequestMapping("monthsalesp")
    //月对比
    public String salesMonthPercent(Model model,
                                              @RequestParam(value="year", required = false) Integer year,
                                              @RequestParam(value="baseY", required = false) Long baseY,
                                              @ModelAttribute("city") JpaCity city) {

        int thisYear = DateUtil.getYearAndMonthAndHour(new Date())[0];
        int yearOne = year == null ? thisYear : year;

        List<Integer> xAxis = new ArrayList<Integer> ();
        for (int i = 1; i <= 12; i++) {
            xAxis.add(i);
        }

        Map<JpaProduct.Type, List<TimeslotReport>> slots = service.getSalesMonthlyTimeslots(city.getId(), city.getMediaType(), yearOne);

        HighChartBuilder<Integer> b = new HighChartBuilder<Integer>("财务收入全年对比", XType.MONTH);

        List<String> seriesNames = new ArrayList<String> ();
        Map<String, String> yNames = new HashMap<String, String> ();
        for (JpaProduct.Type type : JpaProduct.Type.values()) {
            Series<Integer, TimeslotReport> s;
            s = (Series<Integer, TimeslotReport>)Series.newCategorySeries(SeriesType.SALES_INCOME, xAxis);
            s.addName("income", type.getTypeName() + "财务收入");
            for (TimeslotReport data : slots.get(type)) {
                s.put(data.getMonth(), data);
            }
            String name = "MONTH_" + type.name();
            b.addSeries(name, s);
            seriesNames.add(name);
            yNames.put(name, "income");
        }

        b.setxAxis(xAxis);
        b.setStacked(false);

        model.addAttribute("remainTimeSlots", b.build());
        model.addAttribute("year", yearOne);
        model.addAttribute("thisYear", thisYear);
        model.addAttribute("baseY", baseY);
        model.addAttribute("seriesNames", seriesNames);
        model.addAttribute("yNames", yNames);
        return "report_momSalesPercent";
    }

}