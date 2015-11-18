package com.pantuo.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.StringOperation;
import com.pantuo.dao.BoxRepository;
import com.pantuo.dao.GoodsBlackRepository;
import com.pantuo.dao.GoodsRepository;
import com.pantuo.dao.OrdersRepository;
import com.pantuo.dao.ScheduleLogRepository;
import com.pantuo.dao.pojo.JpaBox;
import com.pantuo.dao.pojo.JpaGoods;
import com.pantuo.dao.pojo.JpaGoodsBlack;
import com.pantuo.dao.pojo.JpaInfoImgSchedule;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaTimeslot;
import com.pantuo.dao.pojo.QJpaBox;
import com.pantuo.dao.pojo.QJpaGoods;
import com.pantuo.dao.pojo.QJpaGoodsBlack;
import com.pantuo.dao.pojo.ScheduleLog;
import com.pantuo.mybatis.domain.Attachment;
import com.pantuo.mybatis.domain.Box;
import com.pantuo.mybatis.domain.BoxExample;
import com.pantuo.mybatis.domain.Infoimgschedule;
import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.mybatis.persistence.BoxMapper;
import com.pantuo.mybatis.persistence.GoodsSortMapper;
import com.pantuo.mybatis.persistence.InfoimgscheduleMapper;
import com.pantuo.pojo.SlotBoxBar;
import com.pantuo.service.MailTask.Type;
import com.pantuo.service.ScheduleService.SchedUltResult;
import com.pantuo.simulate.MailJob;
import com.pantuo.util.DateUtil;
import com.pantuo.util.Pair;
import com.pantuo.util.Schedule;
import com.pantuo.vo.SortRequest;
import com.pantuo.web.view.AutoCompleteView;
import com.pantuo.web.view.SolitSortView;

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
	private TaskService taskService;
	@Autowired
	private GoodsRepository goodsRepo;
	@Autowired
	private BoxRepository boxRepo;
	@Autowired
	private BoxMapper boxMapper;
	@Autowired
	private OrdersRepository ordersRepository;
	@Autowired
	private ScheduleLogRepository scheduleLogRepository;
	@Autowired
	private MailJob mailJob;
	@Autowired
	private GoodsSortMapper goodsSortMapper;


	public ScheduleLog schedule(Date day, int orderId) {
		return schedule(day, orderService.getJpaOrder(orderId));
	}

	public ScheduleLog schedule(Date day, JpaOrders order) {
		return schedule(order, day, 1);
	}

	public ScheduleLog schedule(JpaOrders order) {
		if (order == null || order.getId() == 0) {
			log.error("Order {} does not exists or not persisted");
			return new ScheduleLog(order.getCity(), new Date(), 0, ScheduleLog.Status.failed, "Order " + order
					+ " does not exists or not persisted");
		}
		return schedule(order, order.getStartTime(), order.getProduct().getDays());
	}

	//根据每天每个时段的box列表，构建基于slot的行级box
	private List<SlotBoxBar> buildSlotBoxBar(Map<Integer/*slotId*/, List<JpaBox>> boxSlotMap) {
		List<SlotBoxBar> boxes = new ArrayList<SlotBoxBar>();
		Iterator<Map.Entry<Integer, List<JpaBox>>> iter = boxSlotMap.entrySet().iterator();
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
			return new ScheduleLog(city, cal.getTime(), 0, ScheduleLog.Status.failed, "Order " + order
					+ " does not exists or not persisted");
		}
		cal.setTime(start);
		int orderId = order.getId();

		if (days == 0) {
			log.info("Order {} has 0 days", orderId);
			return new ScheduleLog(city, cal.getTime(), orderId, ScheduleLog.Status.scheduled, "Order " + orderId
					+ " has 0 days");
		}

		List<ScheduleLog> slogList = scheduleLogRepository.findByCityAndOrderId(city, orderId);
		if (!slogList.isEmpty()) {
			for (ScheduleLog slog : slogList) {
				if (slog.getStatus() == ScheduleLog.Status.scheduled) {
					log.info("Scheduling for day {} and order {} has already completed", slog.getDay(), orderId);
					return new ScheduleLog(city, slog.getDay(), orderId, ScheduleLog.Status.duplicate,
							"duplicate with day " + slog.getDay());
				} else if (slog.getStatus() == ScheduleLog.Status.scheduling) {
					log.info("Other thread is now scheduling for day {} and order {}, please wait", slog.getDay(),
							orderId);
					return new ScheduleLog(city, slog.getDay(), orderId, ScheduleLog.Status.racing, "racing with day "
							+ slog.getDay());
				}
			}
		}
		Map<Date, ScheduleLog> slogMap = new HashMap<Date, ScheduleLog>();
		for (int i = 0; i < days; i++) {
			ScheduleLog log = new ScheduleLog(city, cal.getTime(), orderId);
			scheduleLogRepository.save(log);
			slogList.add(log);
			slogMap.put(log.getDay(), log);
			cal.add(Calendar.DATE, 1);
		}

		Page<JpaTimeslot> slots = timeslotService.getAllTimeslots(city, null, 0, 9999, null, false);
		List<JpaBox> scheduleResult = new LinkedList<JpaBox>();
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
				Map<Date, List<JpaBox>> boxDayMap = new HashMap<Date, List<JpaBox>>();
				Map<Integer/*slotId*/, List<JpaBox>> boxSlotMap = new HashMap<Integer, List<JpaBox>>();
				for (JpaBox b : boxList) {
					List<JpaBox> l = boxDayMap.get(b.getDay());
					if (l == null) {
						l = new ArrayList<JpaBox>();
						boxDayMap.put(b.getDay(), l);
					}
					l.add(b);
				}
				log.info(":::[R1]Found {} db scheduled boxes from {} to {}, order {}", boxList.size(), start, end,
						orderId);

				log.info(":::[R1]Filling up from timeslots in case db scheduled boxes is empty");
				cal.setTime(start);
				for (int i = 0; i < days; i++) {
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
					log.error("[R1]Can not arrange the schedule, {} entries can not be boxed, will go to [R2]", s
							.getHotNotBoxed().size());
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
			for (int i = 0; i < days; i++) {
				Date now = cal.getTime();

				ScheduleLog slog = null;
				slog = new ScheduleLog(city, cal.getTime(), orderId);//add by impanxh 2015-11-12
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
						success++;
					} else {
						log.error("[R2]Can not arrange the schedule, {} entries can not be boxed", s.getHotNotBoxed()
								.size());
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
						log.setDescription("[Save][" + last.getDay() + " failed]" + last.getDescription());
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
		Predicate query = QJpaBox.jpaBox.day.before(to).and(
				QJpaBox.jpaBox.day.stringValue()
						.goe(StringOperation.create(Ops.STRING_CAST, ConstantImpl.create(from))));

		return boxRepo.findAll(query);
	}

	/**
	 * 供排序查询
	 * @param from inclusive
	 */
	public Iterable<JpaGoodsBlack> getFreeGoodsBySoletId(Date from, int soltid, int days) {
		from = DateUtil.trimDate(from);
		Date to = DateUtils.addDays(from, days);
		BooleanExpression query = QJpaGoodsBlack.jpaGoodsBlack.day.before(to).and(
				QJpaGoodsBlack.jpaGoodsBlack.day.stringValue().goe(
						StringOperation.create(Ops.STRING_CAST, ConstantImpl.create(from))));
		query = query.and(QJpaGoodsBlack.jpaGoodsBlack.slotId.eq(soltid));
		return goodsBlackRepository.findAll(query);
	}

	public Iterable<JpaGoods> getGoodsBySoletId(Date from, int soltid, int days) {
		from = DateUtil.trimDate(from);
		Date to = DateUtils.addDays(from, days);
		BooleanExpression query = QJpaGoods.jpaGoods.box.day.before(to).and(
				QJpaGoods.jpaGoods.box.day.stringValue().goe(
						StringOperation.create(Ops.STRING_CAST, ConstantImpl.create(from))));
		query = query.and(QJpaGoods.jpaGoods.box.timeslot.id.eq(soltid));
		return goodsRepo.findAll(query);
	}

	public List<SolitSortView> querySortView(Date from, int soltid, int days) {

		Iterable<JpaGoodsBlack> black = getFreeGoodsBySoletId(from, soltid, days);

		Iterable<JpaGoods> goods = getGoodsBySoletId(from, soltid, days);
		List<SolitSortView> r = new ArrayList<SolitSortView>();
		Set<Integer> suppliesId = new HashSet<>();
		for (JpaGoods solitSortView : goods) {
			SolitSortView w = new SolitSortView();
			w.setId(solitSortView.getId());
			w.setPosition((int) solitSortView.getInboxPosition());
			w.setSize(solitSortView.getSize());
			w.setSort_index(solitSortView.getSort_index());
			w.setType("N");
			w.setSupplieId(solitSortView.getOrder().getSuppliesId());
			r.add(w);

			suppliesId.add(solitSortView.getOrder().getSuppliesId());
		}
		for (JpaGoodsBlack solitSortView : black) {
			SolitSortView w = new SolitSortView();
			w.setId(solitSortView.getId());
			w.setPosition((int) solitSortView.getInboxPosition());
			w.setSize(solitSortView.getSize());
			w.setSort_index(solitSortView.getSort_index());
			w.setType("B");
			w.setSupplieId(solitSortView.getSuppliesId());
			r.add(w);
			suppliesId.add(solitSortView.getSuppliesId());
		}

		if (!r.isEmpty()) {

			Collections.sort(r, new Comparator<SolitSortView>() {
				public int compare(SolitSortView o1, SolitSortView o2) {
					return (int) (o1.getSort_index() - o2.getSort_index());
				}
			});
			Map<Integer, Supplies> supplieMap = suppliesService.getSuppliesByIds(new ArrayList<>(suppliesId));
			for (SolitSortView view : r) {
				view.setSupplieStr(supplieMap.get(view.getSupplieId()));
			}
		}
		return r;
	}

	public Pair<Boolean, String> sortSolit(String sortString) {

		Pair<Boolean, String> r = new Pair<Boolean, String>(true, StringUtils.EMPTY);
		if (StringUtils.isNoneBlank(sortString)) {
			String[] split = StringUtils.split(sortString, ",");

			List<SortRequest> black = new ArrayList<SortRequest>();
			List<SortRequest> normal = new ArrayList<SortRequest>();
			int i = 1;
			for (String string : split) {
				String[] oneSort = StringUtils.split(string, "_");
				if (StringUtils.equals("B", oneSort[0])) {
					black.add(new SortRequest(i, NumberUtils.toInt(oneSort[1])));

				} else if (StringUtils.equals("N", oneSort[0])) {
					normal.add(new SortRequest(i, NumberUtils.toInt(oneSort[1])));
				}
				i++;
			}

			if (!black.isEmpty()) {
				goodsSortMapper.sortBlackGood(black);
			}
			if (!normal.isEmpty()) {
				goodsSortMapper.sortNormailGood(normal);
			}

		}
		return r;

	}

	/**
	 * 获取1天空的档位
	 * @param from inclusive
	 */
	public Iterable<JpaGoodsBlack> getFreeGoods(Date from, int days) {
		from = DateUtil.trimDate(from);
		Date to = DateUtils.addDays(from, days);
		Predicate query = QJpaGoodsBlack.jpaGoodsBlack.day.before(to).and(
				QJpaGoodsBlack.jpaGoodsBlack.day.stringValue().goe(
						StringOperation.create(Ops.STRING_CAST, ConstantImpl.create(from))));
		return goodsBlackRepository.findAll(query);
	}

	@Autowired
	private SuppliesService suppliesService;
	@Autowired
	private GoodsBlackRepository goodsBlackRepository;

	public boolean scheduleInfoImg(JpaOrders order) {
		Date start = order.getStartTime();
		int days = order.getProduct().getDays();
		Calendar cal = DateUtil.newCalendar();
		cal.setTime(start);
		if (order == null || order.getId() == 0) {
			log.error("Order {} does not exists or not persisted");
			return false;
		}
		if (days == 0) {
			log.info("Order {} has 0 days", order.getId());
			return false;
		}
		for (int i = 0; i < days; i++) {
			Date day = cal.getTime();
			saveInfoImg(day, order);
			cal.add(Calendar.DATE, 1);
		}
		return true;
	}

	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private InfoimgscheduleMapper infoimgscheduleMapper;

	private void saveInfoImg(Date day, JpaOrders order) {
		if (order.getProduct().getType() == JpaProduct.Type.info) {
			Infoimgschedule infoimgschedule = new Infoimgschedule();
			infoimgschedule.setCity(order.getCity());
			infoimgschedule.setDate(day);
			infoimgschedule.setDuration((long) 0);
			infoimgschedule.setOrderId(order.getId());
			infoimgschedule.setType(JpaInfoImgSchedule.Type.info.ordinal());
			infoimgscheduleMapper.insert(infoimgschedule);
		} else {
			List<Attachment> list = attachmentService.queryimg(null, order.getSuppliesId());
			long duration = order.getProduct().getDuration();
			long a = (long) Math.floor((float) duration / list.size() + 0.5d);
			int i = list.size();
			long sum = 0;
			for (Attachment attachment : list) {
				if (attachment != null) {
					Infoimgschedule infoimgschedule = new Infoimgschedule();
					infoimgschedule.setCity(order.getCity());
					infoimgschedule.setDate(day);
					infoimgschedule.setProper("新");
					infoimgschedule.setOrderId(order.getId());
					infoimgschedule.setType(JpaInfoImgSchedule.Type.image.ordinal());
					infoimgschedule.setAttamentId(attachment.getId());
					if (i == 1) {
						infoimgschedule.setDuration(duration - sum);
					} else {
						infoimgschedule.setDuration(a);
					}
					infoimgscheduleMapper.insert(infoimgschedule);
					sum += a;
					i--;
				}
			}
		}

	}
	public class SchedUltResult{
		String msg;
		boolean isScheduled;
		Date notSchedultDay;
		boolean isFrist;
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		public boolean isScheduled() {
			return isScheduled;
		}
		public void setScheduled(boolean isScheduled) {
			this.isScheduled = isScheduled;
		}
		public Date getNotSchedultDay() {
			return notSchedultDay;
		}
		public void setNotSchedultDay(Date notSchedultDay) {
			this.notSchedultDay = notSchedultDay;
		}
		public boolean isFrist() {
			return isFrist;
		}
		public void setFrist(boolean isFrist) {
			this.isFrist = isFrist;
		}
		public SchedUltResult(String msg, boolean isScheduled, Date notSchedultDay, boolean isFrist) {
			this.msg = msg;
			this.isScheduled = isScheduled;
			this.notSchedultDay = notSchedultDay;
			this.isFrist = isFrist;
		}
		@Override
		public String toString() {
			return "SchedUltResult [msg=" + msg + ", isScheduled=" + isScheduled + "]";
		}
		
	}

	public SchedUltResult schedule2(JpaOrders order,boolean isOnlyCheck) {
		Date start = order.getStartTime();
		int days = order.getProduct().getDays();
		Date end = DateUtil.dateAdd(start, days);
		int city = order.getCity();
		Calendar cal = DateUtil.newCalendar();
		List<JpaBox> boxList = null;
		boxList = boxRepo.findByCityAndDayGreaterThanEqualAndDayLessThan(city, start, end);
		Page<JpaTimeslot> slots = timeslotService.getAllTimeslots(city, null, 0, 9999, null, false);
		Map<Date, List<JpaBox>> boxDayMap = new HashMap<Date, List<JpaBox>>();
		for (JpaBox b : boxList) {
			List<JpaBox> l = boxDayMap.get(b.getDay());
			if (l == null) {
				l = new ArrayList<JpaBox>();
				boxDayMap.put(b.getDay(), l);
			}
			l.add(b);
		}
		cal.setTime(start);
		SchedUltResult isAllAllow =new SchedUltResult(StringUtils.EMPTY , false, null, false); 
		List<JpaGoods> gs = new ArrayList<JpaGoods>();
		Map<Integer, JpaBox> boxEx = new HashMap<Integer, JpaBox>();
		for (int i = 0; i < days; i++) {
			Date day = cal.getTime();
			if (!boxDayMap.containsKey(day)) {
				List<JpaBox> boxesForDay = new ArrayList<>(slots.getContent().size());
				for (JpaTimeslot slot : slots) {
					JpaBox box=Schedule.boxFromTimeslot(city, day, slot);
					box.setFsort(1000);
					box.setSort(1000);
					boxesForDay.add(box);
				}
			 
				boxRepo.save(boxesForDay);
				boxDayMap.put(day, boxesForDay);
			}
			cal.add(Calendar.DATE, 1);
		}

		if (order.getProduct().getFirstNumber() > 0) {
			//如果有首播排首播
			int playNum = order.getProduct().getFirstNumber();
			isAllAllow = scheduleFirst(gs, boxEx, order, playNum, start, days, boxDayMap);
			if (isAllAllow.isScheduled) {
				//首播排完了排非首播
				playNum = order.getProduct().getPlayNumber() - order.getProduct().getFirstNumber();
				if (playNum > 0) {
					isAllAllow = scheduleNormal(gs, boxEx, order, playNum, start, days, boxDayMap);
				}
			}
		} else {
			//排非首播
			isAllAllow = scheduleNormal(gs, boxEx, order, order.getProduct().getPlayNumber(), start, days, boxDayMap);
			if (!isAllAllow.isScheduled) {
				isAllAllow = scheduleFirst(gs, boxEx, order, order.getProduct().getPlayNumber(), start, days, boxDayMap);
			}
		}
		if ( !isOnlyCheck && isAllAllow.isScheduled) {
			goodsRepo.save(gs);
			boxRepo.save(boxEx.values());
		}
		log.info(isAllAllow.toString());
		return isAllAllow;

	}

	//排首播
	private SchedUltResult scheduleFirst(List<JpaGoods> gs, Map<Integer, JpaBox> boxEx, JpaOrders order, int numberPlayer,
			Date start, int days, Map<Date, List<JpaBox>> boxDayMap) {
		int duration = (int) order.getProduct().getDuration();
		Calendar cal = DateUtil.newCalendar();
		cal.setTime(start);
		for (int i = 0; i < days; i++) {
			Date day = cal.getTime();
			int k = 0;
			List<JpaBox> list2 = boxDayMap.get(day);
			for (int j = 0; j < numberPlayer; j++) {
				Collections.sort(list2, FRIST_SLOT_COMPARATOR);
				JpaBox box = list2.get(0);
				if (30 - box.getFremain() >= duration) {
					JpaGoods goods = new JpaGoods();
					goods.setOrder(order);
					goods.setCity(order.getCity());
					goods.setCreated(new Date());
					goods.setSize(duration);
					goods.setInboxPosition((int) box.getFremain());
					goods.setBox(box);
					gs.add(goods);
					boxEx.put(box.getId(), box);

					box.setFremain(box.getFremain() + duration);
					box.setFsort(box.getFsort() - duration);
					k++;
				}
			}
			if (k < numberPlayer) {
				return new SchedUltResult("可上刊库存:"+k+" 订单上刊次数"+numberPlayer, false, day, true);
			}
			cal.add(Calendar.DATE, 1);
		}
		return new SchedUltResult(StringUtils.EMPTY, true, start, true);
	}

	//排非首播
	private SchedUltResult scheduleNormal(List<JpaGoods> gs, Map<Integer, JpaBox> boxEx, JpaOrders order, int numberPlayer,
			Date start, int days, Map<Date, List<JpaBox>> boxDayMap) {
		Calendar cal = DateUtil.newCalendar();
		cal.setTime(start);
		int duration = (int) order.getProduct().getDuration();
		for (int i = 0; i < days; i++) {
			Date day = cal.getTime();
			int k = 0;
			List<JpaBox> list2 = boxDayMap.get(day);
			for (int j = 0; j < numberPlayer; j++) {
				Collections.sort(list2, NORMAL_COMPARATOR);
				JpaBox box = list2.get(0);
				if (box.getSize() - box.getRemain() >= duration) {
					JpaGoods goods = new JpaGoods();
					goods.setOrder(order);
					goods.setCity(order.getCity());
					goods.setCreated(new Date());
					goods.setSize(duration);
					goods.setInboxPosition((int) box.getRemain());
					goods.setBox(box);
					gs.add(goods);
					boxEx.put(box.getId(), box);

					box.setRemain(box.getRemain() + duration);
					box.setSort(box.getSort() - duration);
					k++;
				}
			}
			if (k < numberPlayer) {
				return new SchedUltResult("可上刊库存:"+k+" 订单上刊次数"+numberPlayer, false, day, false);
			}
			cal.add(Calendar.DATE, 1);
		}
		return new SchedUltResult(StringUtils.EMPTY, true, start, true);
	}

	Comparator<JpaBox> NORMAL_COMPARATOR = new Comparator<JpaBox>() {
		public int compare(JpaBox o1, JpaBox o2) {
			int w = o2.getSort() - o1.getSort();
			if (w == 0) {
				return (int) ((o2.getSize() - o2.getRemain()) - (o1.getSize() - o1.getRemain()));
			} else {
				return w;
			}
		}
	};
	Comparator<JpaBox> FRIST_SLOT_COMPARATOR = new Comparator<JpaBox>() {
		public int compare(JpaBox o1, JpaBox o2) {
			int w = o2.getFsort() - o1.getFsort();
			if (w == 0) {
				return (int) ((30 - o2.getFremain()) - (30 - o1.getFremain()));
			} else {
				return w;
			}
		}
	};

	
	public SchedUltResult checkInventory(int id, String startdate1) {
		JpaOrders order = orderService.getJpaOrder(id);
		Date d = order.getStartTime();
		if (StringUtils.isNotBlank(startdate1)) {
			try {
				d = DateUtil.longDf.get().parse(startdate1);
				order.setStartTime(d);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		SchedUltResult r = new SchedUltResult("未来30天均无可排日期", false, null, false);
		for (int i = 0; i < 30; i++) {
			r = schedule2(order, true);
			if (!r.isScheduled) {
				d = DateUtils.addDays(d, 1);
				order.setStartTime(d);
			} else {
				break;
			}
		}
		return r;
	}
	
	public SchedUltResult checkInventory(int id, String taskid,String startdate1, boolean ischeck) {
		 JpaOrders order = orderService.getJpaOrder(id);
		 Date d=order.getStartTime();
		 if(StringUtils.isNotBlank(startdate1)){
				try {
					d=DateUtil.longDf.get().parse(startdate1);
					order.setStartTime(d);
				} catch (ParseException e) {
					e.printStackTrace();
				}	 
			 }
		 if(ischeck){
			 return  schedule2(order,ischeck);
		 }else{
			 SchedUltResult r= schedule2(order,ischeck);
			 if(r.isScheduled){
				 Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
				 if(task==null){
					 r=new SchedUltResult("不存在该任务节点", false, d, true);
				 }
				 Map<String, Object> variables = new HashMap<String, Object>();
				 variables.put("scheduleResult", true);
				 MailTask mailTask = new MailTask(order.getUserId(), id, null, task.getTaskDefinitionKey(),
						 Type.sendCompleteMail);
				 taskService.complete(task.getId(), variables);
				 mailJob.putMailTask(mailTask);
			 }
			 Date end = DateUtil.dateAdd(order.getStartTime(), order.getProduct().getDays());
			 order.setEndTime(end);
			 ordersRepository.save(order);
			 return r;
		 }
	}

}
