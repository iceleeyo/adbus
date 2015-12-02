package com.pantuo.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import scala.actors.threadpool.Arrays;

import com.pantuo.dao.BusModelRepository;
import com.pantuo.dao.BusinessCompanyRepository;
import com.pantuo.dao.BuslineRepository;
import com.pantuo.dao.CalendarRepository;
import com.pantuo.dao.CityRepository;
import com.pantuo.dao.FunctionRepository;
import com.pantuo.dao.IndustryRepository;
import com.pantuo.dao.ModelDescRepository;
import com.pantuo.dao.SuppliesRepository;
import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaBusModel;
import com.pantuo.dao.pojo.JpaBusinessCompany;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.JpaCalendar;
import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.JpaFunction;
import com.pantuo.dao.pojo.JpaIndustry;
import com.pantuo.dao.pojo.JpaModeldesc;
import com.pantuo.dao.pojo.JpaTimeslot;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.dao.pojo.UserDetail.UStats;
import com.pantuo.mybatis.domain.BusModel;
import com.pantuo.mybatis.domain.BusModelExample;
import com.pantuo.mybatis.persistence.BusModelMapper;
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
	//车身权限集合
	public static Set<String> bodyAuthSet = new HashSet<String>();

	public static Map<String,java.util.concurrent.atomic.AtomicInteger> CAR_NUMBER = new HashMap<String,java.util.concurrent.atomic.AtomicInteger>(100000);

	@Autowired
	UserServiceInter userService;

	@Autowired
	TimeslotService timeslotService;

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
	ModelDescRepository modelDescRepository;

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
		initializeBuses();
		//初始增加一条记录
		initializeSupplies();
		initializeModeldesc();
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
	//初始化车型描述表
	private void initializeModeldesc() throws Exception {
		long count = userService.countModeldesc();
		if (count > 0) {
			log.info("There are already {} modeldesc in table, skip initialization step", count);
			return;
		}
		
		InputStream is = DataInitializationService.class.getClassLoader().getResourceAsStream("modeldesc.csv");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		String line = null;
		List<JpaModeldesc> list = new ArrayList<JpaModeldesc>();

		while ((line = reader.readLine()) != null) {
			if (line.startsWith("#"))
				continue;
			try {
				String[] str = line.split(",");
				JpaModeldesc model = new JpaModeldesc(NumberUtils.toInt(str[0]), str[1]);
				list.add(model);
			} catch (Exception e) {
				log.warn("Fail to parse modeldesc for {}, e={}", line, e.getMessage());
			}
		}
		modelDescRepository.save(list);

		log.info("Inserted {} modeldesc entries into table", list.size());
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
	//初始化公交车表
	private void initializeBuses() throws Exception {
		initDefaultBusModel();

		long count = busService.count();
		if (count > 0) {
			log.info("There are already {} buses entries in table, skip initialization step", count);
			return;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		InputStream is = DataInitializationService.class.getClassLoader().getResourceAsStream("buses.csv");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		String line = null;
		count = 0;
		Set<JpaBus> buses = new HashSet<JpaBus>();
		Map<String, JpaBusline> newLineMap = new HashMap<String, JpaBusline>();
		Map<String, JpaBusModel> newModelMap = new HashMap<String, JpaBusModel>();
		Map<String, JpaBusinessCompany> newCompanyMap = new HashMap<String, JpaBusinessCompany>();

		Map<String, JpaBusline> lineMap = initBuslineMap();
		Map<String, JpaBusModel> modelMap = initBusModelMap();
		Map<String, JpaBusinessCompany> companyMap = initBusinessCompanyMap();

		while ((line = reader.readLine()) != null) {
			if (line.startsWith("#"))
				continue;
			try {
				String[] b = line.split(",");
				String cityName = b[0];
				JpaCity city = cityService.fromName(cityName, JpaCity.MediaType.body);
				if (city != null) {
					JpaBus.Category category = JpaBus.Category.fromNameStr(b[12]);
					if (category == null)
						throw new Exception("No category found for " + b[12]);
					//#城市,线路名称,级别,新车号,旧车号,车型,合同编号,上刊内容,实际上刊时间,预计下刊时间,
					// 刊期,类别,车辆状态,营销中心,车牌号,车辆描述,公司名称,分公司名称,客户名称
					JpaBusline.Level level = JpaBusline.Level.fromNameStr(b[2]);
					if (level == null) {
						level = JpaBusline.Level.LATLONG;
					}
					//throw new Exception("No busline level found for " + b[2]);

					JpaBusModel model = modelMap.get(city.getId() + "/" + b[5]);
					if (model == null) {
						model = newModelMap.get(city.getId() + "/" + b[5]);
						if (model == null) {
							boolean doubleDecker = "双层".equals(b[15]);
							model = new JpaBusModel(city.getId(), b[5], doubleDecker, null, null);
							newModelMap.put(city.getId() + "/" + b[5], model);
						}
					}

					JpaBusinessCompany company = companyMap.get(city.getId() + "/" + b[13]);
					if (company == null) {
						company = newCompanyMap.get(city.getId() + "/" + b[13]);
						if (company == null) {
							company = new JpaBusinessCompany(city.getId(), b[13], null, null, null, null);
							newCompanyMap.put(city.getId() + "/" + b[13], company);
						}
					}
					JpaBusline busline = lineMap.get(city.getId() + "/" + b[1]);
					if (busline == null) {
						busline = newLineMap.get(city.getId() + "/" + b[1]);
						if (busline == null) {
							busline = new JpaBusline(city.getId(), b[1], level);
							busline.setOffice(b[16]);
							busline.setBranch(b[17]);
							busline.setCompany(company);
							newLineMap.put(city.getId() + "/" + b[1], busline);
						}
					}
					Date startdate = new Date();
					Date endDate = new Date();
					if (StringUtils.isNotBlank(b[8]) && StringUtils.isNotBlank(b[9])) {
						SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyy");
						startdate = (Date) f.parse(b[8].toString());
						endDate = (Date) f.parse(b[9].toString());
					}
					JpaBus bus = new JpaBus(city.getId(), busline, category, b[3], b[4], b[14], model, company, b[16],
							b[17], b[8] + "/" + b[9] + "/" + b[7], b[15], null, null);

					//		log.info("bus: {}, category: {}, level: {}, line: {}, model: {}, company: {}",
					//				bus.getPlateNumber(), category, level, busline.getName(), model.getName(),
					//				company.getName());
					buses.add(bus);
				} else {
					log.warn("No city record found for name {} and mediaType body", cityName);
				}
			} catch (Exception e) {
				log.warn("Fail to parse bus for {}, e={}", line, e.getMessage());
			}

			if (!buses.isEmpty() && buses.size() > 1000) {
				if (!newCompanyMap.isEmpty()) {
					companyRepo.save(newCompanyMap.values());
					companyMap.putAll(newCompanyMap);
				}
				if (!newLineMap.isEmpty()) {
					buslineRepo.save(newLineMap.values());
					lineMap.putAll(newLineMap);
				}
				if (!newModelMap.isEmpty()) {
					busModelRepo.save(newModelMap.values());
					modelMap.putAll(newModelMap);
				}
				busService.saveBuses(buses);
				count += buses.size();
				log.info("Inserted {} buses with: {} new lines, {} new models, {} new companies into table",
						buses.size(), newLineMap.size(), newModelMap.size(), newCompanyMap.size());
				buses.clear();
				newLineMap.clear();
				newModelMap.clear();
				newCompanyMap.clear();
			}
		}
		
		//增加尾部处理  最后集合小于1000时 也需要保存
		if (!buses.isEmpty() ) {
			if (!newCompanyMap.isEmpty()) {
				companyRepo.save(newCompanyMap.values());
				companyMap.putAll(newCompanyMap);
			}
			if (!newLineMap.isEmpty()) {
				buslineRepo.save(newLineMap.values());
				lineMap.putAll(newLineMap);
			}
			if (!newModelMap.isEmpty()) {
				busModelRepo.save(newModelMap.values());
				modelMap.putAll(newModelMap);
			}
			busService.saveBuses(buses);
			count += buses.size();
			log.info("Inserted {} buses with: {} new lines, {} new models, {} new companies into table",
					buses.size(), newLineMap.size(), newModelMap.size(), newCompanyMap.size());
			buses.clear();
			newLineMap.clear();
			newModelMap.clear();
			newCompanyMap.clear();
			
		}

		log.info("Inserted {} buses entries into table", count);
	}

	private void initDefaultBusModel() {
		try {
			BusModelExample example = new BusModelExample();
			BusModelExample.Criteria ca = example.createCriteria();
			ca.andNameEqualTo("default_model");
			List<BusModel> r = busModelMapper.selectByExample(example);
			BusModel model = new BusModel();
			model.setId(0);
			model.setCity(1);
			model.setDoubleDecker(true);
			model.setUpdated(new Date());
			model.setCreated(new Date());
			model.setName("default_model");

			if (r.size() == 0) {
				busModelMapper.insert(model);
			}
			model.setId(0);
			busModelMapper.updateByExample(model, example);

			log.info("insert default-bus-model id=0 ");

		} catch (Exception e1) {
			log.error("id0exist", e1);
		}
	}

	private Map<String, JpaBusline> initBuslineMap() {
		Map<String, JpaBusline> lineMap = new HashMap<String, JpaBusline>();
		List<JpaBusline> lines = buslineRepo.findAll();
		for (JpaBusline l : lines) {
			lineMap.put(l.getCity() + "/" + l.getName(), l);
		}
		return lineMap;
	}

	private Map<String, JpaBusModel> initBusModelMap() {
		Map<String, JpaBusModel> modelMap = new HashMap<String, JpaBusModel>();
		List<JpaBusModel> list = busModelRepo.findAll();
		for (JpaBusModel l : list) {
			modelMap.put(l.getCity() + "/" + l.getName(), l);
		}
		return modelMap;
	}

	private Map<String, JpaBusinessCompany> initBusinessCompanyMap() {
		Map<String, JpaBusinessCompany> companyMap = new HashMap<String, JpaBusinessCompany>();
		List<JpaBusinessCompany> list = companyRepo.findAll();
		for (JpaBusinessCompany l : list) {
			companyMap.put(l.getCity() + "/" + l.getName(), l);
		}
		return companyMap;
	}

	public static void main(String[] args) {
		String s = "北京,05,经纬线,6290,6290,YBL6986,,,,,,,运营车,其他,京G38760,单机,八方达公司,东直门,";
		String[] ss = s.split(",");
		for (String l : ss) {
			System.out.println(l);
		}
	}
}
