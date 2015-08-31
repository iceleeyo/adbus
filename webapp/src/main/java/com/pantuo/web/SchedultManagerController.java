package com.pantuo.web;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.SchedulerConfiguration;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.SpringTaskManagerService;
import com.pantuo.simulate.DemoThread;
import com.pantuo.util.Pair;

@Controller
@RequestMapping("/schedultTask")
public class SchedultManagerController {
	@Autowired
	SpringTaskManagerService springTaskManagerService;

	@RequestMapping("public_tasklist")
	public String getScheduleList() {
		return "spring_task";
	}

	/**
	 * 
	 * 增加demo
	 *
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "/public_add")
	@ResponseBody
	public String public_add() {
		ScheduledMethodRunnable sm;
		try {
			String runtime = "0/10 * * * * ?";
			DemoThread obj = new DemoThread();
			sm = new ScheduledMethodRunnable(obj, "work");
			SchedulerConfiguration.getTaskRegistrar().addCronTask(sm, runtime);
			ScheduledFuture<?> future = SchedulerConfiguration.getTaskRegistrar().getScheduler()
					.schedule(sm, new CronTrigger(runtime));
			springTaskManagerService.getRegistrarFuture().add(future);

		} catch (NoSuchMethodException e) {
		}
		return "success";
	}

	/**
	 * 
	 * 获取任务列表
	 *
	 * @param req
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "/public_tasks")
	@ResponseBody
	public List<CronTask> tasks(TableRequest req) {
		return springTaskManagerService.queryCronTaskList();
	}

	/**
	* 
	* 设置表达式
	*
	* @param methodKey
	* @return
	* @since pantuo 1.0-SNAPSHOT
	*/
	@RequestMapping(value = "/public_expression")
	@ResponseBody
	public Pair<Boolean, String> expression(
			@RequestParam(value = "methodKey", required = true, defaultValue = "") String methodKey, String expression) {

		return springTaskManagerService.setTaskExpression(methodKey, expression);
	}

	/**
	* 
	* 暂停 恢复
	*
	* @param methodKey
	* @return
	* @since pantuo 1.0-SNAPSHOT
	*/
	@RequestMapping(value = "/public_stats")
	@ResponseBody
	public Pair<Boolean, String> setTaskStats(
			@RequestParam(value = "methodKey", required = true, defaultValue = "") String methodKey, String stats) {
		return springTaskManagerService.setTaskStats(methodKey, BooleanUtils.toBoolean(stats));
	}

	/**
	 * 
	 * 立即执行一次任务
	 *
	 * @param methodKey
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "/public_runTaskNow")
	@ResponseBody
	public boolean runTaskNow(@RequestParam(value = "methodKey", required = true, defaultValue = "") String methodKey) {
		return springTaskManagerService.runTaskNow(methodKey);
	}

	/**
	 * 
	 * 删除任务
	 *
	 * @param methodKey
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "/public_canel")
	@ResponseBody
	public boolean canel(@RequestParam(value = "methodKey", required = true, defaultValue = "") String methodKey) {
		return springTaskManagerService.canelTask(methodKey);
	}
}
