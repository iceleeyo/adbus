package com.pantuo.service;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.dao.BoxRepository;
import com.pantuo.dao.GoodsRepository;
import com.pantuo.dao.pojo.JpaGoods;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.QJpaBox;
import com.pantuo.dao.pojo.QJpaGoods;
import com.pantuo.mybatis.domain.BoxExample;
import com.pantuo.mybatis.domain.TimeslotReport;
import com.pantuo.mybatis.persistence.BoxMapper;
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

    public List<TimeslotReport> getRemainTimeslots(Date from, Date to, Boolean peak) {
        List<TimeslotReport> report = mapper.getRemainTimeslots(from, to, peak);

        //check if there are missing days
        DayList days = DayList.range(from, to);
        List<Date> dayList = days.toDayList();

        if (report.size() < dayList.size()) {
            long duration = (peak == null || !peak)? timeslotService.sumDuration() : timeslotService.sumPeakDuration();

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

    private Map<Integer, TimeslotReport> getGoods(Date day,  Boolean paid) {
        BooleanExpression query = QJpaGoods.jpaGoods.box.day.eq(day);
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

    public List<TimeslotReport> getHourlyTimeslots(Date day, Boolean paid) {
        Map<Integer, TimeslotReport> reportMap = getGoods(day, paid);

        //check if there are missing hours
        Map<Integer, Long> durationMap = timeslotService.getDurationByHour();
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

    public List<TimeslotReport> getMonthlyRemainTimeslots(int year, Boolean peak) {
        List<TimeslotReport> report = mapper.getMonthlyRemainTimeslots(year, peak);

        //check if there are missing months
        if (report.size() < 12) {
            long duration = (peak == null || !peak)? timeslotService.sumDuration() : timeslotService.sumPeakDuration();

            HashSet<Integer> monthFetched = new HashSet<Integer> ();
            for (TimeslotReport r : report) {
                monthFetched.add(r.getMonth());
            }

            for (int i = 1; i <=12; i++) {
                if (!monthFetched.contains(i)) {
                    long monthDuration = DateUtil.getDaysInMonth(year, i) * duration;
                    report.add(new TimeslotReport(year, i, monthDuration, monthDuration));
                }
            }
        }
        return report;
    }

    public List<TimeslotReport> getOrderTimeslots(Date from, Date to, Boolean peak) {
        List<TimeslotReport> report = mapper.getOrderTimeslots(from, to, peak);

        //check if there are missing days
        DayList days = DayList.range(from, to);
        List<Date> dayList = days.toDayList();

        if (report.size() < dayList.size()) {
            long duration = (peak == null || !peak)? timeslotService.sumDuration() : timeslotService.sumPeakDuration();

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

}
