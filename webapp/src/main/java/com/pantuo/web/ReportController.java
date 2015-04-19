package com.pantuo.web;

import com.pantuo.dao.IndustryRepository;
import com.pantuo.dao.pojo.JpaIndustry;
import com.pantuo.mybatis.domain.TimeslotReport;
import com.pantuo.pojo.highchart.*;
import com.pantuo.service.*;
import com.pantuo.util.DateUtil;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

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

    @RequestMapping("day")
         public String remainTimeslots(Model model,
                                       @RequestParam(value="day", required = false) String dayStr,
                                       @RequestParam(value="baseY", required = false) Long baseY,
                                       @RequestParam(value="span", required = false, defaultValue = "30") int span) {
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
        slots = service.getRemainTimeslots(days.getEarlyestDay(), days.getLastDay(), null);
        for (TimeslotReport data : slots) {
            s.put(data.getDay(), data);
        }
        b.addSeries("TIMESLOT", s);

        //peak time
        s = Series.newDatetimeSeries(SeriesType.TIMESLOT, days);
        s.addName("remain", "高峰剩余时长").addName("ordered", "高峰售出时长");
        slots = service.getRemainTimeslots(days.getEarlyestDay(), days.getLastDay(), true);
        for (TimeslotReport data : slots) {
            s.put(data.getDay(), data);
        }
        b.addSeries("TIMESLOT_PEAK", s);

        model.addAttribute("remainTimeSlots", b.build());
        model.addAttribute("day", dayStr);
        model.addAttribute("baseY", baseY);
        return "report_remainTimeslots";
    }

    @RequestMapping("dayp")
    public String remainTimeslotsPercent(Model model,
                                  @RequestParam(value="day", required = false) String dayStr,
                                  @RequestParam(value="baseY", required = false) Long baseY,
                                  @RequestParam(value="span", required = false, defaultValue = "30") int span) {
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
        slots = service.getRemainTimeslots(days.getEarlyestDay(), days.getLastDay(), false);
        s.addName("remain", "平峰剩余时长").addName("ordered", "平峰售出时长");
        for (TimeslotReport data : slots) {
            s.put(data.getDay(), data);
        }
        b.addSeries("TIMESLOT1", s);
        b.addSeries("TIMESLOT2", s);

        //peak time
        s = Series.newDatetimeSeries(SeriesType.TIMESLOT, days);
        s.addName("remain", "高峰剩余时长").addName("ordered", "高峰售出时长");
        slots = service.getRemainTimeslots(days.getEarlyestDay(), days.getLastDay(), true);
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

    @RequestMapping("wow")
    //周同比
    public String remainTimeslotsWeekOnWeek(Model model,
                                  @RequestParam(value="day", required = false) String dayStr,
                                  @RequestParam(value="span", required = false, defaultValue = "14") int span,
                                  @RequestParam(value="baseY", required = false) Long baseY) {
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
        List<TimeslotReport> slots = service.getRemainTimeslots(thisWeek.getEarlyestDay(), thisWeek.getLastDay(), null);
        for (TimeslotReport data : slots) {
            if (thisWeekIndex.containsKey(data.getDay())) {
                s.put(thisWeekIndex.get(data.getDay()), data);
            }
        }
        b.addSeries("THIS_WEEK", s);

        //fetch prev week data
        s = (Series<Integer, TimeslotReport>)Series.newCategorySeries(SeriesType.TIMESLOT, xAxis);
        s.addName("remain", "剩余时长（历史）");
        slots = service.getRemainTimeslots(prevWeek.getEarlyestDay(), prevWeek.getLastDay(), null);
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

    @RequestMapping("mom")
    //月同比
    public String remainTimeslotsMonthOnMonth(Model model,
                                            @RequestParam(value="year", required = false) Integer year,
                                            @RequestParam(value="baseY", required = false) Long baseY) {

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
        List<TimeslotReport> slots = service.getMonthlyRemainTimeslots(yearOne, null);
        for (TimeslotReport data : slots) {
            s.put(data.getMonth(), data);
        }
        b.addSeries("THIS_YEAR", s);

        //fetch prev year's data
        s = (Series<Integer, TimeslotReport>)Series.newCategorySeries(SeriesType.LONG_TIMESLOT, xAxis);
        s.addName("remain", "剩余时长（" + yearTwo + "年）");
        slots = service.getMonthlyRemainTimeslots(yearTwo, null);
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

    @RequestMapping("monthp")
    //月对比
    public String remainTimeslotsMonthPercent(Model model,
                                              @RequestParam(value="year", required = false) Integer year,
                                              @RequestParam(value="baseY", required = false) Long baseY) {

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
        List<TimeslotReport> slots = service.getMonthlyRemainTimeslots(yearOne, false);
        for (TimeslotReport data : slots) {
            s.put(data.getMonth(), data);
        }
        b.addSeries("MONTH1", s);
        b.addSeries("MONTH2", s);

        //fetch peak data
        s = (Series<Integer, TimeslotReport>)Series.newCategorySeries(SeriesType.LONG_TIMESLOT, xAxis);
        s.addName("remain", "高峰剩余时长").addName("ordered", "高峰售出时长");
        slots = service.getMonthlyRemainTimeslots(yearOne, true);
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

    @RequestMapping("hour")
    public String remainHourlyTimeslots(Model model,
                                  @RequestParam(value="day", required = false) String dayStr,
                                  @RequestParam(value="baseY", required = false) Long baseY) {
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
            slots = service.getHourlyTimeslots(theDay, null);
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


    @RequestMapping("hourp")
    public String remainHourlyTimeslotsPercent(Model model,
                                        @RequestParam(value="day", required = false) String dayStr,
                                        @RequestParam(value="baseY", required = false) Long baseY) {
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
        slots = service.getHourlyTimeslots(theDay, null);
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

    @RequestMapping("dayorderp")
    public String orderPercent(Model model,
                                         @RequestParam(value="day", required = false) String dayStr,
                                         @RequestParam(value="baseY", required = false) Long baseY,
                                         @RequestParam(value="span", required = false, defaultValue = "90") int span) {
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
        slots = service.getOrderTimeslots(days.getEarlyestDay(), days.getLastDay(), null);
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



    @RequestMapping("dayindustryp")
    public String orderIndustryPercent(Model model,
                               @RequestParam(value="day", required = false) String dayStr,
                               @RequestParam(value="baseY", required = false) Long baseY,
                               @RequestParam(value="industry", required = false, defaultValue = "0,1") String industryIdStr,
                               @RequestParam(value="span", required = false, defaultValue = "90") int span) {
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
        Map<Integer, List<TimeslotReport>> slots =  service.getOrderTimeslotsByIndustries(days.getEarlyestDay(), days.getLastDay(), industryIds, null);
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
                s.setPointer(false, 1).addName("remain", "剩余时长").addName("paid", "其他行业已售").addName("notPaid", "其他行业预售");           } else {
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
}