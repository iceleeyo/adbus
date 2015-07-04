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
import com.pantuo.pojo.SlotBoxBar;
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

    //根据每天每个时段的box列表，构建基于slot的行级box
    private List<SlotBoxBar> buildSlotBoxBar(Map<Integer/*slotId*/, List<JpaBox>> boxSlotMap) {
        List<SlotBoxBar> boxes = new ArrayList<SlotBoxBar>();
        Iterator<Map.Entry<Integer,List<JpaBox>>> iter = boxSlotMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, List<JpaBox>> e = iter.next();
            boxes.add(new SlotBoxBar(e.getValue()));
        }
        return boxes;
    }

    private ScheduleLog schedule(JpaOrders order, Date start, int days) {
        int city = order.getCity();
        Calendar cal = DateUtil.newCalendar();

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

        List<ScheduleLog> slogList = scheduleLogRepository.findByCityAndOrderId(city, orderId);
        if (!slogList.isEmpty()) {
            for (ScheduleLog slog : slogList) {
                if (slog.getStatus() == ScheduleLog.Status.scheduled) {
                    log.info("Scheduling for day {} and order {} has already completed", slog.getDay(), orderId);
                    return new ScheduleLog(city, slog.getDay(), orderId, ScheduleLog.Status.duplicate, "duplicate with day " + slog.getDay());
                } else if (slog.getStatus() == ScheduleLog.Status.scheduling) {
                    log.info("Other thread is now scheduling for day {} and order {}, please wait", slog.getDay(), orderId);
                    return new ScheduleLog(city, slog.getDay(), orderId, ScheduleLog.Status.racing, "racing with day " + slog.getDay());
                }
            }
        }
        Map<Date, ScheduleLog> slogMap = new HashMap<Date, ScheduleLog>();
        for (int i=0; i<days; i++) {
            ScheduleLog log = new ScheduleLog(city, cal.getTime(), orderId);
            scheduleLogRepository.save(log);
            slogList.add(log);
            slogMap.put(log.getDay(), log);
            cal.add(Calendar.DATE, 1);
        }

        Page<JpaTimeslot> slots = timeslotService.getAllTimeslots(city, null, 0, 9999, null, false);
        List<JpaBox> scheduleResult = new LinkedList<JpaBox> ();
        List<JpaBox> boxList = null;
        int success = 0;

        boolean r1Scheduled = false;
        boolean r1Error = false;
        //round 1, overall
        {
            cal.setTime(start);
            cal.add(Calendar.DATE, days);
            Date end = cal.getTime();

            try {
                MDC.put("func", "Schedule[R1]");
                MDC.put("from", DateUtil.longDf.get().format(start));
                MDC.put("to", DateUtil.longDf.get().format(end));
                MDC.put("order", orderId + "");

                log.info(":::[R1]Start scheduling from {} to {}, order {}", start, end, orderId);

                Schedule s = null;
                boxList = boxRepo.findByCityAndDayGreaterThanEqualAndDayLessThan(city, start, end);
                Map<Date, List<JpaBox>> boxDayMap = new HashMap<Date, List<JpaBox>> ();
                Map<Integer/*slotId*/, List<JpaBox>> boxSlotMap = new HashMap<Integer, List<JpaBox>> ();
                for (JpaBox b : boxList) {
                    List<JpaBox> l = boxDayMap.get(b.getDay());
                    if (l == null) {
                        l = new ArrayList<JpaBox>();
                        boxDayMap.put(b.getDay(), l);
                    }
                    l.add(b);
                }
                log.info(":::[R1]Found {} db scheduled boxes from {} to {}, order {}", boxList.size(), start, end, orderId);

                log.info(":::[R1]Filling up from timeslots in case db scheduled boxes is empty");
                cal.setTime(start);
                for (int i=0; i<days; i++) {
                    Date day = cal.getTime();
                    if (!boxDayMap.containsKey(day)) {
                        log.info("[R1]First order to be scheduled on day {}", day);
                        List<JpaBox> boxesForDay = new ArrayList<>(slots.getContent().size());
                        for (JpaTimeslot slot : slots) {
                            boxesForDay.add(Schedule.boxFromTimeslot(city, day, slot));
                        }
                        boxDayMap.put(day, boxesForDay);
                        boxList.addAll(boxesForDay);
                    }
                    cal.add(Calendar.DATE, 1);
                }

                log.info(":::[R1]Total {} boxes loaded from {} to {}, order {}", boxList.size(), start, end, orderId);
                for (JpaBox b : boxList) {
                    List<JpaBox> l = boxSlotMap.get(b.getSlotId());
                    if (l == null) {
                        l = new ArrayList<JpaBox>();
                        boxSlotMap.put(b.getSlotId(), l);
                    }
                    l.add(b);
                }

                log.info(":::[R1]Compositing slot-box-bar...");
                List<? extends JpaBox> boxes = buildSlotBoxBar(boxSlotMap);

                if (!boxList.isEmpty()) {
                    log.info("[R1]There is already scheduled orders between {} and {}", start, end);
                    s = Schedule.newFromBoxes(start, boxes, order);
                }

                r1Scheduled = s.schedule();
                log.info(s.toString());

                if (r1Scheduled) {
                    success = days;
                    scheduleResult.addAll(boxList);
//                    scheduleResult.addAll(s.getOrderedNormalBoxList());
                    for (ScheduleLog slog : slogList) {
                        slog.setStatus(ScheduleLog.Status.scheduled);
                        slog.setDescription("[R1]Success at " + new Date());
                    }
                } else {
                    log.error("[R1]Can not arrange the schedule, {} entries can not be boxed, will go to [R2]", s.getHotNotBoxed().size());
//                    slog.setStatus(ScheduleLog.Status.failed);
//                    slog.setDescription("[R1]" + s.getHotNotBoxed().size() + " entries can not be boxed");
//                    break;
                }
            } catch (Exception e) {
                r1Error = true;
                log.error("[R1]Fail to schedule from {} to {}, order {}", start, end, orderId, e);
                for (ScheduleLog slog : slogList) {
                    slog.setStatus(ScheduleLog.Status.failed);
                    slog.setDescription(e.getMessage());
                }
            } finally {
                MDC.clear();
            }
        }

        if (!r1Scheduled && !r1Error) {
        //round 2: individual day
            for (int i = 0; i< days; i++) {
                Date now = cal.getTime();

                ScheduleLog slog = null;
                try {
                    MDC.put("func", "Schedule[R2]");
                    MDC.put("day", DateUtil.longDf.get().format(now));
                    MDC.put("order", orderId + "");

                    log.info(":::[R2]Start scheduling for day {}, order {}", now, orderId);

                    Schedule s = null;
                    List<JpaBox> boxes = boxRepo.findByCityAndDay(city, now);
                    if (!boxes.isEmpty()) {
                        log.info("[R2]There is already scheduled orders for day {}", now);
                        s = Schedule.newFromBoxes(now, boxes, order);
                    } else {
                        log.info("[R2]First order to be scheduled for day {}", now);
                        s = Schedule.newFromTimeslots(city, now, slots.getContent(), order);
                    }

                    boolean scheduled = s.schedule();
                    log.info(s.toString());

                    if (scheduled) {
                        scheduleResult.addAll(boxList);
//                        scheduleResult.addAll(s.getOrderedNormalBoxList());
                        slog.setStatus(ScheduleLog.Status.scheduled);
                        slog.setDescription("[R2]success at " + new Date());
                        success ++;
                    } else {
                        log.error("[R2]Can not arrange the schedule, {} entries can not be boxed", s.getHotNotBoxed().size());
                        slog.setStatus(ScheduleLog.Status.failed);
                        slog.setDescription("[R2]" + s.getHotNotBoxed().size() + " entries can not be boxed");
                        break;
                    }
                } catch (Exception e) {
                    log.error("[R2]Fail to schedule for day {} and order {}", now, orderId, e);
                    if (slog != null) {
                        slog.setStatus(ScheduleLog.Status.failed);
                        slog.setDescription(e.getMessage());
                    }
                    break;
                } finally {
                    MDC.clear();
                }
                cal.add(Calendar.DATE, 1);
            }
        }
        if (success == days && !scheduleResult.isEmpty()) {
            log.info("[Save]Schedule succeeded for all {} days for order {}", success, orderId);
            try {
                boxRepo.save(scheduleResult);
            } catch (Exception e) {
                log.error("[Save]Fail to save schedule result", e);
                for (ScheduleLog log : slogList) {
                    log.setStatus(ScheduleLog.Status.failed);
                    log.setDescription("[Save]Fail to save schedule result, e=" + e.getMessage());
                }
            }
        }

        Iterator<ScheduleLog> iter = slogList.iterator();
        ScheduleLog removed = null;
        while (iter.hasNext()) {
            ScheduleLog slog = iter.next();
            if (slog.getId() == 0) {
                removed = slog;
                iter.remove();
            }
        }
        if (!slogList.isEmpty()) {
            //fail all if last one (any one) has failes
            ScheduleLog last = slogList.get(slogList.size() - 1);
            if (last.getStatus() != ScheduleLog.Status.scheduled) {
                for (ScheduleLog log : slogList) {
                    if (log.getStatus() == ScheduleLog.Status.scheduled) {
                        log.setStatus(ScheduleLog.Status.failed);
                        log.setDescription("[Save]["+last.getDay()+" failed]" + last.getDescription());
                    }
                }
            }
            try {
                scheduleLogRepository.save(slogList);
            } catch (Exception e) {
                log.error("[Save]Fail to save schedule logs", e);
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
