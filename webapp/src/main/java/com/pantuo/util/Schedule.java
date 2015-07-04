package com.pantuo.util;

import com.pantuo.dao.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 排期算法
 */
public class Schedule {
    private static Logger log = LoggerFactory.getLogger(Schedule.class);
    private Date day;
    private Boxing hotBoxing;
    private Boxing normalBoxing;
    private List<JpaGoods> normalNotBoxed;
    private List<JpaGoods> hotNotBoxed;

    public static JpaBox boxFromTimeslot(int city, Date day, JpaTimeslot slot) {
        return new JpaBox(city, day, slot);
    }

    private static void fromOrder(Date day, JpaOrders order, HashSet<JpaGoods> hot, HashSet<JpaGoods> normal) {
        JpaProduct prod = order.getProduct();
        int city = order.getCity();
        int playNumber = prod.getPlayNumber();

        assert(playNumber > 0);
        assert(prod.getFirstNumber() + prod.getLastNumber() <= playNumber);

        //两次播出不放在同一个广告时段
        int hotNumber = (int) (playNumber * prod.getHotRatio());
        int normalNumber = playNumber - hotNumber;

        int normalFirst = Math.min(prod.getFirstNumber(), normalNumber);
        int normalLast = Math.min(prod.getLastNumber(), normalNumber - normalFirst);
        int normalMiddle = normalNumber - normalFirst - normalLast;

        int hotFirst = Math.min(prod.getFirstNumber() - normalFirst, hotNumber);
        int hotLast = Math.min(prod.getLastNumber() - normalLast, hotNumber - hotFirst);
        int hotMiddle = hotNumber - hotFirst - hotLast;

        int seed = 31 * 0x9572f8ad + day.hashCode();
        for (int i=0; i<normalFirst; i++) {
            normal.add(new JpaGoods(city, order.getId(), order.getProduct().getDuration(), true, false, seed++));
        }
        for (int i=0; i<normalLast; i++) {
            normal.add(new JpaGoods(city, order.getId(), order.getProduct().getDuration(), false, true, seed++));
        }
        for (int i=0; i<normalMiddle; i++) {
            normal.add(new JpaGoods(city, order.getId(), order.getProduct().getDuration(), false, false, seed++));
        }
        for (int i=0; i<hotFirst; i++) {
            hot.add(new JpaGoods(city, order.getId(), order.getProduct().getDuration(), true, false, seed++));
        }
        for (int i=0; i<hotLast; i++) {
            hot.add(new JpaGoods(city, order.getId(), order.getProduct().getDuration(), false, true, seed++));
        }
        for (int i=0; i<hotMiddle; i++) {
            hot.add(new JpaGoods(city, order.getId(), order.getProduct().getDuration(), false, false, seed++));
        }
    }

    public static Schedule newFromTimeslots(int city, Date date, List<JpaTimeslot> timeslots, JpaOrders order) {
        return newFromTimeslots(city, date, timeslots, Arrays.asList(new JpaOrders[] {order}));
    }

    public static Schedule newFromBoxes(Date date, List<? extends JpaBox> boxes, JpaOrders order) {
        return newFromBoxes(date, boxes, Arrays.asList(new JpaOrders[] {order}));
    }

    public static  Schedule newFromTimeslots(int city, Date date, List<JpaTimeslot> timeslots, Iterable<JpaOrders> orders) {
        Schedule schedule = new Schedule();
        Calendar cal = DateUtil.newCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        schedule.day = cal.getTime();

        schedule.hotBoxing = new Boxing();
        schedule.normalBoxing = new Boxing();
        for (JpaTimeslot slot : timeslots) {
            if (slot.isPeak()) {
                schedule.hotBoxing.addBox(boxFromTimeslot(city, schedule.day, slot));
            } else {
                schedule.normalBoxing.addBox(boxFromTimeslot(city, schedule.day, slot));
            }
        }
        for (JpaOrders order : orders) {
            fromOrder(schedule.day, order, schedule.hotBoxing.getGoods(), schedule.normalBoxing.getGoods());
        }
        return schedule;
    }

    public static Schedule newFromBoxes(Date date, List<? extends JpaBox> boxes, Iterable<JpaOrders> orders) {
        Schedule schedule = new Schedule();
        Calendar cal = DateUtil.newCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        schedule.day = cal.getTime();

        schedule.hotBoxing = new Boxing();
        schedule.normalBoxing = new Boxing();
        for (JpaBox box : boxes) {
            if (box.getTimeslot().isPeak()) {
                schedule.hotBoxing.addBox(box);
            } else {
                schedule.normalBoxing.addBox(box);
            }
        }
        for (JpaOrders order : orders) {
            fromOrder(schedule.day, order, schedule.hotBoxing.getGoods(), schedule.normalBoxing.getGoods());
        }
        return schedule;
    }

    public boolean schedule() {
        log.info("Start normal boxing");
        normalNotBoxed = normalBoxing.boxing();
        log.info("Normal boxing with {} goods unboxed, transferring to hot boxes:", normalNotBoxed.size());
        hotBoxing.addGoods(normalNotBoxed);
        log.info("Start hot boxing");
        hotNotBoxed = hotBoxing.boxing();
        log.info("Final (hot) boxing with {} goods unboxed:", hotNotBoxed.size());

        return hotNotBoxed.isEmpty();
    }

    public List<JpaGoods> getNormalNotBoxed() {
        return normalNotBoxed;
    }

    public List<JpaGoods> getHotNotBoxed() {
        return hotNotBoxed;
    }

    public double getHotRemainPercentage() {
        return hotBoxing.getRemainPercentage();
    }

    public double getNormalRemainPercentage() {
        return normalBoxing.getRemainPercentage();
    }

    public List<JpaBox> getOrderedNormalBoxList() {
        return normalBoxing.toOrderedBoxList();
    }

    public List<JpaBox> getOrderedHotBoxList() {
        return hotBoxing.toOrderedBoxList();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================NORMAL BOX STATUS START========================\n");
        sb.append(normalBoxing.toString()).append("\n");
        sb.append("::Normal boxing with " + normalNotBoxed.size() + " not boxed goods that transferred to hot boxes:\n");
        for (JpaGoods g : normalNotBoxed) {
            sb.append("\t").append(g).append("\n");
        }
        sb.append("==========================HOT BOX STATUS START=========================\n");
        sb.append(hotBoxing.toString()).append("\n");
        sb.append("::Final (hot) boxing with " + hotNotBoxed.size() + " goods not boxed:\n");
        for (JpaGoods g : hotNotBoxed) {
            sb.append("\t").append(g).append("\n");
        }
        sb.append("=============================BOX STATUS END============================\n");
        return sb.toString();
    }
}
