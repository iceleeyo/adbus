package com.pantuo.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.dao.AdJustLogRepository;
import com.pantuo.dao.BusModelRepository;
import com.pantuo.dao.BusOnlineRepository;
import com.pantuo.dao.BusRepository;
import com.pantuo.dao.BusUpdateRepository;
import com.pantuo.dao.BusinessCompanyRepository;
import com.pantuo.dao.BuslineRepository;
import com.pantuo.dao.LineUpdateRepository;
import com.pantuo.dao.PublishLineRepository;
import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaBusAdjustLog;
import com.pantuo.dao.pojo.JpaBusModel;
import com.pantuo.dao.pojo.JpaBusOnline;
import com.pantuo.dao.pojo.JpaBusUpLog;
import com.pantuo.dao.pojo.JpaBusinessCompany;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.JpaLineUpLog;
import com.pantuo.dao.pojo.JpaPublishLine;
import com.pantuo.dao.pojo.JpaPublishLine.Sktype;
import com.pantuo.dao.pojo.QJpaBus;
import com.pantuo.dao.pojo.QJpaBusAdjustLog;
import com.pantuo.dao.pojo.QJpaBusModel;
import com.pantuo.dao.pojo.QJpaBusOnline;
import com.pantuo.dao.pojo.QJpaBusUpLog;
import com.pantuo.dao.pojo.QJpaBusinessCompany;
import com.pantuo.dao.pojo.QJpaBusline;
import com.pantuo.dao.pojo.QJpaLineUpLog;
import com.pantuo.dao.pojo.QJpaPublishLine;
import com.pantuo.mybatis.domain.Bus;
import com.pantuo.mybatis.domain.BusExample;
import com.pantuo.mybatis.domain.BusLine;
import com.pantuo.mybatis.domain.BusOnline;
import com.pantuo.mybatis.domain.BusOnlineExample;
import com.pantuo.mybatis.domain.BusUplog;
import com.pantuo.mybatis.domain.CountableBusLine;
import com.pantuo.mybatis.domain.CountableBusModel;
import com.pantuo.mybatis.domain.CountableBusinessCompany;
import com.pantuo.mybatis.domain.Modeldesc;
import com.pantuo.mybatis.domain.ModeldescExample;
import com.pantuo.mybatis.domain.Offlinecontract;
import com.pantuo.mybatis.domain.PublishLine;
import com.pantuo.mybatis.persistence.BusCustomMapper;
import com.pantuo.mybatis.persistence.BusLineMapper;
import com.pantuo.mybatis.persistence.BusMapper;
import com.pantuo.mybatis.persistence.BusOnlineMapper;
import com.pantuo.mybatis.persistence.BusSelectMapper;
import com.pantuo.mybatis.persistence.BusSubExample;
import com.pantuo.mybatis.persistence.BusUplogMapper;
import com.pantuo.mybatis.persistence.ModeldescMapper;
import com.pantuo.mybatis.persistence.OfflinecontractMapper;
import com.pantuo.mybatis.persistence.PublishLineMapper;
import com.pantuo.mybatis.persistence.UserAutoCompleteMapper;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.BusService;
import com.pantuo.simulate.BodyUseMonitor;
import com.pantuo.simulate.QueryBusInfo;
import com.pantuo.util.BeanUtils;
import com.pantuo.util.DateUtil;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.vo.CountView;
import com.pantuo.vo.ModelCountView;
import com.pantuo.web.ScheduleController;
import com.pantuo.web.view.AdjustLogView;
import com.pantuo.web.view.BusInfo;
import com.pantuo.web.view.BusInfoView;
import com.pantuo.web.view.BusModelGroupView;
import com.pantuo.web.view.CarUseView;
import com.pantuo.web.view.ContractLineDayInfo;
import com.pantuo.web.view.PulishLineView;

/**
 * @author tliu
 */
@Service
public class BusServiceImpl implements BusService {
	
	private static Logger log = LoggerFactory.getLogger(BusServiceImpl.class);
	@Autowired
	BusRepository busRepo;
	
	@Autowired
	BusOnlineRepository busOnlineRepository;
	@Autowired
	BusUpdateRepository busUpdateRepository;
	@Autowired
	LineUpdateRepository lineUpdateRepository;
	
	@Autowired
	PublishLineRepository publishLineRepository;
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
	ModeldescMapper modeldescMapper;
	
	@Autowired
	
	BodyUseMonitor bodyUseMonitor;
	
	
	@Autowired
	BusLineMapper busLineMapper;
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
	
	
	@Autowired
	BusSelectMapper busSelectMapper;
	
	@Autowired
	AdJustLogRepository adJustLogRepository;

	@Override
	public long count() {
		return busRepo.count();
	}

	@Override
	public long countFree(int city, JpaBusline.Level level, JpaBus.Category category, Integer lineId,
			Integer busModelId, Integer companyId) {
		return 0;
	}
	 public DataTablePage<AdjustLogView> getAdJustLog(int city, TableRequest req, int page, int pageSize, Sort sort
) {
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		if (sort == null)
			sort = new Sort("id");
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = QJpaBusAdjustLog.jpaBusAdjustLog.city.eq(city);
		String serinum = req.getFilter("serinum"), oldLineId = req.getFilter("oldLineId"), newLineId = req
				.getFilter("newLineId"),becompany = req
						.getFilter("becompany"),afcompany = req
								.getFilter("afcompany"),box= req
										.getFilter("box");
		 
		
		if (StringUtils.isNoneBlank(oldLineId) ) {
			//query = query.and(QJpaBusAdjustLog.jpaBusAdjustLog.oldline.id.eq(NumberUtils.toInt(oldLineId)));
			query = query.and(QJpaBusAdjustLog.jpaBusAdjustLog.oldlineName.eq(oldLineId));
		}

		if (StringUtils.isNoneBlank(newLineId)) {
			//query = query.and(QJpaBusAdjustLog.jpaBusAdjustLog.nowline.id.eq(NumberUtils.toInt(newLineId)));
			query = query.and(QJpaBusAdjustLog.jpaBusAdjustLog.nowLineName.eq((newLineId)));
		}
		
		if (StringUtils.isNotBlank(serinum)) {
			if (NumberUtils.toInt(box) == 0) {
				//query = query.and(QJpaBusAdjustLog.jpaBusAdjustLog.jpabus.serialNumber.like("%" + serinum + "%"));
				query = query.and(QJpaBusAdjustLog.jpaBusAdjustLog.serialNumber.like("%" + serinum + "%"));
			} else {

				List<Integer> busIdsList = busSelectMapper.queryUplog(serinum);
				if (busIdsList != null && !busIdsList.isEmpty()) {
					query = query.and(QJpaBusAdjustLog.jpaBusAdjustLog.jpabus.id.in(busIdsList));
				} else {
					query = query.and(QJpaBusAdjustLog.jpaBusAdjustLog.jpabus.id.eq(0));
				}
			}
		} 
		if (StringUtils.isNotBlank(becompany) && !StringUtils.equals(becompany, "defaultAll")) {
			JpaBusinessCompany company=new JpaBusinessCompany();
			int bid =NumberUtils.toInt(becompany);
			if(bid>0){
			company.setId(bid);
			query = query.and(QJpaBusAdjustLog.jpaBusAdjustLog.oldCompanyId.eq(company));
			}
		}
		if (StringUtils.isNotBlank(afcompany) && !StringUtils.equals(afcompany, "defaultAll")) {
			JpaBusinessCompany company = new JpaBusinessCompany();
			int bid = NumberUtils.toInt(afcompany);
			if (bid > 0) {
				company.setId(bid);
				query = query.and(QJpaBusAdjustLog.jpaBusAdjustLog.nowCompanyId.eq(company));
			}
		}


		Long countTotal = adJustLogRepository.count(query);
		Page<JpaBusAdjustLog> list = query == null ? adJustLogRepository.findAll(p) : adJustLogRepository.findAll(
				query, p);

		List<AdjustLogView> r = new ArrayList<AdjustLogView>();
		Page<AdjustLogView> jpabuspage = new org.springframework.data.domain.PageImpl<AdjustLogView>(r, p,
				countTotal == null ? 0 : countTotal);

		if (!list.getContent().isEmpty()) {
			for (JpaBusAdjustLog log : list.getContent()) {
				AdjustLogView w = new AdjustLogView(queryBusInfo);
				w.setLog(log);
				w.setOldBusLevel(log.getOldline().getLevelStr());
				w.setBusLevel(log.getNowline().getLevelStr());
				BusInfo info = queryBusInfo.getBusInfo2(log.getJpabus().getId());
				w.setBusInfo(info != null ? info : QueryBusInfo.emptybusInfo);
				r.add(w);
			}

			jpabuspage = new org.springframework.data.domain.PageImpl<AdjustLogView>(r, p, countTotal == null ? 0
					: countTotal);
		}
		return new DataTablePage(jpabuspage, req.getDraw());
	}
	
	public DataTablePage<BusInfoView> getMybatisAllBuses(int city, TableRequest req, int page, int pageSize, Sort sort,
			boolean fetchDisabled) {
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
				.getFilter("company"), contractid = req.getFilter("contractid"),contractTag = req.getFilter("contractTag");

		BusSubExample example = new BusSubExample();
		BusSubExample.Criteria2 ca = example.createCriteria2();
		//example.setOrderByClause(" ");
		example.setLimitStart(p.getOffset());
		example.setLimitEnd(pageSize);

		ca.and_CityEqualTo(city);
		if (StringUtils.isNotBlank(plateNumber)) {
			ca.andPlateNumberLike("%" + plateNumber + "%");
		}
		if (StringUtils.isNotBlank(lineid)) {
			int lineId = NumberUtils.toInt(lineid);
			ca.and_LineIdEqualTo(lineId);
			//query = query.and(QJpaBus.jpaBus.line.id.eq(lineId));
		}
		if (StringUtils.isNotBlank(linename)) {
			ca.and_LineNameEqualTo(linename);
			//query = query.and(QJpaBus.jpaBus.line.name.eq(linename));
		}
		if (StringUtils.isNotBlank(category) && !StringUtils.equals(category, "defaultAll")) {

			ca.andCategoryEqualTo(JpaBus.Category.valueOf(category).ordinal());

			//query = query.and(QJpaBus.jpaBus.category.eq(JpaBus.Category.valueOf(category)));
		}
		if (StringUtils.isNotBlank(levelStr) && !StringUtils.equals(levelStr, "defaultAll")) {
			ca.and_LineLevalEqualTo(JpaBusline.Level.valueOf(levelStr).ordinal());
			//query = query.and(QJpaBus.jpaBus.line.level.eq(JpaBusline.Level.valueOf(levelStr)));
		}
		if (StringUtils.isNotBlank(company) && !StringUtils.equals(company, "defaultAll")) {
			ca.andCompanyIdEqualTo(NumberUtils.toInt(company));

			/*JpaBusinessCompany c = new JpaBusinessCompany();
			c.setId(NumberUtils.toInt(company));
			query = query.and(QJpaBus.jpaBus.company.eq(c));*/
		}
		boolean isContractQuery = false;
		if (StringUtils.isNotBlank(contractid)) {
			ca.and_contractidEqualTo(NumberUtils.toInt(contractid));
			isContractQuery = true;
		}
		if (!fetchDisabled) {
			ca.andEnabledEqualTo(true);
		}

		Set<Integer> modelIdsIntegers = new HashSet<Integer>();
		Set<Integer> linesIntegers = new HashSet<Integer>();
		Set<Integer> compaynIntegers = new HashSet<Integer>();

		List<Bus> w = null;
		Integer countTotal = null;

		if (isContractQuery || BooleanUtils.toBoolean(contractTag)) {//按合同查
			ca.and_BusOnlineEnableEqualTo(1);//车辆正常上刊
			w = busSelectMapper.queryAllbusByContract(example);
			countTotal = busSelectMapper.countAllbusByContract(example);
		} else {
			w = busSelectMapper.queryAllbusExample(example);
			countTotal = busSelectMapper.countAllbusExample(example);
		}
		List<BusInfoView> r = new ArrayList<BusInfoView>();
		for (Bus jpaBus : w) {
			BusInfoView view = new BusInfoView();
			view.setBus(jpaBus);
			view.setBusInfo(queryBusInfo.getBusInfo2(jpaBus.getId()));
			r.add(view);
			modelIdsIntegers.add(jpaBus.getModelId());
			linesIntegers.add(jpaBus.getLineId());
			compaynIntegers.add(jpaBus.getCompanyId());
		}
		putBusBaseInfo(r, modelIdsIntegers, linesIntegers, compaynIntegers,"new");

		//	Pageable p = new PageRequest(req.getPage(), req.getLength(),sort);
		Page<BusInfoView> jpabuspage = new org.springframework.data.domain.PageImpl<BusInfoView>(r, p,
				countTotal == null ? 0 : countTotal);//page.getTotalElements()
		return new DataTablePage(jpabuspage, req.getDraw());
		//return query == null ? busRepo.findAll(p) : busRepo.findAll(query, p);

	}
	
	public DataTablePage<BusInfoView> getAllBusesForContract(int city, TableRequest req, int page, int pageSize,
			Sort sort, boolean fetchDisabled) {

		final String contractid = req.getFilter("contractid");
		boolean isNormalQuery = true;
		int _contractid = NumberUtils.toInt(contractid);
		if (_contractid > 0) {
			isNormalQuery = false;
		}
		BooleanExpression query = getQueryCommon(city, req, isNormalQuery);
		if (isNormalQuery) {
			Page<JpaBus> pageObject = getAllBuses(city, req, page, pageSize, sort, fetchDisabled);
			return new DataTablePage<BusInfoView>(queryBusinfoView(req, pageObject), req.getDraw());
		} else {
			BooleanExpression contractQuery = QJpaBusOnline.jpaBusOnline.city.eq(city);
			contractQuery = contractQuery.and(QJpaBusOnline.jpaBusOnline.offlineContract.id.eq(_contractid));
			contractQuery = contractQuery.and(QJpaBusOnline.jpaBusOnline.enable.eq(true));
			contractQuery = contractQuery.and(query);

			Pageable p = new PageRequest(page < 0 ? 0 : page, pageSize < 1 ? 1 : pageSize,
					sort == null ? new Sort("id") : sort);
			Page<JpaBusOnline> r = busOnlineRepository.findAll(contractQuery, p);

			List<BusInfoView> busList = new ArrayList<BusInfoView>();
			List<JpaBusOnline> wt = r.getContent();
			Date now = new Date();

			Offlinecontract contractObj = offlinecontractMapper.selectByPrimaryKey(_contractid);

			for (JpaBusOnline e : wt) {
				BusInfoView temp = new BusInfoView();
				temp.setJpaBus(e.getJpabus());
				boolean isHave = false;
				if (e.getStartDate().before(now)) {
					if (e.getReal_endDate() == null) {
						isHave = true;
					}
				}
				temp.setIshaveAd(isHave);
				BusInfo info = new BusInfo();
				if (contractObj != null) {
					info.setOfflinecontract(contractObj);
					info.setContractCode(contractObj.getContractCode());
				}
				
				info.setStartD(e.getStartDate());
				info.setEndD(e.getEndDate());
				BusOnline be = new BusOnline();
				be.setAdtype(e.getAdtype().ordinal());
				be.setSktype(e.getSktype().ordinal());
				be.setRealEndDate(e.getReal_endDate());
				be.setDays(e.getDays());
				be.setPrint(e.getPrint().ordinal());
				temp.setBusInfo(info);
				info.setBusOnline(be);
				busList.add(temp);
			}
			Page<BusInfoView> result = new org.springframework.data.domain.PageImpl<BusInfoView>(busList, p,
					r.getTotalElements());
			return new DataTablePage<BusInfoView>(result, req.getDraw());
		}
	}
	
	
	

	@Override
	public Page<JpaBus> getAllBuses(int city, TableRequest req, int page, int pageSize, Sort sort, boolean fetchDisabled) {
		if(fetchDisabled){
			if(!isParamerQuery(req)){
				Pageable p = new PageRequest(page, pageSize, sort);
				return new org.springframework.data.domain.PageImpl<JpaBus>(new ArrayList<JpaBus>(0), p, 0);
			}
		}
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		if (sort == null)
			sort = new Sort("id");
		Pageable p = new PageRequest(page, pageSize, sort);
		final String contractid = req.getFilter("contractid");
		boolean isNormalQuery = true;
		int _contractid = NumberUtils.toInt(contractid);
		if (_contractid > 0) {
			isNormalQuery = false;
		}
		BooleanExpression query = getQueryCommon(city, req, isNormalQuery);
		if (!isNormalQuery) {
			BooleanExpression contractQuery = QJpaBusOnline.jpaBusOnline.city.eq(city);
			contractQuery = contractQuery.and(QJpaBusOnline.jpaBusOnline.offlineContract.id.eq(_contractid));
			contractQuery =contractQuery.and(QJpaBusOnline.jpaBusOnline.enable.eq(true));
			contractQuery = contractQuery.and(query);
			Page<JpaBusOnline> r = busOnlineRepository.findAll(contractQuery, p);

			List<JpaBus> busList = new ArrayList<JpaBus>();
			List<JpaBusOnline> wt = r.getContent();
			for (JpaBusOnline e : wt) {
				busList.add(e.getJpabus());
			}
			return new org.springframework.data.domain.PageImpl<JpaBus>(busList, p, r.getTotalElements());
		}
		//ss

		//		
		//		if (!fetchDisabled) {
		//			BooleanExpression q = QJpaBus.jpaBus.enabled.isTrue();
		//			if (query == null)
		//				query = q;
		//			else
		//				query = query.and(q);
		//		}
		return query == null ? busRepo.findAll(p) : busRepo.findAll(query, p);

	}

	private BooleanExpression getQueryCommon(int city, TableRequest req, boolean isNormalQuery) {
		final String sh = req.getFilter("sh"), serinum = req.getFilter("serinum"), oldserinum = req
				.getFilter("oldserinum"), plateNumber = req.getFilter("plateNumber"), linename = req
				.getFilter("linename"), levelStr = req.getFilter("levelStr"), category = req.getFilter("category"), lineid = req
				.getFilter("lineid"), company = req.getFilter("company"); 
		
		
		
		BooleanExpression query =isNormalQuery? QJpaBus.jpaBus.city.eq(city): QJpaBusOnline.jpaBusOnline.city.eq(city);
		BooleanExpression commonEx = getQueryFromPage(sh);
		if (commonEx != null) {
			query = query.and(commonEx);
		}
		if (StringUtils.isNotBlank(serinum)) {
			String[] a = serinum.split(",");
			if (a.length > 1) {
				List<String> list = Arrays.asList(a);
				query = query.and(isNormalQuery ? QJpaBus.jpaBus.serialNumber.in(list)
						: QJpaBusOnline.jpaBusOnline.jpabus.serialNumber.in(list));
			} else {
				query = query.and(isNormalQuery ? QJpaBus.jpaBus.serialNumber.like("%" + serinum + "%")
						: QJpaBusOnline.jpaBusOnline.jpabus.serialNumber.like("%" + serinum + "%"));
			}
		}
		if (StringUtils.isNotBlank(oldserinum)) {
			String[] a = oldserinum.split(",");
			if (a.length > 1) {
				List<String> list = Arrays.asList(a);
				query = query.and(isNormalQuery ? QJpaBus.jpaBus.oldSerialNumber.in(list)
						: QJpaBusOnline.jpaBusOnline.jpabus.oldSerialNumber.in(list));
			} else {
				query = query.and(isNormalQuery ? QJpaBus.jpaBus.oldSerialNumber.like("%" + oldserinum + "%")
						: QJpaBusOnline.jpaBusOnline.jpabus.oldSerialNumber.like("%" + oldserinum + "%"));
			}

		}
		if (StringUtils.isNotBlank(plateNumber)) {
			String[] a = plateNumber.split(",");
			if (a.length > 1) {
				List<String> list = Arrays.asList(a);
				query = query.and(isNormalQuery ? QJpaBus.jpaBus.plateNumber.in(list) : QJpaBus.jpaBus.plateNumber
						.in(list));
			} else {
				query = query.and(isNormalQuery ? QJpaBus.jpaBus.plateNumber.like("%" + plateNumber + "%")
						: QJpaBusOnline.jpaBusOnline.jpabus.plateNumber.like("%" + plateNumber + "%"));
			}
		}
		if (StringUtils.isNotBlank(lineid)) {
			int lineId = NumberUtils.toInt(lineid);
			query = query.and(isNormalQuery ? QJpaBus.jpaBus.line.id.eq(lineId)
					: QJpaBusOnline.jpaBusOnline.jpabus.line.id.eq(lineId));
		}
		if (StringUtils.isNotBlank(linename)) {
			query = query.and(isNormalQuery ? QJpaBus.jpaBus.line.name.eq(linename)
					: QJpaBusOnline.jpaBusOnline.jpabus.line.name.eq(linename));
		}
		if (StringUtils.isNotBlank(category) && !StringUtils.equals(category, "defaultAll")) {
			query = query.and(isNormalQuery ? QJpaBus.jpaBus.category.eq(JpaBus.Category.valueOf(category))
					: QJpaBusOnline.jpaBusOnline.jpabus.category.eq(JpaBus.Category.valueOf(category)));
		}
		if (StringUtils.isNotBlank(levelStr) && !StringUtils.equals(levelStr, "defaultAll")) {
			query = query.and(isNormalQuery ? QJpaBus.jpaBus.line.level.eq(JpaBusline.Level.valueOf(levelStr))
					: QJpaBusOnline.jpaBusOnline.jpabus.line.level.eq(JpaBusline.Level.valueOf(levelStr)));
		}
		if (StringUtils.isNotBlank(company) && !StringUtils.equals(company, "defaultAll")) {
			JpaBusinessCompany c = new JpaBusinessCompany();
			c.setId(NumberUtils.toInt(company));
			query = query.and(isNormalQuery ? QJpaBus.jpaBus.company.eq(c) : QJpaBusOnline.jpaBusOnline.jpabus.company
					.eq(c));
		}
		return query;
	}
	private BooleanExpression getQueryFromPage(String sh) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		BooleanExpression query = null;
		String[] shSplit = StringUtils.split(sh, ",");
		if (shSplit != null) {
			for (String string : shSplit) {//p1
				String[] one = StringUtils.split(string, "_");
				String field = one[0];
				String v = one[1];
				if (!map.containsKey(field)) {
					map.put(field, new ArrayList<String>());
				}
				if (!StringUtils.equals("all", v)) {
					map.get(field).add(v);
				}
			}
		}
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			List<String> vIntegers = entry.getValue();
			//营销中心
			 if (StringUtils.equals(entry.getKey(), "com") && vIntegers.size() > 0) {
				BooleanExpression subQuery = null;
				List<Integer> idsList=new ArrayList<Integer>();
				for (String playNumber : vIntegers) {
					idsList.add(NumberUtils.toInt(playNumber));
				}
				subQuery = subQuery == null ? QJpaBus.jpaBus.company.id.in(idsList) : subQuery
						.and(QJpaBus.jpaBus.company.id.in(idsList));
				query = query == null ? subQuery : query.and(subQuery);
				//公司名称
			}  else if (StringUtils.equals(entry.getKey(), "company") && vIntegers.size() > 0) {
				BooleanExpression subQuery = null;
				for (String type : vIntegers) {
					if (StringUtils.equals("1", type)) {
						subQuery = subQuery == null ?  QJpaBus.jpaBus.office.eq("大公共公司") : subQuery
								.or(QJpaBus.jpaBus.office.eq("大公共公司"));
					} else if (StringUtils.equals("2", type)) {
						subQuery = subQuery == null ? QJpaBus.jpaBus.office.eq("八方达公司") : subQuery
								.or(QJpaBus.jpaBus.office.eq("八方达公司"));
					}
				}
				query = query == null ? subQuery : query.and(subQuery);
			} else if (StringUtils.equals(entry.getKey(), "stats") && vIntegers.size() > 0) {
				BooleanExpression subQuery = null;
				for (String type : vIntegers) {
					if (StringUtils.equals("1", type)) {
						subQuery = subQuery == null ?  QJpaBus.jpaBus.enabled.eq(true) : subQuery
								.or(QJpaBus.jpaBus.enabled.eq(true));
					} else if (StringUtils.equals("2", type)) {
						subQuery = subQuery == null ? QJpaBus.jpaBus.enabled.eq(false) : subQuery
								.or(QJpaBus.jpaBus.enabled.eq(false));
					}
				}
				query = query == null ? subQuery : query.and(subQuery);
			} else if (StringUtils.equals(entry.getKey(), "lev") && vIntegers.size() > 0) {
				BooleanExpression subQuery = null;
				List<JpaBusline.Level> right = new ArrayList<JpaBusline.Level>();
				for (String type : vIntegers) {
					right.add(JpaBusline.Level.valueOf(type));
				}
				subQuery = subQuery == null ? QJpaBus.jpaBus.line.level.in(right) : subQuery
						.and(QJpaBus.jpaBus.line.level.in(right));
				query = query == null ? subQuery : query.and(subQuery);
			} else if (StringUtils.equals(entry.getKey(), "gor") && vIntegers.size() > 0) {
				BooleanExpression subQuery = null;
				List<JpaBus.Category> right = new ArrayList<JpaBus.Category>();
				for (String type : vIntegers) {
					right.add(JpaBus.Category.valueOf(type));
				}
				subQuery = subQuery == null ? QJpaBus.jpaBus.category.in(right) : subQuery
						.and(QJpaBus.jpaBus.category.in(right));
				query = query == null ? subQuery : query.and(subQuery);
			}

		}
		if(query!=null){
			log.debug(query.toString());
		}
		return query;
	}

	public static Specification<JpaBus> emptyPredicate(){
		  return new Specification<JpaBus>(){
		    
			@Override
			public javax.persistence.criteria.Predicate toPredicate(Root<JpaBus> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				//root = query.from(JpaBus.class);  
				Path<JpaBusinessCompany> company = root.get("company");//测试公司
				Path<JpaBus> line = root.get("line");//测试线路
				//Path<JpaBus.Category> c = root.get("category");
				//BooleanExpression query2 = QJpaBus.jpaBus.line.level.eq(JpaBusline.Level.valueOf("A"));
				root.join(root.getModel().getSet("jpabus",JpaBusOnline.class) , JoinType.LEFT);
				//.on(  cb.equal(root.get("id"), QJpaBusOnline. jpaBusOnline.jpabus.id )  );
				return cb.and(cb.equal(line.get("level"), JpaBusline.Level.valueOf("A")),
						cb.equal(company.get("id"), 1), cb.equal(root.get("category"), JpaBus.Category.valueOf("yunyingche").ordinal() ));

				//return root.get("").();
			}
		};
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

	public Page<CarUseView> getLinesUse(int city, TableRequest req) {
		String levelStr = req.getFilter("level");
		String name = req.getFilter("name");
		int page = req.getPage();
		int pageSize = req.getLength();
		Sort sort = req.getSort("id");

		JpaBusline.Level level = null;
		if (!StringUtils.isBlank(levelStr)) {
			level = JpaBusline.Level.fromNameStr(levelStr);
			try {
				level = JpaBusline.Level.valueOf(levelStr);
			} catch (Exception e) {
			}
		}
		Page<JpaBusline> lines = getAllBuslines(city, level, name, page, pageSize, sort);
		List<JpaBusline> w = lines.getContent();
		List<CarUseView> list = new ArrayList<CarUseView>();
		for (JpaBusline e : w) {
			CarUseView one1 = new CarUseView(e);
			one1.setView(bodyUseMonitor.getUserView(e.getId()));
			list.add(one1);
		}
		Pageable p = new PageRequest(req.getPage(), req.getLength(), sort);
		return new org.springframework.data.domain.PageImpl<CarUseView>(list, p, lines.getTotalElements());

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
	public Page<JpaBusModel> getAllBusModels(int city, TableRequest req,String name, String manufacturer, int page, int pageSize,
			Sort sort) {
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		if (sort == null)
			sort = new Sort("updated");
		
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = QJpaBusModel.jpaBusModel.city.eq(city);
		if (name != null) {
			query = query.and(QJpaBusModel.jpaBusModel.name.like("%" + name + "%"));
		}
		if (manufacturer != null) {
			query = query.and(QJpaBusModel.jpaBusModel.manufacturer.like("%" + manufacturer + "%"));
		}
       if(null!=req){
			String description=req.getFilter("description"),doubleDecker=req.getFilter("doubleDecker");
//			if (description != null) {
//				query = query.and(QJpaBusModel.jpaBusModel.description.like("%" + description + "%"));
//			}
			if (doubleDecker != null && !StringUtils.equals(doubleDecker, "defaultAll")) {
				boolean b=BooleanUtils.toBoolean(doubleDecker);
				query = query.and(QJpaBusModel.jpaBusModel.doubleDecker.eq(b));
			}
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

	public void exportBusExcel(TableRequest req, Page<JpaBus> busList, HttpServletResponse resp) {

		String plateNumber = req.getFilter("plateNumber"), linename = req.getFilter("linename"), levelStr = req
				.getFilter("levelStr"), category = req.getFilter("category");

		List<JpaBus> list = busList.getContent();
		List<BusInfoView> r = new ArrayList<BusInfoView>(list.size());
		for (JpaBus jpaBus : list) {
			BusInfoView view = new BusInfoView();
			view.setJpaBus(jpaBus);
			BusInfo info = queryBusInfo.getBusInfo2(jpaBus.getId());
			view.setBusInfo(info != null ? info : QueryBusInfo.emptybusInfo);
			view.setBusLevel(jpaBus.getLine().getLevelStr());
			r.add(view);
		}
		String templateFileName = "/jxls/bus_list.xls";

		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotBlank(plateNumber)) {
			//sb.append("车牌号_" + plateNumber + "_");
			sb.append("_" + plateNumber + "_");
		}
		if (StringUtils.isNotBlank(linename)) {
			sb.append("_" + linename + "_");
			//sb.append("线路_" + linename + "_");
		}

		if (StringUtils.isNotBlank(category) && !StringUtils.equals(category, "defaultAll")) {
			//sb.append("类型_" + JpaBus.Category.valueOf(category).getNameStr() + "_");
			sb.append("_" + JpaBus.Category.valueOf(category).getNameStr() + "_");
		}
		if (StringUtils.isNotBlank(levelStr) && !StringUtils.equals(levelStr, "defaultAll")) {
			//sb.append("线路级别_" + JpaBusline.Level.valueOf(levelStr).getNameStr() + "_");
			sb.append("_" + JpaBusline.Level.valueOf(levelStr).getNameStr() + "_");
		}
		if (sb.length() == 0) {
			sb.append("bus");
		}
		Map beans = new HashMap();
		beans.put("report", r);
		beans.put("title", sb.toString());
		beans.put("number", sb.toString());
		XLSTransformer transformer = new XLSTransformer();
		try {
			resp.setHeader("Content-Type", "application/x-xls");
			resp.setHeader("Content-Disposition", "attachment; filename=\"bus-[" + sb.toString() + "].xls\"");
			InputStream is = new BufferedInputStream(ScheduleController.class.getResourceAsStream(templateFileName));
			org.apache.poi.ss.usermodel.Workbook workbook = transformer.transformXLS(is, beans);
			//	ExcelUtil.dynamicMergeCells((HSSFSheet) workbook.getSheetAt(0), 1, 0, 1, 2);

			OutputStream os = new BufferedOutputStream(resp.getOutputStream());
			workbook.write(os);
			is.close();
			os.flush();
			os.close();
			log.info("export over!");
		} catch (Exception e) {
			log.error("Fail to export excel for city {}, req {}");
			throw new RuntimeException("Fail to export excel", e);
		}

	}
	/**
	 * 
	 *  返回true表示有参数查询
	 *
	 * @param req
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public boolean isParamerQuery(TableRequest req) {
		final String sh = req.getFilter("sh"), serinum = req.getFilter("serinum"), oldserinum = req
				.getFilter("oldserinum"), plateNumber = req.getFilter("plateNumber"), linename = req
				.getFilter("linename"), levelStr = req.getFilter("levelStr"), category = req.getFilter("category"), lineid = req
				.getFilter("lineid"), company = req.getFilter("company"), contractid = req.getFilter("contractid");
		return StringUtils.isNotBlank(sh) || StringUtils.isNotBlank(serinum) || StringUtils.isNotBlank(oldserinum)
				|| StringUtils.isNotBlank(plateNumber) || StringUtils.isNotBlank(linename)
				|| (StringUtils.isNotBlank(category) && !StringUtils.equals(category, "defaultAll"))
				|| StringUtils.isNotBlank(lineid)
				|| (StringUtils.isNotBlank(company) && !StringUtils.equals(levelStr, "defaultAll"))
				|| (StringUtils.isNotBlank(contractid) && NumberUtils.toInt(contractid) > 0)
				|| (StringUtils.isNotBlank(levelStr) && !StringUtils.equals(levelStr, "defaultAll"));

	}

	public Collection<BusModelGroupView> queryModelGroup4Contract(int city, TableRequest req, boolean fetchDisabled) {

		final String contractid = req.getFilter("contractid");
		boolean isNormalQuery = true;
		int _contractid = NumberUtils.toInt(contractid);
		if (_contractid <= 0) {
			Page<JpaBus> jpabuspage = getAllBuses(city, req, 0, Integer.MAX_VALUE, req.getSort("id"), true);
			return queryModelGroup(req, jpabuspage);
		} else {
			BooleanExpression query = getQueryCommon(city, req, isNormalQuery);
			BooleanExpression contractQuery = QJpaBusOnline.jpaBusOnline.city.eq(city);
			contractQuery = contractQuery.and(QJpaBusOnline.jpaBusOnline.offlineContract.id.eq(_contractid));
			contractQuery = contractQuery.and(QJpaBusOnline.jpaBusOnline.enable.eq(true));
			contractQuery = contractQuery.and(query);
			long rc = busOnlineRepository.count();
			List<BusModelGroupView> r = new ArrayList<BusModelGroupView>();
			BusModelGroupView total = new BusModelGroupView("-total-");
			total.setTotal((int) rc);
			r.add(total);
			return r;
		}
	}
	public Collection<BusModelGroupView> queryModelGroup(TableRequest req, Page<JpaBus> page) {
		long t = System.currentTimeMillis();
		Map<String, BusModelGroupView> map = new HashMap<String, BusModelGroupView>();

		List<JpaBus> list = page.getContent();
		for (JpaBus jpaBus : list) {
			String _modelName = jpaBus.getModel().getName();
			BusModelGroupView w = null;
			if (!map.containsKey(_modelName)) {
				map.put(_modelName, w = new BusModelGroupView(_modelName));
			} else {  
				w = map.get(_modelName);
			}
			queryBusInfo.fullBusModelGroupView(jpaBus.getId(), w);
		}
		List<BusModelGroupView> r = new ArrayList<BusModelGroupView>();

		if (!map.isEmpty()) {
			BusModelGroupView total = new BusModelGroupView("-total-");
			for (BusModelGroupView entry : map.values()) {
				int free = entry.getTotal() - entry.getOnline() - entry.getNowDown();
				entry.setFree(free);
				total.setTotal(total.getTotal() + entry.getTotal());
				total.setFree(total.getFree() + entry.getFree());
				total.setOnline(total.getOnline() + entry.getOnline());
				total.setNowDown(total.getNowDown() + entry.getNowDown());
				r.add(entry);
			}
			r.add(total);
		}
		long end = System.currentTimeMillis() - t;
		if (end > 500) {
			log.info("queryModelGroup slow [ms]: " + end);
		}
		return r;

	}
	public Page<BusInfoView> queryBusinfoView(TableRequest req, Page<JpaBus> page) {
		List<JpaBus> list = page.getContent();
		List<BusInfoView> r = new ArrayList<BusInfoView>(list.size());
		for (JpaBus jpaBus : list) {
			BusInfoView view = new BusInfoView();
			view.setJpaBus(jpaBus);
			view.setBusInfo(queryBusInfo.getBusInfo2(jpaBus.getId()));
			boolean ishaveAd=queryBusInfo.ishaveAd(jpaBus.getId());
			view.setIshaveAd(ishaveAd);
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
		Set<Integer> oldmodelIdsIntegers = new HashSet<Integer>();
		Set<Integer> oldlinesIntegers = new HashSet<Integer>();
		Set<Integer> oldcompaynIntegers = new HashSet<Integer>();

		for (JpaBusUpLog log : list) {
			BusInfoView view = new BusInfoView();
			view.setBusUpLog(log);
			Bus jpabus = t.readValue(log.getJsonString(), Bus.class);
			Bus oldbus = t.readValue(log.getOldjsonString(), Bus.class);
			view.setBus(jpabus);
			view.setOldbus(oldbus);
			boolean ishaveAd=queryBusInfo.ishaveAd(jpabus.getId());
			view.setIshaveAd(ishaveAd);
			r.add(view);
			view.setSerinum(req.getFilter("serinum"));
			modelIdsIntegers.add(jpabus.getModelId());
			linesIntegers.add(jpabus.getLineId());
			compaynIntegers.add(jpabus.getCompanyId());
			oldmodelIdsIntegers.add(oldbus.getModelId());
			oldlinesIntegers.add(oldbus.getLineId());
			oldcompaynIntegers.add(oldbus.getCompanyId());
		}

		//List<? > cc=modelIdsIntegers;
		putBusBaseInfo(r, modelIdsIntegers, linesIntegers, compaynIntegers,"new");
		putBusBaseInfo(r, oldmodelIdsIntegers, oldlinesIntegers, oldcompaynIntegers,"old");
		Pageable p = new PageRequest(req.getPage(), req.getLength(), page.getSort());
		return new org.springframework.data.domain.PageImpl<BusInfoView>(r, p, page.getTotalElements());
	}
	public Page<BusInfoView> queryBusinfoView3(TableRequest req, Page<JpaLineUpLog> page) throws JsonParseException,
	JsonMappingException, IOException {
		List<JpaLineUpLog> list = page.getContent();
		List<BusInfoView> r = new ArrayList<BusInfoView>(list.size());
		ObjectMapper t = new ObjectMapper();
		t.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		t.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
		
		Set<Integer> levleIntegers = new HashSet<Integer>();
		Set<Integer> compaynIntegers = new HashSet<Integer>();
		Set<Integer> oldlevleIntegers = new HashSet<Integer>();
		Set<Integer> oldcompaynIntegers = new HashSet<Integer>();
		
		for (JpaLineUpLog log : list) {
			BusInfoView view = new BusInfoView();
			view.setLineUpLog(log);
			BusLine newline = t.readValue(log.getJsonString(), BusLine.class);
			BusLine oldline = t.readValue(log.getOldjsonString(), BusLine.class);
			view.setNewLine(newline);
			view.setOldLine2(oldline);
			r.add(view);
			levleIntegers.add(newline.getLevel());
			compaynIntegers.add(newline.getCompanyId());		
			oldlevleIntegers.add(oldline.getLevel());
			oldcompaynIntegers.add(oldline.getCompanyId());
		}
		
		putLineBaseInfo(r,levleIntegers, compaynIntegers,"new");
		putLineBaseInfo(r, oldlevleIntegers, oldcompaynIntegers,"old");
		Pageable p = new PageRequest(req.getPage(), req.getLength(), page.getSort());
		return new org.springframework.data.domain.PageImpl<BusInfoView>(r, p, page.getTotalElements());
	}

	private void putLineBaseInfo(List<BusInfoView> r, Set<Integer> levleIntegers, Set<Integer> compaynIntegers,
			String type) {
		Map<Integer, ?> companyMap = null;
		Map<Integer, String> levelMap = new HashMap<Integer, String>();
        for (JpaBusline.Level l : JpaBusline.Level.values()) {
        	levelMap.put(l.ordinal(), l.getNameStr());
        }
		if (!compaynIntegers.isEmpty()) {
			List<JpaBusinessCompany> w = companyRepo.findAll(compaynIntegers);
			companyMap = list2Map(w);
		}
		if(StringUtils.equals(type, "new")){
			  for (BusInfoView view : r) {
				  if (view.getNewLine()!= null) {
					  view.setCompany(companyMap != null && view.getNewLine().getCompanyId() != null ? (JpaBusinessCompany) companyMap
							  .get(view.getNewLine().getCompanyId()) : null);
					  view.setLevleString(view.getNewLine().getLevel()!=null?levelMap.get(view.getNewLine().getLevel()):null);
				  }
				  
			  }
		  }else{
			  for (BusInfoView view : r) {
				  if (view.getOldLine2() != null) {
					  view.setOldcompany(companyMap != null && view.getOldLine2().getCompanyId() != null ? (JpaBusinessCompany) companyMap
							  .get(view.getOldLine2().getCompanyId()) : null);
					  view.setOldlevleString(view.getOldLine2().getLevel()!=null?levelMap.get(view.getOldLine2().getLevel()):null);
				  }
				  
			  }
		  }
	}

	private void putBusBaseInfo(List<BusInfoView> r, Set<Integer> modelIdsIntegers, Set<Integer> linesIntegers,
			Set<Integer> compaynIntegers,String type) {
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
  if(StringUtils.equals(type, "new")){
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
  }else{
	  for (BusInfoView view : r) {
		  if (view.getOldbus() != null) {
			  view.setOldmodel(modelMap != null && view.getOldbus().getModelId() != null ? (JpaBusModel) modelMap.get(view
					  .getOldbus().getModelId()) : null);
			  view.setOldline(lineMap != null && view.getOldbus().getLineId() != null ? (JpaBusline) lineMap.get(view
					  .getOldbus().getLineId()) : null);
			  view.setOldcompany(companyMap != null && view.getOldbus().getCompanyId() != null ? (JpaBusinessCompany) companyMap
					  .get(view.getOldbus().getCompanyId()) : null);
			  
			  view.setOldbusCategory(com.pantuo.dao.pojo.JpaBus.Category.values()[view.getOldbus().getCategory()]
					  .getNameStr());
		  }
		  
	  }
  }
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
		Date now =new Date();
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
			if(busOnline.getRealEndDate() != null && busOnline.getRealEndDate().before(now)){
				isDup=false;
			}
			if (isDup) {
				JpaBus bus = findById(busId);
				r = new Pair<Boolean, String>(false, bus == null ? "数据异常# 车辆未找到 编号:" + bus : "上刊冲突<br>[自编号为 "
						+ bus.getSerialNumber() + " 上刊时段内已有其他合同上刊,请先进行上刊处理!]");
				break;
			}
		}
		return r;

	}

	@Override
	public Pair<Boolean, String> batchOnline(String ids, String stday, int days, int contractid, Principal principal,
			int city, int plid, int fday, String adtype, String print, String sktype) throws ParseException {
		Date startDate = (Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(stday);
		Date endDate = com.pantuo.util.DateUtil.dateAdd(startDate, days);
		Date reserveDate = com.pantuo.util.DateUtil.dateAdd(endDate, fday);
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
         PublishLine puLine= publishLineMapper.selectByPrimaryKey(plid);
         if(puLine!=null){
        	 puLine.setSktype(JpaPublishLine.Sktype.valueOf(sktype).ordinal());
        	 puLine.setUpdated(new Date());
        	 publishLineMapper.updateByPrimaryKey(puLine);
         }else{
        	 return new Pair<Boolean, String>(false, "信息丢失");
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
			busOnline.setEditor(Request.getUserId(principal));
			busOnline.setCity(city);
			busOnline.setPublishLineId(plid);
			busOnline.setReserveDate(reserveDate);
			busOnline.setAdtype(JpaBusOnline.Adtype.valueOf(adtype).ordinal());
			busOnline.setPrint(JpaBusOnline.Print.valueOf(print).ordinal());
			busOnline.setSktype(JpaBusOnline.Sktype.valueOf(sktype).ordinal());
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

	public boolean isSerialNumberExist(String serialNumber, int cityId) {
		BusExample example = new BusExample();
		example.createCriteria().andCityEqualTo(cityId).andSerialNumberEqualTo(StringUtils.trim(serialNumber));
		return busMapper.countByExample(example) > 0;
	}

	public Pair<Boolean, String> changeLine(String busIds, int newLineId, int cityId, Principal principal,
			HttpServletRequest request) {
		Pair<Boolean, String> pair = new Pair<Boolean, String>(false, StringUtils.EMPTY);
		if (StringUtils.isBlank(busIds) || newLineId == 0) {
			pair.setRight("参数错误!");
			return pair;
		} else {
			String[] idList = StringUtils.split(busIds,",");
			JpaBusline be = buslineRepository.getOne(newLineId);
			if (be == null) {
				pair.setRight("线路不存在!");
				return pair;
			}
			for (String id : idList) {
				int t = NumberUtils.toInt(id);
				if (t != 0) {
					Bus dbBus = busMapper.selectByPrimaryKey(t);
					if (dbBus != null) {
						int oldLineId = dbBus.getLineId();
						dbBus.setLineId(newLineId);

						busMapper.updateByPrimaryKey(dbBus);
						JpaBusAdjustLog entity = new JpaBusAdjustLog();
						entity.setCity(cityId);
						entity.setCreated(new Date());
						entity.setUpdated(new Date());
						entity.setUpdator(Request.getUserId(principal));
						JpaBus jpabus = new JpaBus(cityId, t);
						entity.setJpabus(jpabus);
						//
						JpaBusline oldline = new JpaBusline();
						oldline.setId(oldLineId);
						entity.setOldline(oldline);
						//
						JpaBusline newLine = new JpaBusline();
						newLine.setId(newLineId);
						entity.setNowline(newLine);
						BusLine oldLineObj = busLineMapper.selectByPrimaryKey(oldLineId);
						if (oldLineObj != null) {
							entity.setOldlineName(oldLineObj.getName());
							JpaBusinessCompany c =new JpaBusinessCompany();
							c.setId(oldLineObj.getCompanyId());
							entity.setOldCompanyId(c);
						}
						entity.setSerialNumber(dbBus.getSerialNumber());
						BusLine newLineObj = busLineMapper.selectByPrimaryKey(newLineId);
						if (newLineObj != null) {
							entity.setNowLineName(newLineObj.getName());
							JpaBusinessCompany c =new JpaBusinessCompany();
							c.setId(newLineObj.getCompanyId());
							entity.setNowCompanyId(c);
						}
						adJustLogRepository.save(entity);
					}
				}
			}
			pair.setLeft(true);
		}
		return pair;
	}
	@Override
	public Pair<Boolean, String> saveBus(Bus bus,String updated1, int cityId, Principal principal, HttpServletRequest request)
			throws JsonGenerationException, JsonMappingException, IOException, ParseException {
		String forceExcute = request.getParameter("forceExcute");
		Date uDate=new Date();
		if(StringUtils.isNotBlank(updated1)){
			uDate=(Date)new SimpleDateFormat("yyyy-MM-dd").parseObject(updated1); 
		}
		if (bus != null && StringUtils.isNoneBlank(bus.getSerialNumber()) && !StringUtils.equals(forceExcute, "Y")) {
			if (null == bus.getId() || bus.getId() == 0) {//如果是保存操作 判断是否已经有
				if (isSerialNumberExist(bus.getSerialNumber(), cityId)) {
					return new Pair<Boolean, String>(false, "serialNumber_exist");
				}
			} else {
				Bus bus2 = busMapper.selectByPrimaryKey(bus.getId());
				if (bus2 == null) {
					return new Pair<Boolean, String>(false, "信息丢失");
				} else {//如果是修改操作 判断当前修改的记录是否是自己
					if (!StringUtils.equals(bus2.getSerialNumber(), bus.getSerialNumber())) {
						BusExample example = new BusExample();
						example.createCriteria().andCityEqualTo(cityId)
								.andSerialNumberEqualTo(StringUtils.trim(bus.getSerialNumber()))
								.andIdNotEqualTo(bus.getId());
						if (busMapper.countByExample(example) > 0) {
							return new Pair<Boolean, String>(false, "serialNumber_exist");
						}
					}
				}
			}
		}

		if (null != bus.getId() && bus.getId() > 0) {
			Bus bus2 = busMapper.selectByPrimaryKey(bus.getId());
			if (bus2 == null) {
				return new Pair<Boolean, String>(false, "信息丢失");
			}
			ObjectMapper t = new ObjectMapper();
			t.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			t.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
			BusUplog log = new BusUplog();
			log.setOldCompanyId(bus2.getCompanyId());
			log.setOldBranch(bus2.getBranch());
			String oldjsonString = t.writeValueAsString(bus2);
			log.setAfSerialNumber(bus2.getSerialNumber());
			String jsonString = t.writeValueAsString(bus);
			List<String> r = BeanUtils.copyPropertiesReturnChangeField(bus, bus2);
			bus2.setUpdated(uDate);
			try {
				int a = busMapper.updateByPrimaryKey(bus2);
				if (a > 0) {
				
					log.setCreated(new Date());
					log.setUpdated(uDate);
					log.setCity(cityId);
					log.setUpdator(Request.getUserId(principal));
					log.setBusid(bus.getId());
					log.setJsonString(jsonString);
					log.setOldjsonString(oldjsonString);
					//--
					log.setChangeFileds(r.isEmpty()?StringUtils.EMPTY:r.toString());
					log.setBeSerialNumber(bus.getSerialNumber());
					
					
					busUplogMapper.insert(log);
					return new Pair<Boolean, String>(true, "修改成功");
				}
			} catch (Exception e) {
				if(e  instanceof org.springframework.dao.DuplicateKeyException){
					return new Pair<Boolean, String>(false, "相同的车牌号已经存在!");
				}
			}
		} else {
			bus.setCity(cityId);
			bus.setEnabled(true);
			bus.setCreated(new Date());
			int a = busMapper.insert(bus);
			if (a > 0) {
				return new Pair<Boolean, String>(true, "添加成功");
			}
		}
		return new Pair<Boolean, String>(false, "操作失败");
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
				.getFilter("plid"), companyId = req
						.getFilter("companyId");
		int companyIdCheck=0;
		if (StringUtils.isNotBlank(busid)) {
			int busId = NumberUtils.toInt(busid);
			query = query.and(QJpaBusUpLog.jpaBusUpLog.jpabus.id.eq(busId));
		}
		if (StringUtils.isNotBlank(serinum)) {
		List<Integer> busIdsList = 	busSelectMapper.queryUplog(serinum);
		if(busIdsList!=null && !busIdsList.isEmpty()){
			query = query.and(QJpaBusUpLog.jpaBusUpLog.jpabus.id .in(busIdsList));//.serialNumber.like("%" + serinum + "%"));		
		}else {
			query = query.and(QJpaBusUpLog.jpaBusUpLog.jpabus.id.eq(0));
		}
			//query = query.and(QJpaBusUpLog.jpaBusUpLog.jpabus.serialNumber.like("%" + serinum + "%"));
		}
		if (StringUtils.isNotBlank(pname)) {
			query = query.and(QJpaBusUpLog.jpaBusUpLog.jpabus.plateNumber.like("%" + pname + "%"));
		}
		
		if (StringUtils.isNotBlank(companyId) && (companyIdCheck= NumberUtils.toInt(companyId) )>0) {
			query = query.and(QJpaBusUpLog.jpaBusUpLog.oldCompanyId.eq(companyIdCheck));
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
	@Override
	public Page<JpaLineUpLog> getlineUphistory(int cityId, TableRequest req, int page, int length, Sort sort) {
		if (page < 0)
			page = 0;
		if (length < 1)
			length = 1;
		if (sort == null)
			sort = new Sort("id");
		Pageable p = new PageRequest(page, length, sort);
		BooleanExpression query =QJpaLineUpLog.jpaLineUpLog.city.eq(cityId);
		String linename = req.getFilter("linename");
		if (StringUtils.isNotBlank(linename)) {
			List<Integer> idsIntegers=findLineUpLogByLinename(cityId,linename);
			if(idsIntegers!=null){
				query = query.and(QJpaLineUpLog.jpaLineUpLog.jpabusline.id.in(idsIntegers));
			}
		}
		return query == null ? lineUpdateRepository.findAll(p) : lineUpdateRepository.findAll(query, p);
	}
	/**
	 * 1:先查publid_line 表
	 * 2：根据publish_line_id 查line_online 查的一个订单的所有车id
	 * 3:组装
	 * 4：
	 *
	 * 
	 */
	public DataTablePage<PulishLineView> queryOrders(int cityId, TableRequest req, int page, int length, Sort sort) {

		if (page < 0)
			page = 0;
		if (length < 1)
			length = 1;
		if (sort == null)
			sort = new Sort(Direction.fromString("desc"), "updated"); //new Sort("updated");
		Pageable p = new PageRequest(page, length, sort);

		String becompany = req.getFilter("becompany"),contractid=req.getFilter("contractid"), adcontent=req.getFilter("adcontent"),sktype = req.getFilter("sktype"), contractCode = req
				.getFilter("contractCode"), box = req.getFilter("box");
		BooleanExpression query = QJpaPublishLine.jpaPublishLine.city.eq(cityId);
		query = query.and(QJpaPublishLine.jpaPublishLine.OfflineContract.id.gt(0));
		if (StringUtils.isNotBlank(becompany) && !StringUtils.equals(becompany, "defaultAll")) {
			JpaBusinessCompany company = new JpaBusinessCompany();
			int bid = NumberUtils.toInt(becompany);
			if (bid > 0) {
				company.setId(bid);
				query = query.and(QJpaPublishLine.jpaPublishLine.jpaBusinessCompany.eq(company));
			}
		}
		if (StringUtils.isNotBlank(box) && NumberUtils.toInt(box) > 0) {
			query = query.and(QJpaPublishLine.jpaPublishLine.remainNuber.gt(0));
		}
		if (StringUtils.isNotBlank(contractCode)) {
			query = query.and(QJpaPublishLine.jpaPublishLine.OfflineContract.contractCode
					.like("%" + contractCode + "%"));
		}
		if (StringUtils.isNotBlank(contractid)) {
			int cid=NumberUtils.toInt(contractid);
			query = query.and(QJpaPublishLine.jpaPublishLine.OfflineContract.id.eq(cid));
		}
		if (StringUtils.isNotBlank(adcontent)) {
			query = query.and(QJpaPublishLine.jpaPublishLine.OfflineContract.adcontent
					.like("%" + adcontent + "%"));
		}

		if (StringUtils.isNotBlank(sktype) && !StringUtils.equals(sktype, "defaultAll")) {
			try {
				Sktype u = Sktype.valueOf(sktype);
				query = query.and(QJpaPublishLine.jpaPublishLine.sktype.eq(u));
			} catch (Exception e) {
			}
		}

		Page<JpaPublishLine> list = query == null ? publishLineRepository.findAll(p) : publishLineRepository.findAll(
				query, p);
		List<PulishLineView> r = new ArrayList<PulishLineView>();
		List<JpaPublishLine> db = list.getContent();

		List<Integer/*订单id列表*/> order_id = new ArrayList<Integer>();
		Map<Integer, PulishLineView/*订单id,订单页面数据对象*/> cache = new java.util.HashMap<Integer, PulishLineView>();
		for (JpaPublishLine jpaPublishLine : db) {
			order_id.add(jpaPublishLine.getId());
			PulishLineView w = new PulishLineView(jpaPublishLine);
			r.add(w);
			cache.put(jpaPublishLine.getId(), w);
		}

		if (!order_id.isEmpty()) {
			BooleanExpression sub2 = QJpaBusOnline.jpaBusOnline.city.eq(cityId);
			sub2 = sub2.and(QJpaBusOnline.jpaBusOnline.publish_lineId.in(order_id));
			sub2 = sub2.and(QJpaBusOnline.jpaBusOnline.enable.eq(true));
			
			Sort t2 = new Sort(Direction.fromString("asc"), "startDate");
			Pageable p2 = new PageRequest(0, Integer.MAX_VALUE, t2);
			//排序 以最后一辆装车的广告形式     印制     上刊类型   和上刊时期做为订单的 属性   
			java.lang.Iterable<JpaBusOnline> subList = busOnlineRepository.findAll(sub2, p2);

			for (JpaBusOnline jpaBusOnline : subList) {
				int id = jpaBusOnline.getPublish_lineId();
				if (cache.containsKey(id)) {
					PulishLineView value = cache.get(id);
					value.setOne(jpaBusOnline);
					value.addBusId(jpaBusOnline.getJpabus().getId());
				}
			}

			List<Bus> bus = busSelectMapper.queryOnlineExample(order_id);

			Map<Integer, String/*busid,serialNumber*/> busMap = new HashMap<Integer, String>();
			for (Bus bus2 : bus) {
				busMap.put(bus2.getId(), bus2.getSerialNumber());
			}
			if (busMap.size() > 0) {
				for (PulishLineView pulishLineView : r) {

					List<Integer> order_busList = pulishLineView.getBusId();
					if (order_busList != null) {
						for (Integer integer : order_busList) {
							pulishLineView.appendBusId(busMap.get(integer));
						}
					}

				}
			}
			//-------------------------------
		}
		return new DataTablePage<PulishLineView>(new org.springframework.data.domain.PageImpl<PulishLineView>(r, p,
				list.getTotalElements()), req.getDraw());
	}
	
	
	private List<Integer> findLineUpLogByLinename(int cityId,String linename) {
		BooleanExpression query =QJpaLineUpLog.jpaLineUpLog.city.eq(cityId);
		query = query.and(QJpaLineUpLog.jpaLineUpLog.oldlinename.eq(linename));
		query = query.or(QJpaLineUpLog.jpaLineUpLog.newlinename.eq(linename));
		List<JpaLineUpLog>  list=(List<JpaLineUpLog>) lineUpdateRepository.findAll(query);
		List<Integer> idsIntegers=new ArrayList<Integer>();
		if(list.size()>0){
			for (JpaLineUpLog jpaLineUpLog : list) {
				idsIntegers.add(jpaLineUpLog.getJpabusline().getId());
			}
			return idsIntegers;
		}
		return null;
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

	@Override
	public Pair<Boolean, String> batchOffline(String ids, String offday, Principal principal, int city) throws ParseException {
		Date offday1 = (Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(offday);
		String idsa[] = ids.split(",");
		for (int i = 0; i < idsa.length; i++) {
			if (!idsa[i].trim().equals("")) {
				BusOnline busOnline=busOnlineMapper.selectByPrimaryKey(Integer.parseInt(idsa[i]));
				if(busOnline!=null){
					busOnline.setRealEndDate(offday1); 
					busOnlineMapper.updateByPrimaryKeySelective(busOnline);
					queryBusInfo.updateBusContractCache(busOnline.getBusid());
				}
			}
		}
		return new Pair<Boolean, String>(true, "下刊成功");
	}

	@Override
	public Page<JpaBusOnline> getbusOnlineList(int cityId, TableRequest req, int page, int length, Sort sort) throws ParseException {
		if (page < 0)
			page = 0;
		if (length < 1)
			length = 1;
		if (sort == null)
			sort = new Sort("id");
		Pageable p = new PageRequest(page, length, sort);
		BooleanExpression query = QJpaBusOnline.jpaBusOnline.city.eq(cityId);
		query = query.and(QJpaBusOnline.jpaBusOnline.enable.eq(true));
		String contracCode = req.getFilter("contracCode"),contractid=req.getFilter("contractid"),linename=req.getFilter("linename"),
				serinum=req.getFilter("serinum"),end1=req.getFilter("end1"),end2=req.getFilter("end2"),
				publishLineid=req.getFilter("publishLineid");
		if (StringUtils.isNotBlank(publishLineid)) {
			int pid=NumberUtils.toInt(publishLineid);
			query = query.and(QJpaBusOnline.jpaBusOnline.publish_lineId.eq(pid));
		}
		if (StringUtils.isNotBlank(contracCode)) {
			query = query.and(QJpaBusOnline.jpaBusOnline.offlineContract.contractCode.like("%"+contracCode+"%"));
		}
		if (StringUtils.isNotBlank(contractid)) {
			int cid=NumberUtils.toInt(contractid);
			query = query.and(QJpaBusOnline.jpaBusOnline.offlineContract.id.eq(cid));
		}
		if (StringUtils.isNotBlank(end1)) {
			Date edtDate1=(Date)new SimpleDateFormat("yyyy-MM-dd").parseObject(end1);
			query = query.and(QJpaBusOnline.jpaBusOnline.endDate.after(edtDate1));
		}
		if (StringUtils.isNotBlank(end2)) {
			Date edtDate2=(Date)new SimpleDateFormat("yyyy-MM-dd").parseObject(end2);
			query = query.and(QJpaBusOnline.jpaBusOnline.endDate.before(edtDate2));
		}
		if (StringUtils.isNotBlank(serinum)) {
			query = query.and(QJpaBusOnline.jpaBusOnline.jpabus.serialNumber.like("%"+serinum+"%"));
		}
		if (StringUtils.isNotBlank(linename)) {
			query = query.and(QJpaBusOnline.jpaBusOnline.jpabus.line.name.eq(linename));
		}
		return query == null ? busOnlineRepository.findAll(p) : busOnlineRepository.findAll(query, p);
	}

	public long getMoneyFromBusModel(JpaBusline.Level level, boolean doubleDecker) {
		long r = 0;
		//  S ("特级"),
		//  APP ("A++"),
		// AP ("A+"),
		// A ("A"),
		if (doubleDecker) {
			if (level == JpaBusline.Level.S) {
				r = 170000;
			} else if (level == JpaBusline.Level.APP) {
				r = 150000;
			} else if (level == JpaBusline.Level.AP) {
				r = 120000;
			}
		} else {
			if (level == JpaBusline.Level.S) {
				r = 39000;
			} else if (level == JpaBusline.Level.APP) {
				r = 26000;
			} else if (level == JpaBusline.Level.AP) {
				r = 20000;
			} else if (level == JpaBusline.Level.A) {
				r = 15000;
			}
		}
		return r;

	}

	@Override
	public Pair<Boolean, String> changeDate(String ids, String sday, int days, String eday, Principal principal,
			int city) throws ParseException {
		Date stday = (Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(sday);
		Date endDate=null;
		int kanqi=0;
		if(days>0){
			endDate= DateUtil.dateAdd(stday, days);
			kanqi=days;
		}
		if(StringUtils.isNotBlank(eday)){
			endDate = (Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(eday);
			kanqi=(int) DateUtil.getQuot(endDate,stday);
		}
		String idsa[] = ids.split(",");
		for (int i = 0; i < idsa.length; i++) {
			if (!idsa[i].trim().equals("")) {
				BusOnline busOnline=busOnlineMapper.selectByPrimaryKey(Integer.parseInt(idsa[i]));
				if(busOnline!=null){
					busOnline.setStartDate(stday);
					busOnline.setEndDate(endDate);
					busOnline.setDays(kanqi);
					busOnline.setUpdated(new Date());
					busOnline.setEditor(Request.getUserId(principal));
					busOnlineMapper.updateByPrimaryKeySelective(busOnline);
					queryBusInfo.updateBusContractCache(busOnline.getBusid());
				}
			}
		}
		return new Pair<Boolean, String>(true, "调刊成功");
	}

	@Override
	public CountView ModelCountlist(TableRequest req, Page<JpaPublishLine> jpabuspage) {
		int totalsalsnum=0;//总订购数
		 int totalalrnum=0;//总已上刊数
		CountView view=new CountView();
		List<Integer> ids=new ArrayList<Integer>();
		List<JpaPublishLine> list = jpabuspage.getContent();
		for (JpaPublishLine jpaPublishLine : list) {
			totalsalsnum+=jpaPublishLine.getSalesNumber();
			totalalrnum+=jpaPublishLine.getRemainNuber();
			ids.add(jpaPublishLine.getId());
		}
		Collection<ModelCountView> Views= userAutoCompleteMapper.selectBusModelGroupView(ids);
		view.setViews(Views);
		view.setTotalsalsnum(totalsalsnum);
		view.setTotalalrnum(totalalrnum);
		view.setTotalfree(totalsalsnum-totalalrnum);
		return view;
	}

	@Override
	public List<Modeldesc> findModedesc(String type) {
		ModeldescExample e=new ModeldescExample();
		if(StringUtils.equals(type, "false")){
			e.createCriteria().andTypeEqualTo(0);
		}else{
			e.createCriteria().andTypeEqualTo(1);
		}
		return modeldescMapper.selectByExample(e);
	}

}


