package com.pantuo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.dao.BuslineRepository;
import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.QJpaBusline;
import com.pantuo.dao.pojo.QJpaContract;
import com.pantuo.mybatis.domain.BusLine;
import com.pantuo.mybatis.domain.BusLineExample;
import com.pantuo.mybatis.persistence.BusLineMapper;
import com.pantuo.mybatis.persistence.BusSelectMapper;
import com.pantuo.service.BusLineCheckService;
import com.pantuo.web.view.AutoCompleteView;

@Service
public class BusLineCheckServiceImpl implements BusLineCheckService {
	@Autowired
	BusSelectMapper busSelectMapper;
	@Autowired
	BusLineMapper buslineMapper;

	@Override
	public int countByFreeCars(int lineId, JpaBus.Category category, String start, String end) {

		int busLineCarCount = busSelectMapper.countBusCar(lineId, category.ordinal(), BooleanUtils.toInteger(true));
		int carIds = busSelectMapper.countOnlineCarList(lineId, category.ordinal(), start, end);
		//总数-被占用数据
		return busLineCarCount - carIds;
	}

	@Autowired
	BuslineRepository buslineRepository;

	@Override
	public List<AutoCompleteView> autoCompleteByName(int city, String name) {
		List<AutoCompleteView> r = new ArrayList<AutoCompleteView>();
		BooleanExpression query = QJpaContract.jpaContract.city.eq(city);
		if (StringUtils.isNotBlank(name)) {
			query = query.and(QJpaBusline.jpaBusline.name.like("%" + name + "%"));
		}
		Pageable p = new PageRequest(1, 30, new Sort("name"));
		Page<JpaBusline> list = buslineRepository.findAll(query, p);
		if (!list.getContent().isEmpty()) {
			for (JpaBusline obj : list.getContent()) {
				r.add(new AutoCompleteView(String.valueOf(obj.getId()), obj.getName() + "  " + obj.getLevelStr()));
			}
		}
		return r;
	}

}
