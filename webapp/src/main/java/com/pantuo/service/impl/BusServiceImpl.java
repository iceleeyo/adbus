package com.pantuo.service.impl;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import com.pantuo.dao.BusinessCompanyRepository;
import com.pantuo.dao.BuslineRepository;
import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaBusModel;
import com.pantuo.dao.pojo.JpaBusOnline;
import com.pantuo.dao.pojo.JpaBusinessCompany;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.QJpaBus;
import com.pantuo.dao.pojo.QJpaBusModel;
import com.pantuo.dao.pojo.QJpaBusOnline;
import com.pantuo.dao.pojo.QJpaBusinessCompany;
import com.pantuo.dao.pojo.QJpaBusline;
import com.pantuo.mybatis.domain.BusOnline;
import com.pantuo.mybatis.domain.CountableBusLine;
import com.pantuo.mybatis.domain.CountableBusModel;
import com.pantuo.mybatis.domain.CountableBusinessCompany;
import com.pantuo.mybatis.domain.Offlinecontract;
import com.pantuo.mybatis.domain.PublishLine;
import com.pantuo.mybatis.persistence.BusCustomMapper;
import com.pantuo.mybatis.persistence.BusOnlineMapper;
import com.pantuo.mybatis.persistence.OfflinecontractMapper;
import com.pantuo.mybatis.persistence.PublishLineMapper;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.BusService;
import com.pantuo.simulate.QueryBusInfo;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.web.view.BusInfoView;

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
    BuslineRepository lineRepo;
    @Autowired
    BusModelRepository modelRepo;
    @Autowired
    BusinessCompanyRepository companyRepo;
    @Autowired
    BusCustomMapper busCustomMapper;
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
    public long countFree(int city, JpaBusline.Level level, JpaBus.Category category, Integer lineId, Integer busModelId, Integer companyId) {
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
        String plateNumber=req.getFilter("plateNumber"),linename=req.getFilter("linename"),
        		levelStr=req.getFilter("levelStr"),category=req.getFilter("category"),lineid=req.getFilter("lineid"),company=req.getFilter("company");
        if (StringUtils.isNotBlank(plateNumber)) {
            query = query.and(QJpaBus.jpaBus.plateNumber.like("%" + plateNumber + "%"));
        }
        if (StringUtils.isNotBlank(lineid)) {
        	 int lineId=NumberUtils.toInt(lineid);
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
        	JpaBusinessCompany	c= new JpaBusinessCompany();
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
    public Page<JpaBusline> getAllBuslines(int city, JpaBusline.Level level, String name, int page, int pageSize, Sort sort) {
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
    public Page<JpaBusModel> getAllBusModels(int city, String name, String manufacturer, int page, int pageSize, Sort sort) {
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
    public Page<JpaBusinessCompany> getAllBusinessCompanies(int city, String name, String contact, int page, int pageSize, Sort sort) {
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
    public List<CountableBusLine> getBuslines(int city, JpaBusline.Level level,
                                         JpaBus.Category category, Integer lineId,
                                         Integer busModelId, Integer companyId) {
        return busCustomMapper.getBuslines(city, (level == null ? null : level.ordinal()),
                (category == null ? null : category.ordinal()), lineId, busModelId, companyId);
    }

    @Override
    public List<CountableBusModel> getBusModels(int city, JpaBusline.Level level,
                                           JpaBus.Category category, Integer lineId,
                                           Integer busModelId, Integer companyId) {
        return busCustomMapper.getBusModels(city, (level == null ? null : level.ordinal()),
                (category == null ? null : category.ordinal()), lineId, busModelId, companyId);
    }

    @Override
    public List<CountableBusinessCompany> getBusinessCompanies(int city, JpaBusline.Level level,
                                                          JpaBus.Category category, Integer lineId,
                                                          Integer busModelId, Integer companyId) {
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

	@Override
	public Pair<Boolean, String> batchOnline(String ids, String stday, int days, int contractid, Principal principal, int city, int plid) throws ParseException {
		Date startDate=(Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(stday);
		Date endDate=com.pantuo.util.DateUtil.dateAdd(startDate, days);
		String idsa[]=ids.split(",");
		List<Integer> ids2=new ArrayList<Integer>();
		for(int i=0;i<idsa.length;i++){
			if(!idsa[i].trim().equals("")){
				ids2.add(Integer.parseInt(idsa[i]));
			}
		}
		for (Integer integer : ids2) {
			BusOnline busOnline=new BusOnline();
			busOnline.setDays(days);
			busOnline.setStartDate(startDate);
			busOnline.setCreated(new Date());
			busOnline.setUpdated(new Date()); 
			busOnline.setContractid(contractid);
			busOnline.setEnable(true);
			busOnline.setUserid(Request.getUserId(principal));
			busOnline.setEndDate(endDate);
			busOnline.setBusid(integer);
			busOnline.setCity(city);
			busOnlineMapper.insert(busOnline);
			queryBusInfo.updateBusContractCache(integer);
			ascRemainNumber(plid, true);
			
		}
		return new Pair<Boolean, String>(true,"上刊成功");
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
        String busid=req.getFilter("busid");
        if (StringUtils.isNotBlank(busid)) {
        	 int busId=NumberUtils.toInt(busid);
        	query = query.and(QJpaBusOnline.jpaBusOnline.jpabus.id.eq(busId));
        }
        return query == null ? busOnlineRepository.findAll(p) : busOnlineRepository.findAll(query, p);
	}

	@Override
	public BusOnline offlineBusContract(int cityId, int id,int publishLineId ,Principal principal) {
		BusOnline record=	busOnlineMapper.selectByPrimaryKey(id);
		if(record!=null && cityId==record.getCity()){
			record.setEnable(false);
			record.setUpdated(new Date()); 
			record.setEditor(Request.getUserId(principal));
			busOnlineMapper.updateByPrimaryKey(record);
			ascRemainNumber(publishLineId, false);
			queryBusInfo.updateBusContractCache(record.getBusid());
		}
		return record;
	}
	
	public List<JpaBusinessCompany> getAllCompany( int city){
		Page<JpaBusinessCompany> page = getAllBusinessCompanies(city, null, null, 0, 50, new Sort("id"));
		return page.getContent();
	}
	
	
}
