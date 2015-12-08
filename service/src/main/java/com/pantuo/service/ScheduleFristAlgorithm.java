package com.pantuo.service;

import com.pantuo.service.ScheduleService.ScheduleContent;
import com.pantuo.web.schedule.SchedUltResult;
/**
 * 
 * <b><code>ScheduleAlgorithm</code></b>
 * <p>
 * 排期算法
 * </p>
 * <b>Creation Time:</b> 2015年12月6日 下午2:54:08
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
public interface ScheduleFristAlgorithm {
	public SchedUltResult scheduleFrist(ScheduleContent command);

}
