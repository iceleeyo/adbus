package com.pantuo.service;

import com.pantuo.mybatis.domain.TimeslotReport;
import com.pantuo.mybatis.persistence.ReportMapper;
import com.pantuo.pojo.highchart.DayList;
import com.pantuo.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @author tliu
 */
@Service
public class ReportService {

    @Autowired
    private ReportMapper mapper;

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
}
