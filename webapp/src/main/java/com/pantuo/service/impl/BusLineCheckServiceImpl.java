package com.pantuo.service.impl;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.dao.BusLockRepository;
import com.pantuo.dao.BuslineRepository;
import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaBusLock;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.QJpaBusLock;
import com.pantuo.dao.pojo.QJpaBusline;
import com.pantuo.mybatis.domain.Bodycontract;
import com.pantuo.mybatis.domain.BusLock;
import com.pantuo.mybatis.domain.BusLockExample;
import com.pantuo.mybatis.persistence.BodycontractMapper;
import com.pantuo.mybatis.persistence.BusLineMapper;
import com.pantuo.mybatis.persistence.BusLockMapper;
import com.pantuo.mybatis.persistence.BusSelectMapper;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.BusLineCheckService;
import com.pantuo.service.MailService;
import com.pantuo.service.MailTask;
import com.pantuo.service.MailTask.Type;
import com.pantuo.simulate.MailJob;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.vo.GroupVo;
import com.pantuo.web.view.AutoCompleteView;

@Service
public class BusLineCheckServiceImpl implements BusLineCheckService {
	@Autowired
	BusSelectMapper busSelectMapper;
	@Autowired
	BusLineMapper buslineMapper;
	@Autowired
	BusLockMapper busLockMapper;
	@Autowired
	BodycontractMapper bodycontractMapper;

	///

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

	private ProcessInstance instance;

	@Autowired
	private MailService mailService;

	@Autowired
	private MailJob mailJob;

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
	@Autowired
	BusLockRepository busLockRepository;

	@Override
	public List<AutoCompleteView> autoCompleteByName(int city, String name, JpaBus.Category category) {
		List<AutoCompleteView> r = new ArrayList<AutoCompleteView>();
		BooleanExpression query = QJpaBusline.jpaBusline.city.eq(city);
		if (StringUtils.isNotBlank(name)) {
			query = query.and(QJpaBusline.jpaBusline.name.like("%" + name + "%"));
		}
		Pageable p = new PageRequest(0, 30, new Sort("name"));
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
				String viewString = obj.getName() + "  " + obj.getLevelStr() + " [" + carNumber + "]";
				r.add(new AutoCompleteView(viewString, viewString, String.valueOf(obj.getId())));//String.valueOf(obj.getId())

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
	public List<JpaBusLock> getBusLockListBySeriNum(long seriaNum) {
		BooleanExpression query = QJpaBusLock.jpaBusLock.seriaNum.eq(seriaNum);
		List<JpaBusLock> list = (List<JpaBusLock>) busLockRepository.findAll(query);
		return list;
	}

	@Override
	public List<GroupVo> countCarTypeByLine(int lineId, JpaBus.Category category) {
		return busSelectMapper.countCarTypeByLine(lineId, category.ordinal());
	}

	@Override
	public Pair<Boolean, String> saveBusLock(BusLock buslock, String startD, String endD) throws ParseException {
		buslock.setEnable(true);
		buslock.setContractId(0);
		buslock.setSalesNumber(buslock.getRemainNuber());
		buslock.setStartDate((Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(startD));
		buslock.setEndDate((Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(endD));
		if (busLockMapper.insert(buslock) > 0) {
			return new Pair<Boolean, String>(true, "保存成功");
		}
		return new Pair<Boolean, String>(false, "保存失败");
	}

	@Override
	public boolean removeBusLock(Principal principal, int city, long seriaNum, int id) {
		BusLockExample example = new BusLockExample();
		BusLockExample.Criteria criteria = example.createCriteria();
		criteria.andCityEqualTo(city);
		criteria.andUserIdEqualTo(Request.getUserId(principal));
		criteria.andSeriaNumEqualTo(seriaNum);
		criteria.andIdEqualTo(id);
		List<BusLock> list = busLockMapper.selectByExample(example);
		if (list.size() > 0) {
			if (busLockMapper.deleteByPrimaryKey(id) > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Pair<Boolean, String> saveBodyContract(Bodycontract bodycontract, long seriaNum, String userId) {
		bodycontract.setCreator(userId);
		int a = bodycontractMapper.insert(bodycontract);
		BusLockExample example = new BusLockExample();
		BusLockExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andSeriaNumEqualTo(seriaNum);
		List<BusLock> list = busLockMapper.selectByExample(example);
		if (list.size() == 0) {
			return new Pair<Boolean, String>(false, "请选择车辆");
		}
		for (BusLock busLock : list) {
			if (busLock != null) {
				if (a > 0) {
					busLock.setContractId(bodycontract.getId());
					busLockMapper.updateByPrimaryKey(busLock);
				} else {
					return new Pair<Boolean, String>(false, "申请合同失败");
				}
			}
		}

		Map<String, Object> initParams = new HashMap<String, Object>();
		initParams.put("username", userId);
		initParams.put(ActivitiService.ORDER_ID, bodycontract.getId());
		initParams.put(ActivitiService.CITY, bodycontract.getCity());
		initParams.put(ActivitiService.CLOSED, false);
		initParams.put(ActivitiService.NOW, new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));
		ProcessInstance process = runtimeService.startProcessInstanceByKey("busFlowV2", initParams);

		List<Task> tasks = taskService.createTaskQuery().processInstanceId(process.getId())
				.processVariableValueEquals(ActivitiService.CITY, bodycontract.getCity()).orderByTaskCreateTime()
				.desc().listPage(0, 1);
		if (!tasks.isEmpty()) {
			Task task = tasks.get(0);
			taskService.claim(task.getId(), userId);
			MailTask mailTask = new MailTask(userId, bodycontract.getId(), null, task.getTaskDefinitionKey(),
					Type.sendCompleteMail);
			taskService.complete(task.getId());
			//	mailJob.putMailTask(mailTask);
		}

		return new Pair<Boolean, String>(true, "申请合同成功");
	}
}
