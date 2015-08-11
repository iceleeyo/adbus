package com.pantuo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.pantuo.mybatis.persistence.BusLineMapper;
import com.pantuo.mybatis.persistence.BusSelectMapper;
import com.pantuo.service.BusLineCheckService;
import com.pantuo.vo.GroupVo;
import com.pantuo.web.view.AutoCompleteView;

@Service
public class BusLineCheckServiceImpl implements BusLineCheckService {
	@Autowired
	BusSelectMapper busSelectMapper;
	@Autowired
	BusLineMapper buslineMapper;

	@Override
	public int countByFreeCars(int lineId, Integer modelId, JpaBus.Category category, String start, String end) {

		int busLineCarCount = busSelectMapper.countBusCar(lineId, modelId, category.ordinal(),
				BooleanUtils.toInteger(true));
		int carIds = busSelectMapper.countOnlineCarList(lineId, modelId, category.ordinal(), start, end);
		//总数-被占用数据
		return busLineCarCount - carIds;
	}

	@Autowired
	BuslineRepository buslineRepository;

	@Override
	public List<AutoCompleteView> autoCompleteByName(int city, String name, JpaBus.Category category) {
		List<AutoCompleteView> r = new ArrayList<AutoCompleteView>();
		BooleanExpression query = QJpaBusline.jpaBusline.city.eq(city);
		if (StringUtils.isNotBlank(name)) {
			query = query.and(QJpaBusline.jpaBusline.name.like("%" + name + "%"));
		}
		Pageable p = new PageRequest(1, 30, new Sort("name"));
		List<Integer> idsList = new ArrayList<Integer>();
		Page<JpaBusline> list = buslineRepository.findAll(query, p);
		if (!list.getContent().isEmpty()) {
			for (JpaBusline obj : list.getContent()) {
				idsList.add(obj.getId());
			}
		}
		if (idsList.size() > 0) {
			//query carnumber group by line
			List<GroupVo> vos = busSelectMapper.countCarByLines(idsList, category.ordinal());
			Map<Integer, Integer> cache = getBusLineMap(vos);
			for (JpaBusline obj : list.getContent()) {
				int carNumber = cache.containsKey(obj.getId()) ? cache.get(obj.getId()) : 0;
				r.add(new AutoCompleteView(String.valueOf(obj.getId()), obj.getName() + "  " + obj.getLevelStr() + " ["
						+ carNumber + "]"));
			}
		}
		return r;
	}

	public Map<Integer, Integer> getBusLineMap(List<GroupVo> vos) {
		Map<Integer, Integer> r = new HashMap<Integer, Integer>();
		for (GroupVo groupVo : vos) {
			r.put(groupVo.getGn1(), groupVo.getCount());
		}
		return r;
	}

	@Override
	public List<GroupVo> countCarTypeByLine(int lineId, JpaBus.Category category) {
		return busSelectMapper.countCarTypeByLine(lineId, category.ordinal());
	}
}
