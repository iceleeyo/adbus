package com.pantuo.simulate;

import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.service.MailService;
import com.pantuo.service.MailTask;
import com.pantuo.service.OrderService;
import com.pantuo.service.impl.UserService;

/**
 * 
 * <b><code> </code></b>
 * 异步发送邮件
 * <p>
 * </p>
 * <b>Creation Time:</b> 2015年1月23日 上午10:47:50
 * @author impanxh@gmail.com
 * @since pantuotech 1.0-SNAPSHOT
 */
@Service
public class MailJob {

	@Autowired
	private OrderService orderService;

	@Autowired
	private MailService mailService;

	private static Logger log = LoggerFactory.getLogger(MailJob.class);
	static BlockingQueue<MailTask> queue = new LinkedBlockingQueue<MailTask>();

	public void putMailTask(MailTask portalLog) {
		try {
			queue.put(portalLog);
		} catch (InterruptedException e) {
			log.error("put queue ex:{}", e);
		}
	}

	public class MailSendService implements Runnable {
		public void run() {
			while (true) {
				MailTask task  = null;
				try {
					  task = queue.take();
					//待办事项完成参数和其他发送邮件参数不一样
					if (task.getMailType() == MailTask.Type.sendCompleteMail) {
						Method method = mailService.getClass().getMethod(task.getMailType().name(),
								new Class[] { String.class, Integer.class });
						method.invoke(mailService, new Object[] { task.getUserName(), task.getOrderId() });
					} else {
						Method method = mailService.getClass().getMethod(task.getMailType().name(),
								new Class[] { UserDetail.class });
						method.invoke(mailService, new Object[] { task.getUser() });
					}
				} catch (Exception e) {
					log.error("send mail ex:{}", e);
					putMailTask(task);
				}

			}
		}
	}
	@PostConstruct
	public void init() {
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
		ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 4, 1, TimeUnit.DAYS, queue);
		for (int i = 0; i < 4; i++) {
			executor.submit(new Thread(new MailSendService(), "MailSendService".concat("" + i)));
		}
		int threadSize = queue.size();
		log.info("init send mail Service thread:-->" + threadSize);
	}

}
