package com.pantuo.service;

import com.pantuo.dao.IndustryRepository;
import com.pantuo.dao.pojo.JpaIndustry;
import com.pantuo.dao.pojo.JpaTimeslot;
import com.pantuo.dao.pojo.UserDetail;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.actors.threadpool.Arrays;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 初始化数据
 *
 * @author tliu
 */
@Service
public class DataInitializationService {
    private static Logger log = LoggerFactory.getLogger(DataInitializationService.class);

    @Autowired
    UserService userService;

    @Autowired
    TimeslotService timeslotService;

    @Autowired
    IndustryRepository industryRepo;

    public void intialize() throws Exception {
        initializeIndustries();
        initializeGroups();
        initializeUsers();
        initializeTimeslots();
    }

    //初始化group表
    private void initializeGroups() throws Exception {
        long count = userService.countGroups();
        if (count > 0) {
            log.info("There are already {} groups in table, skip initialization step", count);
            return;
        }

        InputStream is = DataInitializationService.class.getClassLoader().getResourceAsStream("groups.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = null;
        List<Group> groups = new ArrayList<Group>();

        while ((line = reader.readLine()) != null) {
            try {
                String[] group = line.split(",");
                log.info("group: id {}, name {}, type {} ", group[0], group[1], group[2]);
                Group g = new GroupEntity(group[0]);
                g.setName(group[1]);
                g.setType(group[2]);
                userService.saveGroup(g);
                groups.add(g);
            } catch (Exception e) {
                log.warn("Fail to parse group for {}, e={}", line, e.getMessage());
            }
        }

        log.info("Inserted {} group entries into table", groups.size());
    }

    //初始化用户表
    private void initializeUsers() throws Exception {
        long count = userService.count();
        if (count > 0) {
            log.info("There are already {} users in table, skip initialization step", count);
            return;
        }

        InputStream is = DataInitializationService.class.getClassLoader().getResourceAsStream("users.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = null;
        List<UserDetail> users = new ArrayList<UserDetail>();

        while ((line = reader.readLine()) != null) {
            try {
                String[] user = line.split(",");
                String[] groups = user[5].split(":");
                UserDetail ud = new UserDetail(user[0], user[1], user[2], user[3], user[4]);
                ud.setStringGroups(Arrays.asList(groups));
                log.info("user: {}, group: {}", ud.getUsername(), user[5]);
                userService.createUser(ud);
                users.add(ud);
            } catch (Exception e) {
                log.warn("Fail to parse user for {}, e={}", line, e.getMessage());
            }
        }

        log.info("Inserted {} user entries into table", users.size());
    }

    //初始化时段表
    private void initializeTimeslots() throws Exception {
        long count = timeslotService.count();
        if (count > 0) {
            log.info("There are already {} timeslot entries in table, skip initialization step", count);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        InputStream is = DataInitializationService.class.getClassLoader().getResourceAsStream("timeslot.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = null;
        List<JpaTimeslot> timeslots = new ArrayList<JpaTimeslot>();

        while ((line = reader.readLine()) != null) {
            try {
                String[] time = line.split(",");
                Date start = sdf.parse(time[0]);
                String name = time[1];
                long duration = sdf.parse(time[2]).getTime()/1000;
                boolean isPeak = "1".equals(time[3]);
                log.info("timeslot: {}, start: {}, duration {}, peak {}", name, start, duration, isPeak);
                timeslots.add(new JpaTimeslot(name, start, duration, isPeak));
            } catch (Exception e) {
                log.warn("Fail to parse timeslot for {}, e={}", line, e.getMessage());
            }
        }

        if (!timeslots.isEmpty()) {
            timeslotService.saveProducts(timeslots);
        }
        log.info("Inserted {} timeslot entries into table", timeslots.size());
    }


    //初始化行业表
    private void initializeIndustries() throws Exception {
        long count = industryRepo.count();
        if (count > 0) {
            log.info("There are already {} industries in table, skip initialization step", count);
            return;
        }

        InputStream is = DataInitializationService.class.getClassLoader().
                getResourceAsStream("industry.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = null;
        List<JpaIndustry> list = new ArrayList<JpaIndustry>();

        while ((line = reader.readLine()) != null) {
            try {
                String[] str = line.split(",");
                JpaIndustry industry = new JpaIndustry(str[0], str[1]);
                list.add(industry);
            } catch (Exception e) {
                log.warn("Fail to parse industry for {}, e={}", line, e.getMessage());
            }
        }
        industryRepo.save(list);

        log.info("Inserted {} industry entries into table", list.size());
    }
}
