package com.pantuo.service;

import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.dao.BusScheduleRepository;
import com.pantuo.dao.pojo.*;
import com.pantuo.mybatis.persistence.BusCustomMapper;
import com.pantuo.pojo.highchart.DayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author tliu
 */
@Service
public class BusScheduleService {
    private static Logger log = LoggerFactory.getLogger(BusScheduleService.class);

    @Autowired
    private BusScheduleRepository repo;

    @Autowired
    private BusCustomMapper mapper;

    public synchronized boolean schedule(JpaOrders order) {
        if (order == null) {
            log.info("Fail to schedule buses for order null");
            return false;
        }
        try {
            int deleted = clearSchedule(order.getId());
            if (deleted != 0) {
                log.warn("Start scheduling, cleared {} scheduled buses for order {}", deleted, order.getId());
            }

            boolean result = trySchedule(order);

            if (!result) {
                deleted = clearSchedule(order.getId());
                if (deleted != 0) {
                    log.warn("Failed to schedule, cleared {} scheduled buses for order {}", deleted, order.getId());
                }
            }

            return result;
        } catch (Exception e) {
            log.error("Fail to schedule buses for order {}", order.getId(), e);
            int deleted = clearSchedule(order.getId());
            if (deleted != 0) {
                log.warn("Error scheduling, cleared {} scheduled buses for order {}", deleted, order.getId());
            }
            return false;
        }
    }

    private int clearSchedule (int orderId) {
        return repo.deleteByOrderId(orderId);
    }


    private boolean trySchedule(JpaOrders order) {
        DayList days = DayList.range(order.getStartTime(), -order.getProduct().getDays());

        Iterator<JpaOrderBuses> iter = order.getOrderBusesListByPriority().iterator();
        while (iter.hasNext()) {
            JpaOrderBuses e = iter.next();
            List<JpaBusSchedule> list = scheduleRandom(order.getCity(), days.getEarlyestDay(), days.getLastDay(), order.getId(), e);
            if (list == null) {
                log.error("Fail to schedule buses for order {} and orderBuses {}", order.getId(), e);
                return false;
            }
            batchSave(list);
        }

        int remain = order.getSelectableBusesNumber();
        List<JpaBusSchedule> list = scheduleRandom(order.getCity(), days.getEarlyestDay(), days.getLastDay(), order.getId(), order.getProduct().getLineLevel(), remain);
        if (list == null) {
            log.error("Fail to schedule buses for order {} and remain unselected {} buses", order.getId(), remain);
            return false;
        }
        batchSave(list);

        return true;
    }

    private void batchSave(List<JpaBusSchedule> scheduleList) {
        List<JpaBusSchedule> batch = new ArrayList<JpaBusSchedule>(200);
        int i = 0;
        for (JpaBusSchedule s : scheduleList) {
            batch.add(s);
            i ++;

            if (i == 200) {
                repo.save(batch);
                batch.clear();
                i = 0;
            }
        }

        if (!batch.isEmpty())
            repo.save(batch);
    }

    //return null if failure
    private List<JpaBusSchedule> scheduleRandom(int city, Date startDay, Date endDay, int orderId, JpaBusline.Level level, int number) {
        List<Integer> buses = mapper.getRemainBuses(city, startDay, endDay, level.ordinal(), number);
        if (buses.size() < number) {
            log.error("Fail to schedule buses for city {}, startDay {}, endDay{}, orderId {}, level {}, number {}, not enough buses",
                    city, startDay, endDay, orderId, level, number);
            return null;
        }
        List<JpaBusSchedule> list = new ArrayList<JpaBusSchedule> (number);
        for (Integer bus : buses) {
            list.add(new JpaBusSchedule(city, bus, orderId, startDay, endDay));
        }
        return list;
    }

    //return null if failure
    private List<JpaBusSchedule> scheduleRandom(int city, Date startDay, Date endDay, int orderId, JpaOrderBuses ob) {
        List<Integer> buses = mapper.getRemainBuses2(city, startDay, endDay, ob.getLevel().ordinal(),
                (ob.getCategory() == null ? null : ob.getCategory().ordinal()),
                (ob.getCompany() == null ? null : ob.getCompany().getId()),
                (ob.getLine() == null ? null : ob.getLine().getId()),
                (ob.getModel() == null ? null : ob.getModel().getId()),
                ob.getBusNumber());
        if (buses.size() < ob.getBusNumber()) {
            log.error("Fail to schedule buses for city {}, startDay {}, endDay {}, orderId {}, busSelection {}, not enough buses",
                    city, startDay, endDay, orderId, ob);
            return null;
        }
        List<JpaBusSchedule> list = new ArrayList<JpaBusSchedule> (ob.getBusNumber());
        for (Integer bus : buses) {
            list.add(new JpaBusSchedule(city, bus, orderId, startDay, endDay));
        }
        return list;
    }

    public Page<JpaBusSchedule> getByOrder(int city, int orderId, int page, int pageSize, Sort sort) {
        if (page < 0)
            page = 0;
        if (pageSize < 1)
            pageSize = 1;
        sort = (sort == null ? new Sort("id") : sort);
        Pageable p = new PageRequest(page, pageSize, sort);

        BooleanExpression query = QJpaBusSchedule.jpaBusSchedule.city.eq(city);
        query = query.and(QJpaBusSchedule.jpaBusSchedule.order.id.eq(orderId));

        return repo.findAll(query, p);
    }
}
