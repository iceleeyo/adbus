package com.pantuo.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.scheduling.support.TaskUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.pantuo.SchedulerConfiguration;
import com.pantuo.simulate.ScheduleStatsInter;
import com.pantuo.util.Pair;
/**
 * 
 * <b><code>SpringTaskManagerService</code></b>
 * <p>
 * spring Scheduled 管理 ,大意通过类的方法名 在spring registrat类中找到相应的future然后操作对象并更新相应的config
 * spring 实现scheduled：采用线程包装执行对象与方法，定时执行 
 * 核心的类 
 *  ScheduledTaskRegistrar 记录任务列表
 *  ScheduledMethodRunnable 实际的用户任务实例包装类
 *  DelegatingErrorHandlingRunnable 
 *  ReschedulingRunnable 实际的定时执行者
 * </p>
 * <b>Creation Time:</b> 2015年8月31日 上午8:15:25
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
@Service
public class SpringTaskManagerService {
	private static Logger log = LoggerFactory.getLogger(SpringTaskManagerService.class);
	public String METHOD_SPLIT = "@";
	public String SPRING_THREAD_NAME = "ReschedulingRunnable";

	/**
	 * 
	 * 查找有多少个在运行的任务
	 *
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Set<ScheduledFuture<?>> getRegistrarFuture() {
		ScheduledTaskRegistrar scheduledTaskRegistrar = SchedulerConfiguration.getTaskRegistrar();
		Set<ScheduledFuture<?>> futureSet = null;
		try {
			Field f;
			f = scheduledTaskRegistrar.getClass().getDeclaredField("scheduledFutures");
			ReflectionUtils.makeAccessible(f);
			futureSet = (Set<ScheduledFuture<?>>) ReflectionUtils.getField(f, scheduledTaskRegistrar);
		} catch (NoSuchFieldException | SecurityException e) {
			log.error("getRegistrarFuture", e);
		}
		return futureSet;

	}

	/**
	 * 设计 约定一个类的方法只能存放一个定时任务
	 * 根据方法查找相应的执行对象和操作的类对象
	 * 见ReschedulingRunnable DelegatingErrorHandlingRunnable源码
	 *
	 *
	 *
	 *ReschedulingRunnable 父类 DelegatingErrorHandlingRunnable 
	 *DelegatingErrorHandlingRunnable delegate存的是实际的执行执行类ScheduledMethodRunnable 
	 *ScheduledMethodRunnable中的taget是实际是我们的定时类
	 * @param classKey
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Pair<ScheduledFuture<?>/*ReschedulingRunnable*/, ScheduledMethodRunnable> findRescheduling(String methodKey) {
		Pair<ScheduledFuture<?>, ScheduledMethodRunnable> pair = new Pair<ScheduledFuture<?>, ScheduledMethodRunnable>();
		try {
			Set<ScheduledFuture<?>> futureSet = getRegistrarFuture();
			for (ScheduledFuture<?> future : futureSet) {
				if (future.getClass().getName().contains(SPRING_THREAD_NAME)) {
					Field f2 = future.getClass().getSuperclass().getDeclaredField("delegate");
					ReflectionUtils.makeAccessible(f2);
					Runnable runnable = (Runnable) ReflectionUtils.getField(f2, future);
					if (runnable instanceof ScheduledMethodRunnable) {
						ScheduledMethodRunnable realyRunable = (ScheduledMethodRunnable) runnable;
						if (StringUtils.equals(
								(realyRunable.getTarget().getClass().getName() + METHOD_SPLIT + realyRunable
										.getMethod().getName()), methodKey)) {
							pair.setLeft(future);
							pair.setRight(realyRunable);
							break;
						}
					}

				}
			}
		} catch (Exception e) {
			log.error("findReschedulingEx:{}", methodKey, e);
			TaskUtils.LOG_AND_PROPAGATE_ERROR_HANDLER.handleError(e);
		}
		return pair;
	}

	public void setMenoryExpression(String methodKey, String expressionStr) throws NoSuchFieldException,
			SecurityException {
		List<CronTask> cronTaskList = SchedulerConfiguration.getTaskRegistrar().getCronTaskList();
		List<CronTask> cronTasks = new ArrayList<CronTask>();
		for (CronTask cronTask : cronTaskList) {
			ScheduledMethodRunnable runable = (ScheduledMethodRunnable) cronTask.getRunnable();
			if (StringUtils.equals((runable.getTarget().getClass().getName() + METHOD_SPLIT + runable.getMethod()
					.getName()), methodKey)) {

				//cronTask.	 
				//设置CronTask 的expression
				Field expression = cronTask.getClass().getDeclaredField("expression");
				ReflectionUtils.makeAccessible(expression);
				ReflectionUtils.setField(expression, cronTask, expressionStr);
				//设置CronTask 中trigger中 的expression

				Field trigger = cronTask.getClass().getSuperclass().getDeclaredField("trigger");
				ReflectionUtils.makeAccessible(trigger);
				CronTrigger cronTrigger = (CronTrigger) ReflectionUtils.getField(trigger, cronTask);
				Field sequenceGenerator = cronTrigger.getClass().getDeclaredField("sequenceGenerator");
				ReflectionUtils.makeAccessible(sequenceGenerator);
				ReflectionUtils.setField(sequenceGenerator, cronTrigger, new CronSequenceGenerator(expressionStr,
						TimeZone.getDefault()));
			}
			cronTasks.add(cronTask);
		}
		SchedulerConfiguration.getTaskRegistrar().setCronTasksList(cronTasks);
	}

	/**
	 * 
	 * 1:先设置运行中的trigger 中的 expression
	 * 2:设置TaskRegistrar中的expression 供页面显示用
	 *
	 * @param methodKey
	 * @param expression
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Pair<Boolean, String> setTaskExpression(String methodKey, String expression) {
		expression = StringUtils.trim(expression);
		Pair<Boolean, String> r = new Pair<Boolean, String>(false, StringUtils.EMPTY);
		try {
			setMenoryExpression(methodKey, expression);
			Pair<ScheduledFuture<?>, ScheduledMethodRunnable> pair = findRescheduling(methodKey);
			if (pair.getLeft() != null) {
				Field trigger = pair.getLeft().getClass().getDeclaredField("trigger");
				ReflectionUtils.makeAccessible(trigger);
				CronTrigger cronTrigger = (CronTrigger) ReflectionUtils.getField(trigger, pair.getLeft());
				Field sequenceGenerator = cronTrigger.getClass().getDeclaredField("sequenceGenerator");
				ReflectionUtils.makeAccessible(sequenceGenerator);
				ReflectionUtils.setField(sequenceGenerator, cronTrigger,
						new CronSequenceGenerator(expression, TimeZone.getDefault()));

				Method scheduleMethod = pair.getLeft().getClass().getDeclaredMethod("schedule");
				ReflectionUtils.makeAccessible(scheduleMethod);
				ReflectionUtils.invokeMethod(scheduleMethod, pair.getLeft());
				r.setLeft(true);

			}
		} catch (Exception e) {
			log.error("setTaskExpression:{}", methodKey, e);
			r.setRight(e.getMessage());
		}
		return r;
	}

	public Pair<Boolean, String> setTaskStats(String methodKey, boolean isRunning) {
		Pair<Boolean, String> r = new Pair<Boolean, String>(false, "任务实例未找到StatsMonitor属性,无法进行暂停或是恢复操作!");
		try {
			Pair<ScheduledFuture<?>, ScheduledMethodRunnable> pair = findRescheduling(methodKey);
			if (pair.getLeft() != null) {
				Object target = pair.getRight().getTarget();
				if (target instanceof ScheduleStatsInter) {
					ScheduleStatsInter instance = (ScheduleStatsInter) target;
					instance.getStatsMonitor().changeStats(isRunning);
					r.setLeft(true);
				}
			}
		} catch (Exception e) {
			log.error("runTaskNow:{}", methodKey, e);
			r.setRight(e.getMessage());
		}
		return r;
	}

	public boolean runTaskNow(String methodKey) {
		boolean r = true;
		try {
			Pair<ScheduledFuture<?>, ScheduledMethodRunnable> pair = findRescheduling(methodKey);
			if (pair.getLeft() != null) {
				pair.getRight().run();
			}
		} catch (Exception e) {
			log.error("runTaskNow:{}", methodKey, e);
			r = false;
		}
		return r;
	}

	public boolean canelTask(String methodKey) {
		Pair<ScheduledFuture<?>, ScheduledMethodRunnable> pair = findRescheduling(methodKey);
		boolean r = false;
		//找到相应的任务,尝试关闭 3次尝试
		if (pair.getLeft() != null) {
			r = pair.getLeft().cancel(true);
			int i = 0;
			while (i < 3) {
				if (r = pair.getLeft().isCancelled()) {
					break;
				} else {

					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
					}
					r = pair.getLeft().cancel(true);
				}
				i++;
			}
		}
		if (r) {
			//关闭成功 ,清理内部记录
			Set<ScheduledFuture<?>> futureSet = getRegistrarFuture();
			futureSet.remove(pair.getLeft());
			List<CronTask> cronTaskList = SchedulerConfiguration.getTaskRegistrar().getCronTaskList();
			List<CronTask> cronTasks = new ArrayList<CronTask>();
			for (CronTask cronTask : cronTaskList) {
				ScheduledMethodRunnable runable = (ScheduledMethodRunnable) cronTask.getRunnable();

				if (!StringUtils.equals((runable.getTarget().getClass().getName() + METHOD_SPLIT + runable.getMethod()
						.getName()), methodKey)) {//两个对象相等
					cronTasks.add(cronTask);
				} else {
					log.info("delete task,{} {} {}", runable.getTarget().getClass(), "["
							+ runable.getMethod().getName() + "]", cronTask.getExpression());
				}
			}
			SchedulerConfiguration.getTaskRegistrar().setCronTasksList(cronTasks);
		}
		return r;
	}

	public void edit() {
		try {
			ScheduledTaskRegistrar scheduledTaskRegistrar = SchedulerConfiguration.getTaskRegistrar();
			Field f = scheduledTaskRegistrar.getClass().getDeclaredField("scheduledFutures");
			ReflectionUtils.makeAccessible(f);

			Set<ScheduledFuture<?>> rt = (Set<ScheduledFuture<?>>) ReflectionUtils.getField(f, scheduledTaskRegistrar);
			for (ScheduledFuture<?> it : rt) {
				Field f2 = it.getClass().getDeclaredField("trigger");
				ReflectionUtils.makeAccessible(f2);
				CronTrigger trigger = (CronTrigger) ReflectionUtils.getField(f2, it);
				Field f3 = trigger.getClass().getDeclaredField("sequenceGenerator");
				ReflectionUtils.makeAccessible(f3);
				//ReflectionUtils.setField(f3, trigger,
				//		new CronSequenceGenerator("0/2 * * 5-29 * ?", TimeZone.getDefault()));

				Method scheduleMethod = it.getClass().getDeclaredMethod("schedule");
				ReflectionUtils.makeAccessible(scheduleMethod);
				ReflectionUtils.invokeMethod(scheduleMethod, it);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
}
