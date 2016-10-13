package com.pantuo.service;

import java.io.OutputStream;
import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.pantuo.dao.ScheduleChangeLogRepository;
import com.pantuo.dao.ScheduleLogRepository;
import com.pantuo.dao.TimeslotRepository;
import com.pantuo.dao.pojo.JpaBox;
import com.pantuo.dao.pojo.JpaGoods;
import com.pantuo.dao.pojo.JpaGoodsBlack;
import com.pantuo.dao.pojo.JpaInfoImgSchedule;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaScheduleChangeLog;
import com.pantuo.dao.pojo.JpaTimeslot;
import com.pantuo.dao.pojo.QJpaBox;
import com.pantuo.dao.pojo.QJpaGoods;
import com.pantuo.dao.pojo.QJpaGoodsBlack;
import com.pantuo.dao.pojo.QJpaScheduleChangeLog;
import com.pantuo.dao.pojo.ScheduleLog;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.Attachment;
import com.pantuo.mybatis.domain.Box;
import com.pantuo.mybatis.domain.BoxExample;
import com.pantuo.mybatis.domain.Goods;
import com.pantuo.mybatis.domain.GoodsExample;
import com.pantuo.mybatis.domain.Infoimgschedule;
import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.mybatis.persistence.BoxMapper;
import com.pantuo.mybatis.persistence.GoodsMapper;
import com.pantuo.mybatis.persistence.GoodsSortMapper;
import com.pantuo.mybatis.persistence.InfoimgscheduleMapper;
import com.pantuo.mybatis.persistence.UserAutoCompleteMapper;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.SlotBoxBar;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.MailTask.Type;
import com.pantuo.service.security.Request;
import com.pantuo.simulate.MailJob;
import com.pantuo.util.BeanUtils;
import com.pantuo.util.DateUtil;
import com.pantuo.util.Pair;
import com.pantuo.util.Schedule;
import com.pantuo.vo.MediaInventory;
import com.pantuo.vo.ScheduleView;
import com.pantuo.vo.SortRequest;
import com.pantuo.web.schedule.SchedUltResult;
import com.pantuo.web.schedule.ScheduleProgressListener;
import com.pantuo.web.view.SolitSortView;

/**
 * @author tliu
 * 排期Service
 */
@Service
public class ScheduleService {
	private static Logger log = LoggerFactory.getLogger(ScheduleService.class);

	@Autowired
	ScheduleChangeLogRepository scheduleChangeLogRepository;
	
	@Autowired
	@Lazy
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
	private GoodsMapper goodsMapper;

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
	@Autowired
	@Lazy
	ScheduleAlgorithm scheduleAlgorithm;

	@Autowired
	@Lazy
	ScheduleFristAlgorithm scheduleFristAlgorithm;

	public static enum ScheduleType {
		ALLNORMAL, HASFRIST;
	}

	static Map<Date, AtomicInteger> EMPTY_MAP = Collections.emptyMap();
	private static ReadWriteLock RW_LOCK = new ReentrantReadWriteLock();
	//date -box list
	public static Map<Date, List<Box>> BOXDAYMAP = new HashMap<Date, List<Box>>(365 * 5 * 2);
	//all box 
	public static Map<Integer, Box> ALLBOX = new HashMap<Integer, Box>(107 * 365 * 5 * 2);
	//  soletid#day,boxid
	public static Map<String, Integer> BOX_HELP = new HashMap<String, Integer>(107 * 365 * 5 * 2);
	public static Map<Integer, Integer/*slotid,peak*/> PEAK = new HashMap<Integer, Integer>();

	public static int DEFAULT_ROLE = 100000;

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
				log.info(":::[R1]Found {} db scheduled boxes from {} to {}, order {}", boxList.size(), start, end, orderId);

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
	public List<Box> getBoxes(Date from, int days, Date end) {
		from = DateUtil.trimDate(from);
		Date to = DateUtils.addDays(from, days);
		to = end;
		to = DateUtils.addDays(to, 1);
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
		Predicate query = QJpaBox.jpaBox.day.before(to).and(QJpaBox.jpaBox.day.stringValue().goe(StringOperation.create(Ops.STRING_CAST, ConstantImpl.create(from))));

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
				QJpaGoodsBlack.jpaGoodsBlack.day.stringValue().goe(StringOperation.create(Ops.STRING_CAST, ConstantImpl.create(from))));
		query = query.and(QJpaGoodsBlack.jpaGoodsBlack.slotId.eq(soltid));
		return goodsBlackRepository.findAll(query);
	}

	public Iterable<JpaGoods> getGoodsBySoletId(Date from, int soltid, int days) {
		from = DateUtil.trimDate(from);
		Date to = DateUtils.addDays(from, days);
		BooleanExpression query = QJpaGoods.jpaGoods.box.day.before(to).and(
				QJpaGoods.jpaGoods.box.day.stringValue().goe(StringOperation.create(Ops.STRING_CAST, ConstantImpl.create(from))));
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
				QJpaGoodsBlack.jpaGoodsBlack.day.stringValue().goe(StringOperation.create(Ops.STRING_CAST, ConstantImpl.create(from))));
		return goodsBlackRepository.findAll(query);
	}

	@Autowired
	private SuppliesService suppliesService;
	@Autowired
	private GoodsBlackRepository goodsBlackRepository;

	public SchedUltResult scheduleInfoImg(JpaOrders order, String taskid, String startdate1) {
		Date d = order.getStartTime();
		if (StringUtils.isNotBlank(startdate1)) {
			try {
				d = DateUtil.longDf.get().parse(startdate1);
				order.setStartTime(d);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Date start = order.getStartTime();
		int days = order.getProduct().getDays();
		Calendar cal = DateUtil.newCalendar();
		cal.setTime(start);
		if (order == null || order.getId() == 0) {
			return new SchedUltResult("不存在该订单", false, new Date(), false);
		}
		if (days == 0) {
			return new SchedUltResult("刊期不对", false, new Date(), false);
		}
		for (int i = 0; i < days; i++) {
			Date day = cal.getTime();
			saveInfoImg(day, order);
			cal.add(Calendar.DATE, 1);
		}
		Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
		if (task == null) {
			return new SchedUltResult("不存在该任务节点", false, new Date(), false);
		}
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("scheduleResult", true);
		MailTask mailTask = new MailTask(order.getUserId(), order.getId(), null, task.getTaskDefinitionKey(), Type.sendCompleteMail);
		taskService.complete(task.getId(), variables);
		mailJob.putMailTask(mailTask);
		Date end = DateUtil.dateAdd(order.getStartTime(), order.getProduct().getDays());
		order.setEndTime(end);
		ordersRepository.save(order);
		return new SchedUltResult(StringUtils.EMPTY, true, start, true);
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

	public class ScheduleContent {
		public List<JpaGoods> gs;
		public Map<Integer, Box> boxEx;
		public JpaOrders order;
		public Map<Date, List<Box>> boxMap;
		public int numberPlayer;
		public ScheduleProgressListener listener;
		public ScheduleType type;
		public boolean peak = false;//是否是高峰

		public ScheduleContent(List<JpaGoods> gs, Map<Integer, Box> boxEx, JpaOrders order, Map<Date, List<Box>> boxMap, int numberPlayer, ScheduleProgressListener listener,
				ScheduleType type) {
			this.gs = gs;
			this.boxEx = boxEx;
			this.order = order;
			this.boxMap = boxMap;
			this.numberPlayer = numberPlayer;
			this.listener = listener;
			this.type = type;
			this.needSchedule = Collections.emptyMap();
		}

		public Map<Date, Map<Integer, AtomicInteger>> boxScheduleCount = new HashMap<Date, Map<Integer, AtomicInteger>>();;

		Map<Date, AtomicInteger> needSchedule;

		public Map<Date, AtomicInteger> getNeedSchedule() {
			return needSchedule;
		}

		public void setNeedSchedule(Map<Date, AtomicInteger> needSchedule) {
			this.needSchedule = needSchedule;
		}

		public void debugMap() {
			if (needSchedule != null) {
				for (Map.Entry<Date, AtomicInteger> entry : needSchedule.entrySet()) {
					log.info(entry.getKey() + " = " + entry.getValue().get());
				}
			}

		}

	}

	public String initAllBoxMemory() {
		int fetchsize = 250;//一次查*条记录
		int beginIndex = 0;
		int count = 0;
		long t = System.currentTimeMillis();
		Map<Date, List<Box>> tempBoxMap = new HashMap<Date, List<Box>>(365 * 5 * 2);
		Map<Integer, Box> alBox = new HashMap<Integer, Box>(107 * 365 * 5 * 2);

		Map<String, Integer> boxHelper = new HashMap<String, Integer>(107 * 365 * 5 * 2);
		while (true) {
			BoxExample busExample = new BoxExample();
			busExample.createCriteria().andIdGreaterThan(beginIndex);
			busExample.setOrderByClause("id asc");
			busExample.setLimitStart(0);
			busExample.setLimitEnd(fetchsize);
			List<Box> boxList = boxMapper.selectByExample(busExample);
			for (Box box : boxList) {
				count++;
				beginIndex = box.getId();
				boxHelper.put(box.getSlotId() + "#" + box.getDay().getTime(), box.getId());
			}
			putMemory(boxList, tempBoxMap, alBox);
			if (boxList.size() < fetchsize) {
				break;
			}
		}
		long w1 = System.currentTimeMillis();
		try {
			RW_LOCK.writeLock().lock();
			BOXDAYMAP.putAll(tempBoxMap);
			ALLBOX.putAll(alBox);
			BOX_HELP.putAll(boxHelper);
		} finally {
			RW_LOCK.writeLock().unlock();
		}
		tempBoxMap.clear();
		alBox.clear();
		boxHelper.clear();
		log.info("#*****# update AllMemory record:{} time:{} ms", count, System.currentTimeMillis() - w1);
		log.info("#initBaseBox - Load {} mybatis box data  from Db :{} ms", count, System.currentTimeMillis() - t);

		long t2 = System.currentTimeMillis();
		int city = 1;
		Page<JpaTimeslot> slots = timeslotService.getAllTimeslots(city, null, 0, 9999, null, false);
		/**
		 * init peak
		 */
		for (JpaTimeslot jpaTimeslot : slots) {
			PEAK.put(jpaTimeslot.getId(), jpaTimeslot.isPeak() ? 1 : 0);
		}
		log.info("#Load timesolt data from Db record:{} time:{} ms", PEAK.size(), System.currentTimeMillis() - t2);

		return String.valueOf(count);
	}

	private void putMemory(List<Box> boxList, Map<Date, List<Box>> tempBoxMap, Map<Integer, Box> alBox) {
		for (Box b : boxList) {
			List<Box> l = tempBoxMap.get(b.getDay());
			if (l == null) {
				tempBoxMap.put(b.getDay(), l = new ArrayList<Box>());
			}
			l.add(b);
			alBox.put(b.getId(), b);
		}
	}

	/**
	 * 
	 * 检查内存中是存都缓存了所有box
	 *
	 * @param order
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public boolean checkDbBoxState2(JpaOrders order) {
		boolean r = true;
		Calendar cal = DateUtil.newCalendar();
		Date start = order.getStartTime();
		cal.setTime(start);
		int days = order.getProduct().getDays();
		for (int i = 0; i < days; i++) {
			Date day = cal.getTime();
			if (!BOXDAYMAP.containsKey(day)) {
				r = false;
				break;
			}
			cal.add(Calendar.DATE, 1);
		}
		return r;
	}

	/**
	 * 
	 * 检查要排的日期在内存中是否存在 ,不存在需要初始化db到内存
	 *
	 * @param order
	 * @param isOnlyCheck
	 * @param listener
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public void checkDbBoxState(JpaOrders order, boolean isOnlyCheck, ScheduleProgressListener listener) {
		boolean r = checkDbBoxState2(order);
		if (r) {
			log.info("#order all need box is in memory!");
			return;
		} else {
			log.info("#order need load box from db !");
		}

		Date start = order.getStartTime();
		int days = order.getProduct().getDays();
		Date end = DateUtil.dateAdd(start, days);
		int city = order.getCity();
		Calendar cal = DateUtil.newCalendar();
		List<JpaBox> boxList = null;
		if (listener != null) {
			listener.update("正在检查库存信息.");
		}
		long t1 = System.currentTimeMillis();
		boxList = boxRepo.findByCityAndDayGreaterThanEqualAndDayLessThan(city, start, end);
		log.info("#Load data from Db :{} ms", System.currentTimeMillis() - t1);
		//long m1 = System.currentTimeMillis();
		//log.info(Memory.humanReadableByteCount(MemoryMeasurer.measureBytes(boxList), false));
		//log.info("#MemoryMeasurer.measureBytes :{} ms", System.currentTimeMillis() - m1);

		long t2 = System.currentTimeMillis();
		Page<JpaTimeslot> slots = timeslotService.getAllTimeslots(city, null, 0, 9999, null, false);

		/**
		 * init peak
		 */
		for (JpaTimeslot jpaTimeslot : slots) {
			PEAK.put(jpaTimeslot.getId(), jpaTimeslot.isPeak() ? 1 : 0);
		}

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
		for (int i = 0; i < days; i++) {
			Date day = cal.getTime();
			if (!boxDayMap.containsKey(day)) {
				List<JpaBox> boxesForDay = new ArrayList<>(slots.getContent().size());
				for (JpaTimeslot slot : slots) {
					JpaBox box = Schedule.boxFromTimeslot(city, day, slot);
					box.setFsort(1000);
					box.setSort(1000);
					boxesForDay.add(box);
				}

				boxRepo.save(boxesForDay);
				boxDayMap.put(day, boxesForDay);
			}
			cal.add(Calendar.DATE, 1);
		}
		log.info("#Load data to memory :{} ms", System.currentTimeMillis() - t2);

		initAllBoxMemory();
	}

	/**
	 * 
	 * lock boxlist 
	 *
	 * @param day
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public List<Box> getBoxListByDate(Date day) {
		return BOXDAYMAP.get(day);
	}

	public Box getBox(int id) {
		Box box = null;
		try {
			RW_LOCK.readLock().lock();
			box = ALLBOX.get(id);
		} finally {
			RW_LOCK.readLock().unlock();
		}
		return box;
	}

	public SchedUltResult schedule2(JpaOrders order, boolean isOnlyCheck, boolean peak, ScheduleProgressListener listener) {
		//检查内存
		checkDbBoxState(order, isOnlyCheck, listener);

		Calendar cal = DateUtil.newCalendar();
		Date start = order.getStartTime();
		int days = order.getProduct().getDays();
		cal.setTime(start);
		//得到订单要排期的内部段内 每天的box 列表
		Map<Date, List<Box>> tempMap = new HashMap<Date, List<Box>>();
		for (int i = 0; i < days; i++) {
			Date day = cal.getTime();
			List<Box> memoryBoxList = getBoxListByDate(day);
			tempMap.put(day, copyBoxList(memoryBoxList, peak));
			cal.add(Calendar.DATE, 1);
		}
		//----------

		SchedUltResult isAllAllow = new SchedUltResult(StringUtils.EMPTY, false, null, false);
		List<JpaGoods> gs = new ArrayList<JpaGoods>();
		Map<Integer, Box> boxEx = new HashMap<Integer, Box>();

		listener.update("开始根据订单信息准备排期.");
		if (order.getProduct().getFirstNumber() > 0) {
			//listener.update("发现有首播排期需要.");
			//如果有首播排首播
			int playNum = order.getProduct().getFirstNumber();

			ScheduleContent command1 = new ScheduleContent(gs, boxEx, order, tempMap, playNum, listener, ScheduleType.HASFRIST);
			isAllAllow = scheduleFristAlgorithm.scheduleFrist(command1);// scheduleFirst(gs, boxEx, order, playNum, tempMap, EMPTY_MAP);
			//listener.update("订单首播排期结束!");
			//listener.update("开始常规时间段排期.");
			if (isAllAllow.isScheduled) {
				//首播排完了排非首播
				playNum = order.getProduct().getPlayNumber() - order.getProduct().getFirstNumber();
				if (playNum > 0) {
					ScheduleContent command = new ScheduleContent(gs, boxEx, order, tempMap, playNum, listener, ScheduleType.HASFRIST);
					command.boxScheduleCount = command1.boxScheduleCount;
					isAllAllow = scheduleAlgorithm.scheduleNormal(command);
				}
			}
		} else {
			int playNum = order.getProduct().getPlayNumber();
			//listener.update("开始常规时间段排期.");
			//排非首播
			ScheduleContent command = new ScheduleContent(gs, boxEx, order, tempMap, playNum, listener, ScheduleType.ALLNORMAL);
			//isAllAllow = scheduleNormal(command);
			isAllAllow = scheduleAlgorithm.scheduleNormal(command);
			if (!isAllAllow.isScheduled) {
				isAllAllow = scheduleFristAlgorithm.scheduleFrist(command);
			}
		}
		int updateCount = 0;
		if (!isOnlyCheck && isAllAllow.isScheduled) {
			listener.update("系统开始保存排期结果.");
			long t3 = System.currentTimeMillis();
			for (JpaGoods jpaGoods : gs) {
				jpaGoods.setDeleted(false);
			}
			goodsRepo.save(gs);
			//boxRepo.save(boxEx.values());//原来的保存无用了
			for (Box boxUpdate : boxEx.values()) {
				boxMapper.updateByPrimaryKey(boxUpdate);
				updateCount++;
				if (updateCount % 100 == 0) {
					log.info("#Update boxResult to DB :{}", updateCount);
				}
			}
			try {
				RW_LOCK.writeLock().lock();
				ALLBOX.putAll(boxEx);
			} finally {
				RW_LOCK.writeLock().unlock();
			}

			log.info("#Save data to db :{} ms", System.currentTimeMillis() - t3);
			listener.update("保存排期结果结束!");
		}
		if (isOnlyCheck) {
			listener.update("库存检查结束!", ScheduleProgressListener.SCH_TYPE);
		}
		if (isAllAllow.isScheduled) {
			String w = isOnlyCheck ? "库存充足可排期" : "订单排期成功,5秒后将跳转到待办事项!";
			listener.update(w, ScheduleProgressListener.SCH_TYPE);
			isAllAllow.setMsg(w);
			if (!isOnlyCheck) {
				isAllAllow.setScheduleOver(true);
			}
		}

		log.info(isAllAllow.toString());
		return isAllAllow;

	}

	//排首播
	private SchedUltResult scheduleFirst(ScheduleContent command) {
		int numberCopy = command.numberPlayer;
		Date start = command.order.getStartTime();
		int days = command.order.getProduct().getDays();

		int duration = (int) command.order.getProduct().getDuration();
		Calendar cal = DateUtil.newCalendar();
		cal.setTime(start);

		boolean isEmpty = command.needSchedule.isEmpty();
		//临时变量 播放数次
		for (int i = 0; i < days; i++) {
			Date day = cal.getTime();
			int k = 0;
			List<Box> list2 = command.boxMap.get(day);

			//----取每天的常规时间排期后还需要 时间 次数需要排

			AtomicInteger r = command.needSchedule.get(day);
			if (!isEmpty) {
				command.numberPlayer = r.get();
			}
			//----
			for (int j = 0; j < command.numberPlayer; j++) {
				Collections.sort(list2, FRIST_SLOT_COMPARATOR);
				Box box = list2.get(0);
				if (30 - box.getFremain() >= duration) {
					JpaGoods goods = new JpaGoods();
					goods.setOrder(command.order);
					goods.setCity(command.order.getCity());
					goods.setCreated(new Date());
					goods.setSize(duration);
					goods.setInboxPosition((int) box.getFremain());

					//-----
					//goods.setBox(box);
					JpaBox storeBox = getJpaBoxFromEntity(command.order, box);
					goods.setBox(storeBox);
					//llll
					//goods.setBox(box);
					command.gs.add(goods);
					command.boxEx.put(box.getId(), box);

					box.setFremain(box.getFremain() + duration);
					box.setFsort(box.getFsort() - duration);
					k++;
					if (!isEmpty) {
						r.decrementAndGet();
					}
				}
			}
			if (isEmpty) {//计算是要求有首播时的库存
				if (k < command.numberPlayer) {
					return new SchedUltResult("实际可上刊次数:" + k + " 订单上刊次数" + command.numberPlayer, false, day, true);
				}
			} else {//计算常规时间段 排期后排首播时的库存情况 
				if (k < command.numberPlayer) {
					return new SchedUltResult("实际可上刊次数:" + (numberCopy - r.get()) + " 订单上刊次数" + numberCopy, false, day, true);
				}

			}
			cal.add(Calendar.DATE, 1);
		}
		return new SchedUltResult(StringUtils.EMPTY, true, start, true);
	}

	public Box copyBox(Box box) {
		Box r = new Box();
		BeanUtils.copyProperties(box, r);
		return r;
	}

	/**
	 * 
	 * 获取最新的box copy
	 *
	 * @param box 内存中一天对应的boxlist 会过期
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public List<Box> copyBoxList(List<Box> box, boolean _peak) {
		List<Box> list = new ArrayList<Box>(box.size());
		for (Box expireBox : box) {
			Box newBox = new Box();
			BeanUtils.copyProperties(getBox(expireBox.getId()), newBox);
			newBox.setTmpAbsoluteWight(DEFAULT_ROLE);//新加权重
			if (_peak) {
				if (PEAK.containsKey(newBox.getSlotId())) {
					int isPeak = PEAK.get(newBox.getSlotId());
					if (isPeak == 1) {
						list.add(newBox);
					}
				}
			} else {
				list.add(newBox);
			}
		}
		return list;
	}

	private JpaBox getJpaBoxFromEntity(JpaOrders order, Box box) {
		//-----------------------------------
		JpaTimeslot slot = new JpaTimeslot();
		slot.setId(box.getSlotId());
		//-------------------------------------
		JpaBox storeBox = new JpaBox();
		storeBox.setCity(order.getCity());
		storeBox.setDay(box.getDay());
		storeBox.setId(box.getId());

		storeBox.setTimeslot(slot);
		return storeBox;
	}

	Comparator<Box> NORMAL_COMPARATOR = new Comparator<Box>() {
		public int compare(Box o1, Box o2) {
			int w = o2.getSort() - o1.getSort();
			if (w == 0) {
				return (int) ((o2.getSize() - o2.getRemain()) - (o1.getSize() - o1.getRemain()));
			} else {
				return w;
			}
		}
	};
	Comparator<Box> FRIST_SLOT_COMPARATOR = new Comparator<Box>() {
		public int compare(Box o1, Box o2) {
			int w = o2.getFsort() - o1.getFsort();
			if (w == 0) {
				return (int) ((30 - o2.getFremain()) - (30 - o1.getFremain()));
			} else {
				return w;
			}
		}
	};

	public SchedUltResult checkForWeb(String start, int productId, int city, HttpServletRequest request, Principal principal) {
		Date startDate;
		SchedUltResult r = null;
		try {
			startDate = DateUtil.longDf.get().parse(start);
			JpaOrders order = new JpaOrders();
			Calendar b = DateUtil.newCalendar();
			b.add(Calendar.DATE, -90);
			Calendar f = DateUtil.newCalendar();
			f.add(Calendar.DATE, 365 * 3);

			if (startDate.before(b.getTime())) {
				r = new SchedUltResult("系统目前支持的排期时间段:" + DateUtil.longDf.get().format(b.getTime()) + " - " + DateUtil.longDf.get().format(f.getTime()), false, null, false);
				return r;
			}
			if (startDate.after(f.getTime())) {
				r = new SchedUltResult("系统目前支持的排期时间段:" + DateUtil.longDf.get().format(b.getTime()) + " - " + DateUtil.longDf.get().format(f.getTime()), false, null, false);
				return r;
			}
			order.setStartTime(startDate);
			JpaProduct product = productService.findById(productId);
			if (product == null) {
				r = new SchedUltResult("商品信息未找到!", false, null, false);
			} else {
				order.setProduct(product);
				order.setCity(city);
				r = schedule2(order, true, false, new ScheduleProgressListener(request.getSession(), principal));
			}
		} catch (ParseException e) {
			log.error("params-error :{}", e);
			r = new SchedUltResult("日期参数错误!", false, null, false);
		}
		return r;
	}

	public SchedUltResult checkInventory(int id, String startdate1, HttpServletRequest request, Principal principal) {

		ScheduleProgressListener listener = new ScheduleProgressListener(request.getSession(), principal, ScheduleProgressListener.Type._checkFeature);

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
		final String noTime = "未来30天均无可排日期";
		SchedUltResult r = new SchedUltResult(noTime, false, null, false);
		for (int i = 0; i < 30; i++) {
			listener.updateInfo("正在检查[" + DateUtil.longDf.get().format(order.getStartTime()) + "]库存情况.");
			r = schedule2(order, true, false, listener);
			if (!r.isScheduled) {
				d = DateUtils.addDays(d, 1);
				order.setStartTime(d);
			} else {
				break;
			}
		}
		listener.updateInfo("检查结束.");
		if (!r.isScheduled) {
			r.setMsg(noTime);
		}
		listener.endResult(r);

		return r;
	}

	@Autowired
	private TimeslotRepository timeslotRepository;
	@Autowired
	private UserAutoCompleteMapper userAutoCompleteMapper;

	public void writeExcel(String filePath, int orderId, HttpServletResponse response) {
		Map<Integer, ScheduleView> map = new LinkedHashMap<Integer, ScheduleView>();
		List<JpaTimeslot> lotlList = timeslotRepository.findAll();
		for (JpaTimeslot jpaTimeslot : lotlList) {
			ScheduleView view = new ScheduleView();
			map.put(jpaTimeslot.getId(), view);
			view.setTimeslot(jpaTimeslot);
		}
		JpaOrders order = orderService.getJpaOrder(orderId);
		if (order != null) {
			String dString = DateUtil.longDf.get().format(order.getStartTime());
			List<MediaInventory> list = userAutoCompleteMapper.getScheduleViewByDateStr(orderId, dString);
			for (MediaInventory mediaInventory : list) {
				ScheduleView scheduleView = map.get(mediaInventory.getSotid());
				String d = DateUtil.longDf.get().format(mediaInventory.getDay());
				scheduleView.getMap().put(d, mediaInventory.getNum());

			}
			Collection<ScheduleView> views = map.values();

			if (null != filePath && !"".equals(filePath.trim())) {

				WritableWorkbook wWorkbook = null;
				OutputStream outputStream = null;

				// 根据不同的excel格式创建workbook  
				if (filePath.trim().toLowerCase().endsWith(".xls")) {

					try {
						response.setHeader("Content-Type", "application/x-xls");
						response.setHeader("Content-Disposition", "attachment; filename=\"order-schedule.xls\"");
						outputStream = response.getOutputStream();//new FileOutputStream(filePath);  
						wWorkbook = Workbook.createWorkbook(outputStream);
						WritableSheet wSheet = wWorkbook.createSheet("订单排期表", 0);
						// 添加string  
						wSheet.addCell(new Label(0, 0, "广告名称"));
						wSheet.addCell(new Label(1, 0, "广告内容编号"));
						wSheet.addCell(new Label(2, 0, "播出周期"));
						wSheet.addCell(new Label(3, 0, "广告时长"));
						wSheet.addCell(new Label(4, 0, "播出频次"));
						String startt = DateUtil.longDf.get().format(order.getStartTime());
						String endt = DateUtil.longDf.get().format(order.getEndTime());
						wSheet.addCell(new Label(0, 1, order.getProduct().getName()));
						wSheet.addCell(new Label(1, 1, order.getSupplies().getSeqNumber()));
						wSheet.addCell(new Label(2, 1, startt + "至" + endt));
						wSheet.addCell(new Label(3, 1, String.valueOf(order.getProduct().getDuration()) + "s"));
						wSheet.addCell(new Label(4, 1, String.valueOf(order.getProduct().getPlayNumber()) + "次/天"));

						int j = 5;
						int k = 3;
						wSheet.addCell(new Label(0, 4, "包名"));
						wSheet.addCell(new Label(1, 4, "时间段"));
						wSheet.addCell(new Label(2, 4, "时长"));
						Calendar cal = DateUtil.newCalendar();
						cal.setTime(order.getStartTime());
						List<String> dList = new ArrayList<String>();
						while (cal.getTime().before(order.getEndTime())) {
							String dSt = DateUtil.longDf.get().format(cal.getTime());
							dList.add(dSt);
							wSheet.addCell(new Label(k, 4, dSt));
							cal.add(Calendar.DATE, 1);
							k++;
						}

						for (ScheduleView scheduleView : views) {
							wSheet.addCell(new Label(0, j, scheduleView.getTimeslot().getName()));
							wSheet.addCell(new Label(1, j, scheduleView.getTimeslot().getStartTimeStr()));
							wSheet.addCell(new Label(2, j, scheduleView.getTimeslot().getDurationStr()));

							int h = 3;
							for (String string : dList) {
								Integer a = scheduleView.getMap().get(string);
								String tempd = StringUtils.EMPTY;
								if (a != null) {
									tempd = a.toString();
								}
								wSheet.addCell(new Label(h, j, tempd));
								h++;
							}
							j++;
						}

						//需要write  
						wWorkbook.write();
						wWorkbook.close();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {

						if (null != outputStream) {
							try {
								outputStream.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					}
				}
			}
		}

	}

	static ReentrantLock lock = new ReentrantLock();
	
	static ReentrantLock CALEL_LOCK = new ReentrantLock();
	public static String currOperatorUser = StringUtils.EMPTY;
	
	 
	public Page<JpaScheduleChangeLog> queryChangeLog(TableRequest req) {
		String orderId = req.getFilter("orderId");
		BooleanExpression query = QJpaScheduleChangeLog.jpaScheduleChangeLog.orderid.eq(NumberUtils.toInt(orderId));
		Sort sort = req.getSort("id");
		Pageable p = new PageRequest(0, 100, sort);
		Page<JpaScheduleChangeLog> page = scheduleChangeLogRepository.findAll(query, p);
		return new DataTablePage<JpaScheduleChangeLog>(page, req.getDraw());
	}
	
	public Pair<Boolean, String> canelScheduleStartDay(boolean isCallAfterDayAll, int orderid,String remark,  String startdate1, Principal principal) {
		Pair<Boolean, String> p = null;
		try {
			CALEL_LOCK.lock();
			p = _canelScheduleStartDay(isCallAfterDayAll, orderid,remark, startdate1, principal);
		} catch (Exception e) {
			log.error("lock error:", e);
		} finally {
			CALEL_LOCK.unlock();
		}
		return p;
	}
	public Pair<Boolean, String> _canelScheduleStartDay(boolean isCallAfterDayAll, int orderid,String remark,  String startdate1, Principal principal) {
		Pair<Boolean, String> p = new Pair<Boolean, String>(false, StringUtils.EMPTY);

		JpaOrders order = orderService.getJpaOrder(orderid);
		if (order == null) {
			p.setRight("订单丢失");
			return p;
		}
		if (StringUtils.isNotBlank(startdate1)) {
			try {
				Date d = DateUtil.longDf.get().parse(startdate1);
				Date end = DateUtil.dateAdd(order.getStartTime(), order.getProduct().getDays());
				if (!(((order.getStartTime().getTime() == d.getTime()) || order.getStartTime().before(d)) && end.after(d))) {
					p.setRight("要取消的日期未在订单的播出范围内!");
					return p;
				}

				List<Goods> goods = findGoodsByOrderAndStartDay(isCallAfterDayAll, orderid, d);
				if (goods.isEmpty()) {
					p.setRight("指定的日期没有要取消的排期,可能排期已经取消.");
					return p;
				}
				for (Goods record : goods) {
					record.setIsDeleted(true);
					String key = record.getBoxSlotId() + "#" + record.getDay().getTime();
					if (BOX_HELP.containsKey(key)) {
						Integer boxTableId = BOX_HELP.get(key);
						if (ALLBOX.containsKey(boxTableId)) {
							Box box = ALLBOX.get(boxTableId);
							if (record.getSortIndex() == 0) {
								//更新首播
								box.setFremain(box.getFremain() - record.getSize().intValue());
							} else {
								//更新不是首播
								box.setRemain(box.getRemain() - record.getSize().intValue());
							}
							boxMapper.updateByPrimaryKey(box);
						} else {
							log.warn("goods:miss box:{}", boxTableId);
						}
					} else {
						log.warn("goods:miss box:{}", record.getId());
					}
					goodsMapper.updateByPrimaryKey(record);
				}
				saveChangeLog(isCallAfterDayAll,orderid,remark, startdate1, principal);
				p.setLeft(true);
				p.setRight("订单取消排期成功!");
				return p;

			} catch (ParseException e) {
				log.error("Parse date error:{}", e);
			}
		}

		return p;
	}
	
	public void saveChangeLog(boolean isCallAfterDayAll, int orderid,String remark, String startdate1, Principal principal){
		JpaScheduleChangeLog log = new JpaScheduleChangeLog();
		UserDetail user = 	Request.getUser(principal);
		if(user!=null){
			String name = user.getUsername()+ "#" + user.getUser()!=null?user.getUser().getFirstName():"";
			log.setUserId(name);
			log.setUpdated(new Date());
			log.setCreated(new Date());
			log.setStartDate(startdate1);
			log.setOrderid(orderid);
			log.setRemark(remark);
			log.setIsCallAfterDayAll(BooleanUtils.toStringYesNo(isCallAfterDayAll));
		}
		scheduleChangeLogRepository.save(log);
	}

	public List<Goods> findGoodsByOrderAndStartDay(boolean isCallAfterDayAll, int orderId, Date startDay) {
		GoodsExample example = new GoodsExample();
		if (isCallAfterDayAll) {
			example.createCriteria().andOrderIdEqualTo(orderId).andDayGreaterThanOrEqualTo(startDay).andIsDeletedEqualTo(false);
		} else {
			example.createCriteria().andOrderIdEqualTo(orderId).andDayEqualTo(startDay).andIsDeletedEqualTo(false);
			;
		}
		return goodsMapper.selectByExample(example);
	}

	public SchedUltResult checkInventory(int id, String taskid, String startdate1, boolean ischeck, boolean peak, HttpServletRequest request, Principal principal) {
		//检查
		if (ischeck) {
			return runCheck(id, taskid, startdate1, ischeck, peak, request, principal);
		}
		//排期 上锁
		SchedUltResult result = null;
		try {
			String u = Request.getUserId(principal);
			boolean isLock = false;
			try {
				isLock = lock.tryLock(3L, TimeUnit.SECONDS);
				if (isLock) {
					log.info("user:{} lock checkInventory", u);
					currOperatorUser = u;
					result = runCheck(id, taskid, startdate1, ischeck, peak, request, principal);
				} else {
					result = new SchedUltResult("用户[" + currOperatorUser + "]正在执行排期,请稍后再试.", false, null, false);
					result.setLock(true);
				}

			} catch (Exception e) {
				log.error("lock error:", e);
			} finally {
				if (isLock) {
					lock.unlock();
				}
			}
		} catch (Exception e) {
			log.error("lock error:", e);
		}
		return result;

	}

	private SchedUltResult runCheck(int id, String taskid, String startdate1, boolean ischeck, boolean peak, HttpServletRequest request, Principal principal) {
		ScheduleProgressListener listener = new ScheduleProgressListener(request.getSession(), principal);

		JpaOrders order = orderService.getJpaOrder(id);
		Date d = order.getStartTime();
		if (StringUtils.isNotBlank(startdate1)) {
			try {
				d = DateUtil.longDf.get().parse(startdate1);
				order.setStartTime(d);
			} catch (ParseException e) {
				log.error("Parse date error:{}", e);
			}
		}
		if (ischeck) {
			SchedUltResult result = schedule2(order, ischeck, peak, listener);
			listener.endResult(result);
			return result;
		} else {
			SchedUltResult r = schedule2(order, ischeck, peak, listener);
			if (r.isScheduled) {
				Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
				if (task == null) {
					r = new SchedUltResult("不存在该任务节点", false, d, true);
				}
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("scheduleResult", true);
				MailTask mailTask = new MailTask(order.getUserId(), id, null, task.getTaskDefinitionKey(), Type.sendCompleteMail);
				taskService.complete(task.getId(), variables);
				mailJob.putMailTask(mailTask);
			}
			Date end = DateUtil.dateAdd(order.getStartTime(), order.getProduct().getDays());
			order.setEndTime(end);
			order.setScheduled(true);
			ordersRepository.save(order);
			listener.endResult(r);
			return r;
		}
	}

}
