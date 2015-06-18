package com.pantuo.web;

import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.JpaIndustry;
import com.pantuo.mybatis.domain.TimeslotReport;
import com.pantuo.pojo.highchart.*;
import com.pantuo.service.BodyReportService;
import com.pantuo.service.IndustryService;
import com.pantuo.service.ReportService;
import com.pantuo.util.DateUtil;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * @author tliu
 *
 * 报表
 */
@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/report/body")
public class BodyReportController {
    private static Logger log = LoggerFactory.getLogger(BodyReportController.class);

    @Autowired
    private BodyReportService service;

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
        HighChartBuilder<Integer> b = new HighChartBuilder<Integer>("售出巴士同比趋势图", XType.MONTH);
        Series<Integer, TimeslotReport> s;
        s = (Series<Integer, TimeslotReport>)Series.newCategorySeries(SeriesType.BUSCOUNT_PERCENT, xAxis);
        s.addName("remain", "剩余数量").addName("ordered", "售出数量");
        List<TimeslotReport> slots = service.getMonthlyRemainBuses(city, yearOne);
        for (TimeslotReport data : slots) {
            s.put(data.getMonth(), data);
        }
        b.addSeries("MONTH1", s);
        b.addSeries("MONTH2", s);

        b.setxAxis(xAxis);
        b.setStacked(true);

        model.addAttribute("remainTimeSlots", b.build());
        model.addAttribute("year", yearOne);
        model.addAttribute("thisYear", thisYear);
        model.addAttribute("baseY", baseY);
        return "report_momBodyPercent";
    }

    @RequestMapping("daylinep")
    public String orderLinePercent(Model model,
                               @RequestParam(value="day", required = false) String dayStr,
                               @RequestParam(value="baseY", required = false) Long baseY,
                               @RequestParam(value="level", required = false, defaultValue = "S,APP") String lineLevelStr,
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

        List<JpaBusline.Level> levels = new ArrayList<JpaBusline.Level> ();
        try {
            if (lineLevelStr != null) {
                for (String level : lineLevelStr.split(",")) {
                    levels.add(JpaBusline.Level.valueOf(level));
                }
            }
        } catch (Exception e) {
            log.warn("Can not parse line levels {}", lineLevelStr, e);
            levels.add(JpaBusline.Level.S);
            levels.add(JpaBusline.Level.APP);
        }

        HighChartBuilder<Date> b = new HighChartBuilder<Date>("线路售出情况趋势图", XType.DATE);

        DayList days = DayList.range(to, -span);

        DatetimeSeries s = null;
        Map<String, List<TimeslotReport>> slots =  service.getOrderBusesByLineLevels(city, days.getEarlyestDay(), days.getLastDay(), levels);
        Iterator<Map.Entry<String,List<TimeslotReport>>> iter = slots.entrySet().iterator();

        List<String> seriesNames = new ArrayList<String> ();
        Map<String, String> yNames = new HashMap<String, String> ();

        while (iter.hasNext()) {
            Map.Entry<String, List<TimeslotReport>> e = iter.next();

            s = Series.newDatetimeSeries(SeriesType.BUSCOUNT_PERCENT, days);
            s.setPointer(false, 1).addName("remain", "剩余数量").addName("paid", "已售出数量").addName("notPaid", "预售数量");
            for (TimeslotReport data : e.getValue()) {
                s.put(data.getDay(), data);
            }

            if (e.getKey().equals("other")) {
                b.addSeries("OTHER_PAID", s);
                b.addSeries("OTHER_NOTPAID", s);
                seriesNames.add(0, "OTHER_PAID");
                seriesNames.add(0, "OTHER_NOTPAID");
                yNames.put("OTHER_PAID", "paid");
                yNames.put("OTHER_NOTPAID", "notPaid");
                s.setPointer(false, 1).addName("remain", "剩余数量").addName("paid", "其他线路已售").addName("notPaid", "其他线路预售");
            } else {
                for (JpaBusline.Level level : levels) {
                    if (e.getKey().equals(level.name())) {
                        String levelStr = level.name();
                        b.addSeries(levelStr + "_PAID", s);
                        b.addSeries(levelStr + "_NOTPAID", s);
                        seriesNames.add(levelStr + "_PAID");
                        seriesNames.add(levelStr + "_NOTPAID");
                        yNames.put(levelStr + "_PAID", "paid");
                        yNames.put(levelStr + "_NOTPAID", "notPaid");
                        s.setPointer(false, 1).addName("remain", "剩余数量")
                                .addName("paid", level.getNameStr() + "已售").addName("notPaid", level.getNameStr() +"预售");
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
        return "report_dailyBodyLinePercent";
    }
}