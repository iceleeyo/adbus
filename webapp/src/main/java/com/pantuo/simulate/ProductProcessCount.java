package com.pantuo.simulate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProductProcessCount implements Runnable{
	@Scheduled(cron = "0/50 * * * * ?")
	//30秒更新一次
	public void work() {
		// 任务执行逻辑
		System.out.println(System.currentTimeMillis());
	}

	@Override
	public void run() {
		work();
	}
}
