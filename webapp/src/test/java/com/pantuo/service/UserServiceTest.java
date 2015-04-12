package com.pantuo.service;

import com.pantuo.ActivitiConfiguration;
import com.pantuo.TestCacheConfiguration;
import com.pantuo.TestServiceConfiguration;
import com.pantuo.dao.DaoBeanConfiguration;
import com.pantuo.dao.pojo.UserDetail;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

/**
 * Created by tliu on 9/2/14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoBeanConfiguration.class, ActivitiConfiguration.class, TestCacheConfiguration.class, TestServiceConfiguration.class})
public class UserServiceTest {

    @Autowired
    private UserService service;

    @Before
    public void before() {
    }

    @Test
    public void testUser() {
        UserDetail u1 = new UserDetail("username1", "password1", "first1", "last1", "email1");
        UserDetail u2 = new UserDetail("username2", "password2", "first2", "last2", "email2");
        service.createUser(u1);
        service.createUser(u2);

        Iterable<UserDetail> users = service.getAllUsers("username1", 0, Integer.MAX_VALUE);
        ArrayList<UserDetail> userList = new ArrayList<UserDetail> ();
        for (UserDetail user : users) {
            userList.add(user);
        }
        Assert.assertEquals(1, userList.size());

        users = service.getAllUsers("username2", 0, Integer.MAX_VALUE);
        userList = new ArrayList<UserDetail> ();
        for (UserDetail user : users) {
            userList.add(user);
        }
        Assert.assertEquals(1, userList.size());
    }

}
