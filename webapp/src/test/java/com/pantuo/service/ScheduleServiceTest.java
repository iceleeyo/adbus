package com.pantuo.service;

import com.pantuo.ActivitiConfiguration;
import com.pantuo.InitializationConfiguration;
import com.pantuo.TestCacheConfiguration;
import com.pantuo.TestServiceConfiguration;
import com.pantuo.dao.BoxRepository;
import com.pantuo.dao.DaoBeanConfiguration;
import com.pantuo.dao.ScheduleLogRepository;
import com.pantuo.dao.pojo.*;
import com.pantuo.mybatis.domain.Contract;
import com.pantuo.util.DateUtil;
import com.pantuo.util.Schedule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * @author tliu
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoBeanConfiguration.class, InitializationConfiguration.class, ActivitiConfiguration.class, TestCacheConfiguration.class, TestServiceConfiguration.class})
public class ScheduleServiceTest {
    private static Logger log = LoggerFactory.getLogger(ScheduleServiceTest.class);

    @Autowired
    private ProductService productService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TimeslotService timeslotService;

    @Autowired
    private BoxRepository boxRepo;
    @Autowired
    private ScheduleLogRepository scheduleLogRepository;
    @Autowired
    private ScheduleService scheduleService;

    @Before
    public void before() {
    }

    @Test
    public void testSchedule() {
        Calendar cal = DateUtil.newCalendar();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date now = cal.getTime();
        cal.add(Calendar.DATE, -1);
        Date start = cal.getTime();
        cal.add(Calendar.DATE, 7);
        Date end = cal.getTime();

        Random random = new Random(0);
        Contract contract = new Contract();
        contract.setContractCode("contract001");
        contract.setContractName("contract001");
        contract.setStats(JpaContract.Status.starting.ordinal());
        contractService.saveContract(contract, "tliu", new MockHttpServletRequest());

        JpaProduct[] products = new JpaProduct[] {new JpaProduct(JpaProduct.Type.video, "30S", 30, 4, 1, 1, 0.25, 7, 12000, false),
            new JpaProduct(JpaProduct.Type.video, "120S", 120, 8, 2, 4, 0.25, 7, 36000, false),
            new JpaProduct(JpaProduct.Type.video, "80S", 80, 10, 3, 2, 0.4, 7, 36000, false)};
        for (JpaProduct prod : products) {
            productService.saveProduct(prod);
        }

        List<JpaOrders> orders = new ArrayList<JpaOrders> ();

        for (int i=0; i<19; i++) {
            JpaOrders order = new JpaOrders("tliu", 1, products[random.nextInt(3)].getId(), contract.getId(),
                    contract.getContractCode(), start, end, JpaProduct.Type.video,
                    JpaOrders.PayType.contract, JpaOrders.Status.paid, "manager");
            orderService.saveOrderJpa(order, new UserDetail("tliu", "123456", "Tony", "Liu", "tliutest@gmail.com"));
            orders.add(order);
        }

        Iterable<JpaOrders> ordersForSchedule = orderService.getOrdersForSchedule(now, JpaProduct.Type.video);

        for (JpaOrders o : ordersForSchedule) {
            ScheduleLog slog = scheduleService.schedule(o);
            log.info("Schedule result: {}", slog);
            Assert.assertTrue(slog.getStatus() == ScheduleLog.Status.scheduled);
        }
    }

}
