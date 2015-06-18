package com.pantuo.service;

import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.dao.GoodsRepository;
import com.pantuo.dao.pojo.*;
import com.pantuo.mybatis.domain.TimeslotReport;
import com.pantuo.mybatis.persistence.ReportMapper;
import com.pantuo.pojo.highchart.DayList;
import com.pantuo.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author tliu
 */
@Service
public class ReportService {

    @Autowired
    private ReportMapper mapper;

    @Autowired
    private GoodsRepository goodsRepo;

    @Autowired
    private TimeslotService timeslotService;

    public List<TimeslotReport> getRemainTimeslots(int city, Date from, Date to, Boolean peak) {
        List<TimeslotReport> report = mapper.getRemainTimeslots(city, from, to, peak);

        //check if there are missing days
        DayList days = DayList.range(from, to);
        List<Date> dayList = days.toDayList();

        if (report.size() < dayList.size()) {
            long duration = (peak == null || !peak)? timeslotService.sumDuration(city) : timeslotService.sumPeakDuration(city);

            HashSet<Date> daysFetched = new HashSet<Date> ();
            for (TimeslotReport r : report) {
                daysFetched.add(r.getDay());
            }

            for (Date d : dayList) {
                if (!daysFetched.contains(d)) {
                    report.add(new TimeslotReport(d, duration, duration));
                }
            }
        }
        return report;
    }

    private Map<Integer, TimeslotReport> getGoods(int city, Date day,  Boolean paid) {
        BooleanExpression query = QJpaGoods.jpaGoods.box.day.eq(day);
        query = query.and(QJpaGoods.jpaGoods.box.city.eq(city));
        if (paid != null) {
            query = query.and(QJpaGoods.jpaGoods.order.stats.goe(JpaOrders.Status.paid));
        }
        Iterable<JpaGoods> goods = goodsRepo.findAll(query);

        Calendar cal = DateUtil.newCalendar();
        Map<Integer, TimeslotReport> reportMap = new HashMap<Integer, TimeslotReport>();
        for (JpaGoods g : goods) {
            Map<Integer, Long> span = DateUtil.durationSpan(cal,
                    new Date(g.getBox().getTimeslot().getStartTime().getTime() + g.getInboxPosition() * 1000),
                    g.getSize());
            for (Integer hour : span.keySet()) {
                TimeslotReport r = reportMap.get(hour);
                if (r != null) {
                    r.setRemain(r.getRemain() + span.get(hour));
                } else {
                    //FIXME: use remain to store "ordered" right now
                    r = new TimeslotReport(hour, span.get(hour), -1);
                    reportMap.put(hour, r);
                }
            }
        }
        return reportMap;
    }

    public List<TimeslotReport> getHourlyTimeslots(int city, Date day, Boolean paid) {
        Map<Integer, TimeslotReport> reportMap = getGoods(city, day, paid);

        //check if there are missing hours
        Map<Integer, Long> durationMap = timeslotService.getDurationByHour(city);
        HashSet<Integer> hoursFetched = new HashSet<Integer> ();
        Iterator<TimeslotReport> iter = reportMap.values().iterator();
        while (iter.hasNext()) {
            TimeslotReport r = iter.next();
            hoursFetched.add(r.getHour());
            Long duration = durationMap.get(r.getHour());
            if (duration == null) {
                //should not happen
                iter.remove();
                continue;
            }
            r.setSize(duration);
            //FIXME: reset so "remain" represent correct syntax
            r.setRemain(duration - r.getRemain());
            r.setDay(day);
        }

        List<TimeslotReport> result = new ArrayList<TimeslotReport>(reportMap.values());

        for (Integer i : durationMap.keySet()) {
            if (!hoursFetched.contains(i)) {
                long duration = durationMap.get(i);
                TimeslotReport r = new TimeslotReport(i, duration, duration);
                r.setDay(day);
                result.add(r);
            }
        }
    return result;
    }

    public List<TimeslotReport> getMonthlyRemainTimeslots(int city, int year, Boolean peak) {
        List<TimeslotReport> report = mapper.getMonthlyRemainTimeslots(city, year, peak);

        //check if there are missing months
        if (report.size() < 12) {
            long duration = (peak == null || !peak)? timeslotService.sumDuration(city) : timeslotService.sumPeakDuration(city);

            HashMap<Integer, TimeslotReport> monthFetched = new HashMap<Integer, TimeslotReport> ();
            for (TimeslotReport r : report) {
                monthFetched.put(r.getMonth(), r);
            }

            for (int i = 1; i <=12; i++) {
                long monthDuration = DateUtil.getDaysInMonth(year, i) * duration;
                if (monthFetched.containsKey(i)) {
                    TimeslotReport r = monthFetched.get(i);
                    long ordered = r.getSize() - r.getRemain();
                    r.setSize(monthDuration);
                    r.setRemain(monthDuration - ordered);
                } else {
                    report.add(new TimeslotReport(year, i, monthDuration, monthDuration));
                }
            }
        }
        return report;
    }

    public List<TimeslotReport> getOrderTimeslots(int city, Date from, Date to, Boolean peak) {
        List<TimeslotReport> report = mapper.getOrderTimeslots(city, from, to, peak);

        //check if there are missing days
        DayList days = DayList.range(from, to);
        List<Date> dayList = days.toDayList();

        if (report.size() < dayList.size()) {
            long duration = (peak == null || !peak)? timeslotService.sumDuration(city) : timeslotService.sumPeakDuration(city);

            HashSet<Date> daysFetched = new HashSet<Date> ();
            for (TimeslotReport r : report) {
                daysFetched.add(r.getDay());
            }

            for (Date d : dayList) {
                if (!daysFetched.contains(d)) {
                    report.add(new TimeslotReport(d, 0, 0, duration));
                }
            }
        }
        return report;
    }

    public Map<Integer/*industry id, -1 means others*/, List<TimeslotReport>>
            getOrderTimeslotsByIndustries(int city, Date from, Date to, List<Integer> distinctIndustries, Boolean peak) {
        Map<Integer, List<TimeslotReport>> map = new HashMap<Integer, List<TimeslotReport>> ();

        List<TimeslotReport> base = getRemainTimeslots(city, from, to, peak);
        List<TimeslotReport> report = mapper.getOrderTimeslotsByIndustries(city, from, to, distinctIndustries, peak);
        Map<String /*day + industryId*/, TimeslotReport> industryMap = new HashMap<String, TimeslotReport>();
        for (TimeslotReport r : report) {
            industryMap.put(r.getDay() + "/" + r.getIndustryId(), r);
        }

        distinctIndustries.add(-1);
        for (int industryId : distinctIndustries) {
            List<TimeslotReport> base2 = new ArrayList<TimeslotReport>(base.size());
            for (TimeslotReport r : base) {
                TimeslotReport r2 = r.clone();
                r2.setIndustryId(industryId);

                TimeslotReport r3 = industryMap.get(r.getDay() + "/" + industryId);
                if (r3 != null) {
                    r2.setPaid(r3.getPaid());
                    r2.setNotPaid(r3.getNotPaid());
                } else {
                    r2.setPaid(0L);
                    r2.setNotPaid(0L);
                }
                base2.add(r2);
            }
            map.put(industryId, base2);
        }

        return map;
    }

    public Map<JpaProduct.Type, List<TimeslotReport>> getSalesDailyTimeslots(int city, JpaCity.MediaType mediaType, Date from, Date to) {
        List<TimeslotReport> report = mapper.getDailyIncomeReport(city, mediaType.ordinal(), from, to);

        Map<JpaProduct.Type, List<TimeslotReport>> map = new HashMap<JpaProduct.Type, List<TimeslotReport>> ();
        for (TimeslotReport r : report) {
            List<TimeslotReport> list = map.get(r.getProductType());
            if (list == null) {
                list = new ArrayList<TimeslotReport> ();
                map.put(r.getProductType(), list);
            }
            list.add(r);
        }

        Iterator<Map.Entry<JpaProduct.Type,List<TimeslotReport>>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<JpaProduct.Type, List<TimeslotReport>> e = iter.next();
            fillSalesDailyReportList(e.getKey(), e.getValue(), DayList.range(from, to));
        }

        for (JpaProduct.Type type : JpaProduct.Type.values()) {
            if (!map.containsKey(type)) {
                ArrayList<TimeslotReport> list = new ArrayList<TimeslotReport> ();
                map.put(type, list);
                fillSalesDailyReportList(type, list, DayList.range(from, to));
            }
        }

        return map;
    }

    private void fillSalesDailyReportList(JpaProduct.Type type, List<TimeslotReport> report, DayList days) {
        //check if there are missing days
        List<Date> dayList = days.toDayList();

        if (report.size() < dayList.size()) {
            HashSet<Date> daysFetched = new HashSet<Date> ();
            for (TimeslotReport r : report) {
                daysFetched.add(r.getDay());
            }

            for (Date d : dayList) {
                int[] ym = DateUtil.getYearAndMonthAndHour(d);
                if (!daysFetched.contains(d)) {
                    report.add(TimeslotReport.newSalesReport(d, ym[0], ym[1], type, 0));
                }
            }
        }

    }

    public Map<JpaProduct.Type, List<TimeslotReport>> getSalesMonthlyTimeslots(
            int city, JpaCity.MediaType mediaType, int year) {
        List<TimeslotReport> report = mapper.getMonthlyIncomeReport(city, mediaType.ordinal(), year);

        Map<JpaProduct.Type, List<TimeslotReport>> map = new HashMap<JpaProduct.Type, List<TimeslotReport>> ();
        for (TimeslotReport r : report) {
            List<TimeslotReport> list = map.get(r.getProductType());
            if (list == null) {
                list = new ArrayList<TimeslotReport> ();
                map.put(r.getProductType(), list);
            }
            list.add(r);
        }

        Iterator<Map.Entry<JpaProduct.Type,List<TimeslotReport>>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<JpaProduct.Type, List<TimeslotReport>> e = iter.next();
            fillSalesMonthlyReportList(e.getKey(), e.getValue(), year);
        }

        for (JpaProduct.Type type : JpaProduct.Type.values()) {
            if (!map.containsKey(type)) {
                ArrayList<TimeslotReport> list = new ArrayList<TimeslotReport> ();
                map.put(type, list);
                fillSalesMonthlyReportList(type, list, year);
            }
        }

        return map;
    }

    private void fillSalesMonthlyReportList(JpaProduct.Type type, List<TimeslotReport> report, int year) {
        //check if there are missing days

        if (report.size() < 12) {
            HashSet<Integer> monthFetched = new HashSet<Integer> ();
            for (TimeslotReport r : report) {
                monthFetched.add(r.getMonth());
            }

            for (int m=1; m<=12; m++) {
                if (!monthFetched.contains(m)) {
                    report.add(TimeslotReport.newSalesReport(null, year, m, type, 0));
                }
            }
        }

    }
}
