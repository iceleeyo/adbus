package com.pantuo.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.dao.BusModelRepository;
import com.pantuo.dao.BuslineRepository;
import com.pantuo.dao.pojo.JpaBusModel;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.QJpaBusline;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.BusMapService;
import com.pantuo.service.DataInitializationService;
import com.pantuo.simulate.LineCarsCount;
import com.pantuo.simulate.LineOnlineCount;
import com.pantuo.util.HttpTookit;
import com.pantuo.util.Pair;
import com.pantuo.web.view.JpaBuslineView;
import com.pantuo.web.view.MapLocationSession;

/**
 * 
 * <b><code>BusMapServiceImpl</code></b>
 * <p>
 * 百度地图查询 ,根据一个位置查附近的公交线路 再匹配我们库中的线路
 * </p>
 * <b>Creation Time:</b> 2015年8月22日 上午9:56:38
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
@Service
public class BusMapServiceImpl implements BusMapService {
	private static Logger log = LoggerFactory.getLogger(BusLineCheckServiceImpl.class);

	Map<String, Integer> LINE_CACHE = new HashMap<String, Integer>();
	@Autowired
	BuslineRepository lineRepo;
	@Autowired
	BusModelRepository modelRepo;

	@Autowired
	LineCarsCount lineCarsCount;

	@Autowired
	LineOnlineCount lineOnlineCount;

	public Pair<Double, Double> getLocationFromAddress(Model model, String address) {
		String city = StringUtils.substring(address, 0, 3);
		if (model != null) {
			model.addAttribute("city", city);
		}
		return queryDeviceProvinceId(address, city);
	}

	public Pair<Double, Double> queryDeviceProvinceId(String deviceid, String city) {
		long now = System.currentTimeMillis();
		Pair<Double, Double> r = null;
		try {
			String t = null;
			t = HttpTookit.doGet("http://api.map.baidu.com/telematics/v3/geocoding", String.format(
					"?keyWord=%s&cityName=%s&out_coord_type=gcj02&ak=Ok6Nri8q5UjAa0anpoGv7R3o&output=json", deviceid,
					city), "UTF-8", 2350, 2350);
			if (StringUtils.isNotBlank(t)) {
				ObjectMapper objectMapper = new ObjectMapper();
				Map<String, Object> jsonMap = objectMapper.readValue(t, Map.class);
				if (jsonMap != null) {
					if ("Success".equals(jsonMap.get("status"))) {
						Map<String, Object> dateNode = (Map<String, Object>) jsonMap.get("results");
						Map<String, Object> subMap = (Map<String, Object>) dateNode.get("location");
						if (subMap.get("lng") instanceof Double) {
							Map<String, Double> dMap = (Map<String, Double>) dateNode.get("location");
							return new Pair<Double, Double>(dMap.get("lng"), dMap.get("lat"));
						}
					}
				} else {
					log.info(deviceid + " requestUrl r is empty :" + t);
				}

			} else {
				log.info(deviceid + " requestUrl r is null " + t);
			}
		} catch (Exception e) {
			log.error("error", e);
		}
		log.info(deviceid + " requestUrl  time: " + (System.currentTimeMillis() - now));
		return r;
	}

	public Set<String> queryLineByGps(Pair<Double, Double> pair) {
		long now = System.currentTimeMillis();
		Set<String> r = new HashSet<String>();
		try {
			String t = null;
			t = HttpTookit.doGet(
					"http://api.map.baidu.com/telematics/v3/local",
					String.format("?location=%s,%s&keyWord=%s&ak=Ok6Nri8q5UjAa0anpoGv7R3o&output=json",
							String.valueOf(pair.getLeft()), String.valueOf(pair.getRight()), "公交线路"), "UTF-8", 3050,
					3050);
			if (StringUtils.isNotBlank(t)) {
				ObjectMapper objectMapper = new ObjectMapper();
				Map<String, Object> jsonMap = objectMapper.readValue(t, Map.class);
				if (jsonMap != null) {
					if ("Success".equals(jsonMap.get("status"))) {
						List<Map<String, Object>> dateNode = (List<Map<String, Object>>) jsonMap.get("pointList");
						for (Map<String, Object> map : dateNode) {
							String line = (String) map.get("address");
							String[] lines = line.split(";");
							for (String string : lines) {
								r.add(StringUtils.replace(string, "路", "").replace("线", "").replace("路快车", "")
										.replace("夜", "").replace("运通", ""));
							}
						}
					}
				} else {
					log.info(pair + " requestUrl r is empty :" + t);
				}

			} else {
				log.info(pair + " requestUrl r is null " + t);
			}
		} catch (Exception e) {
			log.error("error", e);
		}
		log.info(pair + " requestUrl  time: " + (System.currentTimeMillis() - now));
		return r;
	}

	@Override
	public Page<JpaBusline> querySiteLineSearch(Model model, int city, String address, int page, int pageSize, Sort sort) {

		Set<String> lineSet = new HashSet<String>();
		List<Map.Entry<String, Integer>> subList = null;
		if (DataInitializationService.lineSiteMap.containsKey(address)) {

			Set<String> site = DataInitializationService.lineSiteMap.get(address);

			Map<String, Integer> lineCount = new HashMap<String, Integer>();
			Map<String, List<String>> lineCount2 = new HashMap<String, List<String>>();
			for (String string : site) {
				Set<String> lines = DataInitializationService.siteLineMap.get(string);
				if (lines != null) {
					for (String line : lines) {
						if (!lineCount.containsKey(line)) {
							lineCount.put(line, 0);
						}
						lineCount.put(line, lineCount.get(line) + 1);

						if (!lineCount2.containsKey(line)) {
							lineCount2.put(line, new ArrayList<String>());
						}
						lineCount2.get(line).add(string);
					}
				}
			}

			List<Map.Entry<String, Integer>> list_Data = new ArrayList<Map.Entry<String, Integer>>(lineCount.entrySet());
			Collections.sort(list_Data, new Comparator<Map.Entry<String, Integer>>() {
				public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
					if (o2.getValue() != null && o1.getValue() != null && o2.getValue().compareTo(o1.getValue()) > 0) {
						return 1;
					} else {
						return -1;
					}
				}
			});

			if (!list_Data.isEmpty()) {
				subList = list_Data.subList(0, list_Data.size() > 50 ? 50 : list_Data.size());
				for (Map.Entry<String, Integer> entry : subList) {
					lineSet.add(entry.getKey());
				}
			}
		}

		if (LINE_CACHE.size() == 0) {
			List<JpaBusline> all = lineRepo.findAll();
			for (JpaBusline jpaBusline : all) {
				LINE_CACHE.put(jpaBusline.getName().replace("路", "").replace("线", ""), jpaBusline.getId());
			}
		}
		model.addAttribute("_mapLocationKey", MapLocationSession.EMPTY);
		Pageable p = new PageRequest(page, pageSize, sort);//new Sort("field(id, 1,111,111)")
		BooleanExpression query = null;
		if (lineSet.size() > 0) {
			List<Integer> ids = new ArrayList<Integer>();
			for (String integer : lineSet) {
				if (LINE_CACHE.containsKey(integer)) {
					ids.add(LINE_CACHE.get(integer));
				}
			}
			if (ids.size() > 0) {
				query = QJpaBusline.jpaBusline.id.in(ids);
			} else {
				query = QJpaBusline.jpaBusline.id.loe(0);
			}
			if (!ids.isEmpty()) {
				model.addAttribute("_mapLocationKey", null);
			}
		} else {//如果匹配不到
			query = QJpaBusline.jpaBusline.id.loe(0);
		}
		Page<JpaBusline> w = lineRepo.findAll(query, p);
		//对数据库结果进行匹配
		if (!w.getContent().isEmpty()) {
			Map<String, JpaBusline> b = new HashMap<String, JpaBusline>();
			for (JpaBusline jpaBusline : w.getContent()) {
				b.put(jpaBusline.getName(), jpaBusline);
			}
			List<JpaBusline> r = new ArrayList<JpaBusline>();
			for (Map.Entry<String, Integer> entry : subList) {
				if (b.containsKey(entry.getKey())) {
					JpaBusline jpaBusline = b.get(entry.getKey());
					jpaBusline.set_sim(entry.getValue());
					r.add(jpaBusline);
				}
			}
			return new org.springframework.data.domain.PageImpl<JpaBusline>(r, p, r.size());
		}

		//Collections.sort(w.getContent());
		return w;
	}

	@Override
	public Page<JpaBusline> getAllBuslines(Model model, int city, String address, int page, int pageSize, Sort sort) {
		Pair<Double, Double> pair = getLocationFromAddress(model, address);
		Set<String> lineSet = queryLineByGps(pair);

		if (LINE_CACHE.size() == 0) {
			List<JpaBusline> all = lineRepo.findAll();
			for (JpaBusline jpaBusline : all) {
				LINE_CACHE.put(jpaBusline.getName().replace("路", "").replace("线", ""), jpaBusline.getId());
			}
		}
		model.addAttribute("_mapLocationKey", MapLocationSession.EMPTY);
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = null;
		if (lineSet.size() > 0) {
			List<Integer> ids = new ArrayList<Integer>();
			for (String integer : lineSet) {
				if (LINE_CACHE.containsKey(integer)) {
					ids.add(LINE_CACHE.get(integer));
				}
			}
			query = QJpaBusline.jpaBusline.id.in(ids);
			if (!ids.isEmpty()) {
				model.addAttribute("_mapLocationKey", new MapLocationSession(pair, (String) model.asMap().get("city"),
						address));
			}
		} else {//如果匹配不到
			query = QJpaBusline.jpaBusline.id.loe(0);
		}
		Page<JpaBusline> w = lineRepo.findAll(query, p);
		//Collections.sort(w.getContent());
		return w;
	}

	public Page<JpaBuslineView> putLineCarToPageView(TableRequest req, Page<JpaBusline> page) {
		List<JpaBusline> list = page.getContent();
		List<JpaBuslineView> r = new ArrayList<JpaBuslineView>(list.size());
		for (JpaBusline jpaBusline : list) {
			jpaBusline.set_cars(lineCarsCount.getCarsByLines(jpaBusline.getId()));
			JpaBuslineView view = new JpaBuslineView();
			view.setCountObj(lineOnlineCount.getCarsByLines(jpaBusline.getId()));
			view.setLine(jpaBusline);
			r.add(view);
		}
		Pageable p = new PageRequest(req.getPage(), req.getLength(), page.getSort());
		return new org.springframework.data.domain.PageImpl<JpaBuslineView>(r, p, page.getTotalElements());
	}

	@Override
	public JpaBusline findLineById(int id) {
		return lineRepo.findOne(id);
	}

	@Override
	public JpaBusModel findModelById(int id) {
		return modelRepo.findOne(id);
	}
}
