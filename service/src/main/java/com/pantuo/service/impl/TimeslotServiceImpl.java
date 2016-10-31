package com.pantuo.service.impl;

import java.security.Principal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Ops;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.StringOperation;
import com.pantuo.dao.InfoImgScheduleRepository;
import com.pantuo.dao.TimeslotRepository;
import com.pantuo.dao.pojo.JpaBlackAd;
import com.pantuo.dao.pojo.JpaInfoImgSchedule;
import com.pantuo.dao.pojo.JpaTimeslot;
import com.pantuo.dao.pojo.QJpaBlackAd;
import com.pantuo.dao.pojo.QJpaBox;
import com.pantuo.dao.pojo.QJpaInfoImgSchedule;
import com.pantuo.dao.pojo.QJpaTimeslot;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.TimeslotService;
import com.pantuo.util.DateUtil;

@Service
public class TimeslotServiceImpl implements TimeslotService {
    @Autowired
    TimeslotRepository timeslotRepo;
    @Autowired
    InfoImgScheduleRepository infoImgScheduleRepository;

   // @Override
    public long count() {
        return timeslotRepo.count();
    }

    public long sumDuration(int city) {
        return timeslotRepo.sumDuration(city);
    }

    public long sumPeakDuration(int city) {
        return timeslotRepo.sumPeakDuration(city);
    }

    public Map<Integer, Long> getDurationByHour(int city) {
        Page<JpaTimeslot> page = getAllTimeslots(city, null, 0, 999, null, false);
        List<JpaTimeslot> list = page.getContent();
        Calendar cal = DateUtil.newCalendar();
        Map<Integer, Long> result = new HashMap<Integer, Long>();
        for (JpaTimeslot t : list) {
            Map<Integer, Long> span = DateUtil.durationSpan(cal, t.getStartTime(), t.getDuration());
            for (Integer hour : span.keySet()) {
                Long d = result.get(hour);
                if (d != null) {
                    result.put(hour, d + span.get(hour));
                } else {
                    result.put(hour, span.get(hour));
                }
            }
        }
        return result;
    }

 //   @Override
    public Page<JpaTimeslot> getAllTimeslots(int city, String name, int page, int pageSize, Sort sort, boolean fetchDisabled) {
        if (page < 0)
            page = 0;
        if (pageSize < 1)
            pageSize = 1;
        if (sort == null)
            sort = new Sort("id");
        Pageable p = new PageRequest(page, pageSize, sort);
        BooleanExpression query = QJpaTimeslot.jpaTimeslot.city.eq(city);
        if (StringUtils.isNotBlank(name)) {
            query = query.and(QJpaTimeslot.jpaTimeslot.name.like("%" + name + "%"));
        }
        if (!fetchDisabled) {
            BooleanExpression q = QJpaTimeslot.jpaTimeslot.enabled.isTrue();
            if (query == null)
                query = q;
            else
                query = query.and(q);
        }
        return query == null ? timeslotRepo.findAll(p) : timeslotRepo.findAll(query, p);
    }

    //@Override
    public JpaTimeslot findById(int id) {
        return timeslotRepo.findOne(id);
    }

   // @Override
    public void saveTimeslot(JpaTimeslot timeslot) {
        timeslotRepo.save(timeslot);
    }

 //   @Override
    public void saveTimeslots(Iterable<JpaTimeslot> timeslots) {
        timeslotRepo.save(timeslots);
    }

//@Override
public List<JpaInfoImgSchedule> getInfoSchedule(int city, TableRequest req, Principal principal,String mtype) throws ParseException {
	 String dayStr = req.getFilter("day");
	 Date day = DateUtil.longDf.get().parse(dayStr);
	 Date from = DateUtil.trimDate(day);
     Date to = DateUtils.addDays(from, 1);
	BooleanExpression query = QJpaInfoImgSchedule.jpaInfoImgSchedule.date.before(to).and(QJpaInfoImgSchedule.jpaInfoImgSchedule.date.stringValue().goe(StringOperation.create(Ops.STRING_CAST, ConstantImpl.create(from))));
	query=query.and(QJpaInfoImgSchedule.jpaInfoImgSchedule.type.eq(JpaInfoImgSchedule.Type.valueOf(mtype)));
	query=query.and(QJpaInfoImgSchedule.jpaInfoImgSchedule.isDeleted.eq(false));
	return (List<JpaInfoImgSchedule>) infoImgScheduleRepository.findAll(query);
}
}
