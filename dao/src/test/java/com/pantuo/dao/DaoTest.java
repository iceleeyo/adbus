package com.pantuo.dao;

import com.pantuo.dao.pojo.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * Created by tliu on 8/12/14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoBeanConfiguration.class})
public class DaoTest {

    @Autowired
    private UserRepository userRepo;

    @Before
    public void before() {
        userRepo.deleteAll();
    }

    @Test
    public void testUser() {
        User u1 = new User("username1", "password1");
        User u2 = new User("username2", "password2");
        userRepo.save(u1);
        userRepo.save(u2);

        Iterable<User> users = userRepo.findAll();
        ArrayList<User> userList = new ArrayList<User> ();
        for (User user : users) {
            userList.add(user);
        }
        Assert.assertEquals(2, userList.size());

        User violation = new User("username1", "password1");
        try {
            userRepo.save(violation);
            Assert.fail("Should violate the constraint");
        } catch (DataIntegrityViolationException e) {

        }
    }


}
