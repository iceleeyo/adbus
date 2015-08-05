package com.pantuo.service.impl;

import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.dao.BusModelRepository;
import com.pantuo.dao.BusRepository;
import com.pantuo.dao.BusinessCompanyRepository;
import com.pantuo.dao.BuslineRepository;
import com.pantuo.dao.pojo.*;
import com.pantuo.mybatis.domain.*;
import com.pantuo.mybatis.persistence.BusCustomMapper;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.BusService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tliu
 */
@Service
public class BusServiceImpl implements BusService {
    @Autowired
    BusRepository busRepo;
    @Autowired
    BuslineRepository lineRepo;
    @Autowired
    BusModelRepository modelRepo;
    @Autowired
    BusinessCompanyRepository companyRepo;
    @Autowired
    BusCustomMapper busCustomMapper;

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
        		levelStr=req.getFilter("levelStr"),category=req.getFilter("category");
        if (StringUtils.isNotBlank(plateNumber)) {
            query = query.and(QJpaBus.jpaBus.plateNumber.like("%" + plateNumber + "%"));
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
}
