package com.pantuo.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pantuo.dao.CalendarRepository;
import com.pantuo.dao.CityRepository;
import com.pantuo.dao.FunctionRepository;
import com.pantuo.dao.IndustryRepository;
import com.pantuo.dao.SuppliesRepository;
import com.pantuo.dao.pojo.JpaCalendar;
import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.JpaFunction;
import com.pantuo.dao.pojo.JpaIndustry;
import com.pantuo.dao.pojo.JpaTimeslot;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.dao.pojo.UserDetail.UStats;
import com.pantuo.mybatis.domain.ContractId;
import com.pantuo.mybatis.domain.ContractIdExample;
import com.pantuo.mybatis.persistence.ContractIdMapper;
import com.pantuo.util.DateUtil;
import com.pantuo.util.IOTools;

/**
 * 初始化数据
 *
 * @author tliu
 */
@Service
public class DataInitializationService {
	private static Logger log = LoggerFactory.getLogger(DataInitializationService.class);

	public static Map<String, String> _GROUPS = new LinkedHashMap<String, String>();

	public static Map<String, Set<String>> lineSiteMap = new HashMap<String, Set<String>>();

	public static Map<String, Set<String>> siteLineMap = new HashMap<String, Set<String>>();
	
	public static Map<String,AtomicInteger> CONTRACT_ID_MAP =new  HashMap<String, AtomicInteger>();
	//车身权限集合
	public static Set<String> bodyAuthSet = new HashSet<String>();

	public static Map<String,java.util.concurrent.atomic.AtomicInteger> CAR_NUMBER = new HashMap<String,java.util.concurrent.atomic.AtomicInteger>(100000);

	@Autowired
	UserServiceInter userService;

	@Autowired
	TimeslotService timeslotService;


	@Autowired
	IndustryRepository industryRepo;

	@Autowired
	FunctionRepository functionRepository;

	@Autowired
	CityRepository cityRepo;

	@Autowired
	SuppliesRepository suppliesRepository;

	@Autowired
	CalendarRepository calendarRepo;

	@Autowired
	CityService cityService;
	@Autowired
	ScheduleService scheduleService;

	public void intialize() throws Exception {
	//	initContract();
		initContractId();
		initBodyAuthList();
		initializeFunctionFunction();
		initializeLineSite();
		initializeCalendar();
		initializeCities();
		initializeIndustries();
		initializeGroups4AddUser();
		initializeGroups();
		initializeUsers();
		initializeTimeslots();
		//初始增加一条记录
		initializeSupplies();
		scheduleService.initAllBoxMemory();
	}

	private void initializeCalendar() throws Exception {
		long count = calendarRepo.count();
		if (count > 0) {
			log.info("There are already {} days in calendar table, skip initialization step", count);
			return;
		}

		List<JpaCalendar> cals = new ArrayList<JpaCalendar>(200);
		Date to;
		Date curr;

		Calendar cal = DateUtil.newCalendar();
		cal.add(Calendar.YEAR, 5);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DATE, 1);
		to = cal.getTime();

		cal.add(Calendar.YEAR, -10);

		int i = 0;
		int total = 0;
		while (!(curr = cal.getTime()).after(to)) {
			cals.add(new JpaCalendar(curr));
			cal.add(Calendar.DATE, 1);
			i++;

			if (i == 200) {
				total += 200;
				calendarRepo.save(cals);
				i = 0;
				cals.clear();
			}
		}

		if (!cals.isEmpty()) {
			calendarRepo.save(cals);
			total += cals.size();
		}

		log.info("Inserted {} days into calendar table", total);
	}

	//初始化group表
	private void initializeGroups4AddUser() throws Exception {
		InputStream is = DataInitializationService.class.getClassLoader().getResourceAsStream("groups.csv");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		String line = null;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("#"))
				continue;
			try {
				String[] group = line.split(",");
				if (!group[0].trim().startsWith("body"))
					_GROUPS.put(group[0].trim(), group[1].trim());
			} catch (Exception e) {
				log.warn("Fail to parse group for {}, e={}", line, e.getMessage());
			}
		}
		_GROUPS.remove("advertiser");
	}

	//初始化group表
	private void initializeGroups() throws Exception {
		long count = userService.countGroups();
		if (count > 0) {
			log.info("There are already {} groups in table, skip initialization step", count);
			return;
		} else {
			List<Group> groups = userService.getAllGroup();
		}

		InputStream is = DataInitializationService.class.getClassLoader().getResourceAsStream("groups.csv");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		String line = null;
		List<Group> groups = new ArrayList<Group>();

		while ((line = reader.readLine()) != null) {
			if (line.startsWith("#"))
				continue;
			try {
				String[] group = line.split(",");
				log.info("group: id {}, name {}, type {} ", group[0], group[1], group[2]);
				Group g = new GroupEntity(group[0]);
				g.setName(group[1]);
				g.setType(group[2]);
				userService.saveGroup(g);
				if (!group[0].trim().startsWith("body"))
					_GROUPS.put(group[0].trim(), group[1].trim());
				groups.add(g);
			} catch (Exception e) {
				log.warn("Fail to parse group for {}, e={}", line, e.getMessage());
			}
		}

		log.info("Inserted {} group entries into table", groups.size());
	}

	//初始化用户表
	private void initializeUsers() throws Exception {
		long count = userService.count();
		if (count > 0) {
			log.info("There are already {} users in table, skip initialization step", count);
		}

		InputStream is = DataInitializationService.class.getClassLoader().getResourceAsStream("users.csv");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		String line = null;
		List<UserDetail> users = new ArrayList<UserDetail>();

		while ((line = reader.readLine()) != null) {
			if (line.startsWith("#"))
				continue;
			try {
				String[] user = line.split(",");
				String[] groups = user[5].split(":");
				UserDetail ud = new UserDetail(user[0], user[1], user[2], user[3], user[4]);
				ud.setStringGroups(Arrays.asList(groups));
				ud.setUstats(UStats.init);
				if (userService.getByUsername(ud.getUsername()) == null) {
					log.info("Creating user: {}, group: {}", ud.getUsername(), user[5]);
					ud.setIsActivate(1);
					userService.createUser(ud);
				}
				users.add(ud);
			} catch (Exception e) {
				log.warn("Fail to parse user for {}, e={}", line, e.getMessage());
			}
		}

		log.info("Inserted {} user entries into table", users.size());
	}

	//初始化时段表
	private void initializeTimeslots() throws Exception {
		long count = timeslotService.count();
		if (count > 0) {
			log.info("There are already {} timeslot entries in table, skip initialization step", count);
			return;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		InputStream is = DataInitializationService.class.getClassLoader().getResourceAsStream("timeslot.csv");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		String line = null;
		List<JpaTimeslot> timeslots = new ArrayList<JpaTimeslot>();

		while ((line = reader.readLine()) != null) {
			if (line.startsWith("#"))
				continue;
			try {
				String[] time = line.split(",");
				String cityName = time[0];
				JpaCity city = cityService.fromName(cityName, JpaCity.MediaType.screen);
				if (city != null) {
					Date start = sdf.parse(time[1]);
					String name = time[2];
					long duration = sdf.parse(time[3]).getTime() / 1000;
					boolean isPeak = "1".equals(time[4]);
					log.info("timeslot: {}, start: {}, duration {}, peak {}", name, start, duration, isPeak);
					timeslots.add(new JpaTimeslot(city.getId(), name, start, duration, isPeak));
				} else {
					log.warn("No city record found for name {}", cityName);
				}
			} catch (Exception e) {
				log.warn("Fail to parse timeslot for {}, e={}", line, e.getMessage());
			}
		}

		if (!timeslots.isEmpty()) {
			timeslotService.saveTimeslots(timeslots);
		}
		log.info("Inserted {} timeslot entries into table", timeslots.size());
	}

	//线路车辆
	private void initializeLineSite() throws Exception {
		InputStream is = DataInitializationService.class.getClassLoader().getResourceAsStream("line_site.csv");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		String line = null;
		while ((line = reader.readLine()) != null) {
			if (line.length() < 20)
				continue;
			try {
				String[] str = line.split(",");
				String lineName = str[0].replace("\"", "");
				String[] s = str[1].replace("\"", "").split("-");
				Set<String> siteSet = new HashSet<String>(Arrays.asList(s));
				lineSiteMap.put(lineName, siteSet);

				for (String siteName : siteSet) {
					siteName = siteName.trim().replace("\"", "");
					if (!siteLineMap.containsKey(siteName)) {
						siteLineMap.put(siteName, new HashSet<String>());
					}
					siteLineMap.get(siteName).add(lineName);
				}

			} catch (Exception e) {
				log.warn("Fail to parse industry for {}, e={}", line, e.getMessage());
			}
		}
		log.info("init line site lines:{},site:{} ", lineSiteMap.size(), siteLineMap.size());
	}

	private void initBodyAuthList() throws Exception {

		InputStream is = DataInitializationService.class.getClassLoader().getResourceAsStream("group_function.csv");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		String line = null;

		while ((line = reader.readLine()) != null) {
			if (line.startsWith("#"))
				continue;
			try {
				String[] str = line.split(",");
				bodyAuthSet.add(str[3]);
			} catch (Exception e) {
				log.warn("Fail to parse function for {}, e={}", line, e.getMessage());
			}
		}
		IOTools.close(reader);
		IOTools.close(is);
	}

	private void initializeFunctionFunction() throws Exception {
		long count = functionRepository.count();
		if (count > 0) {
			log.info("There are already {} function in table, skip initialization step", count);
			return;
		}
		InputStream is = DataInitializationService.class.getClassLoader().getResourceAsStream("group_function.csv");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		String line = null;
		List<JpaFunction> list = new ArrayList<JpaFunction>();

		while ((line = reader.readLine()) != null) {
			if (line.startsWith("#"))
				continue;
			try {
				String[] str = line.split(",");
				JpaFunction function = new JpaFunction(str[1], str[2], str[3]);
				function.setCity(NumberUtils.toInt(str[0]));
				list.add(function);
			} catch (Exception e) {
				log.warn("Fail to parse function for {}, e={}", line, e.getMessage());
			}
		}
		functionRepository.save(list);

		log.info("Inserted {} function entries into table", list.size());
	}

	//初始化行业表
	private void initializeIndustries() throws Exception {
		long count = industryRepo.count();
		if (count > 0) {
			log.info("There are already {} industries in table, skip initialization step", count);
			return;
		}

		InputStream is = DataInitializationService.class.getClassLoader().getResourceAsStream("industry.csv");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		String line = null;
		List<JpaIndustry> list = new ArrayList<JpaIndustry>();

		while ((line = reader.readLine()) != null) {
			if (line.startsWith("#"))
				continue;
			try {
				String[] str = line.split(",");
				JpaIndustry industry = new JpaIndustry(str[0], str[1]);
				list.add(industry);
			} catch (Exception e) {
				log.warn("Fail to parse industry for {}, e={}", line, e.getMessage());
			}
		}
		industryRepo.save(list);

		log.info("Inserted {} industry entries into table", list.size());
	}

	//初始化素材
	private void initializeSupplies() throws Exception {
		try {
			suppliesRepository.insertDefaultSupplies();
		} catch (Exception e) {
			log.warn("Fail to insertDefaultSupplies id{}, e={}", 1, e.getMessage());
		} finally {
		}
	}

	//初始化城市
	private void initializeCities() throws Exception {
		try {
			long count = cityRepo.count();
			if (count > 0) {
				log.info("There are already {} cities in table, skip initialization step", count);
				return;
			}

			InputStream is = DataInitializationService.class.getClassLoader().getResourceAsStream("city.csv");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String line = null;
			List<JpaCity> list = new ArrayList<JpaCity>();

			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#"))
					continue;
				try {
					String[] c = line.split(",");
					JpaCity city = new JpaCity(c[0], JpaCity.MediaType.valueOf(c[1]));
					list.add(city);
				} catch (Exception e) {
					log.warn("Fail to parse industry for {}, e={}", line, e.getMessage());
				}
			}
			cityRepo.save(list);

			log.info("Inserted {} industry entries into table", list.size());
		} finally {
			cityService.init();
		}
	}

	

	private void initContractId() {
		ContractIdExample example = new ContractIdExample();
		int u = contractIdMapper.countByExample(example);
		if (u == 0) {
			Date date = new Date();
			int y = 1000;
			for (int i = 0; i < y; i++) {
				Date e = DateUtil.dateAdd(date, i);
				try {
					String f = new SimpleDateFormat("yy-MM-dd").format(e);
					ContractId id = new ContractId();
					id.setCount(0);
					id.setDateObj(f);
					contractIdMapper.insert(id);
				} catch (Exception e2) {
				}
			}
		}
		ContractIdExample example2 = new ContractIdExample();
		List<ContractId> list = contractIdMapper.selectByExample(example2);
		for (ContractId contractId : list) {
			CONTRACT_ID_MAP.put(contractId.getDateObj(), new AtomicInteger(contractId.getCount()));
		}

	}
	@Autowired
	ContractIdMapper   contractIdMapper;



	public static void main(String[] args) {
		String s = "北京,05,经纬线,6290,6290,YBL6986,,,,,,,运营车,其他,京G38760,单机,八方达公司,东直门,";
		String[] ss = s.split(",");
		for (String l : ss) {
			System.out.println(l);
		}
	}
}
