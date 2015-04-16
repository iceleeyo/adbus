package com.pantuo.web;

import com.pantuo.mybatis.domain.TimeslotReport;
import com.pantuo.mybatis.domain.Box;
import com.pantuo.pojo.highchart.*;
import com.pantuo.service.*;
import com.pantuo.util.DateUtil;
import com.pantuo.util.GlobalMethods;
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

    @RequestMapping("timeslot")
    public String remainTimeslots(Model model,
                                  @RequestParam(value="day", required = false) String dayStr) {
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

        HighChartBuilder<Date> b = new HighChartBuilder<Date>("剩余时段趋势图", Date.class);

        DayList days = DayList.range(to, 30);
        DatetimeSeries s = Series.newDatetimeSeries("剩余时长", SeriesType.TIMESLOT, days);
        List<TimeslotReport> slots = service.getRemainTimeslots(days.getEarlyestDay(), days.getLastDay(), null);
        for (TimeslotReport data : slots) {
            s.put(data.getDay(), data.getRemain());
        }
        b.addSeries("TIMESLOT", s);

        //peak time
        s = Series.newDatetimeSeries("高峰剩余时长", SeriesType.PEAK_TIMESLOT, days);
        slots = service.getRemainTimeslots(days.getEarlyestDay(), days.getLastDay(), true);
        for (TimeslotReport data : slots) {
            s.put(data.getDay(), data.getRemain());
        }
        b.addSeries("TIMESLOT_PEAK", s);

        model.addAttribute("remainTimeSlots", b.build());
        model.addAttribute("day", dayStr);
        return "report_remainTimeslots";
    }

    @RequestMapping("wow")
    //周同比
    public String remainTimeslotsWeekOnWeek(Model model,
                                  @RequestParam(value="day", required = false) String dayStr,
                                  @RequestParam(value="span", required = false, defaultValue = "14") int span) {
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
        HighChartBuilder<Integer> b = new HighChartBuilder<Integer>("剩余时段同比趋势图", Integer.class);
        Series<Integer, TimeslotReport> s = (Series<Integer, TimeslotReport>)Series.newCategorySeries("剩余时长（最近）", SeriesType.TIMESLOT, xAxis);
        List<TimeslotReport> slots = service.getRemainTimeslots(thisWeek.getEarlyestDay(), thisWeek.getLastDay(), null);
        for (TimeslotReport data : slots) {
            if (thisWeekIndex.containsKey(data.getDay())) {
                s.put(thisWeekIndex.get(data.getDay()), data);
            }
        }
        b.addSeries("THIS_WEEK", s);

        //fetch prev week data
        s = (Series<Integer, TimeslotReport>)Series.newCategorySeries("剩余时长（历史）", SeriesType.TIMESLOT, xAxis);
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
        return "report_wowTimeslots";
    }

    @RequestMapping("mom")
    //月同比
    public String remainTimeslotsMonthOnMonth(Model model,
                                            @RequestParam(value="year", required = false) Integer year) {

        int thisYear = DateUtil.getYearAndMonth(new Date())[0];
        int yearOne = year == null ? thisYear : year;
        int yearTwo = yearOne - 1;

        List<Integer> xAxis = new ArrayList<Integer> ();
        for (int i = 1; i <= 12; i++) {
            xAxis.add(i);
        }

        //fetch this year's data
        HighChartBuilder<Integer> b = new HighChartBuilder<Integer>("剩余时段同比趋势图", Integer.class);
        Series<Integer, TimeslotReport> s = (Series<Integer, TimeslotReport>)Series.newCategorySeries("剩余时长（" + yearOne + "年）", SeriesType.TIMESLOT, xAxis);
        List<TimeslotReport> slots = service.getMonthlyRemainTimeslots(yearOne, null);
        for (TimeslotReport data : slots) {
            s.put(data.getMonth(), data);
        }
        b.addSeries("THIS_YEAR", s);

        //fetch prev year's data
        s = (Series<Integer, TimeslotReport>)Series.newCategorySeries("剩余时长（" + yearTwo + "年）", SeriesType.TIMESLOT, xAxis);
        slots = service.getMonthlyRemainTimeslots(yearTwo, null);
        for (TimeslotReport data : slots) {
            s.put(data.getMonth(), data);
        }
        b.addSeries("PREV_YEAR", s);

        b.setxAxis(xAxis);

        model.addAttribute("remainTimeSlots", b.build());
        model.addAttribute("year", yearOne);
        model.addAttribute("thisYear", thisYear);
        return "report_momTimeslots";
    }
}