package com.pantuo.web;

import com.pantuo.dao.pojo.*;
import com.pantuo.mybatis.domain.Box;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.service.OrderService;
import com.pantuo.service.ScheduleService;
import com.pantuo.service.TimeslotService;
import com.pantuo.util.DateUtil;
import com.pantuo.util.GlobalMethods;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

/**
 * @author tliu
 *
 * 排期表controller
 */
@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/schedule")
public class ScheduleController {
    private static Logger log = LoggerFactory.getLogger(ScheduleController.class);

    @Autowired
    private ScheduleService service;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TimeslotService timeslotService;

    /**
     * 排期表表单
     */
    @RequestMapping("{orderId}")
    public String getOrderSchedule (
            Model model,
            @PathVariable("orderId") int orderId) {
        JpaOrders order = orderService.getJpaOrder(orderId);
        if (order != null && order.getStartTime().before(order.getEndTime())) {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            cal.setTime(order.getStartTime());

            List<String> dates = new ArrayList<String> ();
            while (cal.getTime().before(order.getEndTime())) {
                dates.add(GlobalMethods.sdf.get().format(cal.getTime()));
                cal.add(Calendar.DATE, 1);
            }
            model.addAttribute("dates", dates);
            model.addAttribute("order", order);
        }
        return "order_schedule";
    }

    /**
     * 排期表
     */
    @RequestMapping("order-ajax-list")
    @ResponseBody
    public List<Report> getScheduleListForOrder(
            @RequestParam(value = "orderId", required = true) int orderId) {

        try {
            JpaOrders order = orderService.getJpaOrder(orderId);
            if (order.getType() != JpaProduct.Type.video) {
                //TODO: image/info排期
                return Collections.EMPTY_LIST;
            }

            Page<JpaTimeslot> slots = timeslotService.getAllTimeslots(null, 0, 999);
            Iterable<JpaGoods> goods = service.getGoodsForOrder(orderId);

            List<Report> reports = new LinkedList<Report> ();
            Map<Integer, Report> reportMap = new HashMap<Integer, Report> ();
            for (JpaTimeslot slot : slots) {
                Report r = new Report(slot);
                reports.add(r);
                reportMap.put(slot.getId(), r);
            }

            for (JpaGoods g : goods) {
                if (g.getBox() == null) {
                    log.warn ("No boxId for goods {}", g.getId());
                }
                JpaBox b = g.getBox();
                Report r = reportMap.get(b.getSlotId());
                if (r != null) {
                    r.addBox(b, g);
                }
            }

            //remove empty rows
            Iterator<Report> iter = reports.iterator();
            while (iter.hasNext()) {
                Report r = iter.next();
                if (r.getBoxes().isEmpty())
                    iter.remove();
            }

            return reports;
        } catch (Exception e) {
            log.error("Fail to get schedule for order {}", orderId, e);
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * 剩余时段表表单
     * @return
     */
    @RequestMapping("report")
    public String getScheduleReport(Model model,
            @RequestParam(value = "from", required = false) String fromStr,
            @RequestParam(value = "days", required = false, defaultValue = "7") int days,
            @RequestParam(value = "type", required = false, defaultValue = "video") JpaProduct.Type type) {
        Date from = null;

        if (StringUtils.isNotBlank(fromStr)) {
            try {
                from = GlobalMethods.sdf.get().parse(fromStr);
            } catch (Exception e) {}
        }
        if (from == null) {
            from = new Date();
        }

        Date d = from;
        List<String> dates = new ArrayList<String> ();
        dates.add(GlobalMethods.sdf.get().format(d));
        for (int i = 0 ; i< days - 1; i++) {
            d = DateUtils.addDays(d, 1);
            dates.add(GlobalMethods.sdf.get().format(d));
        }
        model.addAttribute("from", GlobalMethods.sdf.get().format(from));
        model.addAttribute("days", days);
        model.addAttribute("dates", dates);
        model.addAttribute("type", type);

        return "schedule_report";
    }

    /**
     * 剩余时段表
     */
    @RequestMapping("box-ajax-list")
    @ResponseBody
    public List<Report> getScheduleReportList (
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "from", required = false) String fromStr,
            @RequestParam(value = "days", required = false, defaultValue = "7") int days,
            @RequestParam(value = "type", required = false, defaultValue = "video") JpaProduct.Type type
    ) {
        if (type != JpaProduct.Type.video) {
            //TODO:image/info 排期单
            return Collections.EMPTY_LIST;
        }

        try {
            Page<JpaTimeslot> slots = timeslotService.getAllTimeslots(name, 0, 999);
            Date from = GlobalMethods.sdf.get().parse(fromStr);
            List<Box> boxes = service.getBoxes(from, days);

            //total row
            long totalDuration = 0;
            Map<String/*date*/, UiBox> totalBoxes = new HashMap<String, UiBox> ();
            Date d = from;
            for (int i=0; i<days; i++) {
                UiBox t = new UiBox();
                t.setDay(d);
                totalBoxes.put(GlobalMethods.sdf.get().format(d), t);
                d = DateUtils.addDays(d, 1);
            }

            List<Report> reports = new LinkedList<Report> ();
            Map<Integer, Report> reportMap = new HashMap<Integer, Report> ();
            for (JpaTimeslot slot : slots) {
                totalDuration += slot.getDuration();
                Report r = new Report(slot);
                reports.add(r);
                reportMap.put(slot.getId(), r);
            }

            for (Box t : totalBoxes.values()) {
                t.setSize(totalDuration);
                t.setRemain(totalDuration);
            }

            for (Box b : boxes) {
                Report r = reportMap.get(b.getSlotId());
                if (r != null) {
                    String key = r.addBox(b);
                    Box t = totalBoxes.get(key);
                    if (t != null) {
                        t.setRemain(t.getRemain()- (b.getSize() - b.getRemain()));
                    }
                }

            }

            //add total row
            Report totalReport = new Report(new JpaTimeslot("汇总", null, totalDuration, false));
            totalReport.setBoxes(totalBoxes);
            reports.add(totalReport);

            return reports;
        } catch (Exception e) {
            log.error("invalid from {}, should be in yyyy-MM-dd", fromStr, e);
            return Collections.EMPTY_LIST;
        }
    }


    /**
     * 排条单表单
     * @return
     */
    @RequestMapping("list")
    public String getScheduleList(Model model,
                                    @RequestParam(value = "day", required = false) String dayStr,
                                    @RequestParam(value = "type", required = false, defaultValue = "video") JpaProduct.Type type) {
        Date day = null;

        if (StringUtils.isNotBlank(dayStr)) {
            try {
                day = GlobalMethods.sdf.get().parse(dayStr);
            } catch (Exception e) {}
        }
        if (day == null) {
            day = new Date();
        }

        model.addAttribute("day", GlobalMethods.sdf.get().format(day));
        model.addAttribute("type", type);

        return "schedule_list";
    }

    /**
     * 排条单
     */
    @RequestMapping("box-detail-ajax-list")
    @ResponseBody
    public List<Report> getScheduleDetailList (
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "day", required = false) String dayStr,
            @RequestParam(value = "type", required = false, defaultValue = "video") JpaProduct.Type type
    ) {
        if (type != JpaProduct.Type.video) {
            //TODO:image/info 排条单
            return Collections.EMPTY_LIST;
        }

        try {
            Page<JpaTimeslot> slots = timeslotService.getAllTimeslots(name, 0, 999);
            Date day = GlobalMethods.sdf.get().parse(dayStr);
            Iterable<JpaBox> boxes = service.getBoxesAndGoods(day, 1);

            //total row
            long totalDuration = 0;
            Map<String/*date*/, UiBox> totalBoxes = new HashMap<String, UiBox> ();
            Date d = day;
            String dStr = GlobalMethods.sdf.get().format(d);
            for (int i=0; i<1; i++) {
                UiBox t = new UiBox();
                t.setDay(d);
                totalBoxes.put(GlobalMethods.sdf.get().format(d), t);
                d = DateUtils.addDays(d, 1);
            }

            List<Report> reports = new LinkedList<Report> ();
            Map<Integer, Report> reportMap = new HashMap<Integer, Report> ();
            for (JpaTimeslot slot : slots) {
                totalDuration += slot.getDuration();
                Report r = new Report(slot);
                reports.add(r);
                reportMap.put(slot.getId(), r);
            }

            for (Box t : totalBoxes.values()) {
                t.setSize(totalDuration);
                t.setRemain(totalDuration);
            }

            for (JpaBox b : boxes) {
                Report r = reportMap.get(b.getSlotId());
                if (r != null) {
                    String key = r.addBox(b);
                    Box t = totalBoxes.get(key);
                    if (t != null) {
                        t.setRemain(t.getRemain()- (b.getSize() - b.getRemain()));
                    }
                }

            }

            //add total row
/*            Report totalReport = new Report(new JpaTimeslot("汇总", null, totalDuration, false));
            totalReport.setBoxes(totalBoxes);
            reports.add(totalReport);*/

            return flatDetailForGoods(dStr, reports);
        } catch (Exception e) {
            log.error("invalid day {}, should be in yyyy-MM-dd", dayStr, e);
            return Collections.EMPTY_LIST;
        }
    }

    private List<Report> flatDetailForGoods(String day, List<Report> reports) {
        List<Report> list = new LinkedList<Report> ();
        for (Report r : reports) {
            UiBox box = r.getBox(day);
            if (box == null || box.getGoods().isEmpty()) {
                //fill in blank box
                list.add(r);
            } else {
                //create report records for each goods
                List<JpaGoods> goods = box.fetchSortedGoods();
                //clear goods to make it ready for later copy
                box.setGoods(null);
                for (JpaGoods g : goods) {
                    UiBox b = new UiBox(box);
                    b.addGood(g);
                    Report newReport = new Report(r.getSlot());
                    newReport.addBox(b);
                    list.add(newReport);
                }
            }
        }
        return list;
    }

    public static class Report implements Serializable {
        private Map<String/*date*/, UiBox> boxes;
        private JpaTimeslot slot;

        public Report(JpaTimeslot slot) {
            this.slot = slot;
            this.boxes = new HashMap<String, UiBox> ();
        }

        public String addBox(Box box) {
            String key = GlobalMethods.sdf.get().format(box.getDay());
            if (!(box instanceof UiBox))
                box = new UiBox(box);
            boxes.put(key, (UiBox)box);
            return key;
        }

        public String addBox(JpaBox box) {
            List<JpaGoods> goods = box.getGoods();

            String key = GlobalMethods.sdf.get().format(box.getDay());
            UiBox b = boxes.get(key);
            if (b == null) {
                b = new UiBox(box);
                boxes.put(key, b);
            }
            if (goods != null) {
                for (JpaGoods g : goods) {
                    b.addGood(g);
                }
            }
            return key;
        }

        public String addBox(JpaBox box, JpaGoods good) {
            String key = GlobalMethods.sdf.get().format(box.getDay());
            UiBox b = boxes.get(key);
            if (b != null) {
                b.addGood(good);
            } else {
                b = new UiBox(box);
                b.addGood(good);
                boxes.put(key, b);
            }

            return key;
        }

        public Map<String/*date*/, UiBox> getBoxes() {
            return boxes;
        }

        private void setBoxes(Map<String, UiBox> boxes) {
            this.boxes = boxes;
        }

        private UiBox getBox(String dayStr) {
            return boxes.get(dayStr);
        }

        public JpaTimeslot getSlot() {
            return slot;
        }
    }

    public static class UiBox extends Box {
        private List<JpaGoods> goods;
        public UiBox () {
            goods = new ArrayList<JpaGoods>();
        }
        public UiBox (Box box) {
            BeanUtils.copyProperties(box, this);
            if (goods == null) {
                goods = new ArrayList<JpaGoods>();
            }
        }

        public UiBox (JpaBox box) {
            BeanUtils.copyProperties(box, this);
            goods = new ArrayList<JpaGoods>();
        }

        public void addGood(JpaGoods good) {
            //cut connection from JpaGoods to Box to avoid loop in serialization
            good.setBox(null);
            goods.add(good);
        }

        public List<JpaGoods> getGoods() {
            return goods;
        }

        public void setGoods(List<JpaGoods> goods) {
            this.goods = goods;
        }

        public List<JpaGoods> fetchSortedGoods() {
            List<JpaGoods> list = null;
            if (goods != null)
                list = new ArrayList<JpaGoods>(goods);
            else
                list = new ArrayList<JpaGoods>();

            Collections.sort(list, new Comparator<JpaGoods>() {
                @Override
                public int compare(JpaGoods o1, JpaGoods o2) {
                    return (int) (o1.getInboxPosition() - o2.getInboxPosition());
                }
            });
            return list;
        }

        public String getRemainStr () {
            return DateUtil.toShortStr(this.getRemain());
        }
    }
}
