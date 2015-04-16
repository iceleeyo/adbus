package com.pantuo.service.impl;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.dao.TimeslotRepository;
import com.pantuo.dao.pojo.JpaTimeslot;
import com.pantuo.dao.pojo.QJpaTimeslot;
import com.pantuo.pojo.highchart.DayList;
import com.pantuo.service.TimeslotService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class TimeslotServiceImpl implements TimeslotService {
    @Autowired
    TimeslotRepository timeslotRepo;

   // @Override
    public long count() {
        return timeslotRepo.count();
    }

    public long sumDuration() {
        return timeslotRepo.sumDuration();
    }

    public long sumPeakDuration() {
        return timeslotRepo.sumPeakDuration();
    }

 //   @Override
    public Page<JpaTimeslot> getAllTimeslots(String name, int page, int pageSize, Sort sort, boolean fetchDisabled) {
        if (page < 0)
            page = 0;
        if (pageSize < 1)
            pageSize = 1;
        if (sort == null)
            sort = new Sort("id");
        Pageable p = new PageRequest(page, pageSize, sort);
        BooleanExpression query = null;
        if (StringUtils.isNotBlank(name)) {
            query = QJpaTimeslot.jpaTimeslot.name.like("%" + name + "%");
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
    public void saveProduct(JpaTimeslot timeslot) {
        timeslotRepo.save(timeslot);
    }

 //   @Override
    public void saveProducts(Iterable<JpaTimeslot> timeslots) {
        timeslotRepo.save(timeslots);
    }
}
