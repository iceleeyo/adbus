package com.pantuo.simulate;

import java.io.Serializable;

//@Component
//unimplemented
public class DemoThread implements Serializable, ScheduleStatsInter {

	

	//@Scheduled(cron = "0/5 * * * * ?")
	public void work() {
		if (statControl.isRunning) {
			try {
				System.out.println(Thread.currentThread() + " =" + System.currentTimeMillis());
			} finally {
				statControl.runover();
			}
		}

	}

	public String getVersion() {
		return "1.0";
	}
	public StatsMonitor statControl = new StatsMonitor(this);
	@Override
	public StatsMonitor getStatsMonitor() {
		return statControl;
	}

}
