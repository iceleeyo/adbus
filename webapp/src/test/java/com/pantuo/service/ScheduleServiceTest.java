package com.pantuo.service;

import com.pantuo.ActivitiConfiguration;
import com.pantuo.InitializationConfiguration;
import com.pantuo.TestCacheConfiguration;
import com.pantuo.TestServiceConfiguration;
import com.pantuo.dao.BoxRepository;
import com.pantuo.dao.DaoBeanConfiguration;
import com.pantuo.dao.ScheduleLogRepository;
import com.pantuo.dao.SuppliesRepository;
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
    private SuppliesRepository suppliesRepo;

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
        cal.add(Calendar.DATE, 60);

        Random random = new Random(0);
        List<Date> starts = new ArrayList<Date> ();
        for (int i=0; i<20; i++) {
            cal.add(Calendar.DATE, random.nextInt(60));
            starts.add(cal.getTime());
        }

        Contract contract = new Contract();
        contract.setContractCode("contract001");
        contract.setContractName("contract001");
        contract.setStats(JpaContract.Status.starting.ordinal());
        contractService.saveContract(contract, "tliu", new MockHttpServletRequest());

        JpaProduct[] products = new JpaProduct[] {
                new JpaProduct(JpaProduct.Type.video, "30S", 30, 4, 1, 1, 0.25, 7, 12000, false),
                new JpaProduct(JpaProduct.Type.video, "30S2", 30, 9, 1, 2, 0.2, 7, 12000, false),
                new JpaProduct(JpaProduct.Type.video, "60S", 60, 9, 1, 2, 0.2, 14, 12000, false),
            new JpaProduct(JpaProduct.Type.video, "120S", 120, 8, 2, 4, 0.25, 7, 36000, false),
                new JpaProduct(JpaProduct.Type.video, "80S", 80, 12, 2, 1, 0.3, 14, 36000, false),
                new JpaProduct(JpaProduct.Type.video, "60S2", 60, 16, 2, 1, 0.3, 30, 36000, false),
            new JpaProduct(JpaProduct.Type.video, "80S", 80, 10, 3, 2, 0.3, 7, 36000, false)};
        for (JpaProduct prod : products) {
            productService.saveProduct(prod);
        }

        JpaSupplies supply = new JpaSupplies("name", JpaProduct.Type.video, 1, "admin", "", "", JpaSupplies.Status.secondApproved,
                "", "", "", "", "", "", "");
        suppliesRepo.save(supply);

        List<JpaOrders> orders = new ArrayList<JpaOrders> ();

        for (int i=0; i<200; i++) {
            if (random.nextInt(10) < 3)
                continue;
            JpaProduct prod = products[random.nextInt(products.length -1 )];
            Date start = starts.get(random.nextInt(20));
            cal.add(Calendar.DATE, prod.getDays());
            Date end = cal.getTime();
            JpaOrders order = new JpaOrders("tliu", 1, prod.getId(), contract.getId(),
                    contract.getContractCode(), start, end, JpaProduct.Type.video,
                    JpaOrders.PayType.contract, JpaOrders.Status.paid, "manager");
            order.setSuppliesId(supply.getId());
            orderService.saveOrderJpa(order, new UserDetail("tliu", "123456", "Tony", "Liu", "tliutest@gmail.com"));
            orders.add(order);
        }

        Iterable<JpaOrders> ordersForSchedule = orderService.getOrdersForSchedule(now, JpaProduct.Type.video);

        for (JpaOrders o : ordersForSchedule) {
            ScheduleLog slog = scheduleService.schedule(o);
            log.info("Schedule result: {}", slog);
            //Assert.assertTrue(slog.getStatus() == ScheduleLog.Status.scheduled);
        }
    }

}
