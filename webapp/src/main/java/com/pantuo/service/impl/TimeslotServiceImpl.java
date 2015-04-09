package com.pantuo.service.impl;

import com.mysema.query.types.Predicate;
import com.pantuo.dao.TimeslotRepository;
import com.pantuo.dao.pojo.JpaTimeslot;
import com.pantuo.dao.pojo.QJpaTimeslot;
import com.pantuo.service.TimeslotService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TimeslotServiceImpl implements TimeslotService {
    @Autowired
    TimeslotRepository timeslotRepo;

    @Override
    public long count() {
        return timeslotRepo.count();
    }

    @Override
    public Page<JpaTimeslot> getAllTimeslots(String name, int page, int pageSize) {
        if (page < 0)
            page = 0;
        if (pageSize < 1)
            pageSize = 1;
        Pageable p = new PageRequest(page, pageSize, new Sort("id"));
        if (name == null || StringUtils.isEmpty(name)) {
            return  timeslotRepo.findAll(p);
        } else {
            Predicate query = QJpaTimeslot.jpaTimeslot.name.like("%" + name + "%");
            return timeslotRepo.findAll(query, p);
        }
    }

    @Override
    public JpaTimeslot findById(int id) {
        return timeslotRepo.findOne(id);
    }

    @Override
    public void saveProduct(JpaTimeslot timeslot) {
        timeslotRepo.save(timeslot);
    }

    @Override
    public void saveProducts(Iterable<JpaTimeslot> timeslots) {
        timeslotRepo.save(timeslots);
    }
}
