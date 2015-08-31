package com.pantuo.simulate;

/**
 * 
 * <b><code>AbstractStatsInter</code></b>
 * <p>
 *任务状态类
 * </p>
 * <b>Creation Time:</b> 2015年8月30日 上午10:40:41
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
public class StatsMonitor {

	public enum TaakStatus {
		running, parse
	}

	long lastRunTime = 0;
	boolean isRunning = true;
	Object target;

	public void runover() {
		lastRunTime = System.currentTimeMillis();
	}

	/**
	 * 
	 * 返回任务的上次执行时间 
	 *
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public long getLastRuntime() {
		return lastRunTime;
	}

	/**
	 * 
	 * 取的任务的状态 在运行和暂停
	 *
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public boolean getTaskStats() {
		return isRunning;
	}

	/**
	 * 暂停 恢复
	 *
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public void changeStats(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public String getClassName() {
		return target != null ? target.getClass().getName() : null;
	}

	public StatsMonitor(Object target) {
		this.target = target;
	}

}
