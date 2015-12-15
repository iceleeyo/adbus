package com.pantuo.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pantuo.dao.BuslineRepository;
import com.pantuo.dao.CalendarRepository;
import com.pantuo.dao.CityRepository;
import com.pantuo.dao.FunctionRepository;
import com.pantuo.dao.IndustryRepository;
import com.pantuo.dao.SuppliesRepository;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.mybatis.domain.Bus;
import com.pantuo.mybatis.domain.BusExample;
import com.pantuo.mybatis.domain.BusOnline;
import com.pantuo.mybatis.domain.BusOnlineExample;
import com.pantuo.mybatis.domain.Offlinecontract;
import com.pantuo.mybatis.domain.OfflinecontractExample;
import com.pantuo.mybatis.domain.PublishLine;
import com.pantuo.mybatis.domain.PublishLineExample;
import com.pantuo.mybatis.persistence.OfflinecontractMapper;
import com.pantuo.mybatis.persistence.PublishLineMapper;
import com.pantuo.util.IOTools;

/**
 * 初始化合同数据
 *
 * @author tliu
 */
@Service
public class ContractInitService {
	private static Logger log = LoggerFactory.getLogger(ContractInitService.class);

	public static Map<String, java.util.concurrent.atomic.AtomicInteger> CAR_NUMBER = new HashMap<String, java.util.concurrent.atomic.AtomicInteger>(
			100000);

	public static Map<String, Integer> BUS_IDMAP = new HashMap<String, Integer>(20000 * 8);
	public static Map<String, Integer> LINE_IDMAP = new HashMap<String, Integer>();
	public static Set<String> lins = new HashSet<String>();
	public static Map<String, Offlinecontract> CONTRACT_MAP = new HashMap<String, Offlinecontract>();
	public static AtomicLong ji = new AtomicLong(System.currentTimeMillis());

	@Autowired
	OfflinecontractMapper offlinecontractMapper;
	@Autowired
	UserServiceInter userService;

	@Autowired
	TimeslotService timeslotService;

	@Autowired
	BusService busService;

	@Autowired
	BuslineRepository buslineRepo;

	@Autowired
	IndustryRepository industryRepo;

	@Autowired
	FunctionRepository functionRepository;
	@Autowired
	PublishLineMapper publishLineMapper;

	@Autowired
	CityRepository cityRepo;

	@Autowired
	SuppliesRepository suppliesRepository;

	@Autowired
	CalendarRepository calendarRepo;


	@Autowired
	CityService cityService;



	private static Date p(String str) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			Date s_date = (Date) sdf.parse(str);
			return s_date;
		} catch (Exception e) {
		}
		return null;
	}

	private void initLine() {
		List<JpaBusline> jpaLines = null;
		jpaLines = buslineRepo.findAll();
		for (JpaBusline jpaBusline : jpaLines) {
			LINE_IDMAP.put(jpaBusline.getName(), jpaBusline.getId());
		}
		System.out.println("init lines :" + LINE_IDMAP.size());
	}


}
