package com.pantuo.web;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.dao.pojo.JapDividPay;
import com.pantuo.dao.pojo.JpaBodyContract;
import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaBusLock;
import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.JpaOfflineContract;
import com.pantuo.dao.pojo.JpaPublishLine;
import com.pantuo.mybatis.domain.Bodycontract;
import com.pantuo.mybatis.domain.BusLine;
import com.pantuo.mybatis.domain.BusLock;
import com.pantuo.mybatis.domain.BusModel;
import com.pantuo.mybatis.domain.BusinessCompany;
import com.pantuo.mybatis.domain.Contract;
import com.pantuo.mybatis.domain.Dividpay;
import com.pantuo.mybatis.domain.Offlinecontract;
import com.pantuo.mybatis.domain.PublishLine;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.HistoricTaskView;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.ActivitiService.TaskQueryType;
import com.pantuo.service.BusLineCheckService;
import com.pantuo.service.ContractService;
import com.pantuo.service.UserServiceInter;
import com.pantuo.util.BusinessException;
import com.pantuo.util.DateUtil;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Only1ServieUniqLong;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.vo.GroupVo;
import com.pantuo.web.view.AutoCompleteView;
import com.pantuo.web.view.BusModelGroupView;
import com.pantuo.web.view.LineBusCpd;
import com.pantuo.web.view.OrderView;

/**
 * 
 * <b><code>BusSelectController</code></b>
 * <p>
 * 车身库存检查
 * </p>
 * <b>Creation Time:</b> 2015年8月11日 下午1:31:41
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
@Controller
@RequestMapping("/busselect")
public class BusSelectController {

	@Autowired
	BusLineCheckService busLineCheckService;

	@Autowired
	ActivitiService activitiService;
	@Autowired
	HistoryService historyService;
	@Autowired
	private UserServiceInter userService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private ContractService contractService;

	@RequestMapping(value = "/myTask/{pageNum}")
	public String list() {
		return "bodyTaskList";
	}

	/**
	 * 
	 * 我的订单和我参与的订单 共用datatables
	 *
	 * @param req
	 * @param principal
	 * @param city
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping("ajax-orderlist")
	@ResponseBody
	public DataTablePage<OrderView> getAllContracts(TableRequest req, Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		Page<OrderView> w = busLineCheckService.getBodyContractList(city, req, principal);
		return new DataTablePage(w, req.getDraw());
	}

	/**
	 * 
	 * 我参与的订单
	 *
	 * @param model
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "/join/{pageNum}")
	public String joinOrder(Model model) {
		model.addAttribute("orderMenu", "我参与订单");
		return "myBodyOrders";
	}

	/**
	 * 
	 * 我的订单
	 *
	 * @param model
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "/myOrders/{pageNum}")
	public String myOrders(Model model) {
		model.addAttribute("orderMenu", "我的订单");
		return "myBodyOrders";
	}

	@RequestMapping(value = "/body_handleView", produces = "text/html;charset=utf-8")
	public String HandleView2(Model model, @RequestParam(value = "taskid", required = true) String taskid,
			Principal principal, @CookieValue(value = "city", defaultValue = "-1") int cityId,
			@ModelAttribute("city") JpaCity city, HttpServletRequest request) throws Exception {
		NumberPageUtil page = new NumberPageUtil(9999, 1, 9999);
		Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
		HistoricTaskInstance currTask = historyService.createHistoricTaskInstanceQuery().taskId(taskid).singleResult();
		Date claimT = currTask.getClaimTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String claimTime = sdf.format(claimT);
		ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery()
				.executionId(task.getExecutionId()).processInstanceId(task.getProcessInstanceId()).singleResult();
		OrderView v = activitiService.findBodyContractByTaskId(taskid, principal);
		String activityId = executionEntity.getActivityId();
		ProcessInstance pe = activitiService.findProcessInstanceByTaskId(taskid);
		List<HistoricTaskView> activitis = activitiService.findHistoricUserTask(cityId, pe.getProcessInstanceId(),
				activityId);
		List<Contract> contracts = contractService.querybodyContractList(cityId);
		model.addAttribute("contracts", contracts);
		model.addAttribute("taskid", taskid);
		model.addAttribute("orderview", v);
		model.addAttribute("bodycontract", busLineCheckService.selectBcById(v.getJpaBodyContract().getId()));
		model.addAttribute("claimTime", claimTime);
		model.addAttribute("activitis", activitis);
		model.addAttribute("activityId", activityId);
		return "body_handleView";
	}

	@RequestMapping("bodyContractDetail/{contractid}")
	public String JpaBodyContract(Model model, @PathVariable("contractid") int contractid,
			@CookieValue(value = "city", defaultValue = "-1") int city, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		JpaBodyContract bodycontract = busLineCheckService.selectBcById(contractid);
		model.addAttribute("bodycontract", bodycontract);
		return "template/bodyBlank_contratDetail";
	}

	@RequestMapping("ajax-myOrders")
	@ResponseBody
	public DataTablePage<OrderView> myOrders(TableRequest req, Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		Page<OrderView> w = busLineCheckService.queryOrders(city, (principal), req, TaskQueryType.my, null);
		return new DataTablePage<OrderView>(w, req.getDraw());
	}

	@RequestMapping(value = "/body_allRuningOrders")
	public String allRuningOrders(Model model, HttpServletRequest request) {
		model.addAttribute("orderMenu", "进行中的订单");
		return "body_allRuningOrders";
	}

	@RequestMapping(value = "/public_bodyContracts")
	public String body_contracts(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "_city", required = true, defaultValue = "2") int _city) {
		ControllerSupport.bcity(response, _city);
		model.addAttribute("orderMenu", "合同列表");
		if (StringUtils.equals(request.getParameter("from"), "pc")) {
			return "body_contracts_PC";
		} else {
			return "body_contracts";
		}
	}

	@RequestMapping("ajax-body-runningAjax")
	@ResponseBody
	public DataTablePage<OrderView> runningAjax(TableRequest req, Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		Page<OrderView> w = busLineCheckService.queryOrders(city, principal, req, TaskQueryType.all_running, null);
		return new DataTablePage<OrderView>(w, req.getDraw());
	}

	@RequestMapping("public_ajax-bodycontracts")
	@ResponseBody
	public DataTablePage<OrderView> ajaxbodycontracts(TableRequest req, Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		Page<OrderView> w = busLineCheckService.queryOrders(city, principal, req, TaskQueryType.all_running, "work");
		return new DataTablePage<OrderView>(w, req.getDraw());
	}

	@RequestMapping("setLockDate/{id}")
	@ResponseBody
	public Pair<Boolean, String> lockDate(Principal principal, @PathVariable("id") int id,
			@RequestParam(value = "lockDate") String lockDate) throws ParseException {
		return busLineCheckService.setLockDate(lockDate, id, principal);
	}

	/**
	 * 
	 * 线路自动补全
	 *
	 * @param city
	 * @param name
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "/autoComplete")
	@ResponseBody
	public List<AutoCompleteView> autoCompleteByName(@CookieValue(value = "city", defaultValue = "-1") int city,
			@RequestParam(value = "term") String name ,@RequestParam(value = "tag", required = false ) String tag) {
		return busLineCheckService.autoCompleteByName(city, name, JpaBus.Category.yunyingche,tag);
	}
	@RequestMapping(value = "/contractAutoComplete")
	@ResponseBody
	public List<AutoCompleteView> contractAutoComplete(@CookieValue(value = "city", defaultValue = "-1") int city,
			@RequestParam(value = "term") String name ) {
		return busLineCheckService.ContractAutoCompleteByName(city, name);
	}

	/**
	 *
	 * 线路车辆汇总统计 适用下拉选择列表
	 *
	 * @param buslinId
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "/ajax-company")
	@ResponseBody
	public List<BusinessCompany> queryLineCompanyByModelid(
			@RequestParam(value = "buslinId", required = true, defaultValue = "0") Integer buslinId,int modelid) {
		return busLineCheckService.queryLineCompanyByModelid(buslinId,modelid);
	}
	/**
	 *
	 * 线路车辆汇总统计 适用下拉选择列表
	 *
	 * @param buslinId
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "/ajax-des")
	@ResponseBody
	public List<String> queryLineDesByModelid(
			@RequestParam(value = "buslinId", required = true, defaultValue = "0") Integer buslinId,int modelid) {
		return busLineCheckService.queryLineDesByModelid(buslinId, modelid);
	}
	
	/**
	 *
	 * 线路车辆汇总统计 适用下拉选择列表
	 *
	 * @param buslinId
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "/selectBusType")
	@ResponseBody
	public List<GroupVo> selectBusType(
			@RequestParam(value = "buslinId", required = true, defaultValue = "0") Integer buslinId) {
		return busLineCheckService.countCarTypeByLine(buslinId, JpaBus.Category.yunyingche);
	}

	/**
	 * 
	 * 工作流测试
	 *
	 * @param model
	 * @param principal
	 * @param request
	 * @param response
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "/busFlow")
	public String busFlow(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		activitiService.startTest();
		return null;
	}

	/**
	 * 
	 * 按线路,车辆类型,上刊时间,下刊时间 查询库存
	 *
	 * @param buslinId
	 * @param modelId
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException 
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "/lineReaminCheck")
	@ResponseBody
	public int lineReaminCheck(@RequestParam(value = "buslinId", required = true, defaultValue = "0") Integer buslinId,
			@RequestParam(value = "modelId", required = true, defaultValue = "0") Integer modelId,
			@RequestParam(value = "start", required = true) String start,
			@RequestParam(value = "end", required = false) String end,@RequestParam(value = "days", required = false) int days) throws ParseException {
		//187 2015-08-07 2015-08-17
		if(days>0){
			Date sDate=(Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(start);
			Date end2=DateUtil.dateAdd(sDate, days);
			end=end2.toString();
		}
		return busLineCheckService.countByFreeCars(buslinId, modelId, JpaBus.Category.yunyingche, start, end);
	}

	/**
	 * 
	 * 合同部 检查库存
	 *
	 * @param buslockid
	 * @param seriaNum
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "/checkStock")
	@ResponseBody
	public Pair<Boolean, String> checkStock(
			@RequestParam(value = "buslockid", required = true, defaultValue = "0") Integer buslockid,
			@RequestParam(value = "seriaNum", required = true) long seriaNum) {
		JpaBusLock busLock = busLineCheckService.findBusLockById(buslockid);
		if (busLock != null) {
			String st = new SimpleDateFormat("yyyy-MM-dd").format(busLock.getStartDate());
			String end = new SimpleDateFormat("yyyy-MM-dd").format(busLock.getEndDate());
			int a = busLineCheckService.countByFreeCars(busLock.getLine().getId(), busLock.getModel().getId(),
					JpaBus.Category.yunyingche, st, end);
			if (busLock.getRemainNuber() > a) {
				return new Pair<Boolean, String>(false, "库存不足");
			} else {
				return new Pair<Boolean, String>(true, "库存充足");
			}
		}
		return new Pair<Boolean, String>(false, "信息丢失");
	}

	/**
	 * 
	 * 选车
	 *
	 * @param buslock
	 * @param city
	 * @param principal
	 * @param request
	 * @param seriaNum
	 * @param startD
	 * @param endD
	 * @return
	 * @throws ParseException
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "/saveBusLock")
	@ResponseBody
	public Pair<Boolean, String> saveBusLock(BusLock buslock,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			HttpServletRequest request, @RequestParam(value = "seriaNum", required = true) long seriaNum,
			@RequestParam(value = "startD", required = true) String startD,
			@RequestParam(value = "endD", required = true) String endD) throws ParseException {
		buslock.setCity(city);
		buslock.setUserId(Request.getUserId(principal));
		buslock.setSeriaNum(seriaNum);
		return busLineCheckService.saveBusLock(buslock, startD, endD);
	}
	@RequestMapping(value = "/savePublishLine")
	@ResponseBody
	public Pair<Boolean, String> savePublishLine(PublishLine publishLine,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			HttpServletRequest request, @RequestParam(value = "seriaNum", required = true) long seriaNum,
			@RequestParam(value = "startD", required = false) String startD
			) throws ParseException {
		publishLine.setCity(city);
		publishLine.setUserId(Request.getUserId(principal));
		publishLine.setSeriaNum(seriaNum);
		return busLineCheckService.savePublishLine(publishLine, startD);
	}
	@RequestMapping(value = "/savePublishLine2")
	@ResponseBody
	public Pair<Boolean, String> savePublishLine2(PublishLine publishLine,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			HttpServletRequest request,
			@RequestParam(value = "obj", required = false) String obj
			) throws JsonProcessingException, IOException {
		return busLineCheckService.savePublishLine2(publishLine,obj,principal,city);
	}

	/**
	 * 
	 * 增加意向合同,下单
	 *
	 * @param bodycontract
	 * @param city
	 * @param principal
	 * @param request
	 * @param seriaNum
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "/saveBodyContract")
	@ResponseBody
	public Pair<Boolean, String> saveBodyContract(Bodycontract bodycontract,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			HttpServletRequest request, @RequestParam(value = "seriaNum", required = true) long seriaNum) {
		bodycontract.setCity(city);
		return busLineCheckService.saveBodyContract(bodycontract, seriaNum, Request.getUserId(principal));
	}
	@RequestMapping(value = "/saveOffContract")
	@ResponseBody
	public Pair<Boolean, String> saveOffContract(Offlinecontract offcontract,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			@RequestParam(value = "signDate1",  required =false) String signDate1,
			@RequestParam(value = "otype1",  required =false) String otype1,
			HttpServletRequest request, @RequestParam(value = "seriaNum", required = true) long seriaNum) throws ParseException {
		offcontract.setCity(city);
		return busLineCheckService.saveOffContract(offcontract, seriaNum, Request.getUserId(principal),signDate1,otype1);
	}
	@RequestMapping(value = "/saveDivid")
	@ResponseBody
	public Pair<Boolean, String> saveDivid(Dividpay dividpay,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			@RequestParam(value = "payDate1") String payDate1,
			HttpServletRequest request, @RequestParam(value = "seriaNum", required = false) long seriaNum) throws ParseException {
		dividpay.setCity(city);
		return busLineCheckService.saveDivid(dividpay, seriaNum, Request.getUserId(principal),payDate1);
	}
	@RequestMapping(value = "/saveLine")
	@ResponseBody
	public Pair<Boolean, String> saveLine(BusLine busLine,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			@RequestParam(value="updated1", required = false) String updated1,
			HttpServletRequest request) throws ParseException, JsonGenerationException, JsonMappingException, IOException, ParseException {
		busLine.setCity(city);
		return busLineCheckService.saveLine(busLine, updated1,city, principal,request);
	}
	@RequestMapping(value = "/saveBusModel")
	@ResponseBody
	public Pair<Boolean, String> saveBusModel(BusModel busmodel,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			HttpServletRequest request) throws ParseException, JsonGenerationException, JsonMappingException, IOException {
		busmodel.setCity(city);
		return busLineCheckService.saveBusModel(busmodel, city, principal,request);
	}

	/**
	 * 
	 * 施工单页面
	 *
	 * @param model
	 * @param line
	 * @param modelId
	 * @param principal
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping("workList/{contractId}")
	public String workList(Model model, @PathVariable("contractId") int contractId,
			@RequestParam(value = "modelId", required = true, defaultValue = "0") int modelId, Principal principal,
			HttpServletRequest request) {
		List<JpaBusLock> lockList = busLineCheckService.getBusLockListByBid(contractId);
		model.addAttribute("lockList", lockList);
		if (!lockList.isEmpty()) {
			JpaBusLock obj = lockList.get(0);
			model.addAttribute("lineId", obj.getLine().getId());
			model.addAttribute("modelId", obj.getModel().getId());
		} else {
			model.addAttribute("lineId", 0);
			model.addAttribute("modelId", 0);
		}
		model.addAttribute("id", contractId);
		if (StringUtils.equals(request.getParameter("from"), "pc")) {
			return "line_workList";
		} else if (Request.isMobileRequest(request)) {
			return "line_workList_mobile";
		} else {
			return "line_workList_PC";
		}
	}

	/**
	 * 
	 * 已施工列表
	 *
	 * @param req
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping("work_done")
	@ResponseBody
	public DataTablePage<LineBusCpd> work_done(TableRequest req) {
		Sort sort = req.getSort("created");
		String lineId = req.getFilter("lineId"), modelId = req.getFilter("modelId"), bodycontract_id = req
				.getFilter("bodycontract_id");
		List<LineBusCpd> leaves = busLineCheckService.queryWorkDone(NumberUtils.toInt(bodycontract_id),
				NumberUtils.toInt(lineId), NumberUtils.toInt(modelId), JpaBus.Category.yunyingche);

		Pageable p = new PageRequest(0, leaves.isEmpty() ? 1 : leaves.size(), sort);
		org.springframework.data.domain.PageImpl<LineBusCpd> r = new org.springframework.data.domain.PageImpl<LineBusCpd>(
				leaves, p, leaves.size());
		return new DataTablePage(r, req.getDraw());
	}

	@RequestMapping(value = "work_online/{bodycontract_id}/{busId}")
	@ResponseBody
	public Pair<Boolean, String> saveBodyContract(@PathVariable("bodycontract_id") int bodycontract_id,
			Principal principal, HttpServletRequest request, @PathVariable("busId") int busId) {
		try {
			busLineCheckService.updateBusDone(bodycontract_id, busId, principal, request);
		} catch (Exception e) {
			return new Pair<Boolean, String>(false, e.getMessage());
		}
		return new Pair<Boolean, String>(true, "车辆上刊成功!");
	}

	/**
	 * 弹出上刊确认窗口
	 * Comment here.
	 * @param busContractId
	 * @param lineid
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "toconfirm_bus/{busContractId}/{lineid}")
	@ResponseBody
	public LineBusCpd toconfirm_bus(@PathVariable("busContractId") int busContractId, @PathVariable("lineid") int lineid) {
		return busLineCheckService.selectLineBusCpd(busContractId, lineid);
	}

	/**
	 * 确认实际上下刊日期
	 * Comment here.
	 * @param busContractId
	 * @param principal
	 * @return
	 * @throws ParseException 
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "confirm_bus/{busContractId}/{lineid}")
	@ResponseBody
	public Pair<Boolean, String> confirm_bus(@PathVariable("busContractId") int busContractId,
			@PathVariable("lineid") int lineid, Principal principal,
			@RequestParam(value = "startDate") String startdate, @RequestParam(value = "endDate") String endDate)
			throws ParseException {
		return busLineCheckService.confirm_bus(busContractId, lineid, startdate, endDate, principal);
	}

	/**
	* 施工单
	*/
	@RequestMapping("work_note")
	@ResponseBody
	public DataTablePage<LineBusCpd> work_note(TableRequest req) {
		Sort sort = req.getSort("created");
		String lineId = req.getFilter("lineId"), modelId = req.getFilter("modelId"), bodycontract_id = req
				.getFilter("bodycontract_id");
		List<LineBusCpd> leaves = busLineCheckService.queryWorkNote(NumberUtils.toInt(bodycontract_id),
				NumberUtils.toInt(lineId), NumberUtils.toInt(modelId), JpaBus.Category.yunyingche);
		Pageable p = new PageRequest(0, leaves.isEmpty() ? 1 : leaves.size(), sort);
		org.springframework.data.domain.PageImpl<LineBusCpd> r = new org.springframework.data.domain.PageImpl<LineBusCpd>(
				leaves, p, leaves.size());
		return new DataTablePage(r, req.getDraw());
	}

	/**
	* 排期表
	*/
	@RequestMapping("order-body-ajax-list2")
	@ResponseBody
	public DataTablePage<LineBusCpd> getBodyScheduleListForOrder2(TableRequest req) {
		Sort sort = req.getSort("created");
		String lineId = req.getFilter("lineId"), modelId = req.getFilter("modelId");
		List<LineBusCpd> leaves = busLineCheckService.getBusListChart(NumberUtils.toInt(lineId),
				NumberUtils.toInt(modelId), JpaBus.Category.yunyingche);
		Pageable p = new PageRequest(0, leaves.size(), sort);
		org.springframework.data.domain.PageImpl<LineBusCpd> r = new org.springframework.data.domain.PageImpl<LineBusCpd>(
				leaves, p, leaves.size());
		return new DataTablePage(r, req.getDraw());
	}

	/**
	 * 
	 * 线路按车辆类型 90天内的销售排期情况
	 *
	 * @param model
	 * @param line
	 * @param modelId
	 * @param principal
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping("lineschedule/{line}")
	public String lineschedule(Model model, @PathVariable("line") Integer line,
			@RequestParam(value = "modelId", required = true, defaultValue = "0") int modelId, Principal principal) {
		List<String> list = new ArrayList<String>();
		Date date = new Date();
		SimpleDateFormat format = DateUtil.longDf.get();
		for (int i = 0; i < 90; i++) {
			list.add(format.format(DateUtil.dateAdd(date, i)));
		}
		model.addAttribute("dates", list);
		model.addAttribute("lineId", line);
		model.addAttribute("modelId", modelId);

		model.addAttribute("modelList", busLineCheckService.countCarTypeByLine(line, JpaBus.Category.yunyingche));
		//model.addAttribute("modelList", busLineCheckService.getBusModel(line, JpaBus.Category.yunyingche.ordinal()));
		return "line_schedule";
	}

	/**
	 * 
	 * 合同详情
	 *
	 * @param bodycontract_id
	 * @param model
	 * @param principal
	 * @return
	 * @throws Exception 
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping("detail/{uniq}")
	public String detail(@PathVariable("uniq") Integer bodycontract_id,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, Model model,
			@RequestParam(value = "taskid", required = false) String taskid, Principal principal) throws Exception {
		if (StringUtils.isNotBlank(taskid)) {
			Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
			ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery()
					.executionId(task.getExecutionId()).processInstanceId(task.getProcessInstanceId()).singleResult();
			String activityId = executionEntity.getActivityId();
			ProcessInstance pe = activitiService.findProcessInstanceByTaskId(taskid);
			List<HistoricTaskView> activitis = activitiService.findHistoricUserTask(cityId, pe.getProcessInstanceId(),
					activityId);
			model.addAttribute("activitis", activitis);
		}
		model.addAttribute("bodycontract", busLineCheckService.selectBcById(bodycontract_id));
		return "bodycontract_detail";
	}

	/**
	 * 
	 * 开始下单页面
	 *
	 * @param model
	 * @param principal
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping("applyBodyCtct")
	public String applyBodyCtct(Model model, Principal principal) {
		model.addAttribute("seriaNum", Only1ServieUniqLong.getUniqLongNumber());
		return "applyBodyCtct";
	}
	@RequestMapping("offContract_enter")
	public String offContract_enter(Model model, Principal principal) {
		model.addAttribute("seriaNum", Only1ServieUniqLong.getUniqLongNumber());
		return "offContract_enter";
	}
	
	@RequestMapping("public_order")
	public String public_order(Model model, Principal principal) {
		model.addAttribute("seriaNum", Only1ServieUniqLong.getUniqLongNumber());
		return "public_order";
	}
	@RequestMapping(value = "/offcontract_edit/{contract_id}", produces = "text/html;charset=utf-8")
	public String offcontract_edit(Model model,@PathVariable("contract_id") int contract_id,Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int cityId,HttpServletRequest request) {
		Offlinecontract  offlinecontract=busLineCheckService.findOffContractById(contract_id);
		model.addAttribute("offlinecontract", offlinecontract);
		if(offlinecontract!=null){
			model.addAttribute("seriaNum", offlinecontract.getSeriaNum());
		}
		return "offContract_enter";
	}
	/**
	 * 
	 * 查合同的选车情况
	 *
	 * @param model
	 * @param city
	 * @param seriaNum
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "ajax-buslock", method = RequestMethod.GET)
	@ResponseBody
	public List<JpaBusLock> getBuses(Model model, @CookieValue(value = "city", defaultValue = "-1") int city,
			@RequestParam("seriaNum") long seriaNum) {
		return busLineCheckService.getBusLockListBySeriNum(seriaNum);
	}
	@RequestMapping(value = "ajax-publishLine", method = RequestMethod.GET)
	@ResponseBody
	public List<JpaPublishLine> publishLine(Model model, @CookieValue(value = "city", defaultValue = "-1") int city,
			@RequestParam("seriaNum") long seriaNum) {
		return busLineCheckService.getpublishLineBySeriNum(seriaNum);
	}
	
	@RequestMapping(value = "ajax-publishLinebyId", method = RequestMethod.GET)
	@ResponseBody
	public List<JpaPublishLine> publishLinebyId(Model model,
			@CookieValue(value = "city", defaultValue = "-1") int city, @RequestParam("plid") int plid) {
		JpaPublishLine v = busLineCheckService.queryPublishLineByid(plid);
		List<JpaPublishLine> r = new ArrayList<JpaPublishLine>(1);
		if (v != null) {
			r.add(v);
		}
		return r;
	}
	
	@RequestMapping(value = "ajax-getDividPay", method = RequestMethod.GET)
	@ResponseBody
	public List<JapDividPay> getDividPay(Model model, @CookieValue(value = "city", defaultValue = "-1") int city,
			@RequestParam("seriaNum") long seriaNum) {
		return busLineCheckService.getDividPay(seriaNum);
	}

	/**
	 * 
	 * 删除选车记录
	 *
	 * @param principal
	 * @param city
	 * @param seriaNum
	 * @param id
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "ajax-remove-buslock", method = RequestMethod.POST)
	@ResponseBody
	public boolean removeBusLock(Principal principal, @CookieValue(value = "city", defaultValue = "-1") int city,
			@RequestParam("seriaNum") long seriaNum, @RequestParam("id") int id) {
		return busLineCheckService.removeBusLock(principal, city, seriaNum, id);
	}
	@RequestMapping(value = "ajax-remove-publishLine", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> removepublishLine(Principal principal, @CookieValue(value = "city", defaultValue = "-1") int city,
			@RequestParam("seriaNum") long seriaNum, @RequestParam("id") int id) {
		return busLineCheckService.removePublishLine(principal, city, seriaNum, id);
	}
	@RequestMapping(value = "ajax-remove-dividPay", method = RequestMethod.POST)
	@ResponseBody
	public boolean removedividPay(Principal principal, @CookieValue(value = "city", defaultValue = "-1") int city,
			@RequestParam("seriaNum") long seriaNum, @RequestParam("id") int id) {
		return busLineCheckService.removedividPay(principal, city, seriaNum, id);
	}
	@RequestMapping(value = "ajax-remove-busmodel", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> removebusmodel(Principal principal, @CookieValue(value = "city", defaultValue = "-1") int city,
			 @RequestParam("id") int id) {
		return busLineCheckService.removebusmodel(principal, city, id);
	}
	@RequestMapping(value = "ajax-remove-line/{type}", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> removebusline(Principal principal, @CookieValue(value = "city", defaultValue = "-1") int city,
			@RequestParam(value="id") int id,@PathVariable("type") int type) {
		return busLineCheckService.removebusline(principal, city, id,type);
	}
	@RequestMapping(value = "ajax-remove-bus/{type}", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> removebus(Principal principal, @CookieValue(value = "city", defaultValue = "-1") int city,
			@RequestParam("id") int id,@PathVariable("type") int type) {
		return busLineCheckService.removebus(principal, city, id,type);
	}
	@RequestMapping(value = "ishaveline/{linename}", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> ishaveline(Principal principal, @CookieValue(value = "city", defaultValue = "-1") int city,
			@PathVariable("linename") String linename) {
		return busLineCheckService.ishaveline(linename);
	}

	/**
	 * 
	 * 锁定时间 
	 *
	 * @param orderid
	 * @param contractid
	 * @param LockDate
	 * @param taskid
	 * @param canSchedule
	 * @param principal
	 * @param request
	 * @param response
	 * @return
	 * @throws NumberFormatException
	 * @throws ParseException
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "LockStore")
	@ResponseBody
	public Pair<Boolean, String> LockStore(@RequestParam(value = "orderid") String orderid,
			@RequestParam(value = "contractid") int contractid, @RequestParam(value = "LockDate") String LockDate,
			@RequestParam(value = "taskid") String taskid, @RequestParam(value = "canSchedule") boolean canSchedule,
			Principal principal, HttpServletRequest request, HttpServletResponse response)
			throws NumberFormatException, ParseException {
		return activitiService.LockStore(Integer.parseInt(orderid), taskid, contractid, principal, canSchedule,
				LockDate);
	}

	@RequestMapping(value = "financialCheck")
	@ResponseBody
	public Pair<Boolean, String> financialCheck(@RequestParam(value = "orderid") int orderid,
			@RequestParam(value = "taskid") String taskid,
			@RequestParam(value = "financialcomment") String financialcomment,
			@RequestParam(value = "paymentResult") boolean paymentResult, Principal principal,
			HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, ParseException {
		return activitiService.financialCheck(orderid, taskid, financialcomment, paymentResult, principal);

	}

	/**
	 * 
	 * Comment here.
	 * 上传小样
	 * @param mainid
	 * @param taskid
	 * @param financialcomment
	 * @param principal
	 * @param request
	 * @param response
	 * @return
	 * @throws NumberFormatException
	 * @throws ParseException
	 * @throws BusinessException 
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "uploadXiaoY", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> uploadXiaoY(@RequestParam(value = "mainid") int mainid,
			@RequestParam(value = "taskid") String taskid,
			@RequestParam(value = "approve2Comments") String approve2Comments, Principal principal,
			HttpServletRequest request) throws NumberFormatException, ParseException, BusinessException {
		return activitiService.uploadXiaoY(mainid, taskid, approve2Comments, principal, request);

	}

	/**
	 * 
	 * 已完成订单
	 *
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "/finished")
	public String finishedOrders() {
		return "finishedBodyOrders";
	}

	/**
	 * 
	 * 已完成的订单 datatable
	 *
	 * @param req
	 * @param principal
	 * @param city
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping("ajax-finishedOrders")
	@ResponseBody
	public DataTablePage<OrderView> finishedAjax(TableRequest req, Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		Page<OrderView> w = busLineCheckService.finished(city, principal, req);
		return new DataTablePage<OrderView>(w, req.getDraw());
	}
	@RequestMapping(value = "/offContract_list")
	public String offContract_list() {
		return "offContract_list";
	}
	
	@RequestMapping(value = "/publicOrder_list")
	public String publicOrder_list() {
		return "publicOrder_list";
	}
	
	//到期提醒
	@RequestMapping(value = "/reminder")
	public String reminder() {
		return "reminder";
	}
	
	//广告查询
	@RequestMapping(value = "/adquery")
	public String adquery() {
		return "adquery";
	}
	
	//配车查询
	@RequestMapping(value = "/matchbusquery")
	public String matchbusquery() {
		return "matchbusquery";
	}
	
	//日常检查
	@RequestMapping(value = "/dailycheck")
	public String dailycheck() {
		return "dailycheck";
	}
	@RequestMapping(value = "/publishLine_list")
	public String publishLine_list() {
		return "publishLine_list";
	}
	@RequestMapping("ajax-offContract_list")
	@ResponseBody
	public DataTablePage<JpaOfflineContract> getAllJpaOfflineContract(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city) {
		Page<JpaOfflineContract> jpabuspage=busLineCheckService.queryOfflineContract(cityId, req, req.getPage(), req.getLength(),
				req.getSort("id"));
		return new DataTablePage(jpabuspage, req.getDraw());
	}
	@RequestMapping("ajax-publishLine_list")
	@ResponseBody
	public DataTablePage<JpaPublishLine> getAllPublishLine(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int cityId, @ModelAttribute("city") JpaCity city) {
		Page<JpaPublishLine> jpabuspage=busLineCheckService.queryAllPublish(cityId, req, req.getPage(), req.getLength(),
				req.getSort("id"));
		return new DataTablePage(jpabuspage, req.getDraw());
	}
	
	@RequestMapping(value = "/queryDividPayByid/{id}")
	@ResponseBody
	public Dividpay queryDividPayByid(Model model,
			@PathVariable("id") int id, Principal principal,
			HttpServletRequest request) {
		   Dividpay dividpay=busLineCheckService.queryDividPayByid(id);
		   return dividpay;
	} 
	@RequestMapping(value = "/queryPublishLineByid/{id}")
	@ResponseBody
	public JpaPublishLine queryPublishLineByid(Model model,
			@PathVariable("id") int id, Principal principal,
			HttpServletRequest request) {
		JpaPublishLine dividpay=busLineCheckService.queryPublishLineByid(id);
		return dividpay;
	}
}