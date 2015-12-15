package com.pantuo.activiti;


import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.OrderService;
import com.pantuo.service.ScheduleService;

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
            int orderId = (Integer) execution.getVariable(ActivitiService.ORDER_ID);
            log.info("Generating schedule for order {} owned by {}", orderId, owner);
            JpaOrders order = orderService.getJpaOrder(orderId);
            System.out.println(order.getStats());
            if (order == null && order.getStats() != JpaOrders.Status.paid) {
                execution.setVariable("scheduleResult", false);
                execution.setVariable("scheduleComments", "order " + orderId + " not exists or not paid");
                return;
            }
            if (order.getType() == JpaProduct.Type.video) {
//                ScheduleLog log = scheduleService.schedule(order);
                scheduleService.schedule2(order,
                		false,false,null);
//                if (log.getStatus() == ScheduleLog.Status.scheduled) {
                    execution.setVariable("scheduleResult", true);
//                } else {
//                    execution.setVariable("scheduleResult", false);
//                    execution.setVariable("scheduleComments", log.getDescription());
//                }
            } else if (order.getType() == JpaProduct.Type.body) {
            } else if (order.getType() == JpaProduct.Type.info || order.getType() == JpaProduct.Type.image) {
//                boolean result = scheduleService.scheduleInfoImg(order);
//                execution.setVariable("scheduleResult", result);
            } else {
                //其他类型暂时不需要排期
                execution.setVariable("scheduleResult", true);
            }
        }
	}
}