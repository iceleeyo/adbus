package com.pantuo.activiti;



import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.ScheduleLog;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.OrderService;
import com.pantuo.service.ScheduleService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.pantuo.dao.pojo.UserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("generateSchedule")
public class GenerateSchedule implements JavaDelegate {
	private static final Logger log = LoggerFactory.getLogger(GenerateSchedule.class);

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private OrderService orderService;

	//@Override
	public void execute(DelegateExecution execution) throws Exception {
		// varOutFromMainprocess<->varInSubprocess
        Boolean b = (Boolean) execution.getVariable("_isTest");
		UserDetail owner = (UserDetail)execution.getVariable(ActivitiService.OWNER);
        log.info("Generating schedule for order owned by " + owner);

        if (b != null && b) {
            //for test
            if (execution.hasVariable("mockScheduleResult")) {
                execution.setVariable("scheduleResult", execution.getVariable("mockScheduleResult"));
            } else {
                execution.setVariable("scheduleResult", true);
            }
        } else {
            int orderId = (int) execution.getVariable(ActivitiService.ORDER_ID);
            log.info("Generating schedule for order {} owned by {}", orderId, owner);
            JpaOrders order = orderService.getJpaOrder(orderId);
            if (order == null || order.getStats() != JpaOrders.Status.paid) {
                execution.setVariable("scheduleResult", false);
                execution.setVariable("scheduleComments", "order " + orderId + " not exists or not paid");
                return;
            }
            if (order.getType() == JpaProduct.Type.video) {
                ScheduleLog log = scheduleService.schedule(order);
                if (log.getStatus() == ScheduleLog.Status.scheduled) {
                    execution.setVariable("scheduleResult", true);
                } else {
                    execution.setVariable("scheduleResult", false);
                    execution.setVariable("scheduleComments", log.getDescription());
                }
            } else {
                //其他类型暂时不需要排期
                execution.setVariable("scheduleResult", true);
            }
        }
	}
}