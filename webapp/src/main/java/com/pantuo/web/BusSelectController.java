package com.pantuo.web;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaBusLock;
import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.mybatis.domain.Bodycontract;
import com.pantuo.mybatis.domain.BusLock;
import com.pantuo.mybatis.domain.Contract;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.HistoricTaskView;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.ActivitiService.TaskQueryType;
import com.pantuo.service.BusLineCheckService;
import com.pantuo.service.ContractService;
import com.pantuo.service.UserServiceInter;
import com.pantuo.util.DateUtil;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Only1ServieUniqLong;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.vo.GroupVo;
import com.pantuo.web.view.AutoCompleteView;
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
	@RequestMapping("ajax-orderlist")
	@ResponseBody
	public DataTablePage<OrderView> getAllContracts(TableRequest req, Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		Page<OrderView> w = busLineCheckService.getBodyContractList(city, req, principal);
		return new DataTablePage(w, req.getDraw());
	}
	@RequestMapping(value = "/myOrders/{pageNum}")
	public String myOrders(Model model) {
		model.addAttribute("orderMenu", "我的订单");
		return "myBodyOrders";
	}
	@RequestMapping(value = "/body_handleView", produces = "text/html;charset=utf-8")
	public String HandleView2(Model model,
            @RequestParam(value = "taskid", required = true) String taskid,
			Principal principal,
            @CookieValue(value = "city", defaultValue = "-1") int cityId,
            @ModelAttribute("city") JpaCity city,
            HttpServletRequest request)
			throws Exception {
		NumberPageUtil page = new NumberPageUtil(9999, 1, 9999);
		Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
		HistoricTaskInstance currTask = historyService
                .createHistoricTaskInstanceQuery().taskId(taskid)
                .singleResult();
		Date claimT=currTask.getClaimTime();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String claimTime=sdf.format(claimT);
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
		model.addAttribute("bodycontract",busLineCheckService.selectBcById(v.getJpaBodyContract().getId()));
		model.addAttribute("claimTime", claimTime);
		model.addAttribute("activitis", activitis);
		model.addAttribute("activityId", activityId);
		return "body_handleView";
	}
	@RequestMapping("ajax-myOrders")
	@ResponseBody
	public DataTablePage<OrderView> myOrders(TableRequest req, Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		Page<OrderView> w = busLineCheckService.queryOrders(city, (principal), req, TaskQueryType.my);
		return new DataTablePage<OrderView>(w, req.getDraw());
	}
	@RequestMapping("setLockDate/{id}")
	@ResponseBody
	public Pair<Boolean, String> lockDate(Principal principal,@PathVariable("id") int id,
			@RequestParam(value="lockDate") String lockDate
			) throws ParseException {
		return busLineCheckService.setLockDate(lockDate,id,principal);
	}

	@RequestMapping(value = "/autoComplete")
	@ResponseBody
	public List<AutoCompleteView> autoCompleteByName(@CookieValue(value = "city", defaultValue = "-1") int city,
			@RequestParam(value = "term") String name) {
		return busLineCheckService.autoCompleteByName(city, name, JpaBus.Category.yunyingche);
	}

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

	@RequestMapping(value = "/lineReaminCheck")
	@ResponseBody
	public int lineReaminCheck(@RequestParam(value = "buslinId", required = true, defaultValue = "0") Integer buslinId,
			@RequestParam(value = "modelId", required = true, defaultValue = "0") Integer modelId,
			@RequestParam(value = "start", required = true) String start,
			@RequestParam(value = "end", required = true) String end) {
		//187 2015-08-07 2015-08-17
		return busLineCheckService.countByFreeCars(buslinId, modelId, JpaBus.Category.yunyingche, start, end);
	}
	@RequestMapping(value = "/checkStock")
	@ResponseBody
	public Pair<Boolean, String> checkStock(@RequestParam(value = "buslockid", required = true, defaultValue = "0") Integer buslockid,
			@RequestParam(value = "seriaNum", required = true) long seriaNum)
			 {
		       JpaBusLock busLock=busLineCheckService.findBusLockById(buslockid);
		       if(busLock!=null){
		    	   String st=new SimpleDateFormat("yyyy-MM-dd").format(busLock.getStartDate());
		    	   String end=new SimpleDateFormat("yyyy-MM-dd").format(busLock.getEndDate());
		    	   int a=busLineCheckService.countByFreeCars(busLock.getLine().getId(), busLock.getModel().getId(), JpaBus.Category.yunyingche,st ,end );
		    	   if(busLock.getRemainNuber()>a){
		    		   return new  Pair<Boolean, String>(false,"库存不足");
		    	   }else{
		    		   return new  Pair<Boolean, String>(true,"库存充足");
		    	   }
		       }
		       return new  Pair<Boolean, String>(false,"信息丢失");
	}

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

	@RequestMapping(value = "/saveBodyContract")
	@ResponseBody
	public Pair<Boolean, String> saveBodyContract(Bodycontract bodycontract,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			HttpServletRequest request, @RequestParam(value = "seriaNum", required = true) long seriaNum) {
		bodycontract.setCity(city);
		return busLineCheckService.saveBodyContract(bodycontract, seriaNum, Request.getUserId(principal));
	}

	/**
	* 排期表
	*/
	@RequestMapping("order-body-ajax-list2")
	@ResponseBody
	public List<LineBusCpd> getBodyScheduleListForOrder2(TableRequest req) {
		try {
			Sort sort = req.getSort("created");
			Pageable p = new PageRequest(1, 3, sort);
			List<LineBusCpd> leaves = new ArrayList<LineBusCpd>();

			SimpleDateFormat format = DateUtil.longDf.get();
			Date date = new Date();
			for (int i = 1; i < 100; i++) {
				Map<String, String> map = new HashMap<String, String>();
				LineBusCpd c = new LineBusCpd();
				int w = (int) (Math.random() * 90) + 0;
				for (int j = w; j < w + 20; j++) {
					map.put(format.format(DateUtil.dateAdd(date, j)), "red");
				}

				c.setMap(map);
				c.setSerialNumber(String.valueOf(i));
				leaves.add(c);
			}

			org.springframework.data.domain.PageImpl<LineBusCpd> r = new org.springframework.data.domain.PageImpl<LineBusCpd>(
					leaves, p, 3);
			return leaves;
			//return new DataTablePage(r, req.getDraw());
		} catch (Exception e) {
			return Collections.EMPTY_LIST;
			//return new DataTablePage(Collections.EMPTY_LIST);
		}
	}

	@RequestMapping("lineschedule/{line}")
	public String lineschedule(Model model, @PathVariable("line") String taskId, Principal principal) {
		List<String> list = new ArrayList<String>();
		Date date = new Date();
		SimpleDateFormat format = DateUtil.longDf.get();
		for (int i = 0; i < 90; i++) {
			list.add(format.format(DateUtil.dateAdd(date, i)));
		}
		model.addAttribute("dates", list);
		return "line_schedule";
	}

	@RequestMapping("detail/{uniq}")
	public String detail( @PathVariable("uniq") Integer bodycontract_id,
			Model model, Principal principal) {
		model.addAttribute("bodycontract",busLineCheckService.selectBcById(bodycontract_id));
		return "bodycontract_detail";
	}

	@RequestMapping("applyBodyCtct")
	public String applyBodyCtct(Model model, Principal principal) {
		model.addAttribute("seriaNum", Only1ServieUniqLong.getUniqLongNumber());
		return "applyBodyCtct";
	}

	@RequestMapping(value = "ajax-buslock", method = RequestMethod.GET)
	@ResponseBody
	public List<JpaBusLock> getBuses(Model model, @CookieValue(value = "city", defaultValue = "-1") int city,
			@RequestParam("seriaNum") long seriaNum) {
		return busLineCheckService.getBusLockListBySeriNum(seriaNum);
	}

	@RequestMapping(value = "ajax-remove-buslock", method = RequestMethod.POST)
	@ResponseBody
	public boolean removeBusLock(Principal principal, @CookieValue(value = "city", defaultValue = "-1") int city,
			@RequestParam("seriaNum") long seriaNum, @RequestParam("id") int id) {
		return busLineCheckService.removeBusLock(principal, city, seriaNum, id);
	}

}