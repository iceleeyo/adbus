package com.pantuo.service;

import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.dao.BusRepository;
import com.pantuo.dao.GoodsRepository;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.JpaGoods;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.QJpaGoods;
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
public class BodyReportService {

    @Autowired
    private ReportMapper mapper;

    @Autowired
    private BusRepository busRepo;

    public List<TimeslotReport> getRemainBuses(int city, Date from, Date to) {
        List<TimeslotReport> report = mapper.getOrderBuses(city, from, to);
        long total = busRepo.countByCityAndEnabled(city, true);

        DayList days = DayList.range(from, to);
        List<Date> dayList = days.toDayList();

        HashSet<Date> daysFetched = new HashSet<Date> ();
        for (TimeslotReport r : report) {
            r.setSize(total);
            daysFetched.add(r.getDay());
        }

        for (Date d : dayList) {
            if (!daysFetched.contains(d)) {
                report.add(new TimeslotReport(d, total, total));
            }
        }
        return report;
    }

    public List<TimeslotReport> getMonthlyRemainBuses(int city, int year) {
        List<TimeslotReport> report = mapper.getMonthlyOrderBuses(city, year);

        long total = busRepo.countByCityAndEnabled(city, true);

        HashMap<Integer, TimeslotReport> monthFetched = new HashMap<Integer, TimeslotReport> ();
        for (TimeslotReport r : report) {
            r.setSize(total);
            monthFetched.put(r.getMonth(), r);
        }

        for (int i = 1; i <=12; i++) {
            if (!monthFetched.containsKey(i)) {
                report.add(new TimeslotReport(year, i, total, total));
            }
        }
        return report;
    }

    public Map<String/*level str, 'other' means others*/, List<TimeslotReport>>
            getOrderBusesByLineLevels(int city, Date from, Date to, List<JpaBusline.Level> levels) {
        List<Integer> levelOrdinals = new ArrayList<Integer> ();
        for (JpaBusline.Level l : levels) {
            levelOrdinals.add(l.ordinal());
        }
        Map<String, List<TimeslotReport>> map = new HashMap<String, List<TimeslotReport>> ();

        List<TimeslotReport> base = getRemainBuses(city, from, to);
        List<TimeslotReport> report = mapper.getOrderBusesByLineLevels(city, from, to, levelOrdinals);
        Map<String /*day + industryId*/, TimeslotReport> levelMap = new HashMap<String, TimeslotReport>();
        for (TimeslotReport r : report) {
            levelMap.put(r.getDay() + "/" + r.getLevel(), r);
        }

        levelOrdinals.add(-1);
        for (Integer l : levelOrdinals) {
            String levelStr = (l == -1? "other" : JpaBusline.Level.values()[l].name());
            List<TimeslotReport> base2 = new ArrayList<TimeslotReport>(base.size());
            for (TimeslotReport r : base) {
                TimeslotReport r2 = r.clone();
                r2.setLevel(levelStr);

                TimeslotReport r3 = levelMap.get(r.getDay() + "/" + l);
                if (r3 != null) {
                    r2.setPaid(r3.getPaid());
                    r2.setNotPaid(r3.getNotPaid());
                } else {
                    r2.setPaid(0L);
                    r2.setNotPaid(0L);
                }
                base2.add(r2);
            }
            map.put(levelStr, base2);
        }

        return map;
    }
}
