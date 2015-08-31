package com.pantuo.simulate;

/**
 * 
 * <b><code>ScheduleWebDataInter</code></b>
 * <p>
 * schedule页面数据接口
 * </p>
 * <b>Creation Time:</b> 2015年8月30日 上午10:13:08
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
public interface ScheduleStatsInter {
	/**
	 * 
	 * 返回任务的执行状态
	 *
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public StatsMonitor getStatsMonitor();

}
