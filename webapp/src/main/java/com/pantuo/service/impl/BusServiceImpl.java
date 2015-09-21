package com.pantuo.service.impl;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.dao.BusModelRepository;
import com.pantuo.dao.BusOnlineRepository;
import com.pantuo.dao.BusRepository;
import com.pantuo.dao.BusUpdateRepository;
import com.pantuo.dao.BusinessCompanyRepository;
import com.pantuo.dao.BuslineRepository;
import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaBusModel;
import com.pantuo.dao.pojo.JpaBusOnline;
import com.pantuo.dao.pojo.JpaBusUpLog;
import com.pantuo.dao.pojo.JpaBusinessCompany;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.QJpaBus;
import com.pantuo.dao.pojo.QJpaBusModel;
import com.pantuo.dao.pojo.QJpaBusOnline;
import com.pantuo.dao.pojo.QJpaBusUpLog;
import com.pantuo.dao.pojo.QJpaBusinessCompany;
import com.pantuo.dao.pojo.QJpaBusline;
import com.pantuo.mybatis.domain.Bus;
import com.pantuo.mybatis.domain.BusExample;
import com.pantuo.mybatis.domain.BusOnline;
import com.pantuo.mybatis.domain.BusOnlineExample;
import com.pantuo.mybatis.domain.BusUplog;
import com.pantuo.mybatis.domain.CountableBusLine;
import com.pantuo.mybatis.domain.CountableBusModel;
import com.pantuo.mybatis.domain.CountableBusinessCompany;
import com.pantuo.mybatis.domain.PublishLine;
import com.pantuo.mybatis.persistence.BusCustomMapper;
import com.pantuo.mybatis.persistence.BusMapper;
import com.pantuo.mybatis.persistence.BusOnlineMapper;
import com.pantuo.mybatis.persistence.BusUplogMapper;
import com.pantuo.mybatis.persistence.OfflinecontractMapper;
import com.pantuo.mybatis.persistence.PublishLineMapper;
import com.pantuo.mybatis.persistence.UserAutoCompleteMapper;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.BusService;
import com.pantuo.simulate.QueryBusInfo;
import com.pantuo.util.BeanUtils;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.web.view.BusInfo;
import com.pantuo.web.view.BusInfoView;
import com.pantuo.web.view.ContractLineDayInfo;

/**
 * @author tliu
 */
@Service
public class BusServiceImpl implements BusService {
	@Autowired
	BusRepository busRepo;
	@Autowired
	BusOnlineRepository busOnlineRepository;
	@Autowired
	BusUpdateRepository busUpdateRepository;
	@Autowired
	BuslineRepository lineRepo;
	@Autowired
	BusModelRepository modelRepo;

	@Autowired
	BuslineRepository buslineRepository;
	@Autowired
	UserAutoCompleteMapper userAutoCompleteMapper;

	@Autowired
	BusMapper busMapper;
	@Autowired
	BusinessCompanyRepository companyRepo;
	@Autowired
	BusCustomMapper busCustomMapper;
	@Autowired
	BusUplogMapper busUplogMapper;
	@Autowired
	BusOnlineMapper busOnlineMapper;
	@Autowired
	QueryBusInfo queryBusInfo;

	@Autowired
	OfflinecontractMapper offlinecontractMapper;

	@Autowired
	PublishLineMapper publishLineMapper;

	@Override
	public long count() {
		return busRepo.count();
	}

	@Override
	public long countFree(int city, JpaBusline.Level level, JpaBus.Category category, Integer lineId,
			Integer busModelId, Integer companyId) {
		return 0;
	}

	@Override
	public Page<JpaBus> getAllBuses(int city, TableRequest req, int page, int pageSize, Sort sort, boolean fetchDisabled) {
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		if (sort == null)
			sort = new Sort("id");
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = QJpaBus.jpaBus.city.eq(city);
		String plateNumber = req.getFilter("plateNumber"), linename = req.getFilter("linename"), levelStr = req
				.getFilter("levelStr"), category = req.getFilter("category"), lineid = req.getFilter("lineid"), company = req
				.getFilter("company");
		if (StringUtils.isNotBlank(plateNumber)) {
			query = query.and(QJpaBus.jpaBus.plateNumber.like("%" + plateNumber + "%"));
		}
		if (StringUtils.isNotBlank(lineid)) {
			int lineId = NumberUtils.toInt(lineid);
			query = query.and(QJpaBus.jpaBus.line.id.eq(lineId));
		}
		if (StringUtils.isNotBlank(linename)) {
			query = query.and(QJpaBus.jpaBus.line.name.eq(linename));
		}
		if (StringUtils.isNotBlank(category) && !StringUtils.equals(category, "defaultAll")) {
			query = query.and(QJpaBus.jpaBus.category.eq(JpaBus.Category.valueOf(category)));
		}
		if (StringUtils.isNotBlank(levelStr) && !StringUtils.equals(levelStr, "defaultAll")) {
			query = query.and(QJpaBus.jpaBus.line.level.eq(JpaBusline.Level.valueOf(levelStr)));
		}
		if (StringUtils.isNotBlank(company) && !StringUtils.equals(company, "defaultAll")) {
			JpaBusinessCompany c = new JpaBusinessCompany();
			c.setId(NumberUtils.toInt(company));
			query = query.and(QJpaBus.jpaBus.company.eq(c));
		}
		if (!fetchDisabled) {
			BooleanExpression q = QJpaBus.jpaBus.enabled.isTrue();
			if (query == null)
				query = q;
			else
				query = query.and(q);
		}
		return query == null ? busRepo.findAll(p) : busRepo.findAll(query, p);

	}

	@Override
	public JpaBus findById(int id) {
		return busRepo.findOne(id);
	}

	@Override
	public void saveBus(JpaBus bus) {
		busRepo.save(bus);
	}

	@Override
	public void saveBuses(Iterable<JpaBus> buses) {
		busRepo.save(buses);
	}

	@Override
	public Page<JpaBusline> getAllBuslines(int city, JpaBusline.Level level, String name, int page, int pageSize,
			Sort sort) {
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		if (sort == null)
			sort = new Sort("id");
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = QJpaBusline.jpaBusline.city.eq(city);
		if (level != null)
			query = query.and(QJpaBusline.jpaBusline.level.eq(level));
		if (name != null) {
			query = query.and(QJpaBusline.jpaBusline.name.like("%" + name + "%"));
		}

		return lineRepo.findAll(query, p);
	}

	@Override
	public Page<JpaBusModel> getAllBusModels(int city, String name, String manufacturer, int page, int pageSize,
			Sort sort) {
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		if (sort == null)
			sort = new Sort("id");
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = QJpaBusModel.jpaBusModel.city.eq(city);
		if (name != null) {
			query = query.and(QJpaBusModel.jpaBusModel.name.like("%" + name + "%"));
		}
		if (manufacturer != null) {
			query = query.and(QJpaBusModel.jpaBusModel.manufacturer.like("%" + manufacturer + "%"));
		}
		return modelRepo.findAll(query, p);
	}

	@Override
	public Page<JpaBusinessCompany> getAllBusinessCompanies(int city, String name, String contact, int page,
			int pageSize, Sort sort) {
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		if (sort == null)
			sort = new Sort("id");
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = QJpaBusinessCompany.jpaBusinessCompany.city.eq(city);
		if (name != null) {
			query = query.and(QJpaBusinessCompany.jpaBusinessCompany.name.like("%" + name + "%"));
		}
		if (contact != null) {
			query = query.and(QJpaBusinessCompany.jpaBusinessCompany.contact.like("%" + contact + "%"));
		}
		return companyRepo.findAll(query, p);
	}

	@Override
	public List<CountableBusLine> getBuslines(int city, JpaBusline.Level level, JpaBus.Category category,
			Integer lineId, Integer busModelId, Integer companyId) {
		return busCustomMapper.getBuslines(city, (level == null ? null : level.ordinal()), (category == null ? null
				: category.ordinal()), lineId, busModelId, companyId);
	}

	@Override
	public List<CountableBusModel> getBusModels(int city, JpaBusline.Level level, JpaBus.Category category,
			Integer lineId, Integer busModelId, Integer companyId) {
		return busCustomMapper.getBusModels(city, (level == null ? null : level.ordinal()), (category == null ? null
				: category.ordinal()), lineId, busModelId, companyId);
	}

	@Override
	public List<CountableBusinessCompany> getBusinessCompanies(int city, JpaBusline.Level level,
			JpaBus.Category category, Integer lineId, Integer busModelId, Integer companyId) {
		return busCustomMapper.getBusinessCompanies(city, (level == null ? null : level.ordinal()),
				(category == null ? null : category.ordinal()), lineId, busModelId, companyId);
	}

	public Page<BusInfoView> queryBusinfoView(TableRequest req, Page<JpaBus> page) {
		List<JpaBus> list = page.getContent();
		List<BusInfoView> r = new ArrayList<BusInfoView>(list.size());
		for (JpaBus jpaBus : list) {
			BusInfoView view = new BusInfoView();
			view.setJpaBus(jpaBus);
			view.setBusInfo(queryBusInfo.getBusInfo2(jpaBus.getId()));
			r.add(view);
		}
		Pageable p = new PageRequest(req.getPage(), req.getLength(), page.getSort());
		return new org.springframework.data.domain.PageImpl<BusInfoView>(r, p, page.getTotalElements());
	}

	public Page<BusInfoView> queryBusinfoView2(TableRequest req, Page<JpaBusUpLog> page) throws JsonParseException,
			JsonMappingException, IOException {
		List<JpaBusUpLog> list = page.getContent();
		List<BusInfoView> r = new ArrayList<BusInfoView>(list.size());
		ObjectMapper t = new ObjectMapper();
		t.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		t.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);

		Set<Integer> modelIdsIntegers = new HashSet<Integer>();
		Set<Integer> linesIntegers = new HashSet<Integer>();
		Set<Integer> compaynIntegers = new HashSet<Integer>();

		for (JpaBusUpLog log : list) {
			BusInfoView view = new BusInfoView();
			view.setBusUpLog(log);
			Bus jpabus = t.readValue(log.getJsonString(), Bus.class);
			view.setBus(jpabus);
			r.add(view);
			modelIdsIntegers.add(jpabus.getModelId());
			linesIntegers.add(jpabus.getLineId());
			compaynIntegers.add(jpabus.getCompanyId());
		}

		//List<? > cc=modelIdsIntegers;
		Map<Integer, ?> modelMap = null;
		Map<Integer, ?> lineMap = null;
		Map<Integer, ?> companyMap = null;
		if (!modelIdsIntegers.isEmpty()) {
			List<JpaBusModel> w = modelRepo.findAll(modelIdsIntegers);
			modelMap = list2Map(w);
		}
		if (!linesIntegers.isEmpty()) {
			List<JpaBusline> w = buslineRepository.findAll(linesIntegers);
			lineMap = list2Map(w);
		}
		if (!compaynIntegers.isEmpty()) {
			List<JpaBusinessCompany> w = companyRepo.findAll(compaynIntegers);
			companyMap = list2Map(w);
		}

		for (BusInfoView view : r) {
			if (view.getBus() != null) {
				view.setModel(modelMap != null && view.getBus().getModelId() != null ? (JpaBusModel) modelMap.get(view
						.getBus().getModelId()) : null);
				view.setLine(lineMap != null && view.getBus().getLineId() != null ? (JpaBusline) lineMap.get(view
						.getBus().getLineId()) : null);
				view.setCompany(companyMap != null && view.getBus().getCompanyId() != null ? (JpaBusinessCompany) companyMap
						.get(view.getBus().getCompanyId()) : null);

				view.setBusCategory(com.pantuo.dao.pojo.JpaBus.Category.values()[view.getBus().getCategory()]
						.getNameStr());
			}

		}
		Pageable p = new PageRequest(req.getPage(), req.getLength(), page.getSort());
		return new org.springframework.data.domain.PageImpl<BusInfoView>(r, p, page.getTotalElements());
	}

	public Map<Integer, ?> list2Map(List<?> list) {
		Map<Integer, Object> wMap = new HashMap<Integer, Object>();
		for (Object w : list) {
			if (w instanceof JpaBusModel) {
				wMap.put(((JpaBusModel) w).getId(), w);
			} else if (w instanceof JpaBusline) {
				wMap.put(((JpaBusline) w).getId(), w);
			} else if (w instanceof JpaBusinessCompany) {
				wMap.put(((JpaBusinessCompany) w).getId(), w);
			}
		}
		return wMap;
	}

	/*public Map<Integer, JpaBusModel> getTempMap(List<JpaBusModel> list) {
		Map<Integer, JpaBusModel> wMap = new HashMap<Integer, JpaBusModel>();
		for (JpaBusModel w : list) {
				wMap.put(w.getId(), w);
		}
		return wMap;
	}
	public Map<Integer, JpaBusline> getTempMap2(List<JpaBusline> list) {
		Map<Integer, JpaBusline> wMap = new HashMap<Integer, JpaBusline>();
		for (JpaBusline w : list) {
				wMap.put(w.getId(), w);
		}
		return wMap;
	}
	public Map<Integer, JpaBusinessCompany> getTempMap3(List<JpaBusinessCompany> list) {
		Map<Integer, JpaBusinessCompany> wMap = new HashMap<Integer, JpaBusinessCompany>();
		for (JpaBusinessCompany w : list) {
				wMap.put(w.getId(), w);
		}
		return wMap;
	}*/

	public Pair<Boolean, String> checkOnlie(int contractid, int busId, int publish_line_id, Date startDate, Date endDate) {
		Pair<Boolean, String> r = null;
		BusOnlineExample example = new BusOnlineExample();
		example.createCriteria().andBusidEqualTo(busId).andPublishLineIdEqualTo(publish_line_id)
				.andContractidEqualTo(contractid).andEnableEqualTo(true);
		List<BusOnline> list = busOnlineMapper.selectByExample(example);
		if (list.size() > 0) {
			JpaBus bus = findById(busId);
			r = new Pair<Boolean, String>(false, bus == null ? " 数据异常#车辆未找到 编号:" + bus : "自编号为 "
					+ bus.getSerialNumber() + " 已经上刊了!<br>如需要调整请进行[车辆上刊错误处理]!");
		}
		//check date
		BusOnlineExample dateCheckExample = new BusOnlineExample();
		dateCheckExample.createCriteria().andBusidEqualTo(busId).andEnableEqualTo(true);
		;
		List<BusOnline> onlineList = busOnlineMapper.selectByExample(dateCheckExample);
		for (BusOnline busOnline : onlineList) {
			boolean isDup = false;//上刊重复标记

			//start<=date && date<=end
			if ((busOnline.getStartDate().before(startDate) || busOnline.getStartDate().equals(startDate))
					&& startDate.before(busOnline.getEndDate())) {
				isDup = true;
			}
			//start<=endDate && endDate<=end
			if (busOnline.getStartDate().before(endDate) && endDate.before(busOnline.getEndDate())) {
				isDup = true;
			}
			//startDate<=start && endDate>=endDate
			if (startDate.before(busOnline.getStartDate()) && endDate.after(busOnline.getEndDate())) {
				isDup = true;
			}
			if (isDup) {
				JpaBus bus = findById(busId);
				r = new Pair<Boolean, String>(false, bus == null ? "数据异常# 车辆未找到 编号:" + bus : "上刊冲突<br>[自编号为 "
						+ bus.getSerialNumber() + " 上刊时段内已有其他合同上刊!]");
				break;
			}
		}
		return r;

	}

	@Override
	public Pair<Boolean, String> batchOnline(String ids, String stday, int days, int contractid, Principal principal,
			int city, int plid) throws ParseException {
		Date startDate = (Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(stday);
		Date endDate = com.pantuo.util.DateUtil.dateAdd(startDate, days);
		String idsa[] = ids.split(",");
		List<Integer> ids2 = new ArrayList<Integer>();
		for (int i = 0; i < idsa.length; i++) {
			if (!idsa[i].trim().equals("")) {
				ids2.add(Integer.parseInt(idsa[i]));
			}
		}
		for (Integer busid : ids2) {
			//上刊判断 
			Pair<Boolean, String> r = checkOnlie(contractid, busid, plid, startDate, endDate);
			if (r != null) {
				return r;
			}
		}

		for (Integer busid : ids2) {
			BusOnline busOnline = new BusOnline();
			busOnline.setDays(days);
			busOnline.setStartDate(startDate);
			busOnline.setCreated(new Date());
			busOnline.setUpdated(new Date());
			busOnline.setContractid(contractid);
			busOnline.setEnable(true);
			busOnline.setUserid(Request.getUserId(principal));
			busOnline.setEndDate(endDate);
			busOnline.setBusid(busid);
			busOnline.setCity(city);
			busOnline.setPublishLineId(plid);
			busOnlineMapper.insert(busOnline);
			queryBusInfo.updateBusContractCache(busid);
			ascRemainNumber(plid, true);

		}
		return new Pair<Boolean, String>(true, "上刊成功");
	}

	public void ascRemainNumber(int id, boolean isAsc) {
		PublishLine act = publishLineMapper.selectByPrimaryKey(id);
		if (act != null) {
			PublishLine record = new PublishLine();
			record.setId(act.getId());
			if (isAsc) {
				record.setRemainNuber(act.getRemainNuber() + 1);
			} else {
				int c = act.getRemainNuber() - 1;
				if (c < 0) {
					c = 0;
				}
				record.setRemainNuber(c);
			}
			publishLineMapper.updateByPrimaryKeySelective(record);
		}
	}

	@Override
	public Page<JpaBusOnline> getbusOnlinehistory(int cityId, TableRequest req, int page, int length, Sort sort) {
		if (page < 0)
			page = 0;
		if (length < 1)
			length = 1;
		if (sort == null)
			sort = new Sort("id");
		Pageable p = new PageRequest(page, length, sort);
		BooleanExpression query = QJpaBusOnline.jpaBusOnline.city.eq(cityId);
		String busid = req.getFilter("busid");
		if (StringUtils.isNotBlank(busid)) {
			int busId = NumberUtils.toInt(busid);
			query = query.and(QJpaBusOnline.jpaBusOnline.jpabus.id.eq(busId));
		}
		return query == null ? busOnlineRepository.findAll(p) : busOnlineRepository.findAll(query, p);
	}

	@Override
	public BusOnline offlineBusContract(int cityId, int id, int publishLineId, Principal principal) {
		BusOnline record = busOnlineMapper.selectByPrimaryKey(id);
		if (record != null && cityId == record.getCity()) {
			record.setEnable(false);
			record.setUpdated(new Date());
			record.setEditor(Request.getUserId(principal));
			busOnlineMapper.updateByPrimaryKey(record);
			if (publishLineId == 0) {
				publishLineId = record.getPublishLineId();
			}
			ascRemainNumber(publishLineId, false);
			queryBusInfo.updateBusContractCache(record.getBusid());
		}
		return record;
	}

	public List<JpaBusinessCompany> getAllCompany(int city) {
		Page<JpaBusinessCompany> page = getAllBusinessCompanies(city, null, null, 0, 50, new Sort("id"));
		return page.getContent();
	}

	@Override
	public Pair<Boolean, String> saveBus(Bus bus, int cityId, Principal principal) throws JsonGenerationException,
			JsonMappingException, IOException {
		Bus bus2 = busMapper.selectByPrimaryKey(bus.getId());
		if (bus2 == null) {
			return new Pair<Boolean, String>(false, "信息丢失");
		}
		ObjectMapper t = new ObjectMapper();
		t.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		t.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
		String jsonString = t.writeValueAsString(bus2);
		BeanUtils.copyProperties(bus, bus2);
		bus2.setUpdated(new Date());
		int a = busMapper.updateByPrimaryKey(bus2);
		if (a > 0) {
			BusUplog log = new BusUplog();
			log.setCreated(new Date());
			log.setUpdated(new Date());
			log.setCity(cityId);
			log.setUpdator(Request.getUserId(principal));
			log.setBusid(bus.getId());
			log.setJsonString(jsonString);
			busUplogMapper.insert(log);
		}
		return new Pair<Boolean, String>(true, "修改成功");
	}

	@Override
	public Page<JpaBusUpLog> getbusUphistory(int cityId, TableRequest req, int page, int length, Sort sort) {
		if (page < 0)
			page = 0;
		if (length < 1)
			length = 1;
		if (sort == null)
			sort = new Sort("id");
		Pageable p = new PageRequest(page, length, sort);
		BooleanExpression query = QJpaBusUpLog.jpaBusUpLog.city.eq(cityId);
		String busid = req.getFilter("busid"), serinum = req.getFilter("serinum"), pname = req.getFilter("pname"), plid = req
				.getFilter("plid");
		if (StringUtils.isNotBlank(busid)) {
			int busId = NumberUtils.toInt(busid);
			query = query.and(QJpaBusUpLog.jpaBusUpLog.jpabus.id.eq(busId));
		}
		if (StringUtils.isNotBlank(serinum)) {
			query = query.and(QJpaBusUpLog.jpaBusUpLog.jpabus.serialNumber.like("%" + serinum + "%"));
		}
		if (StringUtils.isNotBlank(pname)) {
			query = query.and(QJpaBusUpLog.jpaBusUpLog.jpabus.plateNumber.like("%" + pname + "%"));
		}
		if (StringUtils.isNotBlank(plid)) {
			int pid = NumberUtils.toInt(plid);
			List<Integer> busids = userAutoCompleteMapper.selectBusidsByPid(pid);
			if (busids.size() > 0) {
				query = query.and(QJpaBusUpLog.jpaBusUpLog.jpabus.id.in(busids));
			} else {
				query = query.and(QJpaBusUpLog.jpaBusUpLog.jpabus.id.eq(0));
			}
		}
		return query == null ? busUpdateRepository.findAll(p) : busUpdateRepository.findAll(query, p);
	}

	public ContractLineDayInfo getContractBusLineTodayInfo(int publish_line_id) {
		ContractLineDayInfo line = new ContractLineDayInfo();
		PublishLine act = publishLineMapper.selectByPrimaryKey(publish_line_id);
		if (act != null) {
			int lineId = act.getLineId();
			BusExample example = new BusExample();
			example.createCriteria().andLineIdEqualTo(lineId);
			List<Bus> bus = busMapper.selectByExample(example);
			int onlineCount = 0, totalBus = 0;

			for (Bus bus2 : bus) {
				totalBus++;
				BusInfo busInfo = queryBusInfo.getBusInfo2(bus2.getId());
				if (busInfo.getStats() == BusInfo.Stats.now) {
					onlineCount++;
				}

			}
			line.setTotalBus(totalBus);
			line.setDayOnlieBus(onlineCount);
		}
		return line;
	}

	@Override
	public Pair<Boolean, String> checkFree(String stday, int days, int city, int publish_line_id) {
		Pair<Boolean, String> r = new Pair<Boolean, String>(false, "0");
		try {
			Date startDate = (Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(stday);
			Date endDate = com.pantuo.util.DateUtil.dateAdd(startDate, days);
			int c = 0;
			PublishLine act = publishLineMapper.selectByPrimaryKey(publish_line_id);
			if (act != null) {
				int lineId = act.getLineId();
				BusExample example = new BusExample();
				example.createCriteria().andLineIdEqualTo(lineId);
				List<Bus> bus = busMapper.selectByExample(example);

				for (Bus bus2 : bus) {
					BusInfo busInfo = queryBusInfo.getBusInfo2(bus2.getId());

					List<BusOnline> allPlan = busInfo.getAllPlan();
					boolean isEmpty = true;
					if (allPlan != null && !allPlan.isEmpty()) {
						for (BusOnline busOnline : allPlan) {
							if (busOnline.getStartDate().before(startDate) && busOnline.getEndDate().after(startDate)) {
								isEmpty = false;
								break;
							}
							if (busOnline.getStartDate().before(endDate) && busOnline.getEndDate().after(endDate)) {
								isEmpty = false;
								break;
							}
							if (startDate.before(busOnline.getStartDate()) && endDate.after(busOnline.getEndDate())) {
								isEmpty = false;
								break;
							}
						}
					}
					if (isEmpty) {
						c++;
					}
				}
			}
			r.setLeft(true);
			r.setRight(String.valueOf(c));
		} catch (ParseException e) {
			r.setRight("日期 参数不正确!" + e.getMessage());
		}

		return r;
	}

}
