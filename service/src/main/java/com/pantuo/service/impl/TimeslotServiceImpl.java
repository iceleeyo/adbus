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
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.pantuo.util.ExcelFastUtil;

import sun.util.logging.resources.logging;

@Service
public class TimeslotServiceImpl implements TimeslotService {
	private static Logger log = LoggerFactory.getLogger(BusServiceImpl.class);
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

	// @Override
	public Page<JpaTimeslot> getAllTimeslots(int city, String name, int page, int pageSize, Sort sort,
			boolean fetchDisabled) {
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

	// @Override
	public JpaTimeslot findById(int id) {
		return timeslotRepo.findOne(id);
	}

	// @Override
	public void saveTimeslot(JpaTimeslot timeslot) {
		timeslotRepo.save(timeslot);
	}

	// @Override
	public void saveTimeslots(Iterable<JpaTimeslot> timeslots) {
		timeslotRepo.save(timeslots);
	}

	// @Override
	public List<JpaInfoImgSchedule> getInfoSchedule(int city, TableRequest req, Principal principal, String mtype)
			throws ParseException {
		String dayStr = req.getFilter("day");
		Date day = DateUtil.longDf.get().parse(dayStr);
		Date from = DateUtil.trimDate(day);
		Date to = DateUtils.addDays(from, 1);
		BooleanExpression query = QJpaInfoImgSchedule.jpaInfoImgSchedule.date.before(to)
				.and(QJpaInfoImgSchedule.jpaInfoImgSchedule.date.stringValue()
						.goe(StringOperation.create(Ops.STRING_CAST, ConstantImpl.create(from))));
		query = query.and(QJpaInfoImgSchedule.jpaInfoImgSchedule.type.eq(JpaInfoImgSchedule.Type.valueOf(mtype)));
		return (List<JpaInfoImgSchedule>) infoImgScheduleRepository.findAll(query);
	}

	@Override
	public List<JpaInfoImgSchedule> orderSchedule(TableRequest req, Principal principal, String mtype) {
		int orderId = req.getFilterInt("orderId", 0);
		BooleanExpression query = QJpaInfoImgSchedule.jpaInfoImgSchedule.order.id.eq(orderId);
		query = query.and(QJpaInfoImgSchedule.jpaInfoImgSchedule.type.eq(JpaInfoImgSchedule.Type.valueOf(mtype)));
		return (List<JpaInfoImgSchedule>) infoImgScheduleRepository.findAll(query);
	}

	@Value("${publish.path}")
	private String rootPath;

	@Override
	public String exportOrderSchedule(TableRequest req, Principal principal, String mtype) {
		List<JpaInfoImgSchedule> list = orderSchedule(req, principal, mtype);
		String excelPath = "";
		StringBuilder headTitles = new StringBuilder();
		if (StringUtils.equals("info", mtype)) {
			headTitles.append("日期,");
			headTitles.append("审批合格号,");
			headTitles.append("字幕服务信息内容,");
			headTitles.append("状态");

			try {
				String fileName = "字幕Info排期表_";
				ExcelFastUtil exu = new ExcelFastUtil(headTitles.toString(), fileName);
				exu.createHead();
				HSSFCellStyle cellStyle = exu.getDateCellType();
				for (JpaInfoImgSchedule one : list) {
					boolean supoliesNull = null == one.getOrder().getSupplies();
					int cellNum = 0;
					exu.createRow();
					exu.setDateCell(one.getDate(), cellNum++, cellStyle);
					exu.setDynamicCell(supoliesNull ? "" : one.getOrder().getSupplies().getSeqNumber(), cellNum++);
					exu.setCell(supoliesNull ? "" : one.getOrder().getSupplies().getInfoContext(), cellNum++);
					exu.setDynamicCell(one.isDeleted() ? "已失效" : "正常", cellNum++);
				}
				excelPath = exu.exportFileByInation(fileName, rootPath);
			} catch (Exception e) {
				log.info("export ex:{}", e);
			}
		} else {

			headTitles.append("日期,");
			headTitles.append("审批合格号,");
			headTitles.append("图片包名称,");
			headTitles.append("性质,");
			headTitles.append("图片名称,");
			headTitles.append("时长(秒),");
			headTitles.append("状态");

			try {
				String fileName = "图片Info排期表_";
				ExcelFastUtil exu = new ExcelFastUtil(headTitles.toString(), fileName);
				exu.createHead();
				HSSFCellStyle cellStyle = exu.getDateCellType();
				for (JpaInfoImgSchedule one : list) {
					boolean supoliesNull = null == one.getOrder().getSupplies();
					boolean attamentNumm = null == one.getAttachment();
					int cellNum = 0;
					exu.createRow();
					exu.setDateCell(one.getDate(), cellNum++, cellStyle);
					exu.setDynamicCell(supoliesNull ? "" : one.getOrder().getSupplies().getSeqNumber(), cellNum++);
					exu.setDynamicCell(supoliesNull ? "" : one.getOrder().getSupplies().getName(), cellNum++);
					exu.setCell("新", cellNum++);
					exu.setDynamicCell(attamentNumm ? "" : one.getAttachment().getName(), cellNum++);
					exu.setDynamicCell(one.getDuration(), cellNum++);
					exu.setDynamicCell(one.isDeleted() ? "已失效" : "正常", cellNum++);
				}
				excelPath = exu.exportFileByInation(fileName, rootPath);
			} catch (Exception e) {
				log.info("export ex:{}", e);
			}

		}
		return excelPath;
	}

}
