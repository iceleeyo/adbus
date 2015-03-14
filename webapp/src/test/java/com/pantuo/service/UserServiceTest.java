package com.pantuo.service;

import com.pantuo.TestCacheConfiguration;
import com.pantuo.TestServiceConfiguration;
import com.pantuo.dao.DaoBeanConfiguration;
import com.pantuo.dao.pojo.User;
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
@ContextConfiguration(classes = {DaoBeanConfiguration.class, TestCacheConfiguration.class, TestServiceConfiguration.class})
public class UserServiceTest {

    @Autowired
    private UserService service;

    @Before
    public void before() {
    }

    @Test
    public void testUser() {
        User u1 = new User("username1", "password1");
        User u2 = new User("username2", "password2");
        service.getUserRepo().save(u1);
        service.getUserRepo().save(u2);

        Iterable<User> users = service.getAllUsers(0, Integer.MAX_VALUE);
        ArrayList<User> userList = new ArrayList<User> ();
        for (User user : users) {
            userList.add(user);
        }
        Assert.assertEquals(2, userList.size());
    }

}
