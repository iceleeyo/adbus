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

import com.pantuo.dao.BusModelRepository;
import com.pantuo.dao.BusinessCompanyRepository;
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
import com.pantuo.mybatis.persistence.BusMapper;
import com.pantuo.mybatis.persistence.BusModelMapper;
import com.pantuo.mybatis.persistence.BusOnlineMapper;
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
	BusMapper busMapper;

	@Autowired
	BusService busService;
	@Autowired
	BusinessCompanyRepository companyRepo;

	@Autowired
	BuslineRepository buslineRepo;
	@Autowired
	BusModelRepository busModelRepo;
	@Autowired
	BusModelMapper busModelMapper;

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
	BusOnlineMapper busOnlineMapper;

	@Autowired
	CityService cityService;

	public void intialize() throws Exception {
		initLine();
		initBus();
		initContract();

	}

	private void initContract() throws Exception {
		InputStream is = ContractInitService.class.getClassLoader().getResourceAsStream("buses.csv");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		String line = null;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("#"))
				continue;
			try {
				parseLine(line);
			} catch (Exception e) {
				log.warn("Fail to parse bus for {}, e={}", line, e.getMessage());
			}
		}
		for (Map.Entry<String, AtomicInteger> entry : CAR_NUMBER.entrySet()) {
			if (entry.getValue().get() > 1) {
				System.out.println(entry);
			}

		}
		IOTools.close(reader);
	}

	private void parseLine(String line) {
		String[] b = line.split(",");

		lins.add(b[1]);
		String v = b[3].trim() + "-" + b[14].trim();
		String contractCode = StringUtils.trim(b[6]);
		Integer lineId = LINE_IDMAP.get(StringUtils.trim(b[1]));
		Integer busid = BUS_IDMAP.get(v);

		String company = "";

		if (b.length > 18) {
			company = b[b.length - 1];
		}

		Offlinecontract c = new Offlinecontract();
		if (StringUtils.isNoneBlank(contractCode)) {
			if (!CONTRACT_MAP.containsKey(contractCode)) {
				c.setSeriaNum(ji.incrementAndGet());
				c.setAdcontent(b[7]);
				c.setCity(2);
				c.setCompany(company);
				c.setCompanyAddr(company);
				c.setContractCode(contractCode);
				c.setCreated(new Date());
				c.setUpdated(new Date());
				c.setDays(NumberUtils.toInt(b[10]));//index _   10
				c.setIsSchedule(false);
				c.setCreator("sys");
				c.setSalesman("sys");
				c.setPhoneNum("");
				c.setSignDate(new Date());
				c.setRelateMan("sys");
				c.setAdway("暂无");
				c.setAmounts("0");
				c.setTotalNum(0);
				c.setOtype(0);
				c.setMarkcenter("导入");
				OfflinecontractExample example = new OfflinecontractExample();
				example.createCriteria().andContractCodeEqualTo(contractCode);
				List<Offlinecontract> s = offlinecontractMapper.selectByExample(example);
				if (s.size() == 0) {
					offlinecontractMapper.insert(c);
				} else {
					c = s.get(0);
				}
				CONTRACT_MAP.put(contractCode, c);
			} else {
				c = CONTRACT_MAP.get(contractCode);
			}

			PublishLineExample example = new PublishLineExample();
			example.createCriteria().andContractIdEqualTo(c.getId()).andLineIdEqualTo(lineId);

			List<PublishLine> s = publishLineMapper.selectByExample(example);
			int compandId = 10;
			PublishLine record = new PublishLine();
			if (s.size() == 0) {

				record.setCity(2);
				record.setBatch("1");
				record.setCompanyId(compandId);
				record.setContractId(c.getId());
				record.setCreated(new Date());
				record.setDays(NumberUtils.toInt(b[10]));
				record.setLineId(lineId);
				record.setModelId(0);
				record.setSktype(0);
				record.setStats(0);
				record.setRemainNuber(1);
				record.setSalesNumber(1);
				record.setSeriaNum(c.getSeriaNum());
				record.setUserId("sys");
				record.setCreated(new Date());
				record.setUnitPrice("0");
				record.setLineDesc(b[15]);
				record.setMediaType(b[15]);
				record.setUpdated(new Date());
				publishLineMapper.insert(record);
			} else {
				record = s.get(0);
				record.setRemainNuber(record.getRemainNuber() + 1);
				record.setSalesNumber(record.getSalesNumber() + 1);
				publishLineMapper.updateByPrimaryKey(record);
			}

			BusOnlineExample busOnExample = new BusOnlineExample();
			busOnExample.createCriteria().andPublishLineIdEqualTo(record.getId()).andBusidEqualTo(busid);

			List<BusOnline> view = busOnlineMapper.selectByExample(busOnExample);
			if (view.size() == 0) {

				BusOnline busonline = new BusOnline();
				busonline.setBusid(busid);
				busonline.setAdtype(0);
				busonline.setCity(2);
				busonline.setContractid(c.getId());
				busonline.setCreated(new Date());
				busonline.setUpdated(new Date());
				busonline.setDays(NumberUtils.toInt(b[10]));
				busonline.setEditor("sys");
				busonline.setEnable(true);
				busonline.setPrint(0);
				busonline.setSktype(0);
				busonline.setUserid("sys");
				//busonline.setReserveDate(reserveDate);
				busonline.setPublishLineId(record.getId());
				String start = b[8], end = b[9];
				busonline.setStartDate(p(start));
				busonline.setEndDate(p(end));

				busOnlineMapper.insert(busonline);
			} else {

			}
		}
	}

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

	private void initBus() {
		int fetchsize2 = 100;
		int beginIndex2 = 0;
		while (true) {
			BusExample publishExample = new BusExample();
			publishExample.setOrderByClause("id asc");
			publishExample.setLimitStart(beginIndex2);
			publishExample.setLimitEnd(fetchsize2);
			List<Bus> orders = busMapper.selectByExample(publishExample);
			for (Bus b : orders) {
				String key = b.getSerialNumber() + "-" + b.getPlateNumber();
				BUS_IDMAP.put(key, b.getId());
			}
			beginIndex2 += fetchsize2;
			if (orders.size() < fetchsize2) {
				break;
			}
		}
		System.out.println("init bus :" + BUS_IDMAP.size());
	}

}
