package com.pantuo.service;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.StringOperation;
import com.pantuo.dao.BoxRepository;
import com.pantuo.dao.GoodsRepository;
import com.pantuo.dao.ScheduleLogRepository;
import com.pantuo.dao.pojo.*;
import com.pantuo.mybatis.domain.Box;
import com.pantuo.mybatis.domain.BoxExample;
import com.pantuo.mybatis.persistence.BoxMapper;
import com.pantuo.util.DateUtil;
import com.pantuo.util.Schedule;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author tliu
 * 排期Service
 */
@Service
public class ScheduleService {
    private static Logger log = LoggerFactory.getLogger(ScheduleService.class);

    @Autowired
    private ProductService productService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TimeslotService timeslotService;

    @Autowired
    private GoodsRepository goodsRepo;
    @Autowired
    private BoxRepository boxRepo;
    @Autowired
    private BoxMapper boxMapper;
    @Autowired
    private ScheduleLogRepository scheduleLogRepository;

/*    public ScheduleLog schedule(Date day) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date now = cal.getTime();

        ScheduleLog slog = scheduleLogRepository.findByDay(now);
        if (slog != null && (slog.getStatus() == ScheduleLog.Status.scheduling || slog.getStatus() == ScheduleLog.Status.scheduled)) {
            log.info("Scheduling for day {} is running or completed", now);
            return new ScheduleLog(now, ScheduleLog.Status.duplicate);
        }
        log.info(":::Start scheduling for day {}", now);
        slog = new ScheduleLog(now);
        scheduleLogRepository.save(slog);

        try {
            Page<JpaTimeslot> slots = timeslotService.getAllTimeslots(null, 0, 9999);
            Iterable<JpaOrders> ordersForSchedule = orderService.getOrdersForSchedule(now, JpaProduct.Type.video);

            Schedule s = new Schedule(now, slots.getContent(), ordersForSchedule);

            boolean scheduled = s.schedule();
            log.info(s.toString());

            if (scheduled) {
                boxRepo.save(s.getOrderedHotBoxList());
                boxRepo.save(s.getOrderedNormalBoxList());
            }
            slog.setStatus(ScheduleLog.Status.scheduled);
            slog.setDescription("success at " + new Date());
            scheduleLogRepository.save(slog);
            return slog;
        } catch (Exception e) {
            slog.setStatus(ScheduleLog.Status.failed);
            slog.setDescription(e.getMessage());
            scheduleLogRepository.save(slog);
            log.error("Fail to schedule for day {}", now, e);
            return slog;
        }
    }*/

    public ScheduleLog schedule(Date day, int orderId) {
        return schedule(day, orderService.getJpaOrder(orderId));
    }

    public ScheduleLog schedule(Date day, JpaOrders order) {
        return schedule(order, day, 1);
    }

    public ScheduleLog schedule(JpaOrders order) {
        if (order == null || order.getId() == 0) {
            log.error("Order {} does not exists or not persisted");
            return new ScheduleLog(order.getCity(), new Date(), 0, ScheduleLog.Status.failed, "Order " + order + " does not exists or not persisted");
        }
        return schedule(order, order.getStartTime(), order.getProduct().getDays());
    }


/*
    public ScheduleLog schedule(Date day, JpaOrders order) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date now = cal.getTime();

        if (order == null || order.getId() == 0) {
            log.error("Order {} does not exists or not persisted");
            return new ScheduleLog(now, 0, ScheduleLog.Status.failed);
        }
        int orderId = order.getId();

        ScheduleLog slog = scheduleLogRepository.findByDayAndStatus(now, ScheduleLog.Status.scheduling);
        if (slog != null) {
            log.info("Order {} is now scheduling for day {}, please wait", slog.getOrderId(), now);
            return new ScheduleLog(now, orderId, ScheduleLog.Status.racing);
        }

        slog = scheduleLogRepository.findByDayAndOrderId(now, orderId);
        if (slog != null && slog.getStatus() == ScheduleLog.Status.scheduled) {
            log.info("Scheduling for day {} and order {} is running or completed", now, orderId);
            return new ScheduleLog(now, orderId, ScheduleLog.Status.duplicate);
        }

        try {
            MDC.put("func", "Schedule");
            MDC.put("day", GlobalMethods.sdf.get().format(now));
            MDC.put("order", orderId + "");

            log.info(":::Start scheduling for day {}, order {}", now, orderId);
            slog = new ScheduleLog(now, orderId);
            scheduleLogRepository.save(slog);

            Schedule s = null;
            boolean firstOrder = false;
            List<Box> boxes = boxRepo.findByDay(now);
            if (!boxes.isEmpty()) {
                log.info("There is already scheduled orders for day {}", now);
                s = Schedule.newFromBoxes(now, boxes, order);
            } else {
                firstOrder = true;
                log.info("First order to be scheduled for day {}", now);
                Page<JpaTimeslot> slots = timeslotService.getAllTimeslots(null, 0, 9999);
                s = Schedule.newFromTimeslots(now, slots.getContent(), order);
            }

            boolean scheduled = s.schedule();
            log.info(s.toString());

            if (scheduled) {
                boxRepo.save(s.getOrderedHotBoxList());
                boxRepo.save(s.getOrderedNormalBoxList());
            }
            slog.setStatus(ScheduleLog.Status.scheduled);
            slog.setDescription("success at " + new Date());
            scheduleLogRepository.save(slog);
            return slog;
        } catch (Exception e) {
            slog.setStatus(ScheduleLog.Status.failed);
            slog.setDescription(e.getMessage());
            scheduleLogRepository.save(slog);
            log.error("Fail to schedule for day {}", now, e);
            return slog;
        } finally {
            MDC.clear();
        }
    }
*/

    private ScheduleLog schedule(JpaOrders order, Date start, int days) {
        int city = order.getCity();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        if (order == null || order.getId() == 0) {
            log.error("Order {} does not exists or not persisted");
            return new ScheduleLog(city, cal.getTime(), 0, ScheduleLog.Status.failed, "Order " + order + " does not exists or not persisted");
        }
        cal.setTime(start);
        int orderId = order.getId();

        if (days == 0) {
            log.info("Order {} has 0 days", orderId);
            return new ScheduleLog(city, cal.getTime(), orderId, ScheduleLog.Status.scheduled, "Order " + orderId + " has 0 days");
        }

        List<ScheduleLog> slogs = scheduleLogRepository.findByCityAndOrderId(city, orderId);
        if (!slogs.isEmpty()) {
            for (ScheduleLog slog : slogs) {
                if (slog.getStatus() == ScheduleLog.Status.scheduled) {
                    log.info("Scheduling for day {} and order {} has already completed", slog.getDay(), orderId);
                    return new ScheduleLog(city, slog.getDay(), orderId, ScheduleLog.Status.duplicate, "duplicate with day " + slog.getDay());
                } else if (slog.getStatus() == ScheduleLog.Status.scheduling) {
                    log.info("Other thread is now scheduling for day {} and order {}, please wait", slog.getDay(), orderId);
                    return new ScheduleLog(city, slog.getDay(), orderId, ScheduleLog.Status.racing, "racing with day " + slog.getDay());
                }
            }
        }

        slogs = new ArrayList<ScheduleLog> ();
        List<JpaBox> scheduleResult = new LinkedList<JpaBox> ();
        int success = 0;
        for (int i = 0; i< days; i++) {
            cal.add(Calendar.DATE, 1);
            Date now = cal.getTime();

            ScheduleLog slog = null;
            try {
                MDC.put("func", "Schedule");
                MDC.put("day", DateUtil.longDf.get().format(now));
                MDC.put("order", orderId + "");

                log.info(":::Start scheduling for day {}, order {}", now, orderId);
                slog = new ScheduleLog(city, now, orderId);
                scheduleLogRepository.save(slog);
                slogs.add(slog);

                Schedule s = null;
                List<JpaBox> boxes = boxRepo.findByCityAndDay(city, now);
                if (!boxes.isEmpty()) {
                    log.info("There is already scheduled orders for day {}", now);
                    s = Schedule.newFromBoxes(now, boxes, order);
                } else {
                    log.info("First order to be scheduled for day {}", now);
                    Page<JpaTimeslot> slots = timeslotService.getAllTimeslots(city, null, 0, 9999, null, false);
                    s = Schedule.newFromTimeslots(city, now, slots.getContent(), order);
                }

                boolean scheduled = s.schedule();
                log.info(s.toString());

                if (scheduled) {
                    scheduleResult.addAll(s.getOrderedHotBoxList());
                    scheduleResult.addAll(s.getOrderedNormalBoxList());
                    slog.setStatus(ScheduleLog.Status.scheduled);
                    slog.setDescription("success at " + new Date());
                    success ++;
                } else {
                    log.error("Can not arrange the schedule, {} entries can not be boxed", s.getHotNotBoxed().size());
                    slog.setStatus(ScheduleLog.Status.failed);
                    slog.setDescription(s.getHotNotBoxed().size() + " entries can not be boxed");
                    break;
                }
            } catch (Exception e) {
                log.error("Fail to schedule for day {} and order {}", now, orderId, e);
                if (slog != null) {
                    slog.setStatus(ScheduleLog.Status.failed);
                    slog.setDescription(e.getMessage());
                }
                break;
            } finally {
                MDC.clear();
            }
        }
        if (success == days && !scheduleResult.isEmpty()) {
            log.info("Schedule succeeded for all {} days for order {}", success, orderId);
            try {
                boxRepo.save(scheduleResult);
            } catch (Exception e) {
                log.error("Fail to save schedule result", e);
                for (ScheduleLog log : slogs) {
                    log.setStatus(ScheduleLog.Status.failed);
                    log.setDescription("Fail to save schedule result, e=" + e.getMessage());
                }
            }
        }

        Iterator<ScheduleLog> iter = slogs.iterator();
        ScheduleLog removed = null;
        while (iter.hasNext()) {
            ScheduleLog slog = iter.next();
            if (slog.getId() == 0) {
                removed = slog;
                iter.remove();
            }
        }
        if (!slogs.isEmpty()) {
            //fail all if last one (any one) has failes
            ScheduleLog last = slogs.get(slogs.size() - 1);
            if (last.getStatus() != ScheduleLog.Status.scheduled) {
                for (ScheduleLog log : slogs) {
                    if (log.getStatus() == ScheduleLog.Status.scheduled) {
                        log.setStatus(ScheduleLog.Status.failed);
                        log.setDescription("["+last.getDay()+" failed]" + last.getDescription());
                    }
                }
            }
            try {
                scheduleLogRepository.save(slogs);
            } catch (Exception e) {
                log.error("Fail to save schedule logs", e);
            }

            return last;
        }
        return removed;
    }

    public Iterable<JpaGoods> getGoodsForOrder(Integer orderId) {
        BooleanExpression query = QJpaGoods.jpaGoods.order.id.eq(orderId);
        return goodsRepo.findAll(query);
    }

    /**
     * 获取剩余时段表，不获取排期
     * @param from inclusive
     */
    public List<Box> getBoxes(Date from, int days) {
        from = DateUtil.trimDate(from);
        Date to = DateUtils.addDays(from, days);
        BoxExample example = new BoxExample();
        example.createCriteria().andDayGreaterThanOrEqualTo(from).andDayLessThan(to);
        return boxMapper.selectByExample(example);
//        Predicate query = QJpaBox.box.day.stringValue().loe(StringOperation.create(Ops.STRING_CAST, ConstantImpl.create(to)))
//                .and(QJpaBox.box.day.stringValue().goe(StringOperation.create(Ops.STRING_CAST, ConstantImpl.create(from))));
//
//        return boxRepo.findAll(query);
    }

    /**
     * 获取剩余时段表，并获取排期
     * @param from inclusive
     */
    public Iterable<JpaBox> getBoxesAndGoods(Date from, int days) {
        from = DateUtil.trimDate(from);
        Date to = DateUtils.addDays(from, days);
        Predicate query = QJpaBox.jpaBox.day.before(to)
                .and(QJpaBox.jpaBox.day.stringValue().goe(StringOperation.create(Ops.STRING_CAST, ConstantImpl.create(from))));

        return boxRepo.findAll(query);
    }
}
