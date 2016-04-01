package com.pantuo.service;

import com.pantuo.ActivitiConfiguration;
import com.pantuo.InitializationConfiguration;
import com.pantuo.TestCacheConfiguration;
import com.pantuo.TestServiceConfiguration;
import com.pantuo.dao.*;
import com.pantuo.dao.pojo.*;
import com.pantuo.mybatis.domain.Contract;
import com.pantuo.util.DateUtil;
import com.pantuo.util.Schedule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    @Lazy
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

    @Autowired
    private CityRepository cityRepo;
    private JpaCity city = null;

    @Before
    public void before() {
        city = new JpaCity("testCity", JpaCity.MediaType.screen);
        cityRepo.save(city);
    }

    @After
    public void after() {
        cityRepo.deleteAll();
    }

/*    @Test

    public void testSchedule() {
        Calendar cal = DateUtil.newCalendar();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, 30);
        Date now = cal.getTime();

        Random random = new Random(0);
        List<Date> starts = new ArrayList<Date> ();
        for (int i=0; i<90; i++) {
            cal.setTime(now);
            cal.add(Calendar.DATE, random.nextInt(120));
            starts.add(cal.getTime());
        }

        Contract contract = new Contract();
        contract.setContractCode("contract001");
        contract.setContractName("contract001");
        contract.setStats(JpaContract.Status.starting.ordinal());
        contractService.saveContract(city.getId(), contract, "tliu", new MockHttpServletRequest());

        JpaProduct[] products = new JpaProduct[] {
                new JpaProduct(city.getId(), JpaProduct.Type.video, "30S", 30, 4, 1, 1, 0.25, 30, 12000, false),
                new JpaProduct(city.getId(), JpaProduct.Type.video, "30S2", 30, 9, 1, 2, 0.2, 30, 12000, false),
                new JpaProduct(city.getId(), JpaProduct.Type.video, "60S", 60, 9, 1, 2, 0.2, 60, 12000, false),
            new JpaProduct(city.getId(), JpaProduct.Type.video, "120S", 120, 8, 2, 4, 0.25, 30, 36000, false),
                new JpaProduct(city.getId(), JpaProduct.Type.video, "80S", 80, 12, 2, 1, 0.3, 60, 36000, false),
                new JpaProduct(city.getId(), JpaProduct.Type.video, "60S2", 60, 16, 2, 1, 0.3, 30, 36000, false),
            new JpaProduct(city.getId(), JpaProduct.Type.video, "80S", 80, 10, 3, 2, 0.3, 60, 36000, false)};
        for (JpaProduct prod : products) {
            productService.saveProduct(city.getId(), prod);
        }

        JpaSupplies[] supply = new JpaSupplies[] {
                new JpaSupplies(city.getId(), "name", JpaProduct.Type.video, 1, "admin", "", "", JpaSupplies.Status.secondApproved,
                        "", "", "", "", "", "", ""),
                new JpaSupplies(city.getId(), "name2", JpaProduct.Type.video, 3, "admin", "", "", JpaSupplies.Status.secondApproved,
                        "", "", "", "", "", "", "")
        };
        suppliesRepo.save(supply[0]);
        suppliesRepo.save(supply[1]);

        List<JpaOrders> orders = new ArrayList<JpaOrders> ();

        for (int i=0; i<200; i++) {
            if (random.nextInt(10) < 3)
                continue;
            JpaProduct prod = products[random.nextInt(products.length -1 )];
            Date start = starts.get(random.nextInt(60));
            cal.add(Calendar.DATE, prod.getDays());
            Date end = cal.getTime();
            JpaOrders order = new JpaOrders(city.getId(), "tliu", null, prod, contract.getId(),
                    contract.getContractCode(), start, end, JpaProduct.Type.video,
                    JpaOrders.PayType.contract, random.nextInt(10) < 3 ? JpaOrders.Status.unpaid : JpaOrders.Status.paid, "manager", 0, null);
            order.setSuppliesId(supply[random.nextInt(1)].getId());
            orderService.saveOrderJpa(city.getId(), order, new UserDetail("tliu", "123456", "Tony", "Liu", "tliutest@gmail.com"));
            orders.add(order);
        }

        Iterable<JpaOrders> ordersForSchedule = orderService.getOrdersForSchedule(city.getId(), now, JpaProduct.Type.video);

        for (JpaOrders o : ordersForSchedule) {
            ScheduleLog slog = scheduleService.schedule(o);
            log.info("Schedule result: {}", slog);
            //Assert.assertTrue(slog.getStatus() == ScheduleLog.Status.scheduled);
        }
    }
*/

}
